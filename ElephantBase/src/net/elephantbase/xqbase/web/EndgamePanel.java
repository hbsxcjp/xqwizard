package net.elephantbase.xqbase.web;

import net.elephantbase.users.web.BasePanel;

public class EndgamePanel extends BasePanel {
	private static final long serialVersionUID = 1L;

	/** @param fen */
	public EndgamePanel(String fen) {
		super("�����о�", XQBasePage.SUFFIX, WANT_AUTH);
		setInfo("�����оֹ������ڿ����У������½⡭��");
	}
}