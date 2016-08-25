package br.edu.ufcg.embedded.ise.geneguis.e2e.issue;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.ManyToOne;

import com.fasterxml.jackson.annotation.JsonBackReference;

import br.edu.ufcg.embedded.ise.geneguis.jpadomain.Relationship;

@Entity
public class Version {

	@Id
	@GeneratedValue
	private Long id;

	private String name;
	
	@ManyToOne(cascade=CascadeType.ALL)
	@JsonBackReference
	@Relationship(reverse="versions")
	private Project project;


	public Version() {
	}

	public Version(String name, Project project) {
		this.name = name;
		this.project = project;
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

	public Project getProject() {
		return project;
	}

	public void setProject(Project project) {
		this.project = project;
	}

}
