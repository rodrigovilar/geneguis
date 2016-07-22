package br.edu.ufcg.embedded.ise.geneguis.jpadomain;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import br.edu.ufcg.embedded.ise.geneguis.Port;
import br.edu.ufcg.embedded.ise.geneguis.RenderingService;
import br.edu.ufcg.embedded.ise.geneguis.Rule;
import br.edu.ufcg.embedded.ise.geneguis.Widget;
import br.edu.ufcg.embedded.ise.geneguis.jpadomain.service.RuleService;
import br.edu.ufcg.embedded.ise.geneguis.jpadomain.service.WidgetService;

@Service
public class JpaRenderingService implements RenderingService {

	@Autowired
	private WidgetService widgetService;
	
	@Autowired
	private RuleService ruleService;

	public void setRuleService(RuleService service) {
		this.ruleService = service;
	}

	public void setWidgetService(WidgetService service) {
		this.widgetService = service;
	}

	public Widget saveWidget(Widget widget) throws Exception {
		return fromJPA(widgetService.saveWidget(toJPA(widget)));
	}

	private Widget fromJPA(br.edu.ufcg.embedded.ise.geneguis.jpadomain.Widget jpawidget) throws Exception {
		Widget widget = new Widget();
		BeanUtils.copyProperties(jpawidget, widget);
		MetadataUtil.copyCollection(jpawidget.getRequiredPorts(), widget.getRequiredPorts(), Port.class);
		return widget;
	}

	private br.edu.ufcg.embedded.ise.geneguis.jpadomain.Widget toJPA(Widget widget) throws Exception {
		br.edu.ufcg.embedded.ise.geneguis.jpadomain.Widget jpawidget = new br.edu.ufcg.embedded.ise.geneguis.jpadomain.Widget();
		BeanUtils.copyProperties(widget, jpawidget);
		MetadataUtil.copyCollection(widget.getRequiredPorts(), jpawidget.getRequiredPorts(),
				br.edu.ufcg.embedded.ise.geneguis.jpadomain.Port.class);
		return jpawidget;
	}

	public List<Widget> getAll() throws Exception {
		List<Widget> widgets = new ArrayList<Widget>();

		for (br.edu.ufcg.embedded.ise.geneguis.jpadomain.Widget jpawidget : widgetService.getAll()) {
			widgets.add(fromJPA(jpawidget));
		}

		return widgets;
	}

	public Widget getWidget(String widgetName) throws Exception {
		return fromJPA(widgetService.getWidget(widgetName));
	}

	public List<Rule> getAllRulesByVersionGreaterThan(Long version) {
		List<Rule> rules = new ArrayList<Rule>();

		for (br.edu.ufcg.embedded.ise.geneguis.jpadomain.Rule jparule : ruleService
				.getAllRulesByVersionGreaterThan(version)) {
			rules.add(fromJPA(jparule));
		}

		return rules;
	}

	private Rule fromJPA(br.edu.ufcg.embedded.ise.geneguis.jpadomain.Rule jparule) {
		Rule rule = new Rule();
		BeanUtils.copyProperties(jparule, rule);
		BeanUtils.copyProperties(jparule.getPort(), rule.getPort());
		BeanUtils.copyProperties(jparule.getWidget(), rule.getWidget());
		return rule;
	}

	public List<Rule> getAllRules() {
		List<Rule> rules = new ArrayList<Rule>();

		for (br.edu.ufcg.embedded.ise.geneguis.jpadomain.Rule jparule : ruleService.getAllRules()) {
			rules.add(fromJPA(jparule));
		}

		return rules;
	}

	public Rule saveRule(Rule rule) {
		return fromJPA(ruleService.saveRule(toJPA(rule)));
	}

	private br.edu.ufcg.embedded.ise.geneguis.jpadomain.Rule toJPA(Rule rule) {
		br.edu.ufcg.embedded.ise.geneguis.jpadomain.Rule jparule = new br.edu.ufcg.embedded.ise.geneguis.jpadomain.Rule();
		BeanUtils.copyProperties(rule, jparule);
		BeanUtils.copyProperties(rule.getPort(), jparule.getPort());
		BeanUtils.copyProperties(rule.getWidget(), jparule.getWidget());
		return jparule;
	}
}
