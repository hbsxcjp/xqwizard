package net.elephantbase.users.web.admin.questionnaire;

import org.apache.wicket.markup.html.form.Form;
import org.apache.wicket.markup.html.form.PasswordTextField;
import org.apache.wicket.model.Model;

import net.elephantbase.db.DBUtil;
import net.elephantbase.users.biz.BaseSession;
import net.elephantbase.users.biz.EventLog;
import net.elephantbase.users.biz.Users;
import net.elephantbase.users.web.BasePanel;
import net.elephantbase.users.web.ClosePanel;

public class ClearPanel extends BasePanel {
	private static final long serialVersionUID = 1L;

	public ClearPanel() {
		super("����ʾ����");

		final PasswordTextField txtPassword = new
				PasswordTextField("txtPassword", Model.of(""));
		Form<Void> frm = new Form<Void>("frm") {
			private static final long serialVersionUID = 1L;

			@Override
			protected void onSubmit() {
				String password = txtPassword.getModelObject();
				BaseSession session = (BaseSession) getSession();
				int uid = Users.login(session.getUsername(), password);
				if (uid != session.getUid()) {
					setWarn("���벻��ȷ");
					return;
				}
				DBUtil.update("TRUNCATE TABLE xq_qn_user");
				DBUtil.update("TRUNCATE TABLE xq_qn_answer");
				DBUtil.update("TRUNCATE TABLE xq_qn_comment");
				ClosePanel panel = new ClosePanel("����ʾ����");
				panel.setInfo("�ʾ�����Ѿ������");
				EventLog.log(uid, EventLog.QN_CLEAR, 0);
				setResponsePanel(panel);
			}
		};
		frm.add(txtPassword);
		add(frm);
	}
}