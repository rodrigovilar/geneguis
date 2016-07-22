package br.edu.ufcg.embedded.ise.geneguis.jpadomain.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import br.edu.ufcg.embedded.ise.geneguis.jpadomain.Port;
import br.edu.ufcg.embedded.ise.geneguis.jpadomain.repository.PortRepository;

@Service
public class PortService {
	
	
	@Autowired
	private PortRepository repository;
	
	public Port getPortByName(String name) {
		return repository.findByName(name);
	}
	
	public Port createPort(Port port) {
		return repository.saveAndFlush(port);
	}
	
	public void clear() {
		repository.deleteAll();
	}
	
}