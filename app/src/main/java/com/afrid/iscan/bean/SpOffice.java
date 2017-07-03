package com.afrid.iscan.bean;

public class 	SpOffice {
	
	private String primaryKey;
	private String id;
	private String name;
	private String parent;
	
	public SpOffice() {

	}
	
	public SpOffice(final String primaryKey, final String id, final String name, final String parent) {
		this.primaryKey = primaryKey;
		this.id = id;
		this.name = name;
		this.parent = parent;
	}
	
	public void setPrimaryKey(final String primaryKey) {
		this.primaryKey = primaryKey;
	}
	public String getPrimaryKey() {
		return primaryKey;
	}
	
	public void setId(final String id) {
		this.id = id;
	}
	public String getId() {
		return id;
	}
	
	public void setName(final String name) {
		this.name = name;
	}
	public String getName() {
		return name;
	}
	
	public void setParent(final String parent) {
		this.parent = parent;
	}
	public String getParent() {
		return parent;
	}

}
