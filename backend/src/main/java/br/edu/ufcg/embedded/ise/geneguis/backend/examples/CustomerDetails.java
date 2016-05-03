package br.edu.ufcg.embedded.ise.geneguis.backend.examples;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;

@Entity
public class CustomerDetails {

	@Id
	@GeneratedValue
	private Long id;

	private String ssn;
	private String name;
	private Double credit;
	
	public CustomerDetails() {}

	public CustomerDetails(String ssn, String name, double credit) {
		this.ssn = ssn;
		this.name = name;
		this.credit = credit;
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

	public String getSsn() {
		return ssn;
	}

	public void setSsn(String ssn) {
		this.ssn = ssn;
	}

	public Double getCredit() {
		return credit;
	}

	public void setCredit(Double credit) {
		this.credit = credit;
	}
}
