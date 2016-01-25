package br.edu.ufcg.embedded.ise.geneguis.test;

import java.util.Iterator;

import br.edu.ufcg.embedded.ise.geneguis.EntityType;
import junit.framework.Assert;

public class Helper {

	public static void addEntityType(PojoDomainModel domainModel, String typeName) {
		domainModel.getPojoEntityTypes().add(new EntityType(typeName));
	}

	public static void assertNoMoreEntityTypes(Iterator<EntityType> entityTypes) {
		Assert.assertFalse("Has more entity types", entityTypes.hasNext());
	}

	public static void assertNextEntityType(Iterator<EntityType> entityTypes, String typeName) {
		Assert.assertTrue("Has no more entity types", entityTypes.hasNext());
		Assert.assertEquals("Different type names", typeName, entityTypes.next().getName());
	}

}
