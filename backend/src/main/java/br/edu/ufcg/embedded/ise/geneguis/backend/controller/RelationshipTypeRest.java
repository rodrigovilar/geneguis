package br.edu.ufcg.embedded.ise.geneguis.backend.controller;

import br.edu.ufcg.embedded.ise.geneguis.Cardinality;

public class RelationshipTypeRest {

	private String name;
	private String targetType;
	private Cardinality targetCardinality;
	private Cardinality sourceCardinality;
	
	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getTargetType() {
		return targetType;
	}

	public void setTargetType(String targetType) {
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
