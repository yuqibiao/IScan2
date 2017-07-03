package com.afrid.iscan.bean;

public class XdSysUseArea {
	
	private String primaryKey;
	private String id;
	private String name;
	private String parentID;
	private String parentName;
	private String parentPrimaryKey;
	
	public XdSysUseArea() {}
	
	
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
	
	public void setParentID(final String parentID) {
		this.parentID = parentID;
	}
	public String getParentID() {
		return parentID;
	}
	
	public void setParentName(final String parentName) {
		this.parentName = parentName;
	}
	public String getParentName() {
		return parentName;
	}

	public void setParentPrimaryKey(final String parentPrimaryKey) {
		this.parentPrimaryKey = parentPrimaryKey;
	}
	public String getParentPrimaryKey() {
		return parentPrimaryKey;
	}
}
