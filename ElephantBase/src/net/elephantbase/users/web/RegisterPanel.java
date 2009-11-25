package net.elephantbase.users.web;

import net.elephantbase.users.biz.EventLog;
import net.elephantbase.users.biz.Users;
import net.elephantbase.util.wicket.CaptchaPanel;
import net.elephantbase.util.wicket.WicketUtil;

import org.apache.wicket.markup.html.WebMarkupContainer;
import org.apache.wicket.markup.html.form.Form;
import org.apache.wicket.markup.html.form.PasswordTextField;
import org.apache.wicket.markup.html.form.RequiredTextField;
import org.apache.wicket.markup.html.link.Link;
import org.apache.wicket.model.Model;

public class RegisterPanel extends BasePanel {
	private static final long serialVersionUID = 1L;

	CaptchaPanel captcha = new CaptchaPanel("pnlCaptcha");

	public RegisterPanel(final BasePanel... redirectPanels) {
		super("ע��", redirectPanels[0].getSuffix(), NO_AUTH);

		final WebMarkupContainer lblWelcome = new WebMarkupContainer("lblWelcome");
		Link<Void> lnkLogin = new Link<Void>("lnkLogin") {
			private static final long serialVersionUID = 1L;

			@Override
			public void onClick() {
				setResponsePanel(new LoginPanel(redirectPanels));
			}
		};
		lblWelcome.add(lnkLogin);
		add(lblWelcome);

		final RequiredTextField<String> txtUsername = new
				RequiredTextField<String>("txtUsername", Model.of(""));
		final PasswordTextField txtPassword = new
				PasswordTextField("txtPassword", Model.of(""));
		final PasswordTextField txtPassword2 = new
				PasswordTextField("txtPassword2", Model.of(""));
		final RequiredTextField<String> txtEmail = new
				RequiredTextField<String>("txtEmail", Model.of("@"));

		Form<Void> frm = new Form<Void>("frm") {
			private static final long serialVersionUID = 1L;

			@Override
			protected void onSubmit() {
				lblWelcome.setVisible(false);
				if (!captcha.validate()) {
					setWarn("��֤�����");
					return;
				}
				String password = txtPassword.getModelObject();
				if (!password.equals(txtPassword2.getModelObject())) {
					setWarn("�������벻һ��");
					return;
				}
				String username = txtUsername.getModelObject();
				if (username.getBytes().length < 6 || password.length() < 6) {
					setWarn("�û��������붼��������6���ַ�");
					return;
				}
				String email = txtEmail.getModelObject();
				if (!Users.validateEmail(email)) {
					setWarn("Email�����Ϲ��");
					return;
				}
				int uid = Users.register(username, password, email,
						WicketUtil.getServletRequest().getRemoteHost());
				if (uid <= 0) {
					setWarn("�Ѿ�����ͬ�������û�");
					return;
				}
				LoginPanel panel = new LoginPanel(redirectPanels);
				panel.setInfo("���ѳɹ�ע�����ʺ�[" + username + "]�����ھͿ��Ե�¼��");
				EventLog.log(uid, EventLog.EVENT_REGISTER, 0);
				setResponsePanel(panel);
			}
		};
		frm.add(txtUsername, txtPassword, txtPassword2, txtEmail, captcha);
		add(frm);
	}
}