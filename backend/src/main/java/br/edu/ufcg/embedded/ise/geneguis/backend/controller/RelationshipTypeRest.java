package br.edu.ufcg.embedded.ise.geneguis.backend.controller;

import br.edu.ufcg.embedded.ise.geneguis.backend.Cardinality;

public class RelationshipTypeRest {

	private String name;
	private EntityTypeRest targetType;
	private Cardinality targetCardinality;
	private Cardinality sourceCardinality;
	
	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public EntityTypeRest getTargetType() {
		return targetType;
	}

	public void setTargetType(EntityTypeRest targetType) {
		this.targetType = targetType;
	}

	public Cardinality getTargetCardinality() {
		return targetCardinality;
	}

	public void setTargetCardinality(Cardinality targetCardinality) {
		this.targetCardinality = targetCardinality;
	}

	public Cardinality getSourceCardinality() {
		return sourceCardinality;
	}

	public void setSourceCardinality(Cardinality sourceCardinality) {
		this.sourceCardinality = sourceCardinality;
	}
}
