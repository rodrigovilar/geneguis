package br.edu.ufcg.embedded.ise.geneguis.backend.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import br.edu.ufcg.embedded.ise.geneguis.backend.Rule;

public interface RuleRepository extends JpaRepository<Rule, Long>{

	public List<Rule> findByVersionGreaterThan(Long version);
	
}
