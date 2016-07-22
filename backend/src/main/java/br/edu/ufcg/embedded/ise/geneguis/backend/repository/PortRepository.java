package br.edu.ufcg.embedded.ise.geneguis.backend.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import br.edu.ufcg.embedded.ise.geneguis.backend.Port;

public interface PortRepository extends JpaRepository<Port, String> {

	public Port findByName(String name);

}