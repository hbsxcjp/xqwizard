package net.elephantbase.ucenter.web;

import javax.servlet.http.Cookie;

import net.elephantbase.ucenter.Login;
import net.elephantbase.util.Bytes;
import net.elephantbase.util.wicket.CaptchaImageResource;

import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.form.CheckBox;
import org.apache.wicket.markup.html.form.Form;
import org.apache.wicket.markup.html.form.PasswordTextField;
import org.apache.wicket.markup.html.form.TextField;
import org.apache.wicket.markup.html.image.Image;
import org.apache.wicket.markup.html.panel.Panel;
import org.apache.wicket.model.Model;
import org.apache.wicket.protocol.http.WebResponse;

public class LoginPage extends BasePage {
	public class LoginPanel extends Panel {
		private static final long serialVersionUID = 1L;

		public LoginPanel() {
			super(MAIN_PANEL_ID);
			final Model<String> mdlUsername = new Model<String>();
			final Model<String> mdlPassword = new Model<String>();
			final Model<String> mdlCaptcha = new Model<String>();
			final Model<Boolean> mdlCookie = new Model<Boolean>(Boolean.TRUE);
			final String captcha = Bytes.toHexUpper(Bytes.random(2));

			Form<Void> frm = new Form<Void>("frm") {
				private static final long serialVersionUID = 1L;

				@Override
				protected void onSubmit() {
					lblInfo.setVisible(true);
					if (!captcha.equals(mdlCaptcha.getObject())) {
						lblInfo.setDefaultModelObject("��֤�����");
					} else {
						int uid = Login.login(mdlUsername.getObject(), mdlPassword.getObject());
						if (uid > 0) {
							if (mdlCookie.getObject().booleanValue()) {
								Cookie cookie = new Cookie("login", Login.addCookie(uid));
								cookie.setMaxAge(86400 * 30);
								((WebResponse) getResponse()).addCookie(cookie);
							}
							((BaseSession) getSession()).setUid(uid);
							BasePage.setResponsePage(redirectPage);
						} else if (uid < 0) {
							lblInfo.setDefaultModelObject("�޷����ӵ�������ʦ�û����ģ����Ժ�����");
						} else {
							lblInfo.setDefaultModelObject("�û��������벻��ȷ");
						}
					}
				}
			};
			frm.add(new TextField<String>("txtUsername", mdlUsername));
			frm.add(new PasswordTextField("txtPassword", mdlPassword));
			frm.add(new TextField<String>("txtCaptcha", mdlCaptcha));
			frm.add(new Image("imgCaptcha", new CaptchaImageResource(captcha)));
			frm.add(new CheckBox("chkSave", mdlCookie));
			add(frm);
		}
	}

	Label lblInfo = new Label("lblInfo", "");

	BasePage redirectPage;

	public LoginPage(BasePage redirectPage) {
		super("��¼", NO_AUTH);
		this.redirectPage = redirectPage;
		lblInfo.setVisible(false);
		LoginPanel panel = new LoginPanel();
		panel.add(lblInfo);
		add(panel);
	}
}