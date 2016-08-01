package br.edu.ufcg.embedded.ise.geneguis.test;

import java.util.List;

import org.junit.Assert;

import br.edu.ufcg.embedded.ise.geneguis.EntityType;
import br.edu.ufcg.embedded.ise.geneguis.Tag;

public class ExpectedEntityType {

	private String name;
	private ExpectedFieldType[] expectedFields;
	private String[] expectedTags;

	public ExpectedEntityType(String name, ExpectedFieldType[] expectedFields, String... expectedTags) {
		this.name = name;
		this.expectedFields = expectedFields;
		this.expectedTags = expectedTags;
	}

	public String getName() {
		return name;
	}

	public ExpectedFieldType[] getExpectedFields() {
		return expectedFields;
	}

	public String[] getExpectedTags() {
		return expectedTags;
	}

	public void validate(EntityType result) {
		Assert.assertEquals("Different type names", getName(), result.getName());
		
		List<Tag> tags = result.getTags();
		
		String[] expectedTags = getExpectedTags();
		
		for (int i = 0; i < expectedTags.length; i++) {
			String expectedTag = expectedTags[i];
			Tag tag = tags.get(i);
			String tagString = tag.getName() +
					((tag.getValue() == null) ? "" : ";" + tag.getValue());
			Assert.assertEquals("Different entity tags", expectedTag, tagString);
		}
		
		ExpectedFieldType[] expectedFields = getExpectedFields();
		for (int i = 0; i < expectedFields.length; i++) {
			ExpectedFieldType expectedFieldType = expectedFields[i];
			expectedFieldType.validate(result.getFieldTypes().get(i));
		}

	}

}
