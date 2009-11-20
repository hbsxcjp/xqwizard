package net.elephantbase.users.web;

import net.elephantbase.users.BaseSession;
import net.elephantbase.util.Bytes;
import net.elephantbase.util.wicket.CaptchaImageResource;

import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.form.CheckBox;
import org.apache.wicket.markup.html.form.Form;
import org.apache.wicket.markup.html.form.PasswordTextField;
import org.apache.wicket.markup.html.form.TextField;
import org.apache.wicket.markup.html.image.Image;
import org.apache.wicket.markup.html.link.Link;
import org.apache.wicket.model.Model;

public class LoginPanel extends BasePanel {
	private static final long serialVersionUID = 1L;

	String captcha = Bytes.toHexUpper(Bytes.random(2));
	Label lblInfo = new Label("lblInfo", "");
	Label lblWarn = new Label("lblWarn", "");

	public LoginPanel(final BasePanel... redirectPanels) {
		super("��¼", redirectPanels[0].getSuffix(), NO_AUTH);

		lblInfo.setVisible(false);
		lblWarn.setVisible(false);
		add(lblInfo, lblWarn);

		final TextField<String> txtUsername = new TextField<String>("txtUsername", Model.of(""));
		final PasswordTextField txtPassword = new PasswordTextField("txtPassword", Model.of(""));
		final TextField<String> txtCaptcha = new TextField<String>("txtCaptcha", Model.of(""));
		final CheckBox chkSave = new CheckBox("chkSave", Model.of(Boolean.TRUE));

		Link<Void> lnkRegister = new Link<Void>("lnkRegister") {
			private static final long serialVersionUID = 1L;

			@Override
			public void onClick() {
				setResponsePanel(new RegisterPanel(redirectPanels));
			}
		};
		Link<Void> lnkGetPassword = new Link<Void>("lnkGetPassword") {
			private static final long serialVersionUID = 1L;

			@Override
			public void onClick() {
				setResponsePanel(new GetPasswordPanel());
			}
		};

		Form<Void> frm = new Form<Void>("frm") {
			private static final long serialVersionUID = 1L;

			@Override
			protected void onSubmit() {
				lblInfo.setVisible(false);
				lblWarn.setVisible(true);
				if (!txtCaptcha.getModelObject().toUpperCase().equals(captcha)) {
					lblInfo.setDefaultModelObject("��֤�����");
					return;
				}
				int uid = ((BaseSession) getSession()).login(
						txtUsername.getModelObject(), txtPassword.getModelObject(),
						chkSave.getModelObject().booleanValue());
				if (uid > 0) {
					setResponsePanel(redirectPanels);
				} else if (uid < 0) {
					lblInfo.setDefaultModelObject("�޷����ӵ�������ʦ�û����ģ����Ժ�����");
				} else {
					lblInfo.setDefaultModelObject("�û��������벻��ȷ");
				}
			}
		};
		frm.add(txtUsername, txtPassword, txtCaptcha, chkSave);
		frm.add(lnkRegister, lnkGetPassword);
		frm.add(new Image("imgCaptcha", new CaptchaImageResource(captcha)));
		add(frm);
	}

	public void setInfo(String info) {
		lblInfo.setVisible(true);
		lblWarn.setVisible(false);
		lblInfo.setDefaultModelObject(info);
	}
}