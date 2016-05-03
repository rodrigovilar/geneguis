package br.edu.ufcg.embedded.ise.geneguis.backend;

import static br.edu.ufcg.embedded.ise.geneguis.Cardinality.Many;
import static br.edu.ufcg.embedded.ise.geneguis.Cardinality.One;
import static br.edu.ufcg.embedded.ise.geneguis.backend.TestHelper.delete;
import static br.edu.ufcg.embedded.ise.geneguis.backend.TestHelper.deploy;
import static br.edu.ufcg.embedded.ise.geneguis.backend.TestHelper.entityType;
import static br.edu.ufcg.embedded.ise.geneguis.backend.TestHelper.get;
import static br.edu.ufcg.embedded.ise.geneguis.backend.TestHelper.getObjectFromResult;
import static br.edu.ufcg.embedded.ise.geneguis.backend.TestHelper.instance;
import static br.edu.ufcg.embedded.ise.geneguis.backend.TestHelper.objectToMap;
import static br.edu.ufcg.embedded.ise.geneguis.backend.TestHelper.post;
import static br.edu.ufcg.embedded.ise.geneguis.backend.TestHelper.propertyType;
import static br.edu.ufcg.embedded.ise.geneguis.backend.TestHelper.put;
import static br.edu.ufcg.embedded.ise.geneguis.backend.TestHelper.relationshipType;
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
import org.springframework.test.web.servlet.MvcResult;

import br.edu.ufcg.embedded.ise.geneguis.Container;
import br.edu.ufcg.embedded.ise.geneguis.PropertyTypeType;
import br.edu.ufcg.embedded.ise.geneguis.backend.controller.MetadataController;
import br.edu.ufcg.embedded.ise.geneguis.backend.controller.OperationalController;
import br.edu.ufcg.embedded.ise.geneguis.backend.examples.Client;
import br.edu.ufcg.embedded.ise.geneguis.backend.examples.ClientRepository;
import br.edu.ufcg.embedded.ise.geneguis.backend.examples.Customer;
import br.edu.ufcg.embedded.ise.geneguis.backend.examples.CustomerDetails;
import br.edu.ufcg.embedded.ise.geneguis.backend.examples.CustomerDetailsRepository;
import br.edu.ufcg.embedded.ise.geneguis.backend.examples.CustomerRepository;
import br.edu.ufcg.embedded.ise.geneguis.backend.examples.Dependent;
import br.edu.ufcg.embedded.ise.geneguis.backend.examples.DependentRepository;
import br.edu.ufcg.embedded.ise.geneguis.jpadomain.JpaDomainModel;

@RunWith(SpringJUnit4ClassRunner.class)
@SpringApplicationConfiguration(classes = EntryPoint.class)
@FixMethodOrder(MethodSorters.NAME_ASCENDING)
public class DeployEntityTypeRestTest {

	MockMvc mockMvc;

	@InjectMocks
	MetadataController metadataController;

	@InjectMocks
	OperationalController operationalController;

	@Autowired
	ApplicationContext applicationContext;

	@Before
	public void setUp() {
		MockitoAnnotations.initMocks(this);
		this.mockMvc = standaloneSetup(metadataController, operationalController).build();

		EntryPoint.setDomainModel(new JpaDomainModel());
		EntryPoint.setContainer(new Container(EntryPoint.getDomainModel()));
	}


	@DirtiesContext
	@Test
	public void testOneEntityTypeGetEntities() throws Exception {
		deploy(applicationContext, Customer.class, CustomerRepository.class);

		get(mockMvc, "/entities").andExpect(status().isOk()).andExpect(jsonPath("$", hasSize(1)))
				.andExpect(entityType(0, "Customer"));

		get(mockMvc, "/api/" + "Customer").andExpect(status().isOk()).andExpect(jsonPath("$", hasSize(0)));
	}

	@DirtiesContext
	@Test
	public void testCompositionRelationship() throws Exception {
		deploy(applicationContext, Client.class, ClientRepository.class);
		deploy(applicationContext, Dependent.class, DependentRepository.class);

		get(mockMvc, "/entities").andExpect(status().isOk()).andExpect(jsonPath("$", hasSize(2)))
				.andExpect(entityType(0, "Client")).andExpect(entityType(1, "Dependent"));

		get(mockMvc, "/entities/Client").andExpect(status().isOk()).andExpect(entityType("Client"))
				.andExpect(propertyType(0, "id", PropertyTypeType.integer))
				.andExpect(propertyType(1, "name", PropertyTypeType.string))
				.andExpect(relationshipType(2, "dependents", "Dependent", One, Many));

		get(mockMvc, "/entities/Dependent").andExpect(status().isOk()).andExpect(entityType("Dependent"))
				.andExpect(propertyType(0, "id", PropertyTypeType.integer))
				.andExpect(propertyType(1, "name", PropertyTypeType.string))
				.andExpect(propertyType(2, "age", PropertyTypeType.integer))
				.andExpect(relationshipType(3, "client", "Client", Many, One));

		get(mockMvc, "/api/Client").andExpect(status().isOk()).andExpect(jsonPath("$", hasSize(0)));

		get(mockMvc, "/api/Dependent").andExpect(status().isOk()).andExpect(jsonPath("$", hasSize(0)));
	}

	@DirtiesContext
	@Test
	public void testOneEntityTypeWithPropertiesGetEntities() throws Exception {
		deploy(applicationContext, CustomerDetails.class, CustomerDetailsRepository.class);

		get(mockMvc, "/entities").andExpect(status().isOk()).andExpect(jsonPath("$", hasSize(1)))
				.andExpect(entityType(0, "CustomerDetails"));

		get(mockMvc, "/api/" + "CustomerDetails").andExpect(status().isOk()).andExpect(jsonPath("$", hasSize(0)));
	}

	@DirtiesContext
	@Test
	public void testOneEntityTypeGetEntity() throws Exception {
		deploy(applicationContext, CustomerDetails.class, CustomerDetailsRepository.class);

		get(mockMvc, "/entities/CustomerDetails").andExpect(status().isOk()).andExpect(entityType("CustomerDetails"))
				.andExpect(propertyType(0, "id", PropertyTypeType.integer))
				.andExpect(propertyType(1, "ssn", PropertyTypeType.string))
				.andExpect(propertyType(2, "name", PropertyTypeType.string))
				.andExpect(propertyType(3, "credit", PropertyTypeType.real));

		get(mockMvc, "/api/" + "CustomerDetails").andExpect(status().isOk()).andExpect(jsonPath("$", hasSize(0)));
	}

	@DirtiesContext
	@Test
	public void testOperationalCRUD() throws Exception {
		deploy(applicationContext, CustomerDetails.class, CustomerDetailsRepository.class);

		String name = "FooName";
		String ssn = "ssn 1";
		CustomerDetails customer = createCustomerDetails(name, ssn, 0.0);

		Map<String, Object> instanceMap = objectToMap(customer);
		instanceMap.remove("id");

		MvcResult result = post(mockMvc, "/api/" + "CustomerDetails", customer).andExpect(status().isCreated())
				.andExpect(instance(instanceMap)).andReturn();
		customer = getObjectFromResult(result, CustomerDetails.class);

		name = "OtherFooName";
		customer.setName(name);

		instanceMap = objectToMap(customer);
		instanceMap.put("id", customer.getId().intValue());

		result = put(mockMvc, "/api/" + "CustomerDetails/" + customer.getId(), customer).andExpect(status().isCreated())
				.andExpect(instance(instanceMap)).andReturn();
		customer = getObjectFromResult(result, CustomerDetails.class);

		get(mockMvc, "/api/" + "CustomerDetails/" + customer.getId()).andExpect(status().isOk())
				.andExpect(instance(instanceMap));

		delete(mockMvc, "/api/" + "CustomerDetails/" + customer.getId()).andExpect(status().isOk());

		get(mockMvc, "/api/" + "CustomerDetails").andExpect(status().isOk()).andExpect(jsonPath("$", hasSize(0)));

		get(mockMvc, "/api/" + "CustomerDetails/" + customer.getId()).andExpect(status().isNotFound());

	}
	
	@DirtiesContext
	@Test
	public void testRelationshipCRUD() throws Exception {
		deploy(applicationContext, Client.class, ClientRepository.class);
		deploy(applicationContext, Dependent.class, DependentRepository.class);

		Client client = new Client("client1 ");

		Map<String, Object> clientMap = objectToMap(client);
		clientMap.remove("id");

		MvcResult result = post(mockMvc, "/api/Client", client).andExpect(status().isCreated())
				.andExpect(instance(clientMap)).andReturn();
		client = getObjectFromResult(result, Client.class);

		Dependent dependent = client.addDependent("dep 1", 10);

		Map<String, Object> depMap = objectToMap(dependent);
		depMap.remove("id");
		depMap.remove("client");

		result = post(mockMvc, "/api/Dependent", dependent).andExpect(status().isCreated())
				.andExpect(instance(depMap)).andReturn();

		get(mockMvc, "/api/Dependent");
	}

	private CustomerDetails createCustomerDetails(String name, String ssn, Double credit) {
		CustomerDetails customer = new CustomerDetails();
		customer.setName(name);
		customer.setSsn(ssn);
		customer.setCredit(credit);
		return customer;
	}
	
}
