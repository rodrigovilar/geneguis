package br.edu.ufcg.embedded.ise.geneguis.test;

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

	public Entity getEntity(String name, Long entityId) {
		return null;
	}

	public Entity saveEntity(Long entityId, String name, Entity newEntity) {
		// TODO Auto-generated method stub
		return null;
	}

	public boolean deleteEntity(String name, Long entityId) {
		// TODO Auto-generated method stub
		return false;
	}

	public void clear() {
		
	}
}