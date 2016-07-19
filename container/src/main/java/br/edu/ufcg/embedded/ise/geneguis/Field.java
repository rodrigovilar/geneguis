package br.edu.ufcg.embedded.ise.geneguis;

public class Field {

	private FieldType type;
	private Object value;
	private Entity entity;

	
	public Field() {
		this(null,null);
	}
	
	public Field(FieldType type, Entity entity) {
		this.type = type;
		this.entity = entity;
	}

	public Object getValue() {
		return value;
	}

	public void setValue(Object value) {
		this.value = value;
	}

	public FieldType getType() {
		return type;
	}

	public void setType(FieldType type) {
		this.type = type;
	}

	public Entity getEntity() {
		return entity;
	}

	public void setEntity(Entity entity) {
		this.entity = entity;
	}

}
