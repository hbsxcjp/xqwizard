package net.elephantbase.xqbase.biz;

import java.util.ArrayList;

import net.elephantbase.cchess.MoveParser;
import net.elephantbase.cchess.Position;
import net.elephantbase.ecco.Ecco;

public class EccoUtil {
	public static int ecco2id(String ecco) {
		return (ecco.charAt(0) - 'A') * 100 + Integer.parseInt(ecco.substring(1));
	}

	public static String id2ecco(int id) {
		return String.format("%c%02d", Character.valueOf((char) ('A' + id / 100)),
				Integer.valueOf(id % 100));
	}

	public static final String[] LEVEL_1 = {
		"A. �������࿪��",
		"B. ���ڶԷ���������",
		"C. ���ڶ�������",
		"D. ˳�ھֺ����ھ�",
		"E. ����ָ·��",
	};

	public static final String[][] LEVEL_2 = {
		{
			"A0. �ǳ�������",
			"A1. �����(һ)",
			"A2. �����(��)",
			"A3. �����(��)",
			"A4. �����",
			"A5. �˽��ھ�",
			"A6. �����ھ�",
		}, {
			"B0. ���ڶԷǳ�������",
			"B1. ���ڶԵ�����",
			"B2. ���ڶ���������",
			"B3. ���ڶԷ�����(һ)",
			"B4. ���ڶԷ�����(��)���������ڶԷ�����",
			"B5. ���ڶԷ�����(��)���������ڶԷ�����",
		}, {
			"C0. ���ڶ�������(һ)",
			"C1. ���ڶ�������(��)",
			"C2. ���ڹ��ӳ���·�����������ͷ��",
			"C3. ���ڹ��ӳ������߱���������",
			"C4. ���ڹ��ӳ������߱���������ƽ�ڶҳ�",
			"C5. �����ڶ�������",
			"C6. �����ڶ�������(һ)",
			"C7. �����ڶ�������(��)",
			"C8. ����Ѳ���ڶ�������",
			"C9. ����ں�����ڶ�������",
		}, {
			"D0. ˳�ھ�",
			"D1. ˳��ֱ���Ի�����",
			"D2. ˳��ֱ���Ժᳵ",
			"D3. ���ڶ����ڷ⳵ת����",
			"D4. ���ڶ���������ת����",
			"D5. ���ڶ�����",
		}, {
			"E0. ����ָ·��",
			"E1. ����ָ·�������",
			"E2. ����ָ·ת�����ڶ�����ڷ�����(һ)",
			"E3. ����ָ·ת�����ڶ�����ڷ�����(��)",
			"E4. �Ա���",
		}
	};

	public static String[][][] LEVEL_3;

	static {
		int iMax = LEVEL_2.length;
		LEVEL_3 = new String[iMax][][];
		for (int i = 0; i < iMax; i ++) {
			int jMax = LEVEL_2[i].length;
			LEVEL_3[i] = new String[jMax][];
			for (int j = 0; j < jMax; j ++) {
				ArrayList<String> openVarList = new ArrayList<String>();
				String eccoLevel2 = "" + (char) ('A' + i) + (char) ('0' + j);
				for (int k = 0; k < 10; k ++) {
					String ecco = eccoLevel2 + (char) ('0' + k);
					String opening = Ecco.opening(ecco);
					if (opening.isEmpty()) {
						if (k == 0) {
							openVarList.add(ecco + ". ��������");
						} else {
							break;
						}
					} else {
						String variation = Ecco.variation(ecco);
						openVarList.add(ecco + ". " + Ecco.opening(ecco) +
								(variation.isEmpty() ? "" : " " + variation));
					}
				}
				LEVEL_3[i][j] = openVarList.toArray(new String[0]);
			}
		}
	}

	public static String toOpeningString(String ecco) {
		return toOpeningString(ecco2id(ecco));
	}

	public static String toOpeningString(int id) {
		return toOpeningString(id / 100, id / 10 % 10, id % 10);
	}

	public static String toOpeningString(int level1, int level2, int level3) {
		if (level1 < 0 || level1 >= LEVEL_1.length) {
			return null;
		}
		if (level2 < 0 || level2 >= LEVEL_2[level1].length) {
			return LEVEL_1[level1];
		}
		if (level3 < 0 || level3 >= LEVEL_3[level1][level2].length) {
			return LEVEL_2[level1][level2];
		}
		return LEVEL_3[level1][level2][level3];
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