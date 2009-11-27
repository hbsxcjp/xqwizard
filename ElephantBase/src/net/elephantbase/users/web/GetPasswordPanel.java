package net.elephantbase.users.web;

import net.elephantbase.db.DBUtil;
import net.elephantbase.db.Row;
import net.elephantbase.users.biz.EventLog;
import net.elephantbase.users.biz.Users;
import net.elephantbase.util.Smtp;
import net.elephantbase.util.wicket.CaptchaPanel;

import org.apache.wicket.markup.html.form.Form;
import org.apache.wicket.markup.html.form.RequiredTextField;
import org.apache.wicket.model.Model;

public class GetPasswordPanel extends BasePanel {
	private static final long serialVersionUID = 1L;

	CaptchaPanel pnlCaptcha = new CaptchaPanel("pnlCaptcha");

	public GetPasswordPanel() {
		super("�һ�����", UsersPage.SUFFIX, NO_AUTH);

		final RequiredTextField<String> txtUsername = new
				RequiredTextField<String>("txtUsername", Model.of(""));
		final RequiredTextField<String> txtEmail = new
				RequiredTextField<String>("txtEmail", Model.of("@"));

		Form<Void> frm = new Form<Void>("frm") {
			private static final long serialVersionUID = 1L;

			@Override
			protected void onSubmit() {
				if (!pnlCaptcha.validate()) {
					setWarn("��֤�����");
					return;
				}
				String username = txtUsername.getModelObject();
				String email = txtEmail.getModelObject();
				String sql = "SELECT uid, email FROM uc_members WHERE username = ?";
				Row row = DBUtil.query(2, sql, username);
				int uid = row.getInt(1, 0);
				if (uid == 0 || !email.equals(row.getString(1))) {
					setWarn("�û�����Email��ƥ��");
					return;
				}
				String password = Users.getSalt();
				StringBuilder sb = new StringBuilder();
				sb.append(username + "�����ã�\r\n\r\n");
				sb.append("�������������ѱ�����Ϊ��" + password + "\r\n");
				sb.append("�������ô������¼��������ʦ�û����ģ�\r\n");
				sb.append("��������http://www.elephantbase.net:8080/users/\r\n");
				sb.append("������¼�ɹ��������ϰ�����ĵ���\r\n\r\n");
				sb.append("������л��ʹ��������ʦ��\r\n\r\n");
				sb.append("������ʦ�û�����");
				if (Smtp.send(email, username + "�������ѱ�����", sb.toString())) {
					Users.setPassword(uid, password);
					ClosePanel panel = new ClosePanel("�һ�����");
					panel.setInfo("�һ�����ķ����Ѿ�ͨ��Email���͵�����������");
					EventLog.log(uid, EventLog.GETPASSWORD, 0);
					setResponsePanel(panel);
				} else {
					setWarn("����Emailʧ�ܣ����Ժ�����");
				}
			}
		};
		frm.add(txtUsername, txtEmail, pnlCaptcha);
		add(frm);
	}
}