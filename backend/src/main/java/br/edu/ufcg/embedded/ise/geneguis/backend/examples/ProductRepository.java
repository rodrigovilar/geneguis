package br.edu.ufcg.embedded.ise.geneguis.backend.examples;

import org.springframework.data.jpa.repository.JpaRepository;

public interface ProductRepository extends JpaRepository<Product, Long> {

}
