package net.elephantbase.users.web.admin;

import net.elephantbase.db.DBUtil;
import net.elephantbase.users.biz.EventLog;
import net.elephantbase.users.biz.UserData;
import net.elephantbase.users.biz.UserDetail;
import net.elephantbase.users.web.BasePanel;

import org.apache.wicket.markup.html.basic.MultiLineLabel;
import org.apache.wicket.markup.html.form.Form;
import org.apache.wicket.markup.html.form.RequiredTextField;
import org.apache.wicket.model.Model;

public class AddPointsPanel extends BasePanel {
	private static final long serialVersionUID = 1L;

	public AddPointsPanel(final UserDetail user) {
		super("�������");

		final MultiLineLabel lblInfo = new MultiLineLabel("lblInfo", "");
		lblInfo.setVisible(false);
		add(lblInfo);
		final RequiredTextField<String> txtPoints = new
				RequiredTextField<String>("txtPoints", Model.of(""));
		Form<Void> frm = new Form<Void>("frm") {
			private static final long serialVersionUID = 1L;

			@Override
			protected void onSubmit() {
				lblInfo.setVisible(false);
				int points = 0;
				try {
					points = Integer.parseInt(txtPoints.getModelObject());
				} catch (Exception e) {
					// Ignored
				}
				if (points == 0) {
					setWarn("������һ����ֵ�����������䣬��ֵ��������");
					return;
				}
				if (points < 0) {
					String sql = "UPDATE xq_user SET points = points + ? " +
							"WHERE uid = ?";
					DBUtil.update(sql, Integer.valueOf(points),
							Integer.valueOf(user.uid));
					user.points += points;
					setInfo(String.format("�û�[%s]������%s��",
								user.username, "" + -points));
					EventLog.log(user.uid, EventLog.EVENT_ADMIN_CHARGE, points);
					return;
				}
				String sql = "UPDATE xq_user SET points = points + ?, " +
						"charged = charged + ? WHERE uid = ?";
				DBUtil.update(sql, Integer.valueOf(points),
						Integer.valueOf(points), Integer.valueOf(user.uid));
				user.points += points;
				user.charged += points;
				setInfo(String.format("�û�[%s]������%s�㣬��������ı����͸��û���",
						user.username, "" + points));
				EventLog.log(user.uid, EventLog.EVENT_ADMIN_CHARGE, points);
				StringBuilder sb = new StringBuilder();
				sb.append(String.format("������Ϊ����������ʦ�ʺ�[%s]��ֵ%s��",
						user.username, "" + points));
				if (UserData.isPlatinum(user.charged)) {
					sb.append("��������Ϊ�׽��Ա(��ʾ�ͻ��岻�۵�)");
				}
				sb.append(String.format("��\nĿǰ�����ʺŹ���%s����ã�",
						"" + user.points));
				sb.append("����������ʦħ��ѧУ���û�����/��ѯ���������ܲ��ա�\n" +
						"���κ����⡢����ͽ����뼰ʱ��������ϵ����л����������ʦ��֧�֡�");
				lblInfo.setDefaultModelObject(sb.toString());
				lblInfo.setVisible(true);
			}
		};
		frm.add(txtPoints);
		add(frm);
	}
}