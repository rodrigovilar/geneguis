package br.edu.ufcg.embedded.ise.geneguis.backend.controller;

import static br.edu.ufcg.embedded.ise.geneguis.backend.EntryPoint.getContainer;
import static br.edu.ufcg.embedded.ise.geneguis.backend.EntryPoint.getDomainModel;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import br.edu.ufcg.embedded.ise.geneguis.EntityType;

@Controller
@RequestMapping(value = "/api/{entityType}")
public class OperationalController {

	@RequestMapping(method = RequestMethod.GET)
	@ResponseBody
	public ResponseEntity<String> getAll(@PathVariable String entityType) {
		Object[] instances = (Object[]) getContainer().getEntities(entityType).toArray();
		EntityType entityTypeObj = getContainer().getEntityType(entityType);
		return new ResponseEntity<String>(JsonMetadata.renderInstances(instances, entityTypeObj).toString(), HttpStatus.OK);
	}

	@RequestMapping(method = RequestMethod.POST)
	@ResponseBody
	public ResponseEntity<String> create(@PathVariable String entityType, @RequestBody String input) throws Exception {
		EntityType entityTypeObj = getContainer().getEntityType(entityType);

		Object newInstance = getDomainModel().getClass(entityTypeObj.getName()).newInstance();
		JsonMetadata.parseInstance(entityTypeObj, input, newInstance);

		newInstance = getContainer().saveEntity(entityType, newInstance);
		return new ResponseEntity<String>(JsonMetadata.renderInstance(newInstance, entityTypeObj).toString(), HttpStatus.CREATED);
	}

	@RequestMapping(value = "{instanceId}", method = RequestMethod.GET)
	@ResponseBody
	public ResponseEntity<String> get(@PathVariable String entityType, @PathVariable Long instanceId) {
		Object instance = getContainer().getEntity(entityType, instanceId);

		if (instance == null) {
			return new ResponseEntity<String>(HttpStatus.NOT_FOUND);
		}

		EntityType entityTypeObj = getContainer().getEntityType(entityType);
		return new ResponseEntity<String>(JsonMetadata.renderInstance(instance, entityTypeObj).toString(), HttpStatus.OK);
	}

	@RequestMapping(value = "{instanceId}", method = RequestMethod.PUT)
	@ResponseBody
	public ResponseEntity<String> update(@PathVariable String entityType, @PathVariable Long instanceId,
			@RequestBody String input) throws Exception {
		Object instance = getContainer().getEntity(entityType, instanceId);

		if (instance == null) {
			return new ResponseEntity<String>(HttpStatus.NOT_FOUND);
		}
		
		EntityType entityTypeObj = getContainer().getEntityType(entityType);
		Object newInstance = getDomainModel().getClass(entityTypeObj.getName()).newInstance();
		JsonMetadata.parseInstance(entityTypeObj, input, newInstance);

		instance = getContainer().saveEntity(instanceId, entityType, newInstance);
		return new ResponseEntity<String>(JsonMetadata.renderInstance(instance, entityTypeObj).toString(), HttpStatus.CREATED);
	}

	@RequestMapping(value = "{instanceId}", method = RequestMethod.DELETE)
	@ResponseBody
	public ResponseEntity<?> delete(@PathVariable String entityType, @PathVariable Long instanceId) {
		if (getContainer().deleteEntity(entityType, instanceId)) {
			return new ResponseEntity<Object>(HttpStatus.OK);
		}

		return new ResponseEntity<Object>(HttpStatus.NOT_FOUND);
	}
}
