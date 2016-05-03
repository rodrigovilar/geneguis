package br.edu.ufcg.embedded.ise.geneguis;

public abstract class FieldType {

	private String name;

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public abstract FieldKind getKind();
}
