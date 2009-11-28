package net.elephantbase.xqbase.web;

import java.io.ByteArrayOutputStream;
import java.io.PrintStream;
import java.net.URLEncoder;

import org.apache.wicket.behavior.SimpleAttributeModifier;
import org.apache.wicket.markup.html.WebComponent;
import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.link.Link;

import net.elephantbase.cchess.MoveParser;
import net.elephantbase.cchess.Position;
import net.elephantbase.db.DBUtil;
import net.elephantbase.db.Row;
import net.elephantbase.ecco.Ecco;
import net.elephantbase.users.web.BasePanel;
import net.elephantbase.util.wicket.WicketUtil;
import net.elephantbase.xqbase.biz.EccoUtil;
import net.elephantbase.xqbase.biz.PgnInfo;

public class DetailPanel extends BasePanel {
	private static final long serialVersionUID = 1L;

	private static final String[] RESULT_STRING = {
		" (���δ֪)", " (��ʤ)", " (�;�)", " (��ʤ)",
	};

	private static final String[] RESULT_TAG = {
		"*", "1-0", "1/2-1/2", "0-1",
	};

	public DetailPanel(PgnInfo pgnInfo) {
		super(pgnInfo.getResult(), XQBasePage.SUFFIX, WANT_AUTH);

		// �����ݿ��ж�ȡ����
		String sql = "SELECT event, round, date, site, redteam, red, " +
				"blackteam, black, ecco, movelist, result FROM " +
				"xq_pgn WHERE sid = ?";
		Row row = DBUtil.query(11, sql, Integer.valueOf(pgnInfo.getSid()));
		final String event = row.getString(1);
		final String round = row.getString(2);
		final String date = row.getString(3);
		final String site = row.getString(4);
		final String redTeam = row.getString(5);
		final String red = row.getString(6);
		final String blackTeam = row.getString(7);
		final String black = row.getString(8);
		final String ecco = EccoUtil.id2ecco(row.getInt(9));
		String moveList = row.getString(10);
		int result = row.getInt(11);
		if (result < 1 || result > 3) {
			result = 0;
		}
		final String resultTag = RESULT_TAG[result];

		// ���¡�������ص㡢Flash������
		add(new Label("lblEvent", pgnInfo.getEvent()));
		add(new Label("lblDateSite", pgnInfo.getDateSite()));
		add(new Label("lblBlack", "�ڷ� " + blackTeam + " " + black));
		WebComponent embed = new WebComponent("embed");
		try {
			embed.add(new SimpleAttributeModifier("flashvars",
					"MoveList=" + URLEncoder.encode(moveList, "UTF-8")));
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
		add(embed);
		add(new Label("lblRed", "�췽 " + redTeam + " " + red));
		Link<Void> lnkEcco = new Link<Void>("lnkEcco") {
			private static final long serialVersionUID = 1L;

			@Override
			public void onClick() {
				setResponsePanel(new ResultPanel(ecco));
			}
		};
		lnkEcco.add(new Label("lblOpening", pgnInfo.getOpening()));
		add(lnkEcco);

		// �߷�
		StringBuilder sb = new StringBuilder();
		Position pos = new Position();
		pos.fromFen(Position.STARTUP_FEN[0]);
		String[] iccsMoves = moveList.split(" ");
		int counter = 0;
		for (String iccsMove : iccsMoves) {
			if (iccsMove.length() < 5) {
				break;
			}
			int mv = MoveParser.iccs2Move(iccsMove);
			if (mv == 0) {
				break;
			}
			String file = MoveParser.move2File(mv, pos);
			if (file.equals("    ")) {
				break;
			}
			String chin = MoveParser.file2Chin(file, pos.sdPlayer);
			if (pos.sdPlayer == 0) {
				counter ++;
				sb.append((counter < 10 ? " " : "") + counter + ". " + chin + " ");
			} else {
				sb.append(chin + "\r\n");
			}
			if (!pos.makeMove(mv)) {
				break;
			}
			if (pos.captured()) {
				pos.setIrrev();
			}
		}
		if (pos.sdPlayer == 1) {
			sb.append("\r\n");
		}
		final String content = sb.toString();
		Label lblContent = new Label("lblContent", content.replaceAll(" ", "&nbsp;").
				replaceAll("\r\n", "<br>") + "����" + RESULT_STRING[result]);
		lblContent.setEscapeModelStrings(false);
		add(lblContent);

		// ��������
		add(new Link<Void>("lnkPgn") {
			private static final long serialVersionUID = 1L;

			@Override
			public void onClick() {
				ByteArrayOutputStream baos = new ByteArrayOutputStream();
				PrintStream out = new PrintStream(baos);
				out.print("[Game \"Chinese Chess\"]\r\n");
				out.printf("[Event \"%s\"]\r\n", event);
				out.printf("[Round \"%s\"]\r\n", round);
				out.printf("[Date \"%s\"]\r\n", date);
				out.printf("[Site \"%s\"]\r\n", site);
				out.printf("[RedTeam \"%s\"]\r\n", redTeam);
				out.printf("[Red \"%s\"]\r\n", red);
				out.printf("[BlackTeam \"%s\"]\r\n", blackTeam);
				out.printf("[Black \"%s\"]\r\n", black);
				out.printf("[Result \"%s\"]\r\n", resultTag);
				out.printf("[ECCO \"%s\"]\r\n", ecco);
				out.printf("[Opening \"%s\"]\r\n", Ecco.opening(ecco));
				out.printf("[Variation \"%s\"]\r\n", Ecco.variation(ecco));
				out.print(content);
				out.print(resultTag + "\r\n");
				out.print("============================\r\n");
				out.print(" ��ӭ���ʡ�����ٿ�ȫ������ \r\n");
				out.print(" �Ƽ��á�������ʦ���������� \r\n");
				out.print("http://www.elephantbase.net/");
				out.close();
				WicketUtil.download("pgn", "text/plain", baos.toByteArray());
			}
		});
	}
}