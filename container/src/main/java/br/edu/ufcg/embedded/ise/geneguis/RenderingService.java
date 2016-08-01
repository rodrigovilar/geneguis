package br.edu.ufcg.embedded.ise.geneguis;

import java.util.List;

public interface RenderingService {

	Widget saveWidget(Widget widget);

	List<Widget> getAllWidgets();

	Widget getWidget(String widgetName);

	List<Rule> getAllRulesByVersionGreaterThan(Long version);

	List<Rule> getAllRules();

	Rule saveRule(Rule rule);

	List<TagRule> getAllTagRules();

	TagRule saveTagRule(TagRule tag);

}
