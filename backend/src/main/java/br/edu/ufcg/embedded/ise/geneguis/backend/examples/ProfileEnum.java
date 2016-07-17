package br.edu.ufcg.embedded.ise.geneguis.backend.examples;

public enum ProfileEnum {
	TECHNICAL_SUPPORT("Technical Support"), MANAGER("Manager"), SIMPLE_USER("Simple User");

	private String text;

	private ProfileEnum(String text) {
		this.text = text;
	}

	@Override
	public String toString() {
		return this.text;
	}
}
