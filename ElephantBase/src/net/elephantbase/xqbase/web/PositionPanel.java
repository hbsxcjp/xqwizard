package net.elephantbase.xqbase.web;

import net.elephantbase.users.web.BasePanel;

public class PositionPanel extends BasePanel {
	private static final long serialVersionUID = 1L;

	/** @param fen */
	public PositionPanel(String fen) {
		super("��������", XQBasePage.SUFFIX, WANT_AUTH);
		setInfo("�������湦�����ڿ����У������½⡭��");
	}
}