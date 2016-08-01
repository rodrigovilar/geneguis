package br.edu.ufcg.embedded.ise.geneguis.test;

import java.util.List;

import org.junit.Assert;

import br.edu.ufcg.embedded.ise.geneguis.FieldType;
import br.edu.ufcg.embedded.ise.geneguis.Tag;

public class ExpectedFieldType {

	private String name;
	private String[] expectedTags;

	public ExpectedFieldType(String name, String... expectedTags) {
		this.name = name;
		this.expectedTags = expectedTags;
	}

	public String getName() {
		return name;
	}

	public String[] getExpectedTags() {
		return expectedTags;
	}

	public void validate(FieldType result) {
		Assert.assertEquals("Different field type names", getName(), result.getName());

		List<Tag> tags = result.getTags();

		String[] expectedTags = getExpectedTags();

		for (int i = 0; i < expectedTags.length; i++) {
			String expectedTag = expectedTags[i];
			Tag tag = tags.get(i);
			String tagString = tag.getName() + ((tag.getValue() == null) ? "" : ";" + tag.getValue());
			Assert.assertEquals("Different field tags", expectedTag, tagString);
		}

	}

}
