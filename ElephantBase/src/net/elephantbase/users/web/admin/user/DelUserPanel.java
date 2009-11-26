package net.elephantbase.users.web.admin.user;

import net.elephantbase.users.biz.EventLog;
import net.elephantbase.users.biz.UserDetail;
import net.elephantbase.users.biz.Users;
import net.elephantbase.users.web.BasePanel;
import net.elephantbase.users.web.ClosePanel;

import org.apache.wicket.markup.html.form.Form;
import org.apache.wicket.markup.html.form.PasswordTextField;
import org.apache.wicket.model.Model;

public class DelUserPanel extends BasePanel {
	private static final long serialVersionUID = 1L;

	public DelUserPanel(final UserDetail user) {
		super("ɾ���û�");

		final PasswordTextField txtPassword = new
				PasswordTextField("txtPassword", Model.of(""));
		Form<Void> frm = new Form<Void>("frm") {
			private static final long serialVersionUID = 1L;

			@Override
			protected void onSubmit() {
				String password = txtPassword.getModelObject();
				int uid = Users.login(user.username, password);
				if (uid != user.uid) {
					setWarn("���벻��ȷ");
					return;
				}
				Users.delUser(uid);
				ClosePanel panel = new ClosePanel("����");
				panel.setInfo("�û�[" + user.username + "]�Ѿ���ɾ��");
				EventLog.log(uid, EventLog.ADMIN_DELETE, 0);
				setResponsePanel(panel);
			}
		};
		frm.add(txtPassword);
		add(frm);
	}
}