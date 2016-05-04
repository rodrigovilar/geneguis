package br.edu.ufcg.embedded.ise.geneguis.jpadomain;

import java.lang.annotation.Annotation;
import java.lang.reflect.Field;
import java.lang.reflect.ParameterizedType;

import javax.persistence.Entity;
import javax.persistence.ManyToMany;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;

import br.edu.ufcg.embedded.ise.geneguis.Cardinality;
import br.edu.ufcg.embedded.ise.geneguis.ContainerException;
import br.edu.ufcg.embedded.ise.geneguis.EntityType;
import br.edu.ufcg.embedded.ise.geneguis.FieldType;
import br.edu.ufcg.embedded.ise.geneguis.PropertyType;
import br.edu.ufcg.embedded.ise.geneguis.PropertyTypeType;
import br.edu.ufcg.embedded.ise.geneguis.RelationshipType;

public class MetadataUtil {

	public static EntityType extractMetadata(Class<?> clazz) {
//		MetadataUtil.checkJpaEntity(clazz);

		EntityType entityType = new EntityType();
		entityType.setName(clazz.getSimpleName());

		MetadataUtil.processEntity(clazz, entityType);
		return entityType;
	}

	public static <T> void processEntity(Class<T> clazz, EntityType entityType) {
		for (Field field : clazz.getSuperclass().getDeclaredFields()) {
			processField(entityType, field);
		}

		for (Field field : clazz.getDeclaredFields()) {
			processField(entityType, field);
		}
	}

	public static void processField(EntityType entityType, Field field) {
		FieldType fieldType = null;

		if (isProperty(field)) {
			fieldType = propertyTypeFromField(field);
		} else {
			fieldType = relationshipTypeRestFromField(field);
		}
		entityType.getFieldTypes().add(fieldType);
	}

	public static PropertyType propertyTypeFromField(Field field) {
		PropertyType propertyTypeRest = new PropertyType();
		propertyTypeRest.setName(field.getName());
		String fieldName = field.getType().getSimpleName();
		propertyTypeRest.setType(propertyTypeTypeFromString(fieldName));
		return propertyTypeRest;
	}

	public static PropertyTypeType propertyTypeTypeFromString(String typeName) {
		if (typeName.equals("String")) {
			return PropertyTypeType.string;
		}
		if (typeName.equalsIgnoreCase("boolean")) {
			return PropertyTypeType.bool;
		}
		if (typeName.equals("Date")) {
			return PropertyTypeType.date;
		}
		if (typeName.equalsIgnoreCase("double")) {
			return PropertyTypeType.real;
		}
		if (typeName.equals("int") || typeName.equals("Integer")) {
			return PropertyTypeType.integer;
		}
		if (typeName.equalsIgnoreCase("long")) {
			return PropertyTypeType.integer;
		}
		return null;
	}

	public static RelationshipType relationshipTypeRestFromField(Field field) {
		RelationshipType relationshipTypeRest = new RelationshipType();
		relationshipTypeRest.setName(field.getName());
		relationshipTypeRest.setReverse(field.getAnnotation(Relationship.class).reverse());

		String typeName = null;

		if (field.getAnnotation(OneToOne.class) != null) {
			Class<?> type = field.getType();
			typeName = type.getSimpleName();
			relationshipTypeRest.setSourceCardinality(Cardinality.One);
			relationshipTypeRest.setTargetCardinality(Cardinality.One);
		} else if (field.getAnnotation(ManyToOne.class) != null) {
			Class<?> type = field.getType();
			typeName = type.getSimpleName();
			relationshipTypeRest.setSourceCardinality(Cardinality.Many);
			relationshipTypeRest.setTargetCardinality(Cardinality.One);
		} else if (field.getAnnotation(OneToMany.class) != null) {
			ParameterizedType genericType = (ParameterizedType) field.getGenericType();
			Class<?> type = (Class<?>) genericType.getActualTypeArguments()[0];
			typeName = type.getSimpleName();
			relationshipTypeRest.setSourceCardinality(Cardinality.One);
			relationshipTypeRest.setTargetCardinality(Cardinality.Many);
		} else if (field.getAnnotation(ManyToMany.class) != null) {
			ParameterizedType genericType = (ParameterizedType) field.getGenericType();
			Class<?> type = (Class<?>) genericType.getActualTypeArguments()[0];
			typeName = type.getSimpleName();
			relationshipTypeRest.setSourceCardinality(Cardinality.Many);
			relationshipTypeRest.setTargetCardinality(Cardinality.Many);
		}

		EntityType targetType = new EntityType(typeName);
		relationshipTypeRest.setTargetType(targetType);

		return relationshipTypeRest;
	}

	public static boolean isProperty(Field field) {
		return !hasAnnotation(field, OneToMany.class) && !hasAnnotation(field, OneToOne.class)
				&& !hasAnnotation(field, ManyToOne.class);
	}

	static boolean hasAnnotation(Field field, Class<? extends Annotation> annotation) {
		return field.getAnnotation(annotation) != null;
	}

	public static <T> void checkJpaEntity(Class<T> clazz) {
		Entity entity = clazz.getAnnotation(Entity.class);
		if (entity == null) {
			throw new ContainerException("Deployed classes must be annotated with JPA @Entity");
		}
	}

}