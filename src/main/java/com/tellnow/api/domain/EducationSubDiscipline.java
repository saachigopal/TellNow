package com.tellnow.api.domain;

import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import org.hibernate.search.annotations.Analyze;
import org.hibernate.search.annotations.Field;
import org.hibernate.search.annotations.Index;
import org.hibernate.search.annotations.Store;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;

@JsonInclude(Include.NON_NULL)
@Entity
@Table(name = "education_sub_discipline")
public class EducationSubDiscipline {

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private Long id;

	@Field(index = Index.YES, analyze = Analyze.YES, store = Store.NO)
	private String subdiscipline;

	@ManyToOne(fetch = FetchType.EAGER)
	@JoinColumn(name = "education_discipline_id", referencedColumnName = "id")
	private EducationDiscipline discipline;

	public EducationSubDiscipline() {
	}

	public EducationSubDiscipline(String subdiscipline, EducationDiscipline discipline) {
		this.subdiscipline = subdiscipline;
		this.discipline = discipline;
	}

	public EducationSubDiscipline(Long id, String subdiscipline, EducationDiscipline discipline) {
		this.id = id;
		this.discipline = discipline;
		this.subdiscipline = subdiscipline;
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getSubdiscipline() {
		return subdiscipline;
	}

	public void setSubdiscipline(String subdiscipline) {
		this.subdiscipline = subdiscipline;
	}

	public EducationDiscipline getDiscipline() {
		return discipline;
	}

	public void setDiscipline(EducationDiscipline discipline) {
		this.discipline = discipline;
	}
}
