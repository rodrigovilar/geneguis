package br.edu.ufcg.embedded.ise.geneguis.backend.examples;

import java.util.Date;

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
	private Date birthdate;
	private double credit;
	
	public CustomerDetails() {}

	public CustomerDetails(String ssn, String name, Date birthdate, double credit) {
		this.ssn = ssn;
		this.name = name;
		this.birthdate = birthdate;
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

	public Date getBirthdate() {
		return birthdate;
	}

	public void setBirthdate(Date birthdate) {
		this.birthdate = birthdate;
	}

	public double getCredit() {
		return credit;
	}

	public void setCredit(double credit) {
		this.credit = credit;
	}
}
