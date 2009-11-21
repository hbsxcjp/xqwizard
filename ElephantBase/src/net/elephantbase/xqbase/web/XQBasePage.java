package net.elephantbase.xqbase.web;

import net.elephantbase.users.web.BasePanel;
import net.elephantbase.util.wicket.WicketUtil;
import net.elephantbase.xqbase.biz.SearchCond;

import org.apache.wicket.markup.html.WebPage;

public class XQBasePage extends WebPage {
	public static final String SUFFIX = "������ʦ���ײֿ�";

	{
		String ecco = WicketUtil.getServletRequest().getParameter("ecco");
		if (ecco == null) {
			BasePanel.setResponsePanel(new SearchPanel(new SearchCond()));
		} else {
			BasePanel.setResponsePanel(new ResultPanel(new SearchCond(ecco)));
		}
	}
}