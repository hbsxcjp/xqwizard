package net.elephantbase.xqbooth;

import java.io.PrintWriter;

import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.elephantbase.db.DBUtil;
import net.elephantbase.db.RowCallback;
import net.elephantbase.users.biz.EventLog;
import net.elephantbase.users.biz.UserData;
import net.elephantbase.users.biz.Users;
import net.elephantbase.util.EasyDate;
import net.elephantbase.util.Logger;

public class XQBoothServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;

	private static final int INCORRECT = 0;
	private static final int NO_RETRY = -1;
	private static final int INTERNAL_ERROR = -2;

	private static int login(String username, String password, String[] cookie) {
		// 1. Login with Cookie
		String[] username_ = new String[1];
		String[] cookie_ = new String[] {password};
		int uid = Users.loginCookie(cookie_, username_);
		if (uid > 0 && username_[0].equals(username)) {
			if (cookie != null && cookie.length > 0) {
				cookie[0] = cookie_[0];
			}
			return uid;
		}

		// 2. Try Login
		uid = Users.login(username, password);
		if (uid > 0) {
			if (cookie != null && cookie.length > 0) {
				cookie[0] = Users.addCookie(uid);
			}
			String sql = "DELETE FROM xq_retry WHERE uid = ?";
			DBUtil.update(sql, Integer.valueOf(uid));
			return uid;
		}

		// 3. Get "uid" and Check if "noretry"
		String sql = "SELECT uc_members.uid, retrycount, retrytime FROM " +
				"uc_members LEFT JOIN xq_retry USING (uid) WHERE username = ?";
		Object[] row = DBUtil.query(2, sql, username);
		uid = DBUtil.getInt(row, 0);
		if (uid == 0) {
			return INCORRECT;
		}
		int retryCount = DBUtil.getInt(row, 1);
		if (retryCount == 0) {
			sql = "INSERT INTO xq_retry (uid, retrycount, retrytime) " +
			"VALUES (?, 1, 0)";
			DBUtil.update(sql, Integer.valueOf(uid));
			return INCORRECT;
		}
		if (EasyDate.currTimeSec() < DBUtil.getInt(row, 2)) {
			return NO_RETRY;
		}
		if (retryCount < 5) {
			sql = "UPDATE xq_retry SET retrycount = retrycount + 1 WHERE uid = ?";
			DBUtil.update(sql, Integer.valueOf(uid));
			return INCORRECT;
		}
		sql = "UPDATE xq_retry SET retrycount = 1, retrytime = ? WHERE uid = ?";
		DBUtil.update(sql, Integer.valueOf(EasyDate.currTimeSec() + 300),
				Integer.valueOf(uid));
		return NO_RETRY;
	}

	@Override
	protected void doPost(HttpServletRequest req, HttpServletResponse resp) {
		doGet(req, resp);
	}

	@Override
	protected void doGet(HttpServletRequest req, HttpServletResponse resp) {
		String username = req.getHeader("Login-UserName");
		String password = req.getHeader("Login-Password");
		String[] cookie = new String[1];
		int uid = login(username, password, cookie);
		if (uid == INTERNAL_ERROR) {
			resp.setHeader("Login-Result", "internal-error");
			return;
		}
		if (uid == NO_RETRY) {
			resp.setHeader("Login-Result", "noretry");
			return;
		}
		if (uid == INCORRECT) {
			resp.setHeader("Login-Result", "error");
			return;
		}
		resp.setHeader("Login-Cookie", cookie[0]);
		String act = req.getParameter("act");
		if (act == null) {
			return;
		}

		String strStage = req.getParameter("stage");
		if (strStage == null) {
			strStage = req.getParameter("score");
		}
		int stage = 0;
		try {
			stage = Integer.parseInt(strStage);
		} catch (Exception e) {
			// Ignored
		}

		String ip = req.getRemoteAddr();
		UserData user = new UserData(uid, ip);

		if (false) {
	    	// Code Style
	    } else if (act.equals("querypoints")) {
			resp.setHeader("Login-Result", "ok " +
					user.getPoints() + "|" + user.getCharged());
		} else if (act.equals("queryscore")) {
			resp.setHeader("Login-Result", "ok " + user.getScore());
		} else if (act.equals("queryrank")) {
			String sql = "SELECT rank, score FROM xq_rank WHERE uid = ?";
			Object[] row = DBUtil.query(2, sql, Integer.valueOf(uid));
			int scoreToday = DBUtil.getInt(row, 0);
			int rankToday = DBUtil.getInt(row, 1);
			int rankYesterday = 0;
			if (rankToday > 0) {
				sql = "SELECT rank FROM xq_rank0 WHERE uid = ?";
				rankYesterday = DBUtil.getInt(DBUtil.query(sql, Integer.valueOf(uid)));
			}
			resp.setHeader("Login-Result", "ok " + scoreToday + "|" +
					rankToday + "|" + rankYesterday);
		} else if (act.equals("ranklist")) {
			String sql = "SELECT username, score FROM xq_rank LEFT JOIN " +
					"uc_members USING (uid) ORDER BY rank LIMIT 100";
			final PrintWriter out;
			try {
				out = resp.getWriter();
			} catch (Exception e) {
				Logger.severe(e);
				return;
			}
			DBUtil.query(2, sql, new RowCallback() {
				@Override
				public Object onRow(Object[] row_) {
					out.print(row_[1] + "|" + row_[0] + "\r\n");
					return null;
				}
			});
		} else if (act.equals("save")) {
			if (stage > user.getScore()) {
				String sql = "UPDATE xq_user SET score = ? WHERE uid = ?";
				DBUtil.update(sql, Integer.valueOf(stage), Integer.valueOf(uid));
				resp.setHeader("Login-Result", "ok");
				EventLog.log(uid, ip, EventLog.EVENT_SAVE, stage);
			} else {
				resp.setHeader("Login-Result", "nosave");
			}
		} else if (act.equals("hint")) {
			if (stage < 500) {
				resp.setHeader("Login-Result", "ok");
			} else if (user.getPoints() < 10 && !user.isPlatinum()) {
				resp.setHeader("Login-Result", "nopoints");
			} else {
				if (!user.isPlatinum()) {
					String sql = "UPDATE xq_user SET points = " +
							"points - 10 WHERE uid = ?";
					DBUtil.update(sql, Integer.valueOf(uid));
				}
				resp.setHeader("Login-Result", "ok");
				EventLog.log(uid, ip, EventLog.EVENT_HINT, stage);
			}
		} else if (act.equals("retract")) {
			if (stage < 500) {
				resp.setHeader("Login-Result", "ok");
			} else if (user.getPoints() < 1 && !user.isPlatinum()) {
				resp.setHeader("Login-Result", "nopoints");
			} else {
				if (!user.isPlatinum()) {
					String sql = "UPDATE xq_user SET points = " +
							"points - 1 WHERE uid = ?";
					DBUtil.update(sql, Integer.valueOf(uid));
				}
				resp.setHeader("Login-Result", "ok");
				EventLog.log(uid, ip, EventLog.EVENT_RETRACT, stage);
			}
		}
	}
}