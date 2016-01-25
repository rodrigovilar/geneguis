package br.edu.ufcg.embedded.ise.geneguis.backend;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ApplicationContext;

import br.edu.ufcg.embedded.ise.geneguis.Container;
import br.edu.ufcg.embedded.ise.geneguis.jpadomain.JpaDomainModel;

@SpringBootApplication
public class EntryPoint {

	private static ApplicationContext application;
	private static Container container;
	private static JpaDomainModel domainModel;
	
	public static Container getContainer() {
		return container;
	}
	
	public static JpaDomainModel getDomainModel() {
		return domainModel;
	}

	public static void setApplication(ApplicationContext application) {
		EntryPoint.application = application;
	}
	
	public static void setContainer(Container container) {
		EntryPoint.container = container;
	}

	public static void setDomainModel(JpaDomainModel domainModel) {
		EntryPoint.domainModel = domainModel;
	}

	public static void main(String[] args) {
		domainModel = new JpaDomainModel();
		container = new Container(domainModel);
		application = SpringApplication.run(EntryPoint.class, args);
		
		if (args.length > 0 && "initDB".equals(args[0])) {
			Util.initDB(application);
		}
	}
}
