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

	public List<Entity> getEntities(String resource) {
		return model.getEntities(resource);
	}

	public Entity saveEntity(String name, Entity instance) {
		try {
			return model.saveEntity(name, instance);
		} catch (Exception e) {
			throw new ContainerException(e);
		}
	}

	public Entity getEntity(String name, Long entityId) {
		return model.getEntity(name, entityId);
	}

	public Entity saveEntity(Long entityId, String name, Entity newEntity) {
		try {
			return model.saveEntity(entityId, name, newEntity);
		} catch (Exception e) {
			throw new ContainerException(e);
		}
	}

	public boolean deleteEntity(String name, Long entityId) {
		return model.deleteEntity(name, entityId);
	}

}
