package br.edu.ufcg.embedded.ise.geneguis.backend.examples;

import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepository extends JpaRepository<User, Long> {

}