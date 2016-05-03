package br.edu.ufcg.embedded.ise.geneguis.backend.examples;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.ManyToOne;

import com.fasterxml.jackson.annotation.JsonBackReference;

import br.edu.ufcg.embedded.ise.geneguis.jpadomain.Relationship;

@Entity
public class Dependent {

	@Id
	@GeneratedValue
	private Long id;

	private String name;
	private Integer age;
	
	@ManyToOne(cascade=CascadeType.ALL)
	@JsonBackReference
	@Relationship(reverse="dependents")
	private Client client;
	

	public Dependent() {}
	
	public Dependent(String name, Integer age, Client client) {
		this.name = name;
		this.age = age;
		this.client = client;
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

	public Client getClient() {
		return client;
	}

	public void setClient(Client client) {
		this.client = client;
	}

	public Integer getAge() {
		return age;
	}

	public void setAge(Integer age) {
		this.age = age;
	}
}
