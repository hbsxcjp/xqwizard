package net.elephantbase.users.web;

import net.elephantbase.db.DBUtil;
import net.elephantbase.users.biz.BaseSession;
import net.elephantbase.users.biz.EventLog;
import net.elephantbase.users.biz.UserData;

import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.form.Form;
import org.apache.wicket.markup.html.form.RequiredTextField;
import org.apache.wicket.model.Model;

public class ChargePanel extends BasePanel {
	private static final long serialVersionUID = 1L;

	public ChargePanel() {
		super("�������");

		final Label lblInfo = new Label("lblInfo", "");
		add(lblInfo);

		final RequiredTextField<String> txtChargeCode = new
				RequiredTextField<String>("txtChargeCode", Model.of(""));

		Form<Void> frm = new Form<Void>("frm") {
			private static final long serialVersionUID = 1L;

			@Override
			protected void onSubmit() {
				lblInfo.setVisible(false);
				String chargeCode = txtChargeCode.getModelObject();
				String sql = "SELECT points FROM xq_chargecode WHERE chargecode = ?";
				int points = DBUtil.getInt(DBUtil.query(sql, chargeCode));
				if (points == 0) {
					setWarn("�㿨�������");
					return;
				}
				sql = "DELETE FROM xq_chargecode WHERE chargecode = ?";
				int rows = DBUtil.update(sql, chargeCode);
				if (rows == 0) {
					setWarn("�㿨����ʧЧ");
					return;
				}

				int uid = ((BaseSession) getSession()).getUid();
				sql = "UPDATE xq_user SET points = points + ?, " +
						"charged = charged + ? WHERE uid = ?";
				DBUtil.update(sql, Integer.valueOf(points),
						Integer.valueOf(points), Integer.valueOf(uid));

				UserData user = new UserData(uid);
				String info = "���ղŲ����� " + points + " �㣬���ڹ��� " +
						user.getPoints() + " �����";
				if (user.isPlatinum()) {
					lblInfo.setDefaultModelObject(user.isDiamond() ?
							"���Ѿ�����Ϊ����ʯ��Ա�û�" : "���Ѿ�����Ϊ���׽��Ա�û�");
					lblInfo.setVisible(true);
				}
				setInfo(info);
				EventLog.log(uid, EventLog.EVENT_CHARGE, points);
			}
		};
		frm.add(txtChargeCode);
		add(frm);
	}
}