package net.elephantbase.pgndb.biz;

import java.net.URLEncoder;

import net.elephantbase.cchess.MoveParser;
import net.elephantbase.cchess.Position;
import net.elephantbase.ecco.Ecco;

public class PgnUtil {
	public static String toEventString(String event, String round) {
		if (round.isEmpty()) {
			return event;
		}
		String strEvent = (event.isEmpty() ? "" : event + " ");
		try {
			return strEvent + "��" + Integer.parseInt(round) + "��";
		} catch (Exception e) {
			return strEvent + round;
		}
	}

	private static final String[][] START_RESULT_1 = {
		{"����", "����ʤ", "���Ⱥ�", "���ȸ�"},
		{"����", "���ȸ�", "���Ⱥ�", "����ʤ"},
	};

	private static final String[][] START_RESULT_2 = {
		{"��", "��ʤ", "�Ⱥ�", "�ȸ�"},
		{"��", "��ʤ", "���", "��"},
	};

	public static String toResultString(String redTeam, String red,
			String blackTeam, String black, int start, int result) {
		if (red.isEmpty() || black.isEmpty()) {
			return "(�ŷ���" + START_RESULT_1[start][result] + ")";
		}
		String strRed = redTeam + (redTeam.isEmpty() ? "" : " ") + red;
		String strBlack = blackTeam + (blackTeam.isEmpty() ? "" : " ") + black;
		return strRed + " (" + START_RESULT_2 + ") " + strBlack;
	}

	public static String toDateSiteString(String date, String site) {
		if (date.isEmpty()) {
			return site.isEmpty() ? "" : "���� " + site;
		}
		return date + (site.isEmpty() ? "" : " ���� " + site);
	}

	public static String getOpenString(String ecco, String opening, String variation) {
		String strOpening = opening +
				(opening.isEmpty() || variation.isEmpty() ? "" : "����") + variation;
		return strOpening + (ecco.isEmpty() ? "" :
				(strOpening.isEmpty() ? "(ECCO���ֱ�ţ�" + ecco + ")" : "(" + ecco + ")"));
	}

	public static String toFlashString(String fen, String moveList) {
		String flashVars;
		try {
			flashVars = (fen == null ? "MoveList=" : "Position=" + fen + "&MoveList") +
					URLEncoder.encode(moveList, "UTF-8");
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
		return "<embed src=\"http://pub.elephantbase.net/flashxq.swf\" " +
				"width=\"324\" height=\"396\" type=\"application/x-shockwave-flash\" " +
				"pluginspage=\"http://www.macromedia.com/go/getflashplayer\" " +
				"allowScriptAccess=\"always\" quality=\"high\" wmode=\"transparent\" " +
				"flashvars=\"" + flashVars + "\">";
	}

	public static String parseEcco(String moveList) {
		StringBuilder sbFile = new StringBuilder();
		Position pos = new Position();
		pos.fromFen(Position.STARTUP_FEN[0]);
		String[] iccsMoves = moveList.split(" ");
		for (String iccsMove : iccsMoves) {
			if (iccsMove.length() < 5) {
				continue;
			}
			int mv = MoveParser.iccs2Move(iccsMove);
			if (mv == 0) {
				continue;
			}
			sbFile.append(MoveParser.move2File(mv, pos));
			pos.makeMove(mv);
			if (sbFile.length() == 80) {
				break;
			}
		}
		return Ecco.ecco(sbFile.toString());
	}
}