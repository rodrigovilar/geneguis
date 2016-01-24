package br.edu.ufcg.embedded.ise.geneguis.backend.controller;

import static br.edu.ufcg.embedded.ise.geneguis.backend.EntryPoint.*;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.google.gson.Gson;

@Controller
@RequestMapping(value = "/api/{entityType}")
public class OperationalController {

	@SuppressWarnings("unchecked")
	@RequestMapping(method = RequestMethod.GET)
	@ResponseBody
	public <T> ResponseEntity<T[]> getAll(@PathVariable String entityType) {
		T[] instances = (T[]) getContainer().getEntities(entityType).toArray();
		return new ResponseEntity<T[]>(instances, HttpStatus.OK);
	}
	
	@SuppressWarnings("unchecked")
	@RequestMapping(method = RequestMethod.POST)
	@ResponseBody
	public <T> ResponseEntity<T> create(@PathVariable String entityType, @RequestBody String input) {
		T entity = (T) new Gson().fromJson(input, getDomainModel().getClass(entityType));
		entity = getContainer().saveEntity(entityType, entity);
        return new ResponseEntity<T>(entity, HttpStatus.CREATED);
	}
	
	@RequestMapping(value = "{instanceId}", method = RequestMethod.GET)
	@ResponseBody
	public <T> ResponseEntity<T> get(@PathVariable String entityType, @PathVariable Long instanceId) {
		T instance = getContainer().getEntity(entityType, instanceId);
		if(instance == null)
			return new ResponseEntity<T>(HttpStatus.NOT_FOUND);
		return new ResponseEntity<T>(instance, HttpStatus.OK);
	}
	
	@SuppressWarnings("unchecked")
	@RequestMapping(value = "{instanceId}", method = RequestMethod.PUT)
	@ResponseBody
	public <T> ResponseEntity<T> update(@PathVariable String entityType, @PathVariable Long instanceId, @RequestBody String input) {
		T instance = getContainer().getEntity(entityType, instanceId);
		
		if(instance == null) {
			return new ResponseEntity<T>(HttpStatus.NOT_FOUND);
		}
		
		T newInstance = (T) new Gson().fromJson(input, getDomainModel().getClass(entityType));
		instance = getContainer().saveEntity(instanceId, entityType, newInstance);
        return new ResponseEntity<T>(instance, HttpStatus.CREATED);
	}

	@RequestMapping(value = "{instanceId}", method = RequestMethod.DELETE)
	@ResponseBody
	public ResponseEntity<?> delete(@PathVariable String entityType, @PathVariable Long instanceId) {
		if(getContainer().deleteEntity(entityType, instanceId))
			return new ResponseEntity<Object>(HttpStatus.OK);
        return new ResponseEntity<Object>(HttpStatus.NOT_FOUND);
	}	
}
