package com.tellnow.api.domain;

import java.util.Set;

import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.Table;

import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
import com.fasterxml.jackson.annotation.ObjectIdGenerators;

@JsonInclude(Include.NON_NULL)
@JsonIdentityInfo(generator = ObjectIdGenerators.IntSequenceGenerator.class, property = "@id")
@Entity
@Table(name = "education_discipline")
public class EducationDiscipline {

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private Long id;

	private String discipline;

	@OneToMany(fetch = FetchType.EAGER, mappedBy = "discipline")
	private Set<EducationSubDiscipline> subDisciplines;
	
	public EducationDiscipline() {
	}

	public EducationDiscipline(String discipline) {
		this.discipline = discipline;
	}
	
	public EducationDiscipline(Long id, String discipline) {
		this.id = id;
		this.discipline = discipline;
	}
	
	public EducationDiscipline(Long id, String discipline, Set<EducationSubDiscipline> subDisciplines) {
		this.id = id;
		this.discipline = discipline;
		this.subDisciplines = subDisciplines;
	}

	public Long getId() {
		return id;
	}

	public String getDiscipline() {
		return discipline;
	}

	public Set<EducationSubDiscipline> getSubDisciplines() {
		return subDisciplines;
	}

	public void setSubDisciplines(Set<EducationSubDiscipline> subDisciplines) {
		this.subDisciplines = subDisciplines;
	}	
}
