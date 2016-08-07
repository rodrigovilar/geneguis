package br.edu.ufcg.embedded.ise.geneguis;

import java.util.ArrayList;
import java.util.List;

public class Port {

	private String name;
	
	private WidgetType type;
	
	private List<Rule> rules = new ArrayList<Rule>();
	
	private List<Widget> widgets = new ArrayList<Widget>();

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public WidgetType getType() {
		return type;
	}

	public void setType(WidgetType type) {
		this.type = type;
	}

	public List<Rule> getRules() {
		return rules;
	}

	public void setRules(List<Rule> rules) {
		this.rules = rules;
	}

	public List<Widget> getWidgets() {
		return widgets;
	}

	public void setWidgets(List<Widget> widgets) {
		this.widgets = widgets;
	}

	@Override
	public String toString() {
		return "Port [name=" + name + ", type=" + type + "]";
	}
	
}