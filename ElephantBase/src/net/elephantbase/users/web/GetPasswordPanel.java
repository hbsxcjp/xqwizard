package net.elephantbase.users.web;

import net.elephantbase.users.Login;
import net.elephantbase.util.Smtp;
import net.elephantbase.util.wicket.CaptchaImageResource;

import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.form.Form;
import org.apache.wicket.markup.html.form.RequiredTextField;
import org.apache.wicket.markup.html.form.TextField;
import org.apache.wicket.markup.html.image.Image;
import org.apache.wicket.model.Model;

public class GetPasswordPanel extends BasePanel {
	private static final long serialVersionUID = 1L;

	String captcha;
	Image imgCaptcha = new Image("imgCaptcha");
	TextField<String> txtCaptcha = new TextField<String>("txtCaptcha", Model.of(""));

	public GetPasswordPanel() {
		super("�һ�����", UsersPage.SUFFIX, NO_AUTH);

		final Label lblInfo = new Label("lblInfo", "");
		final Label lblWarn = new Label("lblWarn", "");
		lblInfo.setVisible(true);
		lblWarn.setVisible(false);
		add(lblInfo, lblWarn);

		final RequiredTextField<String> txtUsername = new RequiredTextField<String>("txtUsername", Model.of(""));
		final RequiredTextField<String> txtEmail = new RequiredTextField<String>("txtEmail", Model.of("@"));

		Form<Void> frm = new Form<Void>("frm") {
			private static final long serialVersionUID = 1L;

			@Override
			protected void onSubmit() {
				lblInfo.setVisible(false);
				lblWarn.setVisible(true);
				if (!txtCaptcha.getModelObject().toLowerCase().equals(captcha)) {
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
		frm.add(txtUsername, txtEmail, txtCaptcha, imgCaptcha);
		add(frm);
	}

	@Override
	protected void onBeforeRender() {
		captcha = Login.getSalt();
		txtCaptcha.setModelObject("");
		imgCaptcha.setImageResource(new CaptchaImageResource(captcha));
		super.onBeforeRender();
	}
}