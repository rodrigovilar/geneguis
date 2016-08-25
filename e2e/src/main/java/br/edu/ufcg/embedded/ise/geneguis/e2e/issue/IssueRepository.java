package br.edu.ufcg.embedded.ise.geneguis.e2e.issue;

import org.springframework.data.jpa.repository.JpaRepository;

public interface IssueRepository extends JpaRepository<Issue, Long> {

}