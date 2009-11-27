package net.elephantbase.users.web.user;

import net.elephantbase.users.biz.BaseSession;
import net.elephantbase.users.biz.EventLog;
import net.elephantbase.users.biz.Users;
import net.elephantbase.users.web.BasePanel;

import org.apache.wicket.markup.html.form.Form;
import org.apache.wicket.markup.html.form.PasswordTextField;
import org.apache.wicket.markup.html.form.TextField;
import org.apache.wicket.model.Model;

public class UpdatePanel extends BasePanel {
	private static final long serialVersionUID = 1L;

	public UpdatePanel() {
		super("������Ϣ");
	}

	@Override
	protected void onLoad() {
		final BaseSession session = (BaseSession) getSession();

		final PasswordTextField txtPassword0 = new
				PasswordTextField("txtPassword0", Model.of(""));
		final PasswordTextField txtPassword1 = new
				PasswordTextField("txtPassword1", Model.of(""));
		final PasswordTextField txtPassword2 = new
				PasswordTextField("txtPassword2", Model.of(""));
		final TextField<String> txtEmail = new
				TextField<String>("txtEmail", Model.of(session.getEmail()));
		txtPassword0.setRequired(false);
		txtPassword1.setRequired(false);
		txtPassword2.setRequired(false);

		Form<Void> frm = new Form<Void>("frm") {
			private static final long serialVersionUID = 1L;

			@Override
			protected void onSubmit() {
				String email = txtEmail.getModelObject();
				if (email == null || !Users.validateEmail(email)) {
					setWarn("Email�����Ϲ��");
					return;
				}
				int uid = session.getUid();
				String password1 = txtPassword1.getModelObject();
				String password2 = txtPassword2.getModelObject();
				if (password1 == null || password1.length() < 6) {
					Users.setEmail(uid, email);
					setInfo("Email���³ɹ�");
					EventLog.log(uid, EventLog.EMAIL, 0);
					return;
				}
				if (!password1.equals(password2)) {
					setWarn("�������벻һ��");
					return;
				}
				String password0 = txtPassword0.getModelObject();
				if (Users.login(session.getUsername(), password0) != uid) {
					setWarn("ԭ�������");
					return;
				}
				Users.setPassword(uid, password1, email);
				session.setEmail(email);
				setInfo("Email��������³ɹ�");
				EventLog.log(uid, EventLog.PASSWORD, 0);
			}
		};
		frm.add(txtPassword0, txtPassword1, txtPassword2, txtEmail);
		add(frm);
	}
}