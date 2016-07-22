package br.edu.ufcg.embedded.ise.geneguis.backend;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ApplicationContext;

import br.edu.ufcg.embedded.ise.geneguis.Container;
import br.edu.ufcg.embedded.ise.geneguis.jpadomain.JpaDomainModel;
import br.edu.ufcg.embedded.ise.geneguis.jpadomain.JpaRenderingService;

@SpringBootApplication
public class EntryPoint {

	private static ApplicationContext application;
	private static Container container;
	private static JpaDomainModel domainModel;
	private static JpaRenderingService renderingService;

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

	public static void setRenderingService(JpaRenderingService renderingService) {
		EntryPoint.renderingService = renderingService;
	}

	public static JpaRenderingService getRenderingService() {
		return renderingService;
	}

	public static <T> T getBean(Class<T> type) {
		return application.getBean(type);
	}

	public static void main(String[] args) {
		JpaRenderingService renderingService = new JpaRenderingService();
		run(null, new JpaDomainModel(), renderingService, args);
	}

	public static void run(Class<?> app, JpaDomainModel domainModel, JpaRenderingService renderingService,
			String[] args) {
		container = new Container(domainModel, renderingService);
		EntryPoint.domainModel = domainModel;
		EntryPoint.renderingService = renderingService;

		if (app == null) {
			application = SpringApplication.run(EntryPoint.class);

		} else {
			Object[] apps = new Object[] { EntryPoint.class, app };
			application = SpringApplication.run(apps, args);
		}
	}
}
