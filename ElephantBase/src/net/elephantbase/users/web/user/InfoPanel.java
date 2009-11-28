package net.elephantbase.users.web.user;

import net.elephantbase.users.biz.BaseSession;
import net.elephantbase.users.biz.UserData;
import net.elephantbase.users.web.BasePanel;
import net.elephantbase.users.web.UsersPage;

import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.link.Link;

public class InfoPanel extends BasePanel {
	private static final long serialVersionUID = 1L;

	private Label lblScore = new Label("lblScore", "");
	private Label lblPoints = new Label("lblPoints", "");
	private Label lblCharged = new Label("lblCharged", "");
	private Link<Void> lnkAdmin = new Link<Void>("lnkAdmin") {
		private static final long serialVersionUID = 1L;

		@Override
		public void onClick() {
			setResponsePanel(UsersPage.getAdminPanels());
		}
	};

	boolean admin;

	public InfoPanel() {
		super("�û���Ϣ", UsersPage.SUFFIX, NEED_AUTH);
		add(lblScore, lblPoints, lblCharged, lnkAdmin);
	}

	@Override
	protected void onBeforeRender() {
		BaseSession session = getUserSession();
		if (session == null) {
			super.onBeforeRender();
			return;
		}
		UserData data = session.getData();
		lblScore.setDefaultModelObject("" + data.getScore());
		lblPoints.setDefaultModelObject(data.getPoints() == 0 ? "" :
				"������ " + data.getPoints() + " �����");
		lblCharged.setDefaultModelObject(data.isDiamond() ?
				"�������ǣ���ʯ��Ա�û�" : data.isPlatinum() ?
				"�������ǣ��׽��Ա�û�" : "");
		lnkAdmin.setVisible(data.isAdmin());
		super.onBeforeRender();
	}
}