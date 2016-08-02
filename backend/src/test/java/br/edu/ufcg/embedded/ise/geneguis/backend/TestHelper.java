package br.edu.ufcg.embedded.ise.geneguis.backend;

import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;

import java.beans.BeanInfo;
import java.beans.Introspector;
import java.beans.PropertyDescriptor;
import java.io.UnsupportedEncodingException;
import java.lang.reflect.Method;
import java.lang.reflect.Type;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

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
import br.edu.ufcg.embedded.ise.geneguis.FieldKind;
import br.edu.ufcg.embedded.ise.geneguis.PropertyTypeType;
import br.edu.ufcg.embedded.ise.geneguis.backend.controller.EntityTypeRest;
import br.edu.ufcg.embedded.ise.geneguis.backend.controller.PropertyTypeRest;

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
		expected.getFieldTypes().add(prop);
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

	public static ResultMatcher entityType(final int position, final String name, final String... tags) {
		return new ResultMatcher() {
			public void match(MvcResult result) throws Exception {
				jsonPath("$[" + position + "].name").value(name).match(result);
				
				for (int i = 0; i < tags.length; i++) {
					String[] tagArray = tags[i].split(";");					
					String tagName = tagArray[0];
					jsonPath("$[" + position + "].tags[" + i + "].name").value(tagName).match(result);
					if (tagArray.length > 1) {
						String tagValue = tagArray[1];
						jsonPath("$[" + position + "].tags[" + i + "].value").value(tagValue).match(result);						
					}
				}
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
				jsonPath("$.fieldTypes[" + propertyTypePosition + "].name").value(name).match(result);
				jsonPath("$.fieldTypes[" + propertyTypePosition + "].type").value(type.name()).match(result);
				jsonPath("$.fieldTypes[" + propertyTypePosition + "].kind").value(FieldKind.Property.name())
						.match(result);
			}
		};
	}

	public static ResultMatcher relationshipType(final int relationshipTypePosition, final String name,
			final String targetTypeName, final Cardinality sourceCardinality, final Cardinality targetCardinality) {
		return new ResultMatcher() {
			public void match(MvcResult result) throws Exception {
				jsonPath("$.fieldTypes[" + relationshipTypePosition + "].name").value(name).match(result);
				jsonPath("$.fieldTypes[" + relationshipTypePosition + "].targetType").value(targetTypeName)
						.match(result);
				jsonPath("$.fieldTypes[" + relationshipTypePosition + "].targetCardinality")
						.value(targetCardinality.name()).match(result);
				jsonPath("$.fieldTypes[" + relationshipTypePosition + "].sourceCardinality")
						.value(sourceCardinality.name()).match(result);
				jsonPath("$.fieldTypes[" + relationshipTypePosition + "].kind").value(FieldKind.Relationship.name())
						.match(result);
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

}
