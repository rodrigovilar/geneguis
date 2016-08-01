package br.edu.ufcg.embedded.ise.geneguis.test;

import org.junit.Assert;

import br.edu.ufcg.embedded.ise.geneguis.FieldType;
import br.edu.ufcg.embedded.ise.geneguis.PropertyType;
import br.edu.ufcg.embedded.ise.geneguis.PropertyTypeType;

public class ExpectedPropertyType extends ExpectedFieldType {

	private PropertyTypeType type;

	public ExpectedPropertyType(String name, PropertyTypeType type, String[] expectedTags) {
		super(name, expectedTags);
		this.type = type;
	}

	public PropertyTypeType getType() {
		return type;
	}

	@Override
	public void validate(FieldType result) {
		super.validate(result);
		
		PropertyType propertyType = (PropertyType) result;
		Assert.assertEquals("Different field types", getType(), propertyType.getType());

	}

}
