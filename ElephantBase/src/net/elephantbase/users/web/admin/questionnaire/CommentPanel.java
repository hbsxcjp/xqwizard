package net.elephantbase.users.web.admin.questionnaire;

import java.io.Serializable;
import java.util.ArrayList;

import net.elephantbase.db.DBUtil;
import net.elephantbase.db.RowCallback;
import net.elephantbase.users.biz.BaseSession;
import net.elephantbase.users.biz.EventLog;
import net.elephantbase.users.web.BasePanel;
import net.elephantbase.util.EasyDate;

import org.apache.wicket.behavior.SimpleAttributeModifier;
import org.apache.wicket.markup.html.WebMarkupContainer;
import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.link.Link;
import org.apache.wicket.markup.html.list.ListItem;
import org.apache.wicket.markup.html.list.ListView;

class CommentEntry implements Serializable {
	private static final long serialVersionUID = 1L;

	int uid, time;
	String ip, comment;
}

public class CommentPanel extends BasePanel {
	private static final long serialVersionUID = 1L;

	private static final int LINK_NONE = 0;
	private static final int LINK_COMMENT = 1;
	private static final int LINK_DELETE = 2;

	public CommentPanel() {
		super("����");

		final ArrayList<CommentEntry> commentList = new ArrayList<CommentEntry>();
		String sql = "SELECT uid, eventip, eventtime, comment FROM " +
				"xq_qn_comment LEFT JOIN xq_qn_user USING (uid)";
		DBUtil.query(4, sql, new RowCallback() {
			@Override
			public Object onRow(Object[] row) {
				CommentEntry comment = new CommentEntry();
				comment.uid = DBUtil.getInt(row, 0);
				comment.ip = (String) row[1];
				comment.time = DBUtil.getInt(row, 2);
				comment.comment = (String) row[3];
				commentList.add(comment);
				return null;
			}
		});

		ListView<CommentEntry> listView = new
				ListView<CommentEntry>("commentList", commentList) {
			private static final long serialVersionUID = 1L;

			private void addTd(ListItem<CommentEntry> item, String tag,
					String content, int type) {
				WebMarkupContainer td = new WebMarkupContainer("td" + tag);
				item.add(td);
				td.add(new SimpleAttributeModifier("bgcolor",
						item.getIndex() % 2 == 0 ? "#EEEEEE" : "DDDDDD"));
				if (type == LINK_NONE) {
					td.add(new Label("lbl" + tag, content));
					return;
				}
				final String shorten = content.length() < 20 ? content :
						content.substring(0, 18 - 2) + "...";
				if (type == LINK_COMMENT) {
					td.add(new Label("lbl" + tag, shorten));
					td.add(new SimpleAttributeModifier("title", content));
					return;
				}
				final int uid = item.getModelObject().uid;
				td.add(new Link<Void>("lnk" + tag) {
					private static final long serialVersionUID = 1L;

					@Override
					public void onClick() {
						String sql_ = "DELETE FROM xq_qn_comment WHERE uid = ?";
						DBUtil.update(sql_, Integer.valueOf(uid));
						CommentPanel panel = new CommentPanel();
						panel.setInfo("����[" + shorten + "]�ѱ�ɾ��");
						EventLog.log(((BaseSession) getSession()).getUid(),
								EventLog.QN_DELETE, uid);
						setResponsePanel(new AnswerPanel(),
								panel, new ClearPanel());
					}
				});
			}

			@Override
			protected void populateItem(ListItem<CommentEntry> item) {
				CommentEntry comment = item.getModelObject();
				addTd(item, "Ip", comment.ip, LINK_NONE);
				addTd(item, "Time", EasyDate.toStringSec(comment.time), LINK_NONE);
				addTd(item, "Comment", comment.comment, LINK_COMMENT);
				addTd(item, "Delete", comment.comment, LINK_DELETE);
			}
		};
		add(listView);
	}
}