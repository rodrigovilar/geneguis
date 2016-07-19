package br.edu.ufcg.embedded.ise.geneguis.jpadomain;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import org.springframework.beans.BeanWrapper;
import org.springframework.beans.BeanWrapperImpl;

import br.edu.ufcg.embedded.ise.geneguis.Cardinality;
import br.edu.ufcg.embedded.ise.geneguis.Entity;
import br.edu.ufcg.embedded.ise.geneguis.EntityType;
import br.edu.ufcg.embedded.ise.geneguis.Field;
import br.edu.ufcg.embedded.ise.geneguis.FieldType;
import br.edu.ufcg.embedded.ise.geneguis.PropertyType;
import br.edu.ufcg.embedded.ise.geneguis.RelationshipType;

public class DomainMetadata {

	public static void fromDomainProperty(Field field, BeanWrapper instanceWrapper, PropertyType propertyType) {
		Object propertyValue = instanceWrapper.getPropertyValue(propertyType.getName());
	
		if (propertyValue == null) {
			return;
		}
	
		if (propertyValue.getClass().isEnum()) {
			field.setValue(propertyValue.toString());
		} else {
			field.setValue(propertyValue);
		}
	}

	public static <T> Entity fromDomain(T t, EntityType entityType) {
		if (t == null) {
			return null;
		}
		
		Entity entity = new Entity();
		entity.setType(entityType);
		BeanWrapper instanceWrapper = new BeanWrapperImpl(t);
	
		for (FieldType fieldType : entityType.getFieldTypes()) {
	
			Field field = new Field(fieldType, entity);
			switch (fieldType.getKind()) {
			case Property:
				PropertyType propertyType = (PropertyType) fieldType;
				fromDomainProperty(field, instanceWrapper, propertyType);
				break;
			case Relationship:
				RelationshipType relationshipType = (RelationshipType) fieldType;
				DomainMetadata.fromDomainRelationship(field, instanceWrapper, relationshipType);
				break;
			}
			entity.getFields().add(field);
		}
		return entity;
	}

	@SuppressWarnings("rawtypes")
	public static void fromDomainRelationship(Field field, BeanWrapper instanceWrapper,
			RelationshipType relationshipType) {
		if (Cardinality.One == relationshipType.getTargetCardinality()) {
			Object targetInstance = instanceWrapper.getPropertyValue(relationshipType.getName());
			if (targetInstance != null) {
				BeanWrapper targetInstanceWrapper = new BeanWrapperImpl(targetInstance);
				field.setValue(targetInstanceWrapper.getPropertyValue("id"));
			}
		} else { // Many
			ArrayList<Long> ids = new ArrayList<Long>();
			List targetInstances = (List) instanceWrapper.getPropertyValue(relationshipType.getName());
			for (Object targetInstance : targetInstances) {
				BeanWrapper targetInstanceWrapper = new BeanWrapperImpl(targetInstance);
				ids.add((Long) targetInstanceWrapper.getPropertyValue("id"));
			}
			field.setValue(ids);
		}
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

}
