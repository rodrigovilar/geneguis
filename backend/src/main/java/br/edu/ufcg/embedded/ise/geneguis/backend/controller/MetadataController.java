package br.edu.ufcg.embedded.ise.geneguis.backend.controller;

import static br.edu.ufcg.embedded.ise.geneguis.backend.EntryPoint.getContainer;

import java.util.ArrayList;
import java.util.List;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import br.edu.ufcg.embedded.ise.geneguis.EntityType;

@Controller
public class MetadataController {

	@RequestMapping("/entities")
	@ResponseBody
	public List<EntityTypeRest> getEntities() {
		List<EntityTypeRest> entities = new ArrayList<EntityTypeRest>();
		Iterable<EntityType> domains = getContainer().getEntityTypes();
		
		for(EntityType domain : domains) {
			entities.add(Converter.toRest(domain, false));
		}
		
		return entities;
	}

	@RequestMapping("/entities/{name}")
	@ResponseBody
	public EntityTypeRest getEntity(@PathVariable String name) {
		return Converter.toRest(getContainer().getEntityType(name), true);
	}
}
