package br.edu.ufcg.embedded.ise.geneguis.backend;

import java.util.ArrayList;
import java.util.List;

import br.edu.ufcg.embedded.ise.geneguis.PropertyTypeType;
import br.edu.ufcg.embedded.ise.geneguis.backend.controller.PortRest;
import br.edu.ufcg.embedded.ise.geneguis.backend.controller.WidgetCodeRest;
import br.edu.ufcg.embedded.ise.geneguis.backend.controller.WidgetRest;

public class CreateHelper {

	public static WidgetRest createSimpleWidget(String name, WidgetType type, String... requiredPorts) {
		List<PortRest> contexts = null;
		if (requiredPorts.length > 0) {
			contexts = new ArrayList<PortRest>();
			for (String contextName : requiredPorts) {
				PortRest port = new PortRest();
				port.setName(contextName);
				port.setType(type.name());
				contexts.add(port);
			}
		}
		return createWidget(name, type, contexts);
	}

	public static WidgetRest createWidget(String name, WidgetType type, List<PortRest> requiredPorts) {
		WidgetRest widget = new WidgetRest();
		widget.setName(name);
		widget.setType(type.name());
		widget.setRequiredPorts(requiredPorts);
		return widget;
	}
	
	public static WidgetCodeRest createWidgetCode(String name, String code) {
		WidgetCodeRest codeRest = new WidgetCodeRest();
		codeRest.setCode(code);
		codeRest.setName(name);
		return codeRest;
	}
	
	public static Rule createEntityRule(String contextName, String entityTypeLocator, String widgetName) {
		Rule defaultEntityRule = new Rule();
		defaultEntityRule.setEntityTypeLocator(entityTypeLocator);
		
		Port port = new Port();
		port.setName(contextName);
		port.setType(WidgetType.Entity);
		defaultEntityRule.setPort(port);
		
		Widget widget = new Widget();
		widget.setName(widgetName);
		defaultEntityRule.setWidget(widget);
		
		return defaultEntityRule;
	}
	
	public static Rule createPropertyRule(String contextName, PropertyTypeType propertyTypeTypeLocator, String propertyTypeLocator, String widgetName) {
		Rule propertyTypeRule = new Rule();
		propertyTypeRule.setPropertyTypeTypeLocator(propertyTypeTypeLocator);
		propertyTypeRule.setPropertyTypeLocator(propertyTypeLocator);
		
		Port port = new Port();
		port.setName(contextName);
		port.setType(WidgetType.Property);
		propertyTypeRule.setPort(port);
		
		Widget widget = new Widget();
		widget.setName(widgetName);
		propertyTypeRule.setWidget(widget);
		
		return propertyTypeRule;
	}
}
