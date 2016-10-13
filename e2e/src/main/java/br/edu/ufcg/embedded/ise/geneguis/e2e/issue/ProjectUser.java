package br.edu.ufcg.embedded.ise.geneguis.e2e.issue;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;

@Entity
public class ProjectUser {

	@Id
	@GeneratedValue
	private Long id;

	private String email;
	private String name;

	public ProjectUser() {
	}

	public ProjectUser(String email, String name) {
		this.email = email;
		this.name = name;
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

}
