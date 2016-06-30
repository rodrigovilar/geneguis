package br.edu.ufcg.embedded.ise.geneguis;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;

public class EnumPropertyType extends PropertyType {

	private String source;
	private List<Field> enumValues;

	public EnumPropertyType() {
		super();
		this.enumValues = new ArrayList<Field>();
		this.setType(PropertyTypeType.enumeration);
	}

	public List<Field> getEnumValues() {
		return enumValues;
	}

	public void setEnumValue(List<Field> enumValues) {
		this.enumValues = enumValues;
	}

	public void setEnumValues(List<Field> enumValues) {
		this.enumValues = enumValues;
	}

	public String getSource() {
		return source;
	}

	public void setSource(String source) {
		this.source = source;
	}
	
}
