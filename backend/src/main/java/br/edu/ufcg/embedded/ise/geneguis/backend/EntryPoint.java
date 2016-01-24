package br.edu.ufcg.embedded.ise.geneguis.backend;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ConfigurableApplicationContext;

import br.edu.ufcg.embedded.ise.geneguis.Container;
import br.edu.ufcg.embedded.ise.geneguis.jpadomain.JpaDomainModel;

@SpringBootApplication
public class EntryPoint {

	private static ConfigurableApplicationContext application;
	private static Container container;
	
	public static Container getContainer() {
		return container;
	}

	public static void main(String[] args) {
		JpaDomainModel domainModel = new JpaDomainModel();
		container = new Container(domainModel);
		application = SpringApplication.run(EntryPoint.class, args);
		
		if (args.length > 0 && "initDB".equals(args[0])) {
			Util.initDB(application);
		}
	}
}
