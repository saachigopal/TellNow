package com.tellnow.api.domain;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;

@JsonInclude(Include.NON_NULL)
@Entity
@Table(name = "media_file")
public class MediaFile {

	@JsonIgnore
	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private Long id;

	private String name;

	@JsonIgnore
	private String owner;

	private String md5Sum;

	@JsonIgnore
	private String path;

	private String url;

	private String contentType;

	public MediaFile() {
	}

	public MediaFile(String name, String owner, String md5Sum, String path) {
		this.name = name;
		this.owner = owner;
		this.md5Sum = md5Sum;
		this.path = path;
	}

	public MediaFile(String name, String owner, String md5Sum, String path, String url) {
		this.name = name;
		this.owner = owner;
		this.md5Sum = md5Sum;
		this.path = path;
		this.url = url;
	}

	public MediaFile(String name, String owner, String md5Sum, String path, String url, String contentType) {
		this.name = name;
		this.owner = owner;
		this.md5Sum = md5Sum;
		this.path = path;
		this.contentType = contentType;
		this.url = url;
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

	public String getOwner() {
		return owner;
	}

	public void setOwner(String owner) {
		this.owner = owner;
	}

	public String getMd5Sum() {
		return md5Sum;
	}

	public void setMd5Sum(String md5Sum) {
		this.md5Sum = md5Sum;
	}

	public String getPath() {
		return path;
	}

	public void setPath(String path) {
		this.path = path;
	}

	public String getContentType() {
		return contentType;
	}

	public void setContentType(String contentType) {
		this.contentType = contentType;
	}

	public String getUrl() {
		return url;
	}

	public void setUrl(String url) {
		this.url = url;
	}
}
