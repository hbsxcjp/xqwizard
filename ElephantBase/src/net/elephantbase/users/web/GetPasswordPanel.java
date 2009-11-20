package net.elephantbase.users.web;

import net.elephantbase.users.biz.CaptchaValidator;
import net.elephantbase.users.biz.Login;
import net.elephantbase.util.Smtp;

import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.form.Form;
import org.apache.wicket.markup.html.form.RequiredTextField;
import org.apache.wicket.model.Model;

public class GetPasswordPanel extends BasePanel {
	private static final long serialVersionUID = 1L;

	CaptchaValidator captcha = new CaptchaValidator("txtCaptcha", "imgCaptcha");

	public GetPasswordPanel() {
		super("�һ�����", UsersPage.SUFFIX, NO_AUTH);

		final Label lblWarn = new Label("lblWarn", "");
		lblWarn.setVisible(false);
		add(lblWarn);

		final RequiredTextField<String> txtUsername = new
				RequiredTextField<String>("txtUsername", Model.of(""));
		final RequiredTextField<String> txtEmail = new
				RequiredTextField<String>("txtEmail", Model.of("@"));

		Form<Void> frm = new Form<Void>("frm") {
			private static final long serialVersionUID = 1L;

			@Override
			protected void onSubmit() {
				lblWarn.setVisible(true);
				if (!captcha.validate()) {
					lblWarn.setDefaultModelObject("��֤�����");
					return;
				}
				String username = txtUsername.getModelObject();
				String email = txtEmail.getModelObject();
				if (!email.equals(Login.getEmail(username))) {
					lblWarn.setDefaultModelObject("�û�����Email��ƥ��");
					return;
				}
				String password = Login.getSalt();
				StringBuilder sb = new StringBuilder();
				sb.append(username + "�����ã�\r\n\r\n");
				sb.append("�������������ѱ�����Ϊ��" + password + "\r\n");
				sb.append("�������ô������¼��������ʦ�û����ģ�\r\n");
				sb.append("��������http://www.elephantbase.net:8080/users/\r\n");
				sb.append("������¼�ɹ��������ϰ�����ĵ���\r\n\r\n");
				sb.append("������л��ʹ��������ʦ��\r\n\r\n");
				sb.append("������ʦ�û�����");
				if (Smtp.send(email, username + "�������ѱ�����", sb.toString())) {
					Login.updateInfo(username, email, password);
					setResponsePanel(new GetPasswordPanel2());
				} else {
					lblWarn.setDefaultModelObject("����Emailʧ�ܣ����Ժ�����");
				}
			}
		};
		frm.add(txtUsername, txtEmail);
		frm.add(captcha);
		add(frm);
	}

	@Override
	protected void onBeforeRender() {
		captcha.reset();
		super.onBeforeRender();
	}
}