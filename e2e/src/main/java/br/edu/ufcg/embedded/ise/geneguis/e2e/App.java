package br.edu.ufcg.embedded.ise.geneguis.e2e;

import org.springframework.boot.autoconfigure.SpringBootApplication;

import br.edu.ufcg.embedded.ise.geneguis.backend.EntryPoint;
import br.edu.ufcg.embedded.ise.geneguis.backend.examples.Customer;
import br.edu.ufcg.embedded.ise.geneguis.backend.examples.CustomerDetails;
import br.edu.ufcg.embedded.ise.geneguis.backend.examples.CustomerDetailsRepository;
import br.edu.ufcg.embedded.ise.geneguis.backend.examples.CustomerRepository;
import br.edu.ufcg.embedded.ise.geneguis.jpadomain.JpaDomainModel;

@SpringBootApplication
public class App {
	public static void main(String[] args) {
		JpaDomainModel domainModel = new JpaDomainModel();
		EntryPoint.run(App.class, domainModel, args);
		deployDomain(domainModel);
	}

	private static void deployDomain(JpaDomainModel domainModel) {
		CustomerRepository customerRepository = EntryPoint.getBean(CustomerRepository.class);
		EntryPoint.getDomainModel().deployEntityType(Customer.class, customerRepository);
		CustomerDetailsRepository customerDetailsRepository = EntryPoint.getBean(CustomerDetailsRepository.class);
		EntryPoint.getDomainModel().deployEntityType(CustomerDetails.class, customerDetailsRepository);
	}
}
