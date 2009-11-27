package net.elephantbase.users.web.user;

import net.elephantbase.users.biz.BaseSession;
import net.elephantbase.users.biz.UserData;
import net.elephantbase.users.web.BasePanel;
import net.elephantbase.users.web.UsersPage;

import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.link.Link;

public class InfoPanel extends BasePanel {
	private static final long serialVersionUID = 1L;

	public InfoPanel() {
		super("�û���Ϣ", UsersPage.SUFFIX, NEED_AUTH);
	}

	@Override
	protected void onLoad() {
		UserData data = ((BaseSession) getSession()).getData();
		Label lblScore = new Label("lblScore", "" + data.getScore());
		Label lblPoints = new Label("lblPoints", data.getPoints() == 0 ? "" :
				"������ " + data.getPoints() + " �����");
		Label lblCharged = new Label("lblCharged", data.isDiamond() ?
				"�������ǣ���ʯ��Ա�û�" : data.isPlatinum() ?
				"�������ǣ��׽��Ա�û�" : "");
		final boolean admin = data.isAdmin();
		Link<Void> lnkAdmin = new Link<Void>("lnkAdmin") {
			private static final long serialVersionUID = 1L;

			@Override
			public void onClick() {
				if (admin) {
					setResponsePanel(UsersPage.getAdminPanels());
				}
			}
		};
		lnkAdmin.setVisible(admin);
		add(lblScore, lblPoints, lblCharged, lnkAdmin);
	}

	@Override
	protected void onLogout() {
		setResponsePanel(UsersPage.getUserPanels());
	}
}