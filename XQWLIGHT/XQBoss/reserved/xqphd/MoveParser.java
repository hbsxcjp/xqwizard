package xqphd;

import xqwlight.Position;

public class MoveParser {
	private static final int DIRECT_TO_POS = 5;

	private static final String PIECE_TO_CHAR = "KABNRCP";

	private static final String[] DIGIT_TO_WORD = {
		"һ�����������߰˾�", "������������������", "�����H�e����������", "�k����������������"
	};

	private static final String[] PIECE_TO_WORD = {
		"˧�������ڱ�", "��ʿ��������", "�Ӥ��۰�������", "�N�\�H��������"
	};

	private static final String[] DIRECT_TO_WORD = {
		"��ƽ��", "�i���h"
	};

	private static final String[] POS_TO_WORD = {
		"һ��������ǰ�к�", "�k���������e����"
	};

	private static int digit2Char(int n) {
		return '1' + n;
	}

	private static int piece2Char(int n) {
		return n < 0 || n >= 7 ? ' ' : PIECE_TO_CHAR.charAt(n);
	}

	private static int direct2Char(int n) {
		return n < 0 || n >= 3 ? ' ' : "+.-".charAt(n);
	}

	private static int pos2Char(int n) {
		return n < 0 || n >= 8 ? ' ' : "abcde+.-".charAt(n);
	}

	private static int char2Digit(int c) {
		return c >= '1' && c <= '9' ? c - '1' : -1;
	}

	private static int char2Piece(int c) {
		if (c >= 'a' && c <= 'z') {
			return c == 'e' ? 2 : c == 'h' ? 3 : PIECE_TO_CHAR.indexOf(c  - 'a' + 'A');
		}
		return c == 'E' ? 2 : c == 'H' ? 3 : PIECE_TO_CHAR.indexOf(c);
	}

	private static int char2Direct(int c) {
		return c == '+' ? 0 : c == '.' || c == '=' ? 1 : c == '-' ? 2 : -1;
	}

	private static int char2Pos(int c) {
		if (c >= 'a' && c <= 'e') {
			return c - 'a';
		}
		int dir = char2Direct(c);
		return dir == -1 ? -1 : dir + DIRECT_TO_POS;
	}

	private static int word2Digit(int w) {
		for (int i = 0; i < DIGIT_TO_WORD.length; i ++) {
			int index = DIGIT_TO_WORD[i].indexOf(w);
			if (index >= 0) {
				return index;
			}
		}
		return -1;
	}

	private static int word2Piece(int w) {
		if (false) {
			// Code Style
		} else if (w == '��' || w == '��') {
			return 0;
		} else if (w == '�R' || w == '��' || w == '�X') { // �X[��]
			return 3;
		} else if (w == '܇' || w == '��' || w == '��' || w == '�e') { // ��[��]
			return 4;
		} else if (w == '��' || w == '�F' || w == '�h' || w == '��') { // �F[��], ��[�h]
			return 5;
		}
		for (int i = 0; i < PIECE_TO_WORD.length; i ++) {
			int index = PIECE_TO_WORD[i].indexOf(w);
			if (index >= 0) {
				return index;
			}
		}
		return -1;
	}

	private static int word2Direct(int w) {
		if (w == '�M') {
			return 0;
		}
		for (int i = 0; i < DIRECT_TO_WORD.length; i ++) {
			int index = DIRECT_TO_WORD[i].indexOf(w);
			if (index >= 0) {
				return index;
			}
		}
		return -1;
	}

	private static int word2Pos(int w) {
		if (w == '��' || w == '��') { // ��[��]
			return DIRECT_TO_POS + 2;
		}
		for (int i = 0; i < POS_TO_WORD.length; i ++) {
			int index = POS_TO_WORD[i].indexOf(w);
			if (index >= 0) {
				return index;
			}
		}
		return -1;
	}

	public static int file2Move(String strFile, Position pos) {
		return 0;
	}

	public static int iccs2Move(String strIccs) {
		return 0;
	}

	public static String chin2File(String strChin) {
		if (strChin.length() != 4) {
			return "";
		}
		char[] cChin = strChin.toCharArray();
		char[] cFile = new char[4];
		int pos = word2Pos(cChin[0]);
		cFile[0] = (char) piece2Char(word2Piece(pos < 0 ? cChin[0] : cChin[1]));
		cFile[1] = (char) (pos < 0 ? digit2Char(word2Digit(cChin[1])) : pos2Char(pos));
		if ((cChin[2] == '��' || cChin[2] == '��' || cChin[2] == '׃') && cChin[3] == 6) { // ��[׃]
			cFile[2] = '=';
			cFile[3] = 'P';
		} else {
			cFile[2] = (char) direct2Char(word2Direct(cChin[2]));
			cFile[3] = (char) digit2Char(word2Digit(cChin[3]));
		}
		return new String(cFile);
	}
}