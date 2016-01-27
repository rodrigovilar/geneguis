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
	
	public static <T> T getBean(Class<T> type) {
		return application.getBean(type);
	}

	public static void main(String[] args) {
		run(null, new JpaDomainModel(), args);
	}

	public static void run(Class<?> app, JpaDomainModel domainModel, String[] args) {
		container = new Container(domainModel);
		EntryPoint.domainModel = domainModel;
		
		if (app == null) {
			application = SpringApplication.run(EntryPoint.class);

		} else {
			Object[] apps = new Object[] { EntryPoint.class, app };			
			application = SpringApplication.run(apps, args);
		}
	}
}
