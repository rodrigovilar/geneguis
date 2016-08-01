package br.edu.ufcg.embedded.ise.geneguis.test;

import java.util.ArrayList;
import java.util.List;

import br.edu.ufcg.embedded.ise.geneguis.RenderingService;
import br.edu.ufcg.embedded.ise.geneguis.Rule;
import br.edu.ufcg.embedded.ise.geneguis.TagRule;
import br.edu.ufcg.embedded.ise.geneguis.Widget;

public class PojoRenderingService implements RenderingService {
	
	private List<TagRule> tagRules = new ArrayList<TagRule>();

	public Widget saveWidget(Widget widget) {
		return null;
	}

	public List<Widget> getAllWidgets() {
		return null;
	}

	public Widget getWidget(String widgetName) {
		return null;
	}

	public List<Rule> getAllRulesByVersionGreaterThan(Long version) {
		return null;
	}

	public List<Rule> getAllRules() {
		return null;
	}

	public Rule saveRule(Rule rule) {
		return null;
	}

	public List<TagRule> getAllTagRules() {
		return tagRules;
	}

	public TagRule saveTagRule(TagRule tag) {
		tagRules.add(tag);
		return tag;
	}

}
