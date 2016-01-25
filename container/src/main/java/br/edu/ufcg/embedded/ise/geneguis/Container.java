package br.edu.ufcg.embedded.ise.geneguis;

import java.util.List;

public class Container {

	private final DomainModel model;

	public Container(DomainModel model){
		if (model == null) {
			throw new ContainerException("The domain model is mandatory");
		}
		
		this.model = model;
	}
	
	public Iterable<EntityType> getEntityTypes() {
		return model.getEntityTypes();
	}

	public EntityType getEntityType(String name) {
		for (EntityType entityType : getEntityTypes()) {
			if (entityType.getName().equals(name)) {
				return entityType;
			}
		}
		
		return null;
	}

	public <T> List<T> getEntities(String resource) {
		return model.getEntities(resource);
	}

	public <T> T saveEntity(String name, T instance) {
		return model.saveEntity(name, instance);
	}

	public <T> T getEntity(String name, Long entityId) {
		return model.getEntity(name, entityId);
	}

	public <T> T saveEntity(Long entityId, String name, T newEntity) {
		return model.saveEntity(entityId, name, newEntity);
	}

	public boolean deleteEntity(String name, Long entityId) {
		return model.deleteEntity(name, entityId);
	}

}
