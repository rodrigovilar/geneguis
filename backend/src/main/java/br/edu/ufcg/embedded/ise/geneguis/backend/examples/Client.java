package br.edu.ufcg.embedded.ise.geneguis.backend.examples;

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
public class Client {

	@Id
	@GeneratedValue
	private Long id;

	private String name;
	
	public Client() {}
	
	public Client(String name) {
		this.name = name;
	}

	@OneToMany(mappedBy = "client", cascade=CascadeType.ALL)
	@JsonManagedReference
	@Relationship(reverse="client")
	private transient List<Dependent> dependents = new ArrayList<Dependent>();

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

	public List<Dependent> getDependents() {
		return dependents;
	}

	public void setDependents(List<Dependent> dependents) {
		this.dependents = dependents;
	}
	
	public Dependent addDependent(String name, Integer age) {
		Dependent depenpent = new Dependent(name, age, this);
		getDependents().add(depenpent);
		return depenpent;
	}
}
