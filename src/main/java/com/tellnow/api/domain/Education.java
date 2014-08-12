package com.tellnow.api.domain;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.OneToOne;
import javax.persistence.Table;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;

@JsonInclude(Include.NON_NULL)
@Entity
@Table(name = "education")
public class Education {

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private Long id;

	@OneToOne(fetch = FetchType.LAZY, optional = true, cascade=CascadeType.ALL)
	@JoinColumn(name = "education_level_id", referencedColumnName="id")
	private EducationLevel education_level;

	@OneToOne(fetch = FetchType.LAZY, optional = true, cascade=CascadeType.ALL)
	@JoinColumn(name = "education_sub_discipline_id", referencedColumnName="id")
	private EducationSubDiscipline subdiscipline;

	public Education() {
	}

	public Education(EducationLevel education_level, EducationSubDiscipline subdiscipline) {
		this.education_level = education_level;
		this.subdiscipline = subdiscipline;
	}

	public Education(Long id, EducationLevel education_level, EducationSubDiscipline subdiscipline) {
		this.id = id;
		this.education_level = education_level;
		this.subdiscipline = subdiscipline;
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public EducationLevel getEducation_level() {
		return education_level;
	}

	public void setEducation_level(EducationLevel education_level) {
		this.education_level = education_level;
	}

	public EducationSubDiscipline getSubdiscipline() {
		return subdiscipline;
	}

	public void setSubdiscipline(EducationSubDiscipline subdiscipline) {
		this.subdiscipline = subdiscipline;
	}	
}
