package br.edu.ufcg.embedded.ise.geneguis.backend;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import br.edu.ufcg.embedded.ise.geneguis.Port;
import br.edu.ufcg.embedded.ise.geneguis.RenderingService;
import br.edu.ufcg.embedded.ise.geneguis.Rule;
import br.edu.ufcg.embedded.ise.geneguis.Widget;
import br.edu.ufcg.embedded.ise.geneguis.backend.service.RuleService;
import br.edu.ufcg.embedded.ise.geneguis.backend.service.WidgetService;

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

	public Widget saveWidget(Widget widget) {
		return fromJPA(widgetService.saveWidget(toJPA(widget)));
	}

	private Widget fromJPA(br.edu.ufcg.embedded.ise.geneguis.backend.Widget jpawidget) {
		Widget widget = new Widget();
		widget.setCode(jpawidget.getCode());
		widget.setName(jpawidget.getName());
		widget.setType(jpawidget.getType());
		widget.setVersion(jpawidget.getVersion());

		fromJPA(jpawidget.getRequiredPorts(), widget.getRequiredPorts());
		return widget;
	}

	public static void fromJPA(List<br.edu.ufcg.embedded.ise.geneguis.backend.Port> jpaports, List<Port> ports) {

		ports.clear();
		for (br.edu.ufcg.embedded.ise.geneguis.backend.Port jpaport : jpaports) {
			ports.add(fromJPA(jpaport));
		}
	}

	private static Port fromJPA(br.edu.ufcg.embedded.ise.geneguis.backend.Port jpaport) {
		Port port = new Port();
		port.setName(jpaport.getName());
		port.setType(jpaport.getType());
		return port;
	}

	private br.edu.ufcg.embedded.ise.geneguis.backend.Widget toJPA(Widget widget) {
		br.edu.ufcg.embedded.ise.geneguis.backend.Widget jpawidget = new br.edu.ufcg.embedded.ise.geneguis.backend.Widget();
		
		jpawidget.setCode(widget.getCode());
		jpawidget.setName(widget.getName());
		jpawidget.setType(widget.getType());
		jpawidget.setVersion(widget.getVersion());

		toJPA(widget.getRequiredPorts(), jpawidget.getRequiredPorts());

		return jpawidget;
	}

	public static void toJPA(List<Port> ports, List<br.edu.ufcg.embedded.ise.geneguis.backend.Port> jpaports) {

		jpaports.clear();
		for (Port port : ports) {
			jpaports.add(toJPA(port));
		}
	}
	
	private static br.edu.ufcg.embedded.ise.geneguis.backend.Port toJPA(Port port) {
		br.edu.ufcg.embedded.ise.geneguis.backend.Port jpaport = new br.edu.ufcg.embedded.ise.geneguis.backend.Port();
		jpaport.setName(port.getName());
		jpaport.setType(port.getType());
		return jpaport;
	}

	public List<Widget> getAll(){
		List<Widget> widgets = new ArrayList<Widget>();

		for (br.edu.ufcg.embedded.ise.geneguis.backend.Widget jpawidget : widgetService.getAll()) {
			widgets.add(fromJPA(jpawidget));
		}

		return widgets;
	}

	public Widget getWidget(String widgetName){
		return fromJPA(widgetService.getWidget(widgetName));
	}

	public List<Rule> getAllRulesByVersionGreaterThan(Long version) {
		List<Rule> rules = new ArrayList<Rule>();

		for (br.edu.ufcg.embedded.ise.geneguis.backend.Rule jparule : ruleService
				.getAllRulesByVersionGreaterThan(version)) {
			rules.add(fromJPA(jparule));
		}

		return rules;
	}

	private Rule fromJPA(br.edu.ufcg.embedded.ise.geneguis.backend.Rule jparule) {
		Rule rule = new Rule();
		rule.setConfiguration(jparule.getConfiguration());
		rule.setEntityTypeLocator(jparule.getEntityTypeLocator());
		rule.setId(jparule.getId());
		rule.setPort(fromJPA(jparule.getPort()));
		rule.setPropertyTypeLocator(jparule.getPropertyTypeLocator());
		rule.setPropertyTypeTypeLocator(jparule.getPropertyTypeTypeLocator());
		rule.setType(jparule.getType());
		rule.setVersion(jparule.getVersion());
		rule.setWidget(fromJPA(jparule.getWidget()));
		return rule;
	}

	public List<Rule> getAllRules() {
		List<Rule> rules = new ArrayList<Rule>();

		for (br.edu.ufcg.embedded.ise.geneguis.backend.Rule jparule : ruleService.getAllRules()) {
			rules.add(fromJPA(jparule));
		}

		return rules;
	}

	public Rule saveRule(Rule rule) {
		return fromJPA(ruleService.saveRule(toJPA(rule)));
	}

	private br.edu.ufcg.embedded.ise.geneguis.backend.Rule toJPA(Rule rule) {

		br.edu.ufcg.embedded.ise.geneguis.backend.Rule jparule = new br.edu.ufcg.embedded.ise.geneguis.backend.Rule();
		jparule.setConfiguration(rule.getConfiguration());
		jparule.setEntityTypeLocator(rule.getEntityTypeLocator());
		jparule.setId(rule.getId());
		jparule.setPort(toJPA(rule.getPort()));
		jparule.setPropertyTypeLocator(rule.getPropertyTypeLocator());
		jparule.setPropertyTypeTypeLocator(rule.getPropertyTypeTypeLocator());
		jparule.setType(rule.getType());
		jparule.setVersion(rule.getVersion());
		jparule.setWidget(toJPA(rule.getWidget()));
		return jparule;
	}
}
