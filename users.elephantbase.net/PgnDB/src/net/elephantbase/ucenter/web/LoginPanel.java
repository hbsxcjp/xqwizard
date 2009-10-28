package net.elephantbase.ucenter.web;

import java.util.logging.Logger;

import net.elephantbase.ucenter.Login;
import net.elephantbase.util.Bytes;
import net.elephantbase.util.LoggerFactory;
import net.elephantbase.util.wicket.CaptchaImageResource;

import org.apache.wicket.markup.html.form.Form;
import org.apache.wicket.markup.html.form.PasswordTextField;
import org.apache.wicket.markup.html.form.TextField;
import org.apache.wicket.markup.html.image.Image;
import org.apache.wicket.markup.html.panel.Panel;
import org.apache.wicket.model.Model;

public class LoginPanel extends Panel {
	private static final long serialVersionUID = 1L;

	static Logger logger = LoggerFactory.getLogger();

	public LoginPanel(String id) {
		super(id);
		final Model<String> mdlUsername = new Model<String>();
		final Model<String> mdlPassword = new Model<String>();
		final Model<String> mdlCaptcha = new Model<String>();
		final String captcha = Bytes.toHexUpper(Bytes.random(2));
		
		Form<Void> frm = new Form<Void>("frm") {
			private static final long serialVersionUID = 1L;

			@Override
			protected void onSubmit() {
				LoginPage loginPage = new LoginPage();
				if (!captcha.equals(mdlCaptcha.getObject())) {
					loginPage.setInfo("��֤�����", "red");
				} else {
					int uid = Login.login(mdlUsername.getObject(), mdlPassword.getObject());
					if (uid > 0) {
						loginPage.setInfo("��¼�ɹ�", "blue");
					} else if (uid < 0) {
						loginPage.setInfo("�޷����ӵ�������ʦ�û����ģ����Ժ�����", "red");
					} else {
						loginPage.setInfo("�û��������벻��ȷ", "red");
					}
				}
				setResponsePage(loginPage);
			}
		};
		frm.add(new TextField<String>("txtUsername", mdlUsername));
		frm.add(new PasswordTextField("txtPassword", mdlPassword));
		frm.add(new TextField<String>("txtCaptcha", mdlCaptcha));
		frm.add(new Image("imgCaptcha", new CaptchaImageResource(captcha)));
		add(frm);
	}
}