package net.elephantbase.cchess;

public class MoveParser {
	/** �������� */
	private static final int MAX_FILE = 9;
	/** �������� */
	private static final int MAX_RANK = 10;
	/** 8��λ�÷ֱ���"abcde+.-"����"+.-"�ɷ���ת��Ϊλ��ʱ��Ҫ���ϸ�ֵ */
	private static final int DIRECT_TO_POS = 5;

	/** ���Ӵ���"KABNRCP"�ֱ��ʾ"˧(��)��(ʿ)��(��)���ڱ�(��)" */
	private static final String PIECE_TO_CHAR = "KABNRCP";

	/** ��ʶ��������У��������֡�ȫ�ǰ��������֣��Լ����ǵ�BIG5���룬���а�ǰ��������� */
	private static final String[] DIGIT_TO_WORD = {
		"һ�����������߰˾�", "������������������", "�����H�e����������", "�k����������������", "123456789"
	};

	/** ��ʶ��������У��췽��"˧�������ڱ�"���ڷ���"��ʿ��������"���Լ����ǵ�BIG5���� */
	private static final String[] PIECE_TO_WORD = {
		"˧�������ڱ�", "��ʿ��������", "�Ӥ��۰�������", "�N�\�H��������"
	};

	/** ��ʶ��ķ�����"��ƽ��"�Լ����ǵ�BIG5���� */
	private static final String[] DIRECT_TO_WORD = {
		"��ƽ��", "�i���h"
	};

	/** ��ʶ���λ����"һ��������ǰ�к�"�Լ����ǵ�BIG5���� */
	private static final String[] POS_TO_WORD = {
		"һ��������ǰ�к�", "�k���������e����"
	};

	/** ȷ�������߱�ʾһ������(ʿ)��8�֡���(��)��16�ֺ���(ʿ)��(��)����ɱ�(��)��4�� */
	private static final String[] FIX_FILE = {
		"A4-5", "A4+5", "A5-4", "A5+4", "A5-6", "A5+6", "A6-5", "A6+5",
		"B1-3", "B1+3", "B3-1", "B3+1", "B3-5", "B3+5", "B5-3", "B5+3",
		"B5-7", "B5+7", "B7-5", "B7+5", "B7-9", "B7+9", "B9-7", "B9+7",
		"A4=P", "A6=P", "B3=P", "B7=P"
	};

	/** ȷ�������߱�ʾ����Ӧ�췽�߷��������յ����꣬�ڷ���Ҫ����Щ��������ת */
	private static final short[][] FIX_MOVE = {
		{0xa8, 0xb7}, {0xc8, 0xb7}, {0xb7, 0xc8}, {0xb7, 0xa8},
		{0xb7, 0xc6}, {0xb7, 0xa6}, {0xa6, 0xb7}, {0xc6, 0xb7},
		{0xab, 0xc9}, {0xab, 0x89}, {0x89, 0xab}, {0xc9, 0xab},
		{0x89, 0xa7}, {0xc9, 0xa7}, {0xa7, 0xc9}, {0xa7, 0x89},
		{0xa7, 0xc5}, {0xa7, 0x85}, {0x85, 0xa7}, {0xc5, 0xa7},
		{0x85, 0xa3}, {0xc5, 0xa3}, {0xa3, 0xc5}, {0xa3, 0x85},
		{0xc8, 0xc8}, {0xc6, 0xc6}, {0xc9, 0xc9}, {0xc5, 0xc5}
	};

	/** ��λ��(ǰ�к�)���������ӣ���Ӧ������ */
	private static final short[] XY_TO_SQ = {
		0x3b, 0x4b, 0x5b, 0x6b, 0x7b, 0x8b, 0x9b, 0xab, 0xbb, 0xcb,
		0x3a, 0x4a, 0x5a, 0x6a, 0x7a, 0x8a, 0x9a, 0xaa, 0xba, 0xca,
		0x39, 0x49, 0x59, 0x69, 0x79, 0x89, 0x99, 0xa9, 0xb9, 0xc9,
		0x38, 0x48, 0x58, 0x68, 0x78, 0x88, 0x98, 0xa8, 0xb8, 0xc8,
		0x37, 0x47, 0x57, 0x67, 0x77, 0x87, 0x97, 0xa7, 0xb7, 0xc7,
		0x36, 0x46, 0x56, 0x66, 0x76, 0x86, 0x96, 0xa6, 0xb6, 0xc6,
		0x35, 0x45, 0x55, 0x65, 0x75, 0x85, 0x95, 0xa5, 0xb5, 0xc5,
		0x34, 0x44, 0x54, 0x64, 0x74, 0x84, 0x94, 0xa4, 0xb4, 0xc4,
		0x33, 0x43, 0x53, 0x63, 0x73, 0x83, 0x93, 0xa3, 0xb3, 0xc3
	};

	/** ������������λ��(ǰ�к�)����Ӧ������ */
	private static byte[] SQ_TO_XY = {
		0, 0, 0,  0,  0,  0,  0,  0,  0,  0,  0, 0, 0, 0, 0, 0,
		0, 0, 0,  0,  0,  0,  0,  0,  0,  0,  0, 0, 0, 0, 0, 0,
		0, 0, 0,  0,  0,  0,  0,  0,  0,  0,  0, 0, 0, 0, 0, 0,
		0, 0, 0, 80, 70, 60, 50, 40, 30, 20, 10, 0, 0, 0, 0, 0,
		0, 0, 0, 81, 71, 61, 51, 41, 31, 21, 11, 1, 0, 0, 0, 0,
		0, 0, 0, 82, 72, 62, 52, 42, 32, 22, 12, 2, 0, 0, 0, 0,
		0, 0, 0, 83, 73, 63, 53, 43, 33, 23, 13, 3, 0, 0, 0, 0,
		0, 0, 0, 84, 74, 64, 54, 44, 34, 24, 14, 4, 0, 0, 0, 0,
		0, 0, 0, 85, 75, 65, 55, 45, 35, 25, 15, 5, 0, 0, 0, 0,
		0, 0, 0, 86, 76, 66, 56, 46, 36, 26, 16, 6, 0, 0, 0, 0,
		0, 0, 0, 87, 77, 67, 57, 47, 37, 27, 17, 7, 0, 0, 0, 0,
		0, 0, 0, 88, 78, 68, 58, 48, 38, 28, 18, 8, 0, 0, 0, 0,
		0, 0, 0, 89, 79, 69, 59, 49, 39, 29, 19, 9, 0, 0, 0, 0,
		0, 0, 0,  0,  0,  0,  0,  0,  0,  0,  0, 0, 0, 0, 0, 0,
		0, 0, 0,  0,  0,  0,  0,  0,  0,  0,  0, 0, 0, 0, 0, 0,
		0, 0, 0,  0,  0,  0,  0,  0,  0,  0,  0, 0, 0, 0, 0, 0,
	};

	private static int digit2Char(int n) {
		return n < 0 || n > 9 ? ' ' : '1' + n;
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
			return c == 'e' ? 2 : c == 'h' ? 3 : PIECE_TO_CHAR.indexOf(c - 'a' + 'A');
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
		return dir < 0 ? -1 : dir + DIRECT_TO_POS;
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

	private static int xy2Sq(int x, int y, int sd) {
		int sq = XY_TO_SQ[x * 10 + y];
		return sd == 0 ? sq : Position.SQUARE_FLIP(sq);
	}

	private static boolean findPiece(int pt, int x, int y, Position p) {
		return p.squares[xy2Sq(x, y, p.sdPlayer)] == Position.SIDE_TAG(p.sdPlayer) + pt;
	}

	/** WXF��ʾת��Ϊ�ڲ��ŷ���ʾ */
	public static int file2Move(String strFile, Position p) {
		// ���߷��ű�ʾת��Ϊ�ڲ��ŷ���ʾ��ͨ����Ϊ���¼������裺

		// 1. ������߷����Ƿ�����(ʿ)��(��)��28�̶ֹ����߱�ʾ��
		// ����֮ǰ���ȱ�������֡�Сд�Ȳ�ͳһ�ĸ�ʽת��Ϊͳһ��ʽ��
		char[] cFile = strFile.toCharArray();
		switch (cFile[0]) {
		case 'a':
			cFile[0] = 'A';
			break;
		case 'b':
		case 'E':
		case 'e':
			cFile[0] = 'B';
			break;
		}
		if (cFile[3] == 'p') {
			cFile[3] = 'P';
		}
		String strFile2 = new String(cFile);
		for (int i = 0; i < FIX_FILE.length; i ++) {
			if (strFile2.equals(FIX_FILE[i])) {
				if (p.sdPlayer == 0) {
					return Position.MOVE(FIX_MOVE[i][0], FIX_MOVE[i][1]);
				}
				return Position.MOVE(Position.SQUARE_FLIP(FIX_MOVE[i][0]),
						Position.SQUARE_FLIP(FIX_MOVE[i][1]));
			}
		}

		// 2. ���������28�̶ֹ����߱�ʾ����ô�����ӡ�λ�ú��������(�к�)��������
		int pt;
		int pos = char2Direct(cFile[0]);
		if (pos < 0) {
			pt = char2Piece(cFile[0]);
			pos = char2Pos(cFile[1]);
		} else {
			pt = char2Piece(cFile[1]);
			pos += DIRECT_TO_POS;
		}
		if (pt < 0) {
			return 0;
		}
		int xSrc = -1, ySrc = -1;
		if (pos < 0) {

			// 3. ������������кű�ʾ�ģ���ô����ֱ�Ӹ����������ҵ�������ţ�
			xSrc = char2Digit(cFile[1]);
			if (xSrc < 0) {
				return 0;
			}
			for (ySrc = 0; ySrc < MAX_RANK; ySrc ++) {
				if (findPiece(pt, xSrc, ySrc, p)) {
					break;
				}
			}
			if (ySrc == MAX_RANK) {
				return 0;
			}
		} else {

			// 4. �����������λ�ñ�ʾ�ģ���ô���밴˳���ҵ����������е����ӣ�
			if (pos >= DIRECT_TO_POS) {
				pos -= DIRECT_TO_POS;
			}
			for (int x = 0; x < MAX_FILE; x ++) {
				for (int y = 0; y < MAX_RANK; y ++) {
					if (!findPiece(pt, x, y, p)) {
						continue;
					}
					// ע�⣺�ų�һ����ֻ��һö���ӵ����
					int n = 0;
					for (int yy = 0; yy < MAX_RANK && n <= 1; yy ++) {
						if (findPiece(pt, x, yy, p)) {
							n ++;
						}
					}
					if (n == 1) {
						break;
					}
					xSrc = x;
					ySrc = y;
					// �ж��Ƿ񵽴�����Ӧ��λ��
					pos --;
					if (pos < 0) {
						x = MAX_FILE;
						break;
					}
				}
			}
			if (xSrc < 0 || ySrc < 0) {
				return 0;
			}
		}

		// 6. ������֪���ŷ�����㣬�Ϳ��Ը������߱�ʾ�ĺ�����������ȷ���ŷ����յ㣻
		int xDst, yDst;
		int n = char2Digit(cFile[3]);
		if (n < 0) {
			return 0;
		}
		if (pt == Position.PIECE_KNIGHT) {
			xDst = n;
			if (cFile[2] == '+') {
				yDst = ySrc - 3 + Math.abs(xDst - xSrc);
			} else {
				yDst = ySrc + 3 - Math.abs(xDst - xSrc);
			}
		} else {
			if (cFile[2] == '+') {
				xDst = xSrc;
				yDst = ySrc - n - 1;
			} else if (cFile[2] == '-') {
				xDst = xSrc;
				yDst = ySrc + n + 1;
			} else {
				xDst = n;
				yDst = ySrc;
			}
		}
		if (yDst < 0 || yDst >= MAX_RANK) {
			return 0;
		}
		return Position.MOVE(xy2Sq(xSrc, ySrc, p.sdPlayer), xy2Sq(xDst, yDst, p.sdPlayer));
	}

	private static int XY_RANK_Y(int sq) {
		return sq % 10;
	}

	private static int XY_FILE_X(int sq) {
		return sq / 10;
	}

	/** �ڲ��ŷ���ʾת��ΪWXF��ʾ */
	public static String move2File(int mv, Position p) {
		char[] cFile = new char[4];
		// ���߷��ű�ʾת��Ϊ�ڲ��ŷ���ʾ��ͨ����Ϊ���¼������裺

		// 1. ������߷����Ƿ�����(ʿ)��(��)��28�̶ֹ����߱�ʾ������֮ǰ���ȱ�������֡�Сд�Ȳ�ͳһ�ĸ�ʽת��Ϊͳһ��ʽ��
		int sqSrc = Position.SRC(mv);
		int sqDst = Position.DST(mv);
		if (sqSrc == 0 || sqDst == 0) {
			return "    ";
		}
		// ��һ���ַ�������
		int pc = p.squares[sqSrc];
		if (pc == 0) {
			return "    ";
		}
		int pt = pc & 7;
		cFile[0] = PIECE_TO_CHAR.charAt(pt);
		int xSrc, ySrc, xDst, yDst;
		if (p.sdPlayer == 0) {
			xSrc = XY_FILE_X(SQ_TO_XY[sqSrc]);
			ySrc = XY_RANK_Y(SQ_TO_XY[sqSrc]);
			xDst = XY_FILE_X(SQ_TO_XY[sqDst]);
			yDst = XY_RANK_Y(SQ_TO_XY[sqDst]);
		} else {
			xSrc = XY_FILE_X(SQ_TO_XY[Position.SQUARE_FLIP(sqSrc)]);
			ySrc = XY_RANK_Y(SQ_TO_XY[Position.SQUARE_FLIP(sqSrc)]);
			xDst = XY_FILE_X(SQ_TO_XY[Position.SQUARE_FLIP(sqDst)]);
			yDst = XY_RANK_Y(SQ_TO_XY[Position.SQUARE_FLIP(sqDst)]);
		}

		// �ڶ����ַ���λ��
		if (pt >= Position.PIECE_KING && pt <= Position.PIECE_BISHOP) {
			cFile[1] = (char) digit2Char(xSrc);
		} else {
			// ���������ϵ����м�ö��ͬ������
			int x = Position.FILE_X(Position.SRC(mv));
			int y = 0;
			for (; y < MAX_RANK; y ++) {
				if (y != ySrc && findPiece(pt, xSrc, y, p)) {
					break;
				}
			}
			// ����n��y��ֵ�ж�ǰ�к�
			if (y == MAX_RANK) {
				// ������û����ͬ���֣����������ʾ
				cFile[1] = (char) digit2Char(xSrc);
			} else if (pt != Position.PIECE_PAWN) {
				// ���Ǳ�������ǰ���ʾ
				cFile[1] = (y < ySrc ? '-' : '+');
			} else {
				int[] nFile = new int[9];
				// ����Ǳ���Ҫ�ж����������ϵı�����ô�ֲ���
				for (x = 0; x < MAX_FILE; x ++) {
					nFile[x] = 0;
					for (y = 0; y < MAX_RANK; y ++) {
						if (findPiece(Position.PIECE_PAWN, x, y, p)) {
							nFile[x] ++;
						}
					}
				}
				int nTotal = 0; // �ж�����ȫ�������ϣ����м�������
				int nCurr = -1; // �ж�����ȫ�������ϣ���ǰ�����ǵڼ���
				for (x = 0; x < MAX_FILE; x ++) {
					if (nFile[x] > 1) {
						for (y = 0; y < MAX_RANK; y ++) {
							if (findPiece(pt, x, y, p)) {
								nTotal ++;
								if (x == xSrc && y == ySrc) {
									nCurr = nTotal;
								}
							}
						}
					}
				}
				if (nTotal == 2) {
					cFile[1] = (nCurr == 1 ? '-' : '+');
				} else if (nTotal == 3) {
					cFile[1] = (nCurr == 1 ? '-' : nCurr == 2 ? ':' : '+');
				} else {
					cFile[1] = (char) ('a' + nTotal - nCurr);
				}
			}
		}

		// �������ĸ��ַ��������Ŀ��
		if (pt >= Position.PIECE_ADVISOR && pt <= Position.PIECE_KNIGHT) {
			if (sqSrc == sqDst) {
				cFile[2] = '=';
				cFile[3] = 'P';
			} else {
				cFile[2] = (yDst > ySrc ? '-' : '+');
				cFile[3] = (char) digit2Char(xDst);
			}
		} else {
			cFile[2] = (yDst == ySrc ? '.' : yDst > ySrc ? '-' : '+');
			cFile[3] = (char) (yDst == ySrc ? digit2Char(xDst) :
					digit2Char(Math.abs(ySrc - yDst) - 1));
		}
		return String.valueOf(cFile);
	}

	/** ���ı�ʾת��ΪWXF��ʾ */
	public static String chin2File(String strChin) {
		char[] cChin = strChin.toCharArray();
		char[] cFile = new char[4];
		int pos = word2Pos(cChin[0]);
		cFile[0] = (char) piece2Char(word2Piece(pos < 0 ? cChin[0] : cChin[1]));
		cFile[1] = (char) (pos < 0 ? digit2Char(word2Digit(cChin[1])) : pos2Char(pos));
		if ((cChin[2] == '��' || cChin[2] == '��' || cChin[2] == '׃') &&
				word2Piece(cChin[3]) == 6) { // ��[׃]
			cFile[2] = '=';
			cFile[3] = 'P';
		} else {
			cFile[2] = (char) direct2Char(word2Direct(cChin[2]));
			cFile[3] = (char) digit2Char(word2Digit(cChin[3]));
		}
		return new String(cFile);
	}

	/** WXF��ʾת��Ϊ���ı�ʾ */
	public static String file2Chin(String strFile, int sd) {
		char[] cFile = strFile.toCharArray();
		char[] cChin = new char[4];
		int pos = char2Pos(cFile[0]);
		if (pos < 0) {
			pos = char2Pos(cFile[1]);
			cChin[0] = (pos < 0 ? PIECE_TO_WORD[sd].charAt(char2Piece(cFile[0])) :
					POS_TO_WORD[0].charAt(pos));
			cChin[1] = (pos < 0 ? DIGIT_TO_WORD[sd].charAt(char2Digit(cFile[1])) :
					PIECE_TO_WORD[sd].charAt(char2Piece(cFile[0])));
		} else {
			cChin[0] = POS_TO_WORD[0].charAt(pos + DIRECT_TO_POS);
			cChin[1] = PIECE_TO_WORD[sd].charAt(char2Piece(cFile[0]));
		}
		if (cFile[2] == '-' && char2Piece(cFile[3]) == Position.PIECE_PAWN) {
			cChin[2] = '��';
			cChin[3] = PIECE_TO_WORD[sd].charAt(Position.PIECE_PAWN);
		} else {
			cChin[2] = DIRECT_TO_WORD[0].charAt(char2Direct(cFile[2]));
			cChin[3] = DIGIT_TO_WORD[sd].charAt(char2Digit(cFile[3]));
		}
		return String.valueOf(cChin);
	}

	/** ICCS��ʾת��Ϊ�ڲ��ŷ���ʾ */
	public static int iccs2Move(String strIccs) {
		char[] cIccs = strIccs.toCharArray();
		if (cIccs[0] < 'A' || cIccs[0] > 'I' || cIccs[1] < '0' || cIccs[1] > '9' ||
				cIccs[3] < 'A' || cIccs[3] > 'I' || cIccs[4] < '0' || cIccs[4] > '9') {
			return 0;
		}
		int sqSrc = Position.COORD_XY(cIccs[0] - 'A' + Position.FILE_LEFT,
				'9' + Position.RANK_TOP - cIccs[1]);
		int sqDst = Position.COORD_XY(cIccs[3] - 'A' + Position.FILE_LEFT,
				'9' + Position.RANK_TOP - cIccs[4]);
		return Position.MOVE(sqSrc, sqDst);
	}

	/** �ڲ��ŷ���ʾת��ΪICCS��ʾ */
	public static String move2Iccs(int mv) {
		char[] cIccs = new char[5];
		int src = Position.SRC(mv);
		int dst = Position.DST(mv);
		cIccs[0] = (char) ('A' + Position.FILE_X(src) - Position.FILE_LEFT);
		cIccs[1] = (char) ('9' - Position.RANK_Y(src) + Position.RANK_TOP);
		cIccs[2] = '-';
		cIccs[3] = (char) ('A' + Position.FILE_X(dst) - Position.FILE_LEFT);
		cIccs[4] = (char) ('9' - Position.RANK_Y(dst) + Position.RANK_TOP);
		return String.valueOf(cIccs);
	}
}