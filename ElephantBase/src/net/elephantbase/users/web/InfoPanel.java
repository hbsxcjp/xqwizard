package net.elephantbase.users.web;

import net.elephantbase.users.biz.BaseSession;
import net.elephantbase.users.biz.UserData;
import net.elephantbase.util.wicket.WicketUtil;

import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.link.Link;

public class InfoPanel extends BasePanel {
	private static final long serialVersionUID = 1L;

	public InfoPanel() {
		super("�û���Ϣ", UsersPage.SUFFIX, NEED_AUTH);
	}

	@Override
	protected void onLoad(BaseSession session) {
		UserData user = new UserData(session.getUid(),
				WicketUtil.getServletRequest().getRemoteHost());
		Label lblScore = new Label("lblScore", "" + user.getScore());
		Label lblPoints = new Label("lblPoints", user.getPoints() == 0 ? "" :
				"������ " + user.getPoints() + " �����");
		Label lblCharged = new Label("lblCharged", user.isDiamond() ?
				"�������ǣ���ʯ��Ա�û�" : user.isPlatinum() ?
				"�������ǣ��׽��Ա�û�" : "");
		final boolean admin = user.isAdmin();
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
}