package br.edu.ufcg.embedded.ise.geneguis.backend.controller;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.JsonNodeFactory;
import com.fasterxml.jackson.databind.node.ObjectNode;

import br.edu.ufcg.embedded.ise.geneguis.Cardinality;
import br.edu.ufcg.embedded.ise.geneguis.ContainerException;
import br.edu.ufcg.embedded.ise.geneguis.Entity;
import br.edu.ufcg.embedded.ise.geneguis.EntityType;
import br.edu.ufcg.embedded.ise.geneguis.Field;
import br.edu.ufcg.embedded.ise.geneguis.FieldType;
import br.edu.ufcg.embedded.ise.geneguis.PropertyType;
import br.edu.ufcg.embedded.ise.geneguis.RelationshipType;

public class JsonMetadata {

	static ObjectMapper mapper = new ObjectMapper();

	public static ArrayNode renderInstances(Object[] instances, EntityType entityType) {
		ArrayNode an = JsonNodeFactory.instance.arrayNode();

		for (Object entity : instances) {
			ObjectNode on = renderInstance((Entity) entity, entityType);
			an.add(on);
		}
		return an;
	}

	public static void parseInstance(String input, Entity entity) throws Exception {
		JsonNode on = mapper.readTree(input);

		for (FieldType fieldType : entity.getType().getFieldTypes()) {

			JsonNode fv = on.get(fieldType.getName());

			if (fv != null) {

				switch (fieldType.getKind()) {
				case Property:
					parseProperty(fv, entity, fieldType);

					break;

				case Relationship:
					parseRelationship(entity, fieldType, fv);

					break;
				}
			}

		}
	}

	public static void parseProperty(JsonNode fv, Entity entity, FieldType fieldType) throws ContainerException {
		Object value = null;
		PropertyType pt = (PropertyType) fieldType;
		Field property = new Field();
		property.setType(pt);
		property.setEntity(entity);
		entity.getFields().add(property);

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

		case enumeration:
			value = fv.asText();
			break;
		}
		property.setValue(value);
	}

	@SuppressWarnings({ "rawtypes", "unchecked" })
	public static void parseRelationship(Entity entity, FieldType fieldType, JsonNode fv) throws Exception {
		RelationshipType rt = (RelationshipType) fieldType;
		Object value = null;

		if (rt.getTargetCardinality() == Cardinality.One) {
			value = fv.asLong();

		} else {
			value = new ArrayList();

			Iterator<JsonNode> elements = fv.elements();
			while (elements.hasNext()) {
				Object item = elements.next().asLong();
				((ArrayList) value).add(item);
			}
		}

		Field field = new Field();
		field.setType(rt);
		field.setValue(value);
		field.setEntity(entity);
		entity.getFields().add(field);
	}

	public static ObjectNode renderInstance(Entity entity, EntityType entityType) {
		ObjectNode on = JsonNodeFactory.instance.objectNode();
		
		for (Field field : entity.getFields()) {
			FieldType fieldType = field.getType();
			switch (fieldType.getKind()) {
			case Property:
				PropertyType propertyType = (PropertyType) fieldType;
				renderPropertyType(on, field, propertyType);
				break;
			case Relationship:
				RelationshipType relationshipType = (RelationshipType) fieldType;
				renderRelationshipType(on, field, relationshipType);
				break;
			}
			
		}

		return on;
	}	
	
	public static void renderPropertyType(ObjectNode on, Field field, PropertyType propertyType) {
		Object propertyValue = field.getValue();

		if (propertyValue != null) {
			if (propertyValue instanceof Long) {
				on.put(propertyType.getName(), (Long) propertyValue);
			} else if (propertyValue instanceof Integer) {
				on.put(propertyType.getName(), (Integer) propertyValue);
			} else if (propertyValue instanceof Double) {
				on.put(propertyType.getName(), (Double) propertyValue);
			} else if (propertyValue instanceof String || propertyValue.getClass().isEnum()) {
				on.put(propertyType.getName(), (String) propertyValue.toString());
			}
		}
	}

	@SuppressWarnings("rawtypes")
	public static void renderRelationshipType(ObjectNode on, Field field,
			RelationshipType relationshipType) {
		
		if (field.getValue() == null) {
			return;
		}

		if (Cardinality.One == relationshipType.getTargetCardinality()) {
			on.put(relationshipType.getName(), (Long) field.getValue());
		} else { //Many
			ArrayNode an = JsonNodeFactory.instance.arrayNode();
			List targetInstances = (List) field.getValue();
			for (Object targetInstance : targetInstances) {
				an.add((Long) targetInstance);
			}
			on.set(relationshipType.getName(), an);
		}
	}
}
