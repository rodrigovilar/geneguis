package br.edu.ufcg.embedded.ise.geneguis;

public class TagRule {

	private Long id;
	private String name;
	private TagType type;
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

	public TagType getType() {
		return type;
	}

	public void setType(TagType type) {
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

	@Override
	public String toString() {
		return "TagRule [id=" + id + ", name=" + name + ", type=" + type + ", value=" + value + ", entityLocator="
				+ entityLocator + ", fieldLocator=" + fieldLocator + "]";
	}

}
