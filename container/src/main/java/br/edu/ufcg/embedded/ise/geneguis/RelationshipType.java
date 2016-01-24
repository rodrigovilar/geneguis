package br.edu.ufcg.embedded.ise.geneguis;

public class RelationshipType {

	private String name;
	private EntityType targetType;
	private Cardinality targetCardinality;
	private Cardinality sourceCardinality;
	
	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public EntityType getTargetType() {
		return targetType;
	}

	public void setTargetType(EntityType targetType) {
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
