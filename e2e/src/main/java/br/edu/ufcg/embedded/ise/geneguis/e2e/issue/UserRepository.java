package br.edu.ufcg.embedded.ise.geneguis.e2e.issue;

import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepository extends JpaRepository<User, Long> {

}