package br.edu.ufcg.embedded.ise.geneguis.backend.controller;

import java.util.ArrayList;
import java.util.List;

public class EntityTypeRest {

	private String name;
	private List<FieldTypeRest> fieldTypes = new ArrayList<FieldTypeRest>();

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public List<FieldTypeRest> getFieldTypes() {
		return fieldTypes;
	}

	public void setFieldTypes(List<FieldTypeRest> fieldTypes) {
		this.fieldTypes = fieldTypes;
	}
	

}
