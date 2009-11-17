package net.elephantbase.pgndb.web;

import net.elephantbase.pgndb.biz.SearchCond;
import net.elephantbase.users.web.BasePanel;

import org.apache.wicket.markup.html.WebPage;
import org.apache.wicket.protocol.http.WebRequest;

public class PgnDBPage extends WebPage {
	public static final String SUFFIX = "������ʦ���ײֿ�";

	{
		String ecco = ((WebRequest) getRequest()).
				getHttpServletRequest().getParameter("ecco");
		if (ecco == null) {
			BasePanel.setResponsePanel(new SearchPanel(new SearchCond()));
		} else {
			BasePanel.setResponsePanel(new ResultPanel(new SearchCond(ecco)));
		}
	}
}