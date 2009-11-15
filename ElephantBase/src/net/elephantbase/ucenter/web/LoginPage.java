package net.elephantbase.ucenter.web;

import net.elephantbase.ucenter.BaseSession;
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
						int uid = ((BaseSession) getSession()).login(
								mdlUsername.getObject(), mdlPassword.getObject(),
								mdlCookie.getObject().booleanValue());
						if (uid > 0) {
							BasePage.setResponsePage(redirectPage);
						} else if (uid < 0) {
							lblInfo.setDefaultModelObject("�޷����ӵ�������ʦ�û����ģ����Ժ�����");
						} else {
							lblInfo.setDefaultModelObject("�û��������벻��ȷ");
						}
					}
				}
			};
			frm.add(new TextField<String>("txtUsername", mdlUsername),
					new PasswordTextField("txtPassword", mdlPassword),
					new TextField<String>("txtCaptcha", mdlCaptcha),
					new Image("imgCaptcha", new CaptchaImageResource(captcha)),
					new CheckBox("chkSave", mdlCookie));
			add(frm);
		}
	}

	Label lblInfo = new Label("lblInfo", "");

	BasePage redirectPage;

	public LoginPage(BasePage redirectPage) {
		super("��¼ - " + redirectPage.getSuffix(), NO_AUTH);
		this.redirectPage = redirectPage;
		lblInfo.setVisible(false);
		LoginPanel panel = new LoginPanel();
		panel.add(lblInfo);
		add(panel);
	}
}