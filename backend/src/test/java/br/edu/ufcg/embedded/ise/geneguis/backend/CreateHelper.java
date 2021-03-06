package br.edu.ufcg.embedded.ise.geneguis.backend;

import java.util.ArrayList;
import java.util.List;

import br.edu.ufcg.embedded.ise.geneguis.PropertyTypeType;
import br.edu.ufcg.embedded.ise.geneguis.WidgetType;
import br.edu.ufcg.embedded.ise.geneguis.backend.controller.PortRest;
import br.edu.ufcg.embedded.ise.geneguis.backend.controller.RuleRest;
import br.edu.ufcg.embedded.ise.geneguis.backend.controller.WidgetCodeRest;
import br.edu.ufcg.embedded.ise.geneguis.backend.controller.WidgetRest;

public class CreateHelper {

	public static WidgetRest createSimpleWidget(String name, WidgetType type, String... requiredPorts) {
		List<PortRest> ports = null;
		if (requiredPorts.length > 0) {
			ports = new ArrayList<PortRest>();
			for (String contextName : requiredPorts) {
				PortRest port = new PortRest();
				port.setName(contextName);
				port.setType(type.name());
				ports.add(port);
			}
		}
		return createWidget(name, type, ports);
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

	public static RuleRest createEntitySetRule(String portName, String entityTypeLocator, String widgetName) {
		RuleRest entitySetRule = new RuleRest();
		entitySetRule.setEntityTypeLocator(entityTypeLocator);
		entitySetRule.setPortName(portName);
		entitySetRule.setWidgetName(widgetName);
		entitySetRule.setType(WidgetType.EntityType.name());
		return entitySetRule;
	}

	public static RuleRest createEntityRule(String portName, String entityTypeLocator, String widgetName) {
		RuleRest entityRule = new RuleRest();
		entityRule.setEntityTypeLocator(entityTypeLocator);
		entityRule.setPortName(portName);
		entityRule.setWidgetName(widgetName);
		entityRule.setType(WidgetType.Entity.name());
		return entityRule;
	}

	public static RuleRest createPropertyRule(String portName, PropertyTypeType propertyTypeTypeLocator,
			String propertyTypeLocator, String widgetName) {
		
		RuleRest propertyTypeRule = new RuleRest();
		propertyTypeRule.setPropertyTypeTypeLocator(propertyTypeTypeLocator);
		propertyTypeRule.setPropertyTypeLocator(propertyTypeLocator);
		propertyTypeRule.setPortName(portName);
		propertyTypeRule.setWidgetName(widgetName);
		propertyTypeRule.setType(WidgetType.PropertyType.name());
		return propertyTypeRule;
	}
}
