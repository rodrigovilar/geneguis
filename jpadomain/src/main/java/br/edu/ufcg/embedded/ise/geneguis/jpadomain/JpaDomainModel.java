package br.edu.ufcg.embedded.ise.geneguis.jpadomain;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.BeanWrapper;
import org.springframework.beans.BeanWrapperImpl;
import org.springframework.data.jpa.repository.JpaRepository;

import br.edu.ufcg.embedded.ise.geneguis.Cardinality;
import br.edu.ufcg.embedded.ise.geneguis.ContainerException;
import br.edu.ufcg.embedded.ise.geneguis.DomainModel;
import br.edu.ufcg.embedded.ise.geneguis.Entity;
import br.edu.ufcg.embedded.ise.geneguis.EntityType;
import br.edu.ufcg.embedded.ise.geneguis.EnumType;
import br.edu.ufcg.embedded.ise.geneguis.Field;
import br.edu.ufcg.embedded.ise.geneguis.FieldKind;
import br.edu.ufcg.embedded.ise.geneguis.PropertyType;
import br.edu.ufcg.embedded.ise.geneguis.PropertyTypeType;
import br.edu.ufcg.embedded.ise.geneguis.RelationshipType;

@SuppressWarnings("unchecked")
public class JpaDomainModel implements DomainModel {

	private List<EntityType> entityTypes = new ArrayList<EntityType>();
	private List<JpaRepository<?, ?>> repositories = new ArrayList<JpaRepository<?, ?>>();
	private List<Class<?>> classes = new ArrayList<Class<?>>();

	public List<EntityType> getEntityTypes() {
		return entityTypes;
	}

	public <T> void deployEntityType(Class<T> clazz, JpaRepository<T, ?> repository) {
		MetadataUtil.checkJpaEntity(clazz);

		String name = clazz.getSimpleName();
		int position = checkUnique(name);

		EntityType entityType = null;

		if (position >= 0) {
			entityType = entityTypes.get(position);
			repositories.set(position, repository);
			classes.set(position, clazz);
			entityType.getFieldTypes().clear();

		} else {
			entityType = new EntityType();
			entityType.setName(name);
			entityTypes.add(entityType);
			repositories.add(repository);
			classes.add(clazz);
		}

		MetadataUtil.processEntity(clazz, entityType);
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

	private int getEntityPosition(String entityTypeName) {
		for (int i = 0; i < entityTypes.size(); i++) {
			EntityType entityType = entityTypes.get(i);
			if (entityType.getName().equals(entityTypeName)) {
				return i;
			}
		}

		throw new ContainerException("Unknown entity: " + entityTypeName);
	}

	public <T> List<Entity> getEntities(String entityType) {
		int entityPosition = getEntityPosition(entityType);
		JpaRepository<T, ?> repository = (JpaRepository<T, ?>) repositories.get(entityPosition);
		
		List<Entity> entities = new ArrayList<Entity>();
		for (T t : repository.findAll()) {
			entities.add(DomainMetadata.fromDomain(t, entityTypes.get(entityPosition)));
		}
		
		return entities;
	}

	public Class<?> getClass(String entityType) {
		int entityPosition = getEntityPosition(entityType);
		return classes.get(entityPosition);
	}

	public <T> Entity saveEntity(String entityType, Entity entity) throws Exception {
		int entityPosition = getEntityPosition(entityType);
		JpaRepository<T, ?> repository = (JpaRepository<T, ?>) repositories.get(entityPosition);
		T domain = toDomain(entity);
		return DomainMetadata.fromDomain(repository.saveAndFlush(domain), entity.getType());
	}

	private <T> T toDomain(Entity entity) throws Exception {
		T newInstance = (T) getClass(entity.getType().getName()).newInstance();

		for (Field field : entity.getFields()) {

			if (FieldKind.Property == field.getType().getKind()) {
				toDomainProperty(field, newInstance);

			} else { // Relationship
				toDomainRelationship(field, newInstance);
			}
		}
		return newInstance;
	}

	public void toDomainProperty(Field field, Object newInstance) throws ContainerException {
		BeanWrapper instanceWrapper = new BeanWrapperImpl(newInstance);
		PropertyType pt = (PropertyType) field.getType();
		Object value = null;

		if (PropertyTypeType.enumeration == pt.getType()) {
			value = getDomainEnumValue(field.getValue(), (EnumType) pt);

		} else {
			value = field.getValue();
		}

		instanceWrapper.setPropertyValue(pt.getName(), value);
	}

	private Object getDomainEnumValue(Object value, EnumType enumPropertyType) throws ContainerException {
		try {
			Class<?> clz = Class.forName(enumPropertyType.getSource());
			for (int i = 0; i < clz.getEnumConstants().length; i++) {
				Object enumValue = clz.getEnumConstants()[i];
				if (value.equals(enumValue.toString())) {
					return clz.getEnumConstants()[i];
				}
			}
		} catch (ClassNotFoundException e) {
			throw new ContainerException("Enum source not found");
		}

		return null;
	}

	@SuppressWarnings("rawtypes")
	public void toDomainRelationship(Field field, Object newInstance) throws Exception {
		BeanWrapper instanceWrapper = new BeanWrapperImpl(newInstance);
		RelationshipType rt = (RelationshipType) field.getType();
		Object value = null;

		if (rt.getTargetCardinality() == Cardinality.One) {
			value = createWithId((Long) field.getValue(), rt);
			DomainMetadata.setReverse(newInstance, rt, value);

		} else {
			value = new ArrayList();
			for (Long id : (List<Long>) field.getValue()) {
				Object item = createWithId(id, rt);
				((ArrayList) value).add(item);
				DomainMetadata.setReverse(newInstance, rt, item);
			}
		}

		instanceWrapper.setPropertyValue(rt.getName(), value);
	}

	public Object createWithId(Long id, RelationshipType rt) throws Exception {
		return getEntity(rt.getTargetType().getName(), id);
	}

	public <T,K extends Serializable> Entity getEntity(String entityType, K entityId) {
		int entityPosition = getEntityPosition(entityType);
		JpaRepository<T, K> repository = (JpaRepository<T, K>) repositories.get(entityPosition);
		
		T t = repository.findOne(entityId);
		return DomainMetadata.fromDomain(t, entityTypes.get(entityPosition));
	}

	public <T,K extends Serializable> Entity saveEntity(K entityId, String entityType, Entity entity) throws Exception {
		int entityPosition = getEntityPosition(entityType);
		JpaRepository<T, K> repository = (JpaRepository<T, K>) repositories.get(entityPosition);
		T domain = toDomain(entity);
		return DomainMetadata.fromDomain(repository.saveAndFlush(domain), entity.getType());
	}

	public <K extends Serializable> boolean deleteEntity(String entityType, K entityId) {
		int entityPosition = getEntityPosition(entityType);
		JpaRepository<?, K> repository = (JpaRepository<?, K>) repositories.get(entityPosition);

		if (!repository.exists(entityId)) {
			return false;
		}

		repository.delete(entityId);
		return true;
	}

	public void clear() {
		entityTypes = new ArrayList<EntityType>();
		classes = new ArrayList<Class<?>>();
		for (JpaRepository<?, ?> repository : repositories) {
			repository.deleteAll();
		}
		repositories = new ArrayList<JpaRepository<?, ?>>();
	}
}
