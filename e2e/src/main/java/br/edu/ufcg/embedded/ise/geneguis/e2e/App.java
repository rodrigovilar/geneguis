package br.edu.ufcg.embedded.ise.geneguis.e2e;

import org.springframework.boot.autoconfigure.SpringBootApplication;

import br.edu.ufcg.embedded.ise.geneguis.backend.EntryPoint;
import br.edu.ufcg.embedded.ise.geneguis.jpadomain.JpaDomainModel;
import br.edu.ufcg.embedded.ise.geneguis.jpadomain.JpaRenderingService;

@SpringBootApplication
public class App {
	public static void main(String[] args) {
		JpaDomainModel domainModel = new JpaDomainModel();
		JpaRenderingService renderingService = new JpaRenderingService();
		EntryPoint.run(App.class, domainModel, renderingService, args);
	}

}
