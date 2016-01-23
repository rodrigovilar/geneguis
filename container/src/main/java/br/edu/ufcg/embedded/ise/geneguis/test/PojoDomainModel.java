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
}