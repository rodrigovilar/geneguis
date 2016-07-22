package br.edu.ufcg.embedded.ise.geneguis.jpadomain.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import br.edu.ufcg.embedded.ise.geneguis.jpadomain.Rule;


public interface RuleRepository extends JpaRepository<Rule, Long>{

	public List<Rule> findByVersionGreaterThan(Long version);
	
}
