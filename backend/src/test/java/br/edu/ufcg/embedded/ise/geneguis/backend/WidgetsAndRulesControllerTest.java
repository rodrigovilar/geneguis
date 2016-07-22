package br.edu.ufcg.embedded.ise.geneguis.backend;

import static br.edu.ufcg.embedded.ise.geneguis.backend.TestHelper.fixRuleInstanceMap;
import static br.edu.ufcg.embedded.ise.geneguis.backend.TestHelper.fixWidgetInstanceMap;
import static br.edu.ufcg.embedded.ise.geneguis.backend.TestHelper.get;
import static br.edu.ufcg.embedded.ise.geneguis.backend.TestHelper.getObjectFromResult;
import static br.edu.ufcg.embedded.ise.geneguis.backend.TestHelper.instance;
import static br.edu.ufcg.embedded.ise.geneguis.backend.TestHelper.objectToMap;
import static br.edu.ufcg.embedded.ise.geneguis.backend.TestHelper.post;
import static org.hamcrest.Matchers.hasSize;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.setup.MockMvcBuilders.standaloneSetup;

import java.util.Map;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.SpringApplicationConfiguration;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;

import br.edu.ufcg.embedded.ise.geneguis.Container;
import br.edu.ufcg.embedded.ise.geneguis.DomainModel;
import br.edu.ufcg.embedded.ise.geneguis.PropertyTypeType;
import br.edu.ufcg.embedded.ise.geneguis.WidgetType;
import br.edu.ufcg.embedded.ise.geneguis.backend.controller.RuleRest;
import br.edu.ufcg.embedded.ise.geneguis.backend.controller.RulesController;
import br.edu.ufcg.embedded.ise.geneguis.backend.controller.WidgetCodeRest;
import br.edu.ufcg.embedded.ise.geneguis.backend.controller.WidgetController;
import br.edu.ufcg.embedded.ise.geneguis.backend.controller.WidgetRest;
import br.edu.ufcg.embedded.ise.geneguis.jpadomain.JpaDomainModel;

@RunWith(SpringJUnit4ClassRunner.class)
@SpringApplicationConfiguration(classes = EntryPoint.class)
public class WidgetsAndRulesControllerTest {

	MockMvc mockMvc;

	@InjectMocks
	RulesController rulesController;
	@InjectMocks
	WidgetController widgetController;
	@Autowired
	JpaRenderingService renderingService;


	@Before
	public void setUp() {
		MockitoAnnotations.initMocks(this);
		this.mockMvc = standaloneSetup(rulesController, widgetController).build();
		DomainModel model = new JpaDomainModel();
		EntryPoint.setRenderingService(renderingService);
		EntryPoint.setContainer(new Container(model, EntryPoint.getRenderingService()));
	}

	@DirtiesContext
	@Test
	public void testCreateWidgetWithCodeAndRequiredPorts() throws Exception {
		WidgetRest widget = CreateHelper.createSimpleWidget("Widget1", WidgetType.Property, "port1", "port2");
		Map<String, Object> widgetInstanceMap = objectToMap(widget);
		fixWidgetInstanceMap(widgetInstanceMap, 1, widget.getType());
		
		post(mockMvc, "/widgets", widget).andExpect(status().isCreated()).andExpect(instance(widgetInstanceMap));

		WidgetCodeRest widgetCode = CreateHelper.createWidgetCode("Widget1", "\"any code\"");
		Map<String, Object> widgetCodeInstanceMap = objectToMap(widgetCode);

		post(mockMvc, "/widgets/Widget1/code", "any code").andExpect(status().isCreated()).andExpect(
				instance(widgetCodeInstanceMap));
	}

	@DirtiesContext
	@Test
	public void testCreateWidgets() throws Exception {
		WidgetRest widget = CreateHelper.createSimpleWidget("fooName", WidgetType.Property);

		Map<String, Object> widgetInstanceMap = objectToMap(widget);
		fixWidgetInstanceMap(widgetInstanceMap, 1, widget.getType());

		post(mockMvc, "/widgets", widget).andExpect(status().isCreated()).andExpect(instance(widgetInstanceMap));

		WidgetRest widget2 = CreateHelper.createSimpleWidget("fooName", WidgetType.Property);

		Map<String, Object> widgetInstanceMap2 = objectToMap(widget2);
		fixWidgetInstanceMap(widgetInstanceMap2, 2, widget2.getType());

		post(mockMvc, "/widgets", widget2).andExpect(status().isCreated()).andExpect(instance(widgetInstanceMap2));

		WidgetRest widget3 = CreateHelper.createSimpleWidget("otherFooName", WidgetType.Property);

		Map<String, Object> widgetInstanceMap3 = objectToMap(widget3);
		fixWidgetInstanceMap(widgetInstanceMap3, 1, widget3.getType());

		post(mockMvc, "/widgets", widget3).andExpect(status().isCreated()).andExpect(instance(widgetInstanceMap3));

		WidgetRest entityWidget = CreateHelper.createSimpleWidget("entityWidget", WidgetType.Entity);

		Map<String, Object> widgetInstanceMap4 = objectToMap(entityWidget);
		fixWidgetInstanceMap(widgetInstanceMap4, 1, entityWidget.getType());

		post(mockMvc, "/widgets", entityWidget).andExpect(status().isCreated()).andExpect(instance(widgetInstanceMap4));

		WidgetRest entityWidget2 = CreateHelper.createSimpleWidget("entityWidget", WidgetType.Entity);

		Map<String, Object> widgetInstanceMap5 = objectToMap(entityWidget2);
		fixWidgetInstanceMap(widgetInstanceMap5, 2, entityWidget2.getType());

		post(mockMvc, "/widgets", entityWidget2).andExpect(status().isCreated())
				.andExpect(instance(widgetInstanceMap5));

		WidgetRest widgetWithPorts = CreateHelper.createSimpleWidget("entityWidget", WidgetType.Entity, "form",
				"report");

		Map<String, Object> widgetInstanceMap6 = objectToMap(widgetWithPorts);
		fixWidgetInstanceMap(widgetInstanceMap6, 3, widgetWithPorts.getType());

		post(mockMvc, "/widgets", widgetWithPorts).andExpect(status().isCreated())
				.andExpect(instance(widgetInstanceMap6));

	}

	@DirtiesContext
	@Test
	public void testGetWidgets() throws Exception {
		get(mockMvc, "/widgets").andExpect(status().isOk()).andExpect(jsonPath("$", hasSize(0)));

		WidgetRest widget = CreateHelper.createSimpleWidget("entityWidget", WidgetType.Entity);
		post(mockMvc, "/widgets", widget).andExpect(status().isCreated());
		get(mockMvc, "/widgets").andExpect(status().isOk()).andExpect(jsonPath("$", hasSize(1)));

		WidgetRest widget2 = CreateHelper.createSimpleWidget("fooName", WidgetType.Property);
		post(mockMvc, "/widgets", widget2).andExpect(status().isCreated());
		get(mockMvc, "/widgets").andExpect(status().isOk()).andExpect(jsonPath("$", hasSize(2)));
	}

	@DirtiesContext
	@Test
	public void testCreateRules() throws Exception {

		String portName = "form";
		WidgetRest entitySetWidget = CreateHelper.createSimpleWidget("CRUDComponent", WidgetType.EntityType,
				portName);
		post(mockMvc, "/widgets", entitySetWidget).andExpect(status().isCreated());
		String portName2 = "row";
		WidgetRest entityWidget = CreateHelper.createSimpleWidget("TableFormWidget", WidgetType.EntityType, portName2);
		post(mockMvc, "/widgets", entityWidget).andExpect(status().isCreated());
		RuleRest defaultEntityRule = CreateHelper.createEntityRule(portName, "*", entityWidget.getName());
		Map<String, Object> instanceMap = objectToMap(defaultEntityRule);
		fixRuleInstanceMap(instanceMap);

		post(mockMvc, "/rules", defaultEntityRule).andExpect(status().isCreated()).andExpect(instance(instanceMap));

		WidgetRest propertyWidget = CreateHelper.createSimpleWidget("StringTextField", WidgetType.Property);
		post(mockMvc, "/widgets", propertyWidget).andExpect(status().isCreated());
		RuleRest propertyTypeRule = CreateHelper.createPropertyRule(portName2, PropertyTypeType.string, "*",
				propertyWidget.getName());
		instanceMap = objectToMap(propertyTypeRule);
		fixRuleInstanceMap(instanceMap);
		instanceMap.put("propertyTypeTypeLocator", propertyTypeRule.getPropertyTypeTypeLocator().name());

		post(mockMvc, "/rules", propertyTypeRule).andExpect(status().isCreated()).andExpect(instance(instanceMap));

		RuleRest propertyRule = CreateHelper.createPropertyRule(portName2, PropertyTypeType.string, "*.name",
				propertyWidget.getName());
		instanceMap = objectToMap(propertyRule);
		fixRuleInstanceMap(instanceMap);
		instanceMap.put("propertyTypeTypeLocator", propertyTypeRule.getPropertyTypeTypeLocator().name());

		post(mockMvc, "/rules", propertyRule).andExpect(status().isCreated()).andExpect(instance(instanceMap));

		RuleRest entityRule = CreateHelper.createEntityRule(portName, "*Item", entityWidget.getName());
		instanceMap = objectToMap(entityRule);
		fixRuleInstanceMap(instanceMap);

		post(mockMvc, "/rules", entityRule).andExpect(status().isCreated()).andExpect(instance(instanceMap));

	}

	@DirtiesContext
	@Test
	public void testGetAllRules() throws Exception {
		get(mockMvc, "/rules").andExpect(status().isOk()).andExpect(jsonPath("$", hasSize(0)));

		String contextName = "form";
		WidgetRest entitySetWidget = CreateHelper.createSimpleWidget("CRUDComponent", WidgetType.EntityType,
				contextName);
		post(mockMvc, "/widgets", entitySetWidget).andExpect(status().isCreated());
		String contextName2 = "row";
		WidgetRest entityWidget = CreateHelper.createSimpleWidget("TableFormWidget", WidgetType.EntityType, contextName2);
		post(mockMvc, "/widgets", entityWidget).andExpect(status().isCreated());
		RuleRest defaultEntityRule = CreateHelper.createEntityRule(contextName, "*", entityWidget.getName());

		MvcResult mvcResult = post(mockMvc, "/rules", defaultEntityRule).andExpect(status().isCreated()).andReturn();
		defaultEntityRule = getObjectFromResult(mvcResult, RuleRest.class);

		get(mockMvc, "/rules").andExpect(status().isOk()).andExpect(jsonPath("$", hasSize(1)));

		WidgetRest propertyWidget = CreateHelper.createSimpleWidget("fooName", WidgetType.Property);
		post(mockMvc, "/widgets", propertyWidget).andExpect(status().isCreated());
		RuleRest propertyTypeRule = CreateHelper.createPropertyRule(contextName2, PropertyTypeType.string, "*",
				propertyWidget.getName());

		MvcResult mvcResult2 = post(mockMvc, "/rules", propertyTypeRule).andExpect(status().isCreated()).andReturn();
		propertyTypeRule = getObjectFromResult(mvcResult2, RuleRest.class);

		get(mockMvc, "/rules").andExpect(status().isOk()).andExpect(jsonPath("$", hasSize(2)));

		get(mockMvc, "/rules?version=" + defaultEntityRule.getVersion()).andExpect(status().isOk())
				.andExpect(jsonPath("$", hasSize(1)));

		RuleRest defaultEntityRule2 = CreateHelper.createEntityRule(contextName, "*", entityWidget.getName());

		MvcResult mvcResult3 = post(mockMvc, "/rules", defaultEntityRule2).andExpect(status().isCreated()).andReturn();
		defaultEntityRule2 = getObjectFromResult(mvcResult3, RuleRest.class);

		get(mockMvc, "/rules").andExpect(status().isOk()).andExpect(jsonPath("$", hasSize(3)));

		get(mockMvc, "/rules?version=" + defaultEntityRule.getVersion()).andExpect(status().isOk())
				.andExpect(jsonPath("$", hasSize(2)));

		get(mockMvc, "/rules?version=" + propertyTypeRule.getVersion()).andExpect(status().isOk())
				.andExpect(jsonPath("$", hasSize(1)));

	}

}