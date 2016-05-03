package br.edu.ufcg.embedded.ise.geneguis;

public class PropertyType extends FieldType {

	private PropertyTypeType type;

	
	public PropertyTypeType getType() {
		return type;
	}

	public void setType(PropertyTypeType type) {
		this.type = type;
	}

	@Override
	public FieldKind getKind() {
		return FieldKind.Property;
	}

}
