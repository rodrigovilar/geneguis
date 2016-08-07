package br.edu.ufcg.embedded.ise.geneguis.jpadomain;

import static br.edu.ufcg.embedded.ise.geneguis.test.Helper.*;

import java.util.Iterator;

import org.junit.Test;

import br.edu.ufcg.embedded.ise.geneguis.Container;
import br.edu.ufcg.embedded.ise.geneguis.ContainerException;
import br.edu.ufcg.embedded.ise.geneguis.EntityType;
import br.edu.ufcg.embedded.ise.geneguis.RenderingService;
import br.edu.ufcg.embedded.ise.geneguis.jpadomain.examples.Item;
import br.edu.ufcg.embedded.ise.geneguis.jpadomain.examples.Product;
import br.edu.ufcg.embedded.ise.geneguis.jpadomain.examples.Supplier;
import br.edu.ufcg.embedded.ise.geneguis.test.Helper;
import br.edu.ufcg.embedded.ise.geneguis.test.PojoRenderingService;

public class JpaDomainModelTest {

	@Test
	public void withoutEntitites() {
		JpaDomainModel domainModel = new JpaDomainModel();
		RenderingService renderingService = new PojoRenderingService();
		Container container = new Container(domainModel, renderingService);
		
		Iterator<EntityType> entityTypes = container.getEntityTypes().iterator();
		Helper.assertNoMoreEntityTypes(entityTypes);
	}

	@Test(expected=ContainerException.class)
	public void entityTypeWithoutJpaEntity() {
		JpaDomainModel domainModel = new JpaDomainModel();
		domainModel.deployEntityType(Supplier.class, new RepositoryAdapter<Supplier,Long>());
	}

	@Test
	public void withOneEntityType() {
		JpaDomainModel domainModel = new JpaDomainModel();
		domainModel.deployEntityType(Item.class, new RepositoryAdapter<Item,Long>());
		RenderingService renderingService = new PojoRenderingService();
		Container container = new Container(domainModel, renderingService);

		Iterator<EntityType> entityTypes = container.getEntityTypes().iterator();
		Helper.assertNextEntityType(entityTypes, entityType("Item"));
		Helper.assertNoMoreEntityTypes(entityTypes);
	}

	@Test
	public void withTwoEntityTypes() {
		JpaDomainModel domainModel = new JpaDomainModel();
		domainModel.deployEntityType(Item.class, new RepositoryAdapter<Item,Long>());
		domainModel.deployEntityType(Product.class, new RepositoryAdapter<Product,Long>());
		RenderingService renderingService = new PojoRenderingService();
		Container container = new Container(domainModel, renderingService);

		Iterator<EntityType> entityTypes = container.getEntityTypes().iterator();
		Helper.assertNextEntityType(entityTypes, entityType("Item"));
		Helper.assertNextEntityType(entityTypes, entityType("Product"));
		Helper.assertNoMoreEntityTypes(entityTypes);
	}

}

