package br.edu.ufcg.embedded.ise.geneguis.backend.controller;

public class TagRuleRest {

	private Long id;
	private String name;
	private String type;
	private String value;
	private String entityLocator;
	private String fieldLocator;


	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public String getValue() {
		return value;
	}

	public void setValue(String value) {
		this.value = value;
	}

	public String getEntityLocator() {
		return entityLocator;
	}

	public void setEntityLocator(String entityLocator) {
		this.entityLocator = entityLocator;
	}

	public String getFieldLocator() {
		return fieldLocator;
	}

	public void setFieldLocator(String fieldLocator) {
		this.fieldLocator = fieldLocator;
	}

}
