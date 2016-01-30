package br.edu.ufcg.embedded.ise.geneguis.backend;

import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;

import java.beans.BeanInfo;
import java.beans.Introspector;
import java.beans.PropertyDescriptor;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.lang.reflect.Method;
import java.lang.reflect.Type;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClientBuilder;
import org.junit.Assert;
import org.openqa.selenium.By;
import org.springframework.context.ApplicationContext;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.ResultMatcher;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonParseException;
import com.google.gson.JsonSyntaxException;

import br.edu.ufcg.embedded.ise.geneguis.Cardinality;
import br.edu.ufcg.embedded.ise.geneguis.PropertyTypeType;
import br.edu.ufcg.embedded.ise.geneguis.backend.controller.EntityTypeDeployRest;
import br.edu.ufcg.embedded.ise.geneguis.backend.controller.EntityTypeRest;
import br.edu.ufcg.embedded.ise.geneguis.backend.controller.PortRest;
import br.edu.ufcg.embedded.ise.geneguis.backend.controller.PropertyTypeRest;
import br.edu.ufcg.embedded.ise.geneguis.backend.controller.RuleRest;
import br.edu.ufcg.embedded.ise.geneguis.backend.controller.WidgetRest;

public class TestHelper {

	@SuppressWarnings("unchecked")
	public static <T> void deploy(ApplicationContext applicationContext, Class<T> entityTypeClass,
			Class<?> repositoryType) {
		JpaRepository<T, ?> rep = (JpaRepository<T, ?>) applicationContext.getBean(repositoryType);
		EntryPoint.getDomainModel().deployEntityType(entityTypeClass, rep);

	}

	public static EntityTypeRest createEntityTypeRest(String name) {
		EntityTypeRest expected = new EntityTypeRest();
		expected.setName(name);
		return expected;
	}

	public static void addPropertyTypeRest(EntityTypeRest expected, String name, PropertyTypeType type) {
		PropertyTypeRest prop = new PropertyTypeRest();
		prop.setName(name);
		prop.setType(type);
		expected.getPropertyTypes().add(prop);
	}

	public static void equals(ResultActions actions, long id, String name) throws Exception {
		actions.andExpect(jsonPath("$[0].id").value(id)).andExpect(jsonPath("$[0].name").value(name));
	}

	public static ResultActions get(MockMvc mockMvc, String url) throws Exception {
		return mockMvc.perform(MockMvcRequestBuilders.get(url).accept(MediaType.APPLICATION_JSON));
	}

	public static ResultActions post(MockMvc mockMvc, String url, Object instance) throws Exception {
		return mockMvc.perform(MockMvcRequestBuilders.post(url).accept(MediaType.APPLICATION_JSON)
				.content(new Gson().toJson(instance)));
	}

	public static ResultActions put(MockMvc mockMvc, String url, Object instance) throws Exception {
		return mockMvc.perform(MockMvcRequestBuilders.put(url).accept(MediaType.APPLICATION_JSON)
				.content(new Gson().toJson(instance)));
	}

	public static ResultActions delete(MockMvc mockMvc, String url) throws Exception {
		return mockMvc.perform(MockMvcRequestBuilders.delete(url).accept(MediaType.APPLICATION_JSON));
	}

	public static ResultMatcher entityType(final int position, final String name) {
		return new ResultMatcher() {
			public void match(MvcResult result) throws Exception {
				jsonPath("$[" + position + "].name").value(name).match(result);
			}
		};
	}

	public static ResultMatcher entityType(final String name) {
		return new ResultMatcher() {
			public void match(MvcResult result) throws Exception {
				jsonPath("$.name").value(name).match(result);
			}
		};
	}

	public static ResultMatcher propertyType(final int propertyTypePosition, final String name,
			final PropertyTypeType type) {
		return new ResultMatcher() {
			public void match(MvcResult result) throws Exception {
				jsonPath("$.propertyTypes[" + propertyTypePosition + "].name").value(name).match(result);
				jsonPath("$.propertyTypes[" + propertyTypePosition + "].type").value(type.name()).match(result);
			}
		};
	}

	public static ResultMatcher relationshipType(final int relationshipTypePosition, final String name,
			final String targetTypeName, final Cardinality sourceCardinality, final Cardinality targetCardinality) {
		return new ResultMatcher() {
			public void match(MvcResult result) throws Exception {
				jsonPath("$.relationshipTypes[" + relationshipTypePosition + "].name").value(name).match(result);
				jsonPath("$.relationshipTypes[" + relationshipTypePosition + "].targetType").value(targetTypeName)
						.match(result);
				jsonPath("$.relationshipTypes[" + relationshipTypePosition + "].targetCardinality")
						.value(targetCardinality.name()).match(result);
				jsonPath("$.relationshipTypes[" + relationshipTypePosition + "].sourceCardinality")
						.value(sourceCardinality.name()).match(result);
			}
		};
	}

	public static ResultMatcher instance(final Map<String, Object> instanceMap) {
		return new ResultMatcher() {
			@SuppressWarnings("unchecked")
			public void match(MvcResult result) throws Exception {
				for (String key : instanceMap.keySet()) {
					Object value = instanceMap.get(key);

					if (value instanceof List) {
						List<Object> list = (List<Object>) value;
						int i = 0;
						for (Object item : list) {
							jsonPath("$." + key + "[" + i + "]").value(item).match(result);
							i++;
						}
					} else {
						jsonPath("$." + key).value(value).match(result);
					}
				}
			}
		};
	}

	public static Map<String, Object> objectToMap(Object obj) throws Exception {
		Map<String, Object> result = new HashMap<String, Object>();
		BeanInfo info = Introspector.getBeanInfo(obj.getClass());
		for (PropertyDescriptor pd : info.getPropertyDescriptors()) {
			Method reader = pd.getReadMethod();
			if (reader != null && !pd.getName().equals("class"))
				result.put(pd.getName(), reader.invoke(obj));
		}
		return result;
	}

	public static void fixWidgetInstanceMap(Map<String, Object> widgetInstanceMap, int version, String typeName) {
		widgetInstanceMap.remove("id");
		widgetInstanceMap.remove("requiredPorts");
		widgetInstanceMap.put("version", version);
		widgetInstanceMap.put("type", typeName);
	}

	public static void fixRuleInstanceMap(Map<String, Object> ruleInstanceMap) {
		ruleInstanceMap.remove("id");
		ruleInstanceMap.remove("version");
		ruleInstanceMap.remove("widgetVersion");
		ruleInstanceMap.remove("type");
	}

	public static Date getDate(int year, int month, int day, int hour, int minute, int second) {
		Calendar cal = Calendar.getInstance();
		cal.setTimeInMillis(0);
		cal.set(year, month, day, hour, minute, second);
		return cal.getTime();
	}

	public static <T> T getObjectFromResult(MvcResult result, Class<T> type)
			throws JsonSyntaxException, UnsupportedEncodingException {
		Gson gson = new GsonBuilder().registerTypeAdapter(Date.class, new JsonDeserializer<Date>() {

			public Date deserialize(JsonElement jsonElement, Type type, JsonDeserializationContext context)
					throws JsonParseException {
				return new Date(jsonElement.getAsLong());
			}
		}).create();
		return gson.fromJson(result.getResponse().getContentAsString(), type);
	}

	static void widget(String name, WidgetType type, PortRest... ports) {
		WidgetRest widget = new WidgetRest();
		widget.setName(name);
		widget.setType(type.name());
		widget.setRequiredPorts(Arrays.asList(ports));
		
		TestHelper.postJSON(TestHelper.SERVER_URL + "widgets", widget);
		
		TestHelper.post(TestHelper.SERVER_URL + "widgets/" + name + "/code", TestHelper.readWidgetFile(name));
	}

	static String readWidgetFile(String fileName) {
		URL resource = EntryPoint.class.getResource("/widgets/" + fileName + ".js");
		String filePath = resource.getPath();
		try {
			Path widgetPath = Paths.get(filePath);
			return new String(Files.readAllBytes(widgetPath));
		} catch (Exception e) {
			Assert.fail(e.getMessage());
			return null;
		}
	}

	static void rule(String port, String widget) {
		RuleRest rule = new RuleRest();
		rule.setPortName(port);
		rule.setWidgetName(widget);
		TestHelper.postJSON(TestHelper.SERVER_URL + "rules", rule);
	}

	static void openApp() {
		EntityWidgetTest.driver.get(TestHelper.SERVER_URL);
	}

	static final String SERVER_URL = "http://localhost:8080/";

	static void clickEntityType(Class<?> entityType) {
		EntityWidgetTest.driver.findElement(By.id("entityType_" + entityType.getSimpleName())).click();
	}

	static void checkTitle(Class<?> entityType) {
		EntityWidgetTest.driver.findElement(By.id("title_" + entityType.getSimpleName()));
	}

	static void checkList(Class<?> entityType) {
		EntityWidgetTest.driver.findElement(By.id("list_" + entityType.getSimpleName()));
	}

	static void deployEntityType(Class<?> entityType, Class<?> repository) {
		EntityTypeDeployRest rest = new EntityTypeDeployRest();
		rest.setEntity(entityType.getName());
		rest.setRepository(repository.getName());
		TestHelper.postJSON(SERVER_URL + "entities", rest);
	}

	static void postJSON(String url, Object data) {
	
		try (CloseableHttpClient httpClient = HttpClientBuilder.create().build()) {
	
			StringEntity input = new StringEntity(new Gson().toJson(data));
			input.setContentType("application/json");
			HttpPost request = new HttpPost(url);
			request.setEntity(input);
			CloseableHttpResponse response = httpClient.execute(request);
	
			Assert.assertEquals(201, response.getStatusLine().getStatusCode());
	
		} catch (IOException e) {
			Assert.fail(e.getMessage());
		}
	}
	
	static void post(String url, String data) {
		
		try (CloseableHttpClient httpClient = HttpClientBuilder.create().build()) {
	
			StringEntity input = new StringEntity(data);
			input.setContentType("application/json");
			HttpPost request = new HttpPost(url);
			request.setEntity(input);
			CloseableHttpResponse response = httpClient.execute(request);
	
			Assert.assertEquals(201, response.getStatusLine().getStatusCode());
	
		} catch (IOException e) {
			Assert.fail(e.getMessage());
		}
	}

}
