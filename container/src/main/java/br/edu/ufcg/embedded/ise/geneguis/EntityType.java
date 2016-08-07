package br.edu.ufcg.embedded.ise.geneguis;

import java.util.ArrayList;
import java.util.List;

public class EntityType {

	private String name;
	private List<FieldType> fieldTypes = new ArrayList<FieldType>();
	private List<Tag> tags = new ArrayList<Tag>();

	
	public EntityType() {}

	public EntityType(String name) {
		this.name = name;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}
	

	public List<FieldType> getFieldTypes() {
		return fieldTypes;
	}

	public void setFieldTypes(List<FieldType> fieldTypes) {
		this.fieldTypes = fieldTypes;
	}

	public List<Tag> getTags() {
		return tags;
	}

	public void setTags(List<Tag> tags) {
		this.tags = tags;
	}
	
	EntityType tag(String name, String value) {
		Tag tag = new Tag();
		tag.setName(name);
		tag.setValue(value);
		tags.add(tag);
		return this;
	}
	
	public EntityType propertyType(String name, PropertyTypeType type) {
		PropertyType propertyType = new PropertyType();
		propertyType.setName(name);
		propertyType.setType(type);
		fieldTypes.add(propertyType);
		return this;
	}

}
