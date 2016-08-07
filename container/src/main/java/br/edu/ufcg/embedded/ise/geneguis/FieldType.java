package br.edu.ufcg.embedded.ise.geneguis;

import java.util.ArrayList;
import java.util.List;

public abstract class FieldType {

	private String name;
	private List<Tag> tags = new ArrayList<Tag>();

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public List<Tag> getTags() {
		return tags;
	}

	public void setTags(List<Tag> tags) {
		this.tags = tags;
	}

	FieldType tag(String name, String value) {
		Tag tag = new Tag();
		tag.setName(name);
		tag.setValue(value);
		tags.add(tag);
		return this;
	}

	public abstract FieldKind getKind();
}
