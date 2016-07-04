package br.edu.ufcg.embedded.ise.geneguis;

import java.util.ArrayList;
import java.util.List;

public class EnumType extends PropertyType {

	private String source;
	private List<String> enumValues;

	public EnumType() {
		super();
		this.enumValues = new ArrayList<String>();
		this.setType(PropertyTypeType.enumeration);
	}

	public List<String> getEnumValues() {
		return enumValues;
	}

	public void setEnumValue(List<String> enumValues) {
		this.enumValues = enumValues;
	}

	public String getSource() {
		return source;
	}

	public void setSource(String source) {
		this.source = source;
	}
	
}
