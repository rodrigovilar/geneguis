package br.edu.ufcg.embedded.ise.geneguis.backend.controller;

import static br.edu.ufcg.embedded.ise.geneguis.backend.EntryPoint.getContainer;
import static br.edu.ufcg.embedded.ise.geneguis.backend.EntryPoint.getDomainModel;

import java.util.ArrayList;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.google.gson.Gson;

import br.edu.ufcg.embedded.ise.geneguis.EntityType;
import br.edu.ufcg.embedded.ise.geneguis.backend.EntryPoint;

@Controller
@RequestMapping(value = "/entities")
public class MetadataController {

	@RequestMapping(method = RequestMethod.GET)
	@ResponseBody
	public List<EntityTypeRest> getEntities() {
		List<EntityTypeRest> entities = new ArrayList<EntityTypeRest>();
		Iterable<EntityType> domains = getContainer().getEntityTypes();
		
		for(EntityType domain : domains) {
			entities.add(Converter.toRest(domain, false));
		}
		
		return entities;
	}

	@RequestMapping(value = "{name}", method = RequestMethod.GET)
	@ResponseBody
	public EntityTypeRest getEntity(@PathVariable String name) {
		return Converter.toRest(getContainer().getEntityType(name), true);
	}
	
	@SuppressWarnings("unchecked")
	@RequestMapping(method = RequestMethod.POST)
	@ResponseBody
	public <T> ResponseEntity<EntityTypeDeployRest> create(@RequestBody String input) {
		EntityTypeDeployRest deploy = (EntityTypeDeployRest) new Gson().fromJson(input, EntityTypeDeployRest.class);
		
		Class<T> entityClass;
		try {
			entityClass = (Class<T>) Class.forName(deploy.getEntity());
			Class<?> repositoryClass = Class.forName(deploy.getRepository());
			JpaRepository<T, ?> repository = (JpaRepository<T, ?>) EntryPoint.getBean(repositoryClass);
			
			getDomainModel().deployEntityType(entityClass, repository);
			
		} catch (ClassNotFoundException e) {
	        return new ResponseEntity<EntityTypeDeployRest>(HttpStatus.NOT_FOUND);
		}
		
        return new ResponseEntity<EntityTypeDeployRest>(deploy, HttpStatus.CREATED);
	}

	
}
