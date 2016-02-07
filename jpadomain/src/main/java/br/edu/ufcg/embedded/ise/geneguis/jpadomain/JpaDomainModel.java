package br.edu.ufcg.embedded.ise.geneguis.jpadomain;

import java.lang.annotation.Annotation;
import java.lang.reflect.Field;
import java.lang.reflect.ParameterizedType;
import java.util.ArrayList;
import java.util.List;

import javax.persistence.Entity;
import javax.persistence.ManyToMany;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;

import org.springframework.data.jpa.repository.JpaRepository;

import br.edu.ufcg.embedded.ise.geneguis.Cardinality;
import br.edu.ufcg.embedded.ise.geneguis.ContainerException;
import br.edu.ufcg.embedded.ise.geneguis.DomainModel;
import br.edu.ufcg.embedded.ise.geneguis.EntityType;
import br.edu.ufcg.embedded.ise.geneguis.PropertyType;
import br.edu.ufcg.embedded.ise.geneguis.PropertyTypeType;
import br.edu.ufcg.embedded.ise.geneguis.RelationshipType;

@SuppressWarnings("unchecked")
public class JpaDomainModel implements DomainModel {

	private List<EntityType> entityTypes = new ArrayList<EntityType>();
	private List<JpaRepository<?, ?>> repositories = new ArrayList<JpaRepository<?, ?>>();
	private List<Class<?>> classes = new ArrayList<Class<?>>();

	public Iterable<EntityType> getEntityTypes() {
		return entityTypes;
	}

	public <T> void deployEntityType(Class<T> clazz, JpaRepository<T, ?> repository) {
		checkJpaEntity(clazz);

		String name = clazz.getSimpleName();
		int position = checkUnique(name);
		
		EntityType entityType = null;
				
		if (position >= 0) {
			entityType = entityTypes.get(position);
			repositories.set(position, repository);
			classes.set(position, clazz);
			entityType.getPropertyTypes().clear();
			entityType.getRelationshipTypes().clear();

		} else {
			entityType = new EntityType();
			entityType.setName(name);			
			entityTypes.add(entityType);
			repositories.add(repository);
			classes.add(clazz);
		}
		
		for (Field field : clazz.getSuperclass().getDeclaredFields()) {
			processField(entityType, field);
		}

		for (Field field : clazz.getDeclaredFields()) {
			processField(entityType, field);
		}
	}

	private int checkUnique(String name) {
		
		for (int i = 0; i < entityTypes.size(); i++) {
			EntityType entityType = entityTypes.get(i);
			if (entityType.getName().equals(name)) {
				return i;
			}
			
		}
		
		return -1;
	}

	private static void processField(EntityType entityType, Field field) {
		if (isProperty(field)) {
			PropertyType propertyTypeRest = propertyTypeFromField(field);
			entityType.getPropertyTypes().add(propertyTypeRest);
		} else {
			RelationshipType relationshipTypeRest = relationshipTypeRestFromField(field);
			entityType.getRelationshipTypes().add(relationshipTypeRest);
		}
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

	private static boolean isProperty(Field field) {
		return !hasAnnotation(field, OneToMany.class) && !hasAnnotation(field, OneToOne.class)
				&& !hasAnnotation(field, ManyToOne.class);
	}

	private static boolean hasAnnotation(Field field, Class<? extends Annotation> annotation) {
		return field.getAnnotation(annotation) != null;
	}

	private <T> void checkJpaEntity(Class<T> clazz) {
		Entity entity = clazz.getAnnotation(Entity.class);
		if (entity == null) {
			throw new ContainerException("Deployed classes must be annotated with JPA @Entity");
		}
	}

	private int getEntityPosition(String entityTypeName) {
		for (int i = 0; i < entityTypes.size(); i++) {
			EntityType entityType = entityTypes.get(i);
			if (entityType.getName().equals(entityTypeName)) {
				return i;
			}
		}

		throw new ContainerException("Unknown entity: " + entityTypeName);
	}

	public <T> List<T> getEntities(String entityType) {
		int entityPosition = getEntityPosition(entityType);
		JpaRepository<T, ?> repository = (JpaRepository<T, ?>) repositories.get(entityPosition);
		return repository.findAll();
	}

	public Class<?> getClass(String entityType) {
		int entityPosition = getEntityPosition(entityType);
		return classes.get(entityPosition);
	}

	public <T> T saveEntity(String entityType, T entity) {
		int entityPosition = getEntityPosition(entityType);
		JpaRepository<T, ?> repository = (JpaRepository<T, ?>) repositories.get(entityPosition);
		return repository.saveAndFlush(entity);
	}

	public <T> T getEntity(String entityType, Long entityId) {
		int entityPosition = getEntityPosition(entityType);
		JpaRepository<T, Long> repository = (JpaRepository<T, Long>) repositories.get(entityPosition);
		return repository.findOne(entityId);
	}

	public <T> T saveEntity(Long entityId, String entityType, T newEntity) {
		int entityPosition = getEntityPosition(entityType);
		JpaRepository<T, ?> repository = (JpaRepository<T, ?>) repositories.get(entityPosition);
		return repository.saveAndFlush(newEntity);
	}

	public boolean deleteEntity(String entityType, Long entityId) {
		int entityPosition = getEntityPosition(entityType);
		JpaRepository<?, Long> repository = (JpaRepository<?, Long>) repositories.get(entityPosition);

		if (!repository.exists(entityId)) {
			return false;
		}

		repository.delete(entityId);
		return true;
	}
}
