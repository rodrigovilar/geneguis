package br.edu.ufcg.embedded.ise.geneguis.backend;

import java.util.ArrayList;
import java.util.List;

import br.edu.ufcg.embedded.ise.geneguis.PropertyTypeType;

public class CreateHelper {

	public static Widget createSimpleWidget(String name, String code, WidgetType type, String... requiredContexts) {
		List<Port> contexts = null;
		if (requiredContexts.length > 0) {
			contexts = new ArrayList<Port>();
			for (String contextName : requiredContexts) {
				Port context = new Port();
				context.setName(contextName);
				context.setType(type);
				contexts.add(context);
			}
		}
		return createWidget(name, code, type, contexts);
	}

	public static Widget createWidget(String name, String code, WidgetType type, List<Port> requiredContexts) {
		Widget widget = new Widget();
		widget.setName(name);
		widget.setCode(code);
		widget.setType(type);
		widget.setRequiredPorts(requiredContexts);
		return widget;
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
