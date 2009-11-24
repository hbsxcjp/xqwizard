package net.elephantbase.users.web;

import net.elephantbase.users.biz.BaseSession;
import net.elephantbase.users.biz.Users;

import org.apache.wicket.markup.html.form.Form;
import org.apache.wicket.markup.html.form.PasswordTextField;
import org.apache.wicket.markup.html.form.RequiredTextField;
import org.apache.wicket.model.Model;

public class UpdatePanel extends BasePanel {
	private static final long serialVersionUID = 1L;

	public UpdatePanel() {
		super("������Ϣ");
	}

	@Override
	protected void init() {
		final String username = ((BaseSession) getSession()).getUsername();

		final PasswordTextField txtPassword0 = new
				PasswordTextField("txtPassword0", Model.of(""));
		final PasswordTextField txtPassword1 = new
				PasswordTextField("txtPassword1", Model.of(""));
		final PasswordTextField txtPassword2 = new
				PasswordTextField("txtPassword2", Model.of(""));
		final RequiredTextField<String> txtEmail = new
				RequiredTextField<String>("txtEmail",
				Model.of(Users.getEmail(username)));
		txtPassword0.setRequired(false);
		txtPassword1.setRequired(false);
		txtPassword2.setRequired(false);

		Form<Void> frm = new Form<Void>("frm") {
			private static final long serialVersionUID = 1L;

			@Override
			protected void onSubmit() {
				String email = txtEmail.getModelObject();
				if (!Users.validateEmail(email)) {
					setWarn("Email�����Ϲ��");
					return;
				}
				String password1 = txtPassword1.getModelObject();
				String password2 = txtPassword2.getModelObject();
				if (password1 == null || password1.length() < 6 ||
						password2 == null || password2.length() < 6) {
					Users.updateInfo(username, email, null);
					setInfo("Email���³ɹ�");
					return;
				}
				if (!password1.equals(password2)) {
					setWarn("�������벻һ��");
				}
				if (Users.login(username, password1) <= 0) {
					setWarn("ԭ�������");
				}
				Users.updateInfo(username, email, password1);
				setInfo("Email��������³ɹ�");
			}
		};
		frm.add(txtPassword0, txtPassword1, txtPassword2, txtEmail);
		add(frm);
	}
}