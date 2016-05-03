package br.edu.ufcg.embedded.ise.geneguis;

public class RelationshipType extends FieldType {

	private EntityType targetType;
	private Cardinality targetCardinality;
	private Cardinality sourceCardinality;
	private String reverse;
	
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
	
	@Override
	public FieldKind getKind() {
		return FieldKind.Relationship;
	}

	public String getReverse() {
		return reverse;
	}

	public void setReverse(String reverse) {
		this.reverse = reverse;
	}
}
