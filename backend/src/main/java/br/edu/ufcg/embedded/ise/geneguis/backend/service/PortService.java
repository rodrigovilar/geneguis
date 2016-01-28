package br.edu.ufcg.embedded.ise.geneguis.backend.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import br.edu.ufcg.embedded.ise.geneguis.backend.Port;
import br.edu.ufcg.embedded.ise.geneguis.backend.repository.PortRepository;

@Service
public class PortService {
	
	
	@Autowired
	private PortRepository repository;
	
	public Port getPortByName(String name) {
		return repository.findByName(name);
	}
	
	public Port createPort(Port port) {
		Port oldPort = getPortByName(port.getName());

		if (oldPort != null) {
			port.setId(oldPort.getId());
		}
		
		return repository.saveAndFlush(port);
	}
	
}