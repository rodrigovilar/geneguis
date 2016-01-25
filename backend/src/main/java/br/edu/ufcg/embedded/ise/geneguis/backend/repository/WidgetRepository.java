package br.edu.ufcg.embedded.ise.geneguis.backend.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import br.edu.ufcg.embedded.ise.geneguis.backend.Widget;

public interface WidgetRepository extends JpaRepository<Widget, Long>{

	public List<Widget> findByNameAndVersion(String name, Long version);
	public List<Widget> findByNameOrderByVersionDesc(String name);

}