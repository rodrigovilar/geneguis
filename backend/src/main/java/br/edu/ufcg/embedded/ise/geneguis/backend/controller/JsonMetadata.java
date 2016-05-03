package br.edu.ufcg.embedded.ise.geneguis.backend.controller;

import static br.edu.ufcg.embedded.ise.geneguis.backend.EntryPoint.getDomainModel;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;

import org.springframework.beans.BeanWrapper;
import org.springframework.beans.BeanWrapperImpl;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.JsonNodeFactory;
import com.fasterxml.jackson.databind.node.ObjectNode;

import br.edu.ufcg.embedded.ise.geneguis.Cardinality;
import br.edu.ufcg.embedded.ise.geneguis.EntityType;
import br.edu.ufcg.embedded.ise.geneguis.FieldType;
import br.edu.ufcg.embedded.ise.geneguis.PropertyType;
import br.edu.ufcg.embedded.ise.geneguis.RelationshipType;

public class JsonMetadata {

	static ObjectMapper mapper = new ObjectMapper();

	public static ArrayNode renderInstances(Object[] instances, EntityType entityType) {
		ArrayNode an = JsonNodeFactory.instance.arrayNode();
	
		for (Object object : instances) {
			ObjectNode on = renderInstance(object, entityType);
			an.add(on);
		}
	
		return an;
	}

	public static void parseInstance(EntityType entityType, String input, Object newInstance) throws Exception {
	
		JsonNode on = mapper.readTree(input);
	
		for (FieldType fieldType : entityType.getFieldTypes()) {
	
			JsonNode fv = on.get(fieldType.getName());
			
			if (fv != null) {
				
				switch (fieldType.getKind()) {
				case Property:
					parseProperty(fv, newInstance, fieldType);
					
					break;
					
				case Relationship:
					parseRelationship(newInstance, fieldType, fv);
					
					break;
				}
			}
	
		}
	}

	@SuppressWarnings({ "rawtypes", "unchecked" })
	public static void parseRelationship(Object newInstance, FieldType fieldType, JsonNode fv) throws Exception {
		BeanWrapper instanceWrapper = new BeanWrapperImpl(newInstance);
		RelationshipType rt = (RelationshipType) fieldType;
		Object value = null;
	
		if (rt.getTargetCardinality() == Cardinality.One) {
			value = createWithId(fv, rt);
			setReverse(newInstance, rt, value);
	
		} else {
			value = new ArrayList();
	
			Iterator<JsonNode> elements = fv.elements();
			while (elements.hasNext()) {
				Object item = createWithId(elements.next(), rt);
				((ArrayList) value).add(item);
				setReverse(newInstance, rt, item);
			}
		}
	
		instanceWrapper.setPropertyValue(rt.getName(), value);
	}

	@SuppressWarnings({ "rawtypes", "unchecked" })
	public static void setReverse(Object newInstance, RelationshipType rt, Object item) {
		if (item == null) {
			return;
		}
		
		BeanWrapper itemWrapper = new BeanWrapperImpl(item);
		
		if (rt.getSourceCardinality() == Cardinality.One) {
			itemWrapper.setPropertyValue(rt.getReverse(), newInstance);
	
		} else {
			Collection reverseValue = (Collection) itemWrapper.getPropertyValue(rt.getReverse());
			reverseValue.add(newInstance);
		}
	}

	public static Object createWithId(JsonNode jn, RelationshipType rt) throws Exception {
		if (getDomainModel() == null) {
			return null;
		}
		return getDomainModel().getEntity(rt.getTargetType().getName(), jn.asLong());
	}

	public static void parseProperty(JsonNode fv, Object newInstance, FieldType fieldType) {
		BeanWrapper instanceWrapper = new BeanWrapperImpl(newInstance);
		Object value = null;
		PropertyType pt = (PropertyType) fieldType;
	
		switch (pt.getType()) {
	
		case bool:
			value = fv.asBoolean();
			break;
	
		case date:
			value = fv.asText();
			break;
	
		case integer:
			value = fv.asLong();
			break;
	
		case real:
			value = fv.asDouble();
			break;
	
		case string:
			value = fv.asText();
			break;
		}
	
		instanceWrapper.setPropertyValue(pt.getName(), value);
	}

	public static ObjectNode renderInstance(Object instance, EntityType entityType) {
		ObjectNode on = JsonNodeFactory.instance.objectNode();
		BeanWrapper instanceWrapper = new BeanWrapperImpl(instance);
	
		for (FieldType fieldType : entityType.getFieldTypes()) {
	
			switch (fieldType.getKind()) {
			case Property:
				PropertyType propertyType = (PropertyType) fieldType;
				renderPropertyType(on, instanceWrapper, propertyType);
				break;
			case Relationship:
				RelationshipType relationshipType = (RelationshipType) fieldType;
				renderRelationshipType(on, instanceWrapper, relationshipType);
				break;
			}
		}
	
		return on;
	}

	public static void renderPropertyType(ObjectNode on, BeanWrapper instanceWrapper, PropertyType propertyType) {
		Object propertyValue = instanceWrapper.getPropertyValue(propertyType.getName());
		if (propertyValue != null) {
			if (propertyValue instanceof Long) {
				on.put(propertyType.getName(), (Long) propertyValue);
			} else if (propertyValue instanceof Integer) {
				on.put(propertyType.getName(), (Integer) propertyValue);
			} else if (propertyValue instanceof Double) {
				on.put(propertyType.getName(), (Double) propertyValue);
			} else {
				on.put(propertyType.getName(), (String) propertyValue);
			}
		}
	}

	@SuppressWarnings("rawtypes")
	public static void renderRelationshipType(ObjectNode on, BeanWrapper instanceWrapper, RelationshipType relationshipType) {
		switch (relationshipType.getTargetCardinality()) {
		case One:
			Object targetInstance = instanceWrapper.getPropertyValue(relationshipType.getName());
			if (targetInstance != null) {
				BeanWrapper targetInstanceWrapper = new BeanWrapperImpl(targetInstance);
				on.put(relationshipType.getName(), (Long) targetInstanceWrapper.getPropertyValue("id"));
			}
			break;
		case Many:
			ArrayNode an = JsonNodeFactory.instance.arrayNode();
			List targetInstances = (List) instanceWrapper.getPropertyValue(relationshipType.getName());
			for (Object targetInstance2 : targetInstances) {
				BeanWrapper targetInstanceWrapper2 = new BeanWrapperImpl(targetInstance2);
				an.add((Long) targetInstanceWrapper2.getPropertyValue("id"));
			}
			on.set(relationshipType.getName(), an);
			break;
		}
	}


}
