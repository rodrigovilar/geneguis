package br.edu.ufcg.embedded.ise.geneguis.e2e.issue;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.OneToMany;

import com.fasterxml.jackson.annotation.JsonManagedReference;

import br.edu.ufcg.embedded.ise.geneguis.jpadomain.Relationship;

@Entity
public class Project {

	@Id
	@GeneratedValue
	private Long id;

	private String name;

	public Project() {
	}

	public Project(String name) {
		this.name = name;
	}

	@OneToMany(mappedBy = "project", cascade = CascadeType.ALL)
	@JsonManagedReference
	@Relationship(reverse = "project")
	private transient List<Version> versions = new ArrayList<Version>();

	@OneToMany(mappedBy = "project", cascade = CascadeType.ALL)
	@JsonManagedReference
	@Relationship(reverse = "project")
	private transient List<Component> components = new ArrayList<Component>();

	@OneToMany(mappedBy = "project", cascade = CascadeType.ALL)
	@JsonManagedReference
	@Relationship(reverse = "project")
	private transient List<Issue> issues = new ArrayList<Issue>();

	public String getName() {
		return name;
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public void setName(String name) {
		this.name = name;
	}

	public List<Version> getVersions() {
		return versions;
	}

	public void setVersions(List<Version> versions) {
		this.versions = versions;
	}

	public Version addVersion(String name) {
		Version version = new Version(name, this);
		getVersions().add(version);
		return version;
	}

	public List<Component> getComponents() {
		return components;
	}

	public void setComponents(List<Component> components) {
		this.components = components;
	}

	public Component addComponent(String item, String name) {
		Component component = new Component(item, name, this);
		getComponents().add(component);
		return component;
	}

	public List<Issue> getIssues() {
		return issues;
	}

	public void setIssues(List<Issue> issues) {
		this.issues = issues;
	}

	public Issue addIssue(IssueType type, String summary) {
		Issue issue = new Issue(type, summary, this);
		getIssues().add(issue);
		return issue;
	}

	
}
