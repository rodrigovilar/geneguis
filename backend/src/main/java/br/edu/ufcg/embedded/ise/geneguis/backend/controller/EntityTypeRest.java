package br.edu.ufcg.embedded.ise.geneguis.backend.controller;

import java.util.ArrayList;
import java.util.List;

public class EntityTypeRest {

	private String name;
	private List<FieldTypeRest> fieldTypes = new ArrayList<FieldTypeRest>();
	private List<TagRest> tags = new ArrayList<TagRest>();

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

	public List<TagRest> getTags() {
		return tags;
	}

	public void setTags(List<TagRest> tags) {
		this.tags = tags;
	}
	

}
