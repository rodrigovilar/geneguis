package br.edu.ufcg.embedded.ise.geneguis.test;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import br.edu.ufcg.embedded.ise.geneguis.DomainModel;
import br.edu.ufcg.embedded.ise.geneguis.Entity;
import br.edu.ufcg.embedded.ise.geneguis.EntityType;

public class PojoDomainModel implements DomainModel {

	private List<EntityType> pojoEntityTypes = new ArrayList<EntityType>();

	public List<EntityType> getPojoEntityTypes() {
		return pojoEntityTypes;
	}

	public List<EntityType> getEntityTypes() {
		return pojoEntityTypes;
	}

	public List<Entity> getEntities(String resource) {
		return null;
	}

	public Entity saveEntity(String name, Entity instance) {
		return null;
	}

	public void clear() {
		
	}

	public <T, K extends Serializable> Entity getEntity(String name, K entityId) {
		return null;
	}

	public <T, K extends Serializable> Entity saveEntity(K entityId, String name, Entity newEntity) throws Exception {
		return null;
	}

	public <K extends Serializable> boolean deleteEntity(String name, K entityId) {
		return false;
	}
}