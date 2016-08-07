package br.edu.ufcg.embedded.ise.geneguis.backend;

import static br.edu.ufcg.embedded.ise.geneguis.backend.TestHelper.deploy;
import static br.edu.ufcg.embedded.ise.geneguis.backend.TestHelper.entityType;
import static br.edu.ufcg.embedded.ise.geneguis.backend.TestHelper.get;
import static br.edu.ufcg.embedded.ise.geneguis.backend.TestHelper.instance;
import static br.edu.ufcg.embedded.ise.geneguis.backend.TestHelper.objectToMap;
import static br.edu.ufcg.embedded.ise.geneguis.backend.TestHelper.post;
import static br.edu.ufcg.embedded.ise.geneguis.backend.TestHelper.propertyType;
import static org.hamcrest.Matchers.hasSize;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.setup.MockMvcBuilders.standaloneSetup;

import java.util.Map;

import org.junit.Before;
import org.junit.FixMethodOrder;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.MethodSorters;
import org.mockito.InjectMocks;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.SpringApplicationConfiguration;
import org.springframework.context.ApplicationContext;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;

import br.edu.ufcg.embedded.ise.geneguis.Container;
import br.edu.ufcg.embedded.ise.geneguis.PropertyTypeType;
import br.edu.ufcg.embedded.ise.geneguis.TagType;
import br.edu.ufcg.embedded.ise.geneguis.backend.controller.MetadataController;
import br.edu.ufcg.embedded.ise.geneguis.backend.controller.OperationalController;
import br.edu.ufcg.embedded.ise.geneguis.backend.controller.TagController;
import br.edu.ufcg.embedded.ise.geneguis.backend.controller.TagRuleRest;
import br.edu.ufcg.embedded.ise.geneguis.backend.examples.Customer;
import br.edu.ufcg.embedded.ise.geneguis.backend.examples.CustomerDetails;
import br.edu.ufcg.embedded.ise.geneguis.backend.examples.CustomerDetailsRepository;
import br.edu.ufcg.embedded.ise.geneguis.backend.examples.CustomerRepository;
import br.edu.ufcg.embedded.ise.geneguis.jpadomain.JpaDomainModel;

@RunWith(SpringJUnit4ClassRunner.class)
@SpringApplicationConfiguration(classes = EntryPoint.class)
@FixMethodOrder(MethodSorters.NAME_ASCENDING)
public class TagsTest {

	MockMvc mockMvc;

	@InjectMocks
	MetadataController metadataController;

	@InjectMocks
	OperationalController operationalController;

	@InjectMocks
	TagController tagController;

	@Autowired
	ApplicationContext applicationContext;

	@Autowired
	JpaRenderingService renderingService;

	@Before
	public void setUp() {
		MockitoAnnotations.initMocks(this);
		this.mockMvc = standaloneSetup(metadataController, operationalController, tagController).build();

		EntryPoint.setDomainModel(new JpaDomainModel());
		EntryPoint.setRenderingService(renderingService);
		EntryPoint.setContainer(new Container(EntryPoint.getDomainModel(), EntryPoint.getRenderingService()));
	}

	@DirtiesContext
	@Test
	public void testOneEntityTypeGetEntities() throws Exception {
		deploy(applicationContext, Customer.class, CustomerRepository.class);

		get(mockMvc, "/entities").andExpect(status().isOk()).andExpect(jsonPath("$", hasSize(1)))
				.andExpect(entityType(0, "Customer", "Primary key;id"));

		postTagRule(new TagRuleRest("tag1", TagType.EntityType.name(), "value1", "*", null));
		checkEntityTag(0, "Customer", "Primary key;id", "tag1;value1");

		postTagRule(new TagRuleRest("tag2", TagType.EntityType.name(), null, "Customer", null));
		checkEntityTag(0, "Customer", "Primary key;id", "tag1;value1", "tag2");

		postTagRule(new TagRuleRest("tag3", TagType.EntityType.name(), null, "Client", null));
		postTagRule(new TagRuleRest("tag4", TagType.FieldType.name(), null, "Product", "*"));
		checkEntityTag(0, "Customer", "Primary key;id", "tag1;value1", "tag2");

		postTagRule(new TagRuleRest("tag6", TagType.EntityType.name(), "value6", "Cust*", null));
		checkEntityTag(0, "Customer", "Primary key;id", "tag1;value1", "tag2", "tag6;value6");

	}

	private void postTagRule(TagRuleRest tagRule) throws Exception {
		Map<String, Object> instanceMap = objectToMap(tagRule);
		instanceMap.remove("id");

		post(mockMvc, "/tags", tagRule).andExpect(status().isCreated()).andExpect(instance(instanceMap)).andReturn();
	}

	private void checkEntityTag(int position, String entity, String... expectedTags) throws Exception {
		get(mockMvc, "/entities").andExpect(status().isOk()).andExpect(jsonPath("$", hasSize(1)))
				.andExpect(entityType(position, entity, expectedTags));
	}

	@DirtiesContext
	@Test
	public void testOneEntityTypeGetEntity() throws Exception {
		deploy(applicationContext, CustomerDetails.class, CustomerDetailsRepository.class);

		postTagRule(new TagRuleRest("tag1", TagType.FieldType.name(), "value1", "*", "*"));
		postTagRule(new TagRuleRest("tag2", TagType.FieldType.name(), null, "CustomerDetails", "*"));
		postTagRule(new TagRuleRest("tag3", TagType.FieldType.name(), null, "*", "id"));
		postTagRule(new TagRuleRest("tag4", TagType.FieldType.name(), null, "CustomerDetails", "id"));
		postTagRule(new TagRuleRest("tag5", TagType.FieldType.name(), null, "Product", "*"));
		postTagRule(new TagRuleRest("tag6", TagType.FieldType.name(), null, "*", "abc"));
		postTagRule(new TagRuleRest("tag7", TagType.FieldType.name(), null, "Product", "abc"));
		postTagRule(new TagRuleRest("tag8", TagType.EntityType.name(), null, "CustomerDetails", null));

		ResultActions result = get(mockMvc, "/entities/CustomerDetails").andExpect(status().isOk())
				.andExpect(entityType("CustomerDetails"));

		checkFieldTag(result, 0, "id", PropertyTypeType.integer, "tag1;value1", "tag2", "tag3", "tag4");
		checkFieldTag(result, 1, "ssn", PropertyTypeType.string, "tag1;value1", "tag2");
	}

	private void checkFieldTag(ResultActions result, int position, String field, PropertyTypeType ptt,
			String... expectedTags) throws Exception {
		result.andExpect(propertyType(position, field, ptt, expectedTags));
	}

//	@Ignore
//	@DirtiesContext
//	@Test
//	public void testCompositionRelationship() throws Exception {
//		deploy(applicationContext, Client.class, ClientRepository.class);
//		deploy(applicationContext, Dependent.class, DependentRepository.class);
//
//		get(mockMvc, "/entities").andExpect(status().isOk()).andExpect(jsonPath("$", hasSize(2)))
//				.andExpect(entityType(0, "Client")).andExpect(entityType(1, "Dependent"));
//
//		get(mockMvc, "/entities/Client").andExpect(status().isOk()).andExpect(entityType("Client"))
//				.andExpect(propertyType(0, "id", PropertyTypeType.integer))
//				.andExpect(propertyType(1, "name", PropertyTypeType.string))
//				.andExpect(relationshipType(2, "dependents", "Dependent", One, Many));
//
//		get(mockMvc, "/entities/Dependent").andExpect(status().isOk()).andExpect(entityType("Dependent"))
//				.andExpect(propertyType(0, "id", PropertyTypeType.integer))
//				.andExpect(propertyType(1, "name", PropertyTypeType.string))
//				.andExpect(propertyType(2, "age", PropertyTypeType.integer))
//				.andExpect(relationshipType(3, "client", "Client", Many, One));
//
//		get(mockMvc, "/api/Client").andExpect(status().isOk()).andExpect(jsonPath("$", hasSize(0)));
//
//		get(mockMvc, "/api/Dependent").andExpect(status().isOk()).andExpect(jsonPath("$", hasSize(0)));
//	}

}
