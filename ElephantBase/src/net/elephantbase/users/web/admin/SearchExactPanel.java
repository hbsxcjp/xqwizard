package net.elephantbase.users.web.admin;

import net.elephantbase.users.web.BasePanel;
import net.elephantbase.users.web.UsersPage;

public class SearchExactPanel extends BasePanel {
	private static final long serialVersionUID = 1L;

	public SearchExactPanel() {
		super("����", UsersPage.SUFFIX, NEED_AUTH);
	}

	@Override
	protected void onBeforeRender() {
		setTitle("��ȷ����");
		super.onBeforeRender();
	}
}