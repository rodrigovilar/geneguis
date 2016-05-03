package br.edu.ufcg.embedded.ise.geneguis.backend.controller;

import br.edu.ufcg.embedded.ise.geneguis.FieldKind;
import br.edu.ufcg.embedded.ise.geneguis.PropertyTypeType;

public class PropertyTypeRest extends FieldTypeRest {

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
