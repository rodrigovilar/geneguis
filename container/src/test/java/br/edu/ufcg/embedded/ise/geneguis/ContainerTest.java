package br.edu.ufcg.embedded.ise.geneguis;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.junit.Test;

import br.edu.ufcg.embedded.ise.geneguis.test.Helper;
import br.edu.ufcg.embedded.ise.geneguis.test.PojoDomainModel;

public class ContainerTest {

	@Test(expected = ContainerException.class)
	public void withoutDomainModel() {
		new Container(null, new DummyRenderingService());
	}

	@Test(expected = ContainerException.class)
	public void withoutRenderingService() {
		new Container(new DomainWithoutEntityTypes(), null);
	}

	@Test
	public void withoutEntityTypes() {
		Container container = new Container(new DomainWithoutEntityTypes(), new DummyRenderingService());

		Iterator<EntityType> entityTypes = container.getEntityTypes().iterator();
		Helper.assertNoMoreEntityTypes(entityTypes);
	}

	@Test
	public void withOneEntityType() {
		PojoDomainModel domainModel = new PojoDomainModel();
		Helper.addEntityType(domainModel, "Product");
		Container container = new Container(domainModel, new DummyRenderingService());

		Iterator<EntityType> entityTypes = container.getEntityTypes().iterator();
		Helper.assertNextEntityType(entityTypes, "Product");
		Helper.assertNoMoreEntityTypes(entityTypes);
	}

	@Test
	public void withTwoEntityType() {
		PojoDomainModel domainModel = new PojoDomainModel();
		Helper.addEntityType(domainModel, "Product");
		Helper.addEntityType(domainModel, "Customer");
		Container container = new Container(domainModel, new DummyRenderingService());

		Iterator<EntityType> entityTypes = container.getEntityTypes().iterator();
		Helper.assertNextEntityType(entityTypes, "Product");
		Helper.assertNextEntityType(entityTypes, "Customer");
		Helper.assertNoMoreEntityTypes(entityTypes);
	}

}

class DomainWithoutEntityTypes implements DomainModel {
	public Iterable<EntityType> getEntityTypes() {
		return new ArrayList<EntityType>();
	}

	public List<Entity> getEntities(String resource) {
		return null;
	}

	public Entity saveEntity(String name, Entity instance) {
		return null;
	}

	public Entity getEntity(String name, Long entityId) {
		return null;
	}

	public Entity saveEntity(Long entityId, String name, Entity newEntity) {
		return null;
	}

	public boolean deleteEntity(String name, Long entityId) {
		return false;
	}

	public void clear() {
	}
}

class DummyRenderingService implements RenderingService {

	public Widget saveWidget(Widget widget) {
		return null;
	}

	public List<Widget> getAll() {
		return null;
	}

	public Widget getWidget(String widgetName) {
		return null;
	}

	public List<Rule> getAllRulesByVersionGreaterThan(Long version) {
		return null;
	}

	public List<Rule> getAllRules() {
		return null;
	}

	public Rule saveRule(Rule rule) {
		return null;
	}
	
}