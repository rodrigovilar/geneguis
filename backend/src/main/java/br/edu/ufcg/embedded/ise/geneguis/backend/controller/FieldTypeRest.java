package br.edu.ufcg.embedded.ise.geneguis.backend.controller;

import java.util.ArrayList;
import java.util.List;

import br.edu.ufcg.embedded.ise.geneguis.FieldKind;

public abstract class FieldTypeRest {

	private String name;
	private List<TagRest> tags = new ArrayList<TagRest>();

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}
	
	public List<TagRest> getTags() {
		return tags;
	}

	public void setTags(List<TagRest> tags) {
		this.tags = tags;
	}
	
	public abstract FieldKind getKind();

}
