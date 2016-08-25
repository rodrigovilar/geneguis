package br.edu.ufcg.embedded.ise.geneguis.e2e.issue;

import java.util.Date;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.ManyToOne;

import com.fasterxml.jackson.annotation.JsonBackReference;

import br.edu.ufcg.embedded.ise.geneguis.jpadomain.Relationship;

@Entity
public class Issue {

	@Id
	@GeneratedValue
	private Long id;

	@Enumerated(EnumType.STRING)
	private IssueType type;
	private String summary;

	@Enumerated(EnumType.STRING)
	private Priority priority;

	private Date dueDate;
	private String environment;
	private String description;
	private String originalEstimate;

	@ManyToOne(cascade = CascadeType.ALL)
	@JsonBackReference
	@Relationship(reverse = "versions")
	private Project project;

	public Issue() {
	}

	public Issue(IssueType type, String summary, Project project) {
		this.summary = summary;
		this.project = project;
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public IssueType getType() {
		return type;
	}

	public void setType(IssueType type) {
		this.type = type;
	}

	public String getSummary() {
		return summary;
	}

	public void setSummary(String summary) {
		this.summary = summary;
	}

	public Priority getPriority() {
		return priority;
	}

	public void setPriority(Priority priority) {
		this.priority = priority;
	}

	public Date getDueDate() {
		return dueDate;
	}

	public void setDueDate(Date dueDate) {
		this.dueDate = dueDate;
	}

	public String getEnvironment() {
		return environment;
	}

	public void setEnvironment(String environment) {
		this.environment = environment;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public String getOriginalEstimate() {
		return originalEstimate;
	}

	public void setOriginalEstimate(String originalEstimate) {
		this.originalEstimate = originalEstimate;
	}

	public Project getProject() {
		return project;
	}

	public void setProject(Project project) {
		this.project = project;
	}

}
