package br.edu.ufcg.embedded.ise.geneguis.jpadomain.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import br.edu.ufcg.embedded.ise.geneguis.jpadomain.Port;

public interface PortRepository extends JpaRepository<Port, String> {

	public Port findByName(String name);

}