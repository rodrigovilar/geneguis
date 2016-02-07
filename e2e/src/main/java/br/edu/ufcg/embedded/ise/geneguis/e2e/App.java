package br.edu.ufcg.embedded.ise.geneguis.e2e;

import org.springframework.boot.autoconfigure.SpringBootApplication;

import br.edu.ufcg.embedded.ise.geneguis.backend.EntryPoint;
import br.edu.ufcg.embedded.ise.geneguis.jpadomain.JpaDomainModel;

@SpringBootApplication
public class App {
	public static void main(String[] args) {
		JpaDomainModel domainModel = new JpaDomainModel();
		EntryPoint.run(App.class, domainModel, args);
	}

}
