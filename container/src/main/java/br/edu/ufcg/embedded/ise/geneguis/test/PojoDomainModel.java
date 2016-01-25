package br.edu.ufcg.embedded.ise.geneguis.test;

import java.util.ArrayList;
import java.util.List;

import br.edu.ufcg.embedded.ise.geneguis.DomainModel;
import br.edu.ufcg.embedded.ise.geneguis.EntityType;

public class PojoDomainModel implements DomainModel {

	private List<EntityType> pojoEntityTypes = new ArrayList<EntityType>();

	public List<EntityType> getPojoEntityTypes() {
		return pojoEntityTypes;
	}

	public Iterable<EntityType> getEntityTypes() {
		return pojoEntityTypes;
	}

	public <T> List<T> getEntities(String resource) {
		return null;
	}

	public <T> T saveEntity(String name, T instance) {
		return null;
	}

	public <T> T getEntity(String name, Long entityId) {
		return null;
	}

	public <T> T saveEntity(Long entityId, String name, T newEntity) {
		// TODO Auto-generated method stub
		return null;
	}

	public boolean deleteEntity(String name, Long entityId) {
		// TODO Auto-generated method stub
		return false;
	}
}