package br.edu.ufcg.embedded.ise.geneguis.backend;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.Arrays;

import org.springframework.context.ApplicationContext;

import br.edu.ufcg.embedded.ise.geneguis.PropertyTypeType;
import br.edu.ufcg.embedded.ise.geneguis.WidgetType;
import br.edu.ufcg.embedded.ise.geneguis.backend.service.PortService;
import br.edu.ufcg.embedded.ise.geneguis.backend.service.RuleService;
import br.edu.ufcg.embedded.ise.geneguis.backend.service.WidgetService;

public class Util {

	private static WidgetService widgetService;
	private static RuleService ruleService;
	private static PortService contextService;

	public static void initDB(ApplicationContext application) {
		widgetService = application.getBean(WidgetService.class);
		ruleService = application.getBean(RuleService.class);
		contextService = application.getBean(PortService.class);

		Port propertyContext = createPort("property", WidgetType.Property);
		Port formContext = createPort("form", WidgetType.Entity);
		Port fieldContext = createPort("field", WidgetType.Property);
		Widget listingTableWidget = createWidget("ListingTable", WidgetType.EntityType,
				readWidgetFile("ListingTable.js"), propertyContext, formContext);
		Widget toStringPropertyWidget = createWidget("ToStringProperty", WidgetType.Property,
				readWidgetFile("ToStringProperty.js"));
		Widget DateFormatterWidget = createWidget("DateFormatterWidget", WidgetType.Property,
				readWidgetFile("DateFormatterWidget.js"));
		Widget simpleFormWidget = createWidget("SimpleFormWidget", WidgetType.Entity,
				readWidgetFile("SimpleFormWidget.js"), fieldContext);
		Widget simpleTextFieldPropertyWidget = createWidget("SimpleTextFieldPropertyWidget", WidgetType.Property,
				readWidgetFile("SimpleTextFieldProperty.js"));
		createRule("root", "*", null, null, listingTableWidget, null);
		createRule(propertyContext.getName(), null, null, "*", toStringPropertyWidget, null);
		createRule(propertyContext.getName(), null, PropertyTypeType.date, null, DateFormatterWidget,
				"{\"format\": \"dd-mm-yy\"}");
		createRule(formContext.getName(), null, null, null, simpleFormWidget, null);
		createRule(fieldContext.getName(), null, null, null, simpleTextFieldPropertyWidget, null);
	}

	public static Port createPort(String name, WidgetType type) {
		Port context = new Port();
		context.setName(name);
		context.setType(type);
		return contextService.createPort(context);
	}

	public static Widget createWidget(String name, WidgetType type, String code, Port... requiredContexts) {
		Widget widget = new Widget();
		widget.setName(name);
		widget.setType(type);
		widget.setCode(code);
		if (requiredContexts.length > 0)
			widget.setRequiredPorts(Arrays.asList(requiredContexts));
		return widgetService.saveWidget(widget);
	}

	public static Rule createRule(String providedContextName, String entityTypeLocator,
			PropertyTypeType propertyTypeTypeLocator, String propertyTypeLocator, Widget widget, String configuration) {
		Rule rule = new Rule();
		rule.setPort(contextService.getPortByName(providedContextName));
		rule.setEntityTypeLocator(entityTypeLocator);
		rule.setPropertyTypeTypeLocator(propertyTypeTypeLocator);
		rule.setPropertyTypeLocator(propertyTypeLocator);
		rule.setWidget(widget);
		rule.setConfiguration(configuration);
		return ruleService.saveRule(rule);
	}

	public static String readWidgetFile(String fileName) {
		
		String result = "";
		
		InputStream inputStream = Util.class.getClassLoader().getResourceAsStream("widgets/" + fileName);
		BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream));
		
		try {
		    String line;
		    while ((line = reader.readLine()) != null) {
		        result += line + "\n";
		    }
		    
		    return result;
		    
		} catch (Exception e) {
		    return null;
		}

//		String filePath = EntryPoint.class.getResource("/widgets/" + fileName).getPath();
//		filePath = filePath.startsWith("file:") ? filePath.substring(5) : filePath;
//		filePath = System.getProperty("os.name").contains("indow") && filePath.startsWith("/") ? filePath.substring(1)
//				: filePath;
//		try {
//			Path widgetPath = Paths.get(filePath);
//			return new String(Files.readAllBytes(widgetPath));
//		} catch (Exception e) {
//			e.printStackTrace();
//			return null;
//		}
	}
}
