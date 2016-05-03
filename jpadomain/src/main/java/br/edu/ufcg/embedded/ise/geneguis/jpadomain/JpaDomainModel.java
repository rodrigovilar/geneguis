package br.edu.ufcg.embedded.ise.geneguis.jpadomain;

import java.util.ArrayList;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import br.edu.ufcg.embedded.ise.geneguis.ContainerException;
import br.edu.ufcg.embedded.ise.geneguis.DomainModel;
import br.edu.ufcg.embedded.ise.geneguis.EntityType;

@SuppressWarnings("unchecked")
public class JpaDomainModel implements DomainModel {

	private List<EntityType> entityTypes = new ArrayList<EntityType>();
	private List<JpaRepository<?, ?>> repositories = new ArrayList<JpaRepository<?, ?>>();
	private List<Class<?>> classes = new ArrayList<Class<?>>();

	public Iterable<EntityType> getEntityTypes() {
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

	public void clear() {
		entityTypes = new ArrayList<EntityType>();
		classes = new ArrayList<Class<?>>();
		for (JpaRepository<?, ?> repository : repositories) {
			repository.deleteAll();
		}
		repositories = new ArrayList<JpaRepository<?, ?>>();
	}
}
