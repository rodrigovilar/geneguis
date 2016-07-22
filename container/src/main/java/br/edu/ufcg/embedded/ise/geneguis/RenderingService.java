package br.edu.ufcg.embedded.ise.geneguis;

import java.util.List;

public interface RenderingService {

	Widget saveWidget(Widget widget) throws Exception;

	List<Widget> getAll() throws Exception;

	Widget getWidget(String widgetName) throws Exception;

	List<Rule> getAllRulesByVersionGreaterThan(Long version);

	List<Rule> getAllRules();

	Rule saveRule(Rule rule);

}
