package br.edu.ufcg.embedded.ise.geneguis.backend.examples;

import org.springframework.data.jpa.repository.JpaRepository;

public interface CustomerRepository extends JpaRepository<Customer, Long> {

}