package br.edu.ufcg.embedded.ise.geneguis.test;

import java.util.Iterator;

import org.junit.Assert;

import br.edu.ufcg.embedded.ise.geneguis.Container;
import br.edu.ufcg.embedded.ise.geneguis.EntityType;
import br.edu.ufcg.embedded.ise.geneguis.PropertyTypeType;
import br.edu.ufcg.embedded.ise.geneguis.TagRule;
import br.edu.ufcg.embedded.ise.geneguis.TagType;

public class Helper {

	public static void addTagRuleForEntity(Container container, String entityLocator, String name, String value) {
		TagRule tagRule = new TagRule();
		tagRule.setEntityLocator(entityLocator);
		tagRule.setName(name);
		tagRule.setType(TagType.EntityType);
		tagRule.setValue(value);
		container.saveTagRule(tagRule);
	}

	public static void addTagRuleForField(Container container, String entityLocator, String fieldLocator, String name, String value) {
		TagRule tagRule = new TagRule();
		tagRule.setEntityLocator(entityLocator);
		tagRule.setFieldLocator(fieldLocator);
		tagRule.setName(name);
		tagRule.setType(TagType.FieldType);
		tagRule.setValue(value);
		container.saveTagRule(tagRule);
	}

	public static EntityType addEntityType(PojoDomainModel domainModel, String typeName) {
		EntityType entityType = new EntityType(typeName);
		domainModel.getPojoEntityTypes().add(entityType);
		return entityType;
	}

	public static void assertNoMoreEntityTypes(Iterator<EntityType> entityTypes) {
		Assert.assertFalse("Has more entity types", entityTypes.hasNext());
	}

	public static void assertNextEntityType(Iterator<EntityType> entityTypes, ExpectedEntityType expectedEntityType) {
		Assert.assertTrue("Has no more entity types", entityTypes.hasNext());
		EntityType next = entityTypes.next();
		expectedEntityType.validate(next);
		
	}
	
	public static ExpectedEntityType entityType(String name, ExpectedFieldType[] expectedFields, String... expectedTags) {
		return new ExpectedEntityType(name, expectedFields, expectedTags);
	}

	public static ExpectedEntityType entityType(String name, String... expectedTags) {
		return new ExpectedEntityType(name, new ExpectedFieldType[] {}, expectedTags);
	}
	
	public static ExpectedEntityType entityType(String name) {
		return new ExpectedEntityType(name, new ExpectedFieldType[] {}, new String[] {});
	}
	
	public static ExpectedPropertyType propertyType(String name, PropertyTypeType type, String... expectedTags) {
		return new ExpectedPropertyType(name, type, expectedTags);
	}

	public static <T> T[] ar(T... ts) {
		return ts;
	}

}
