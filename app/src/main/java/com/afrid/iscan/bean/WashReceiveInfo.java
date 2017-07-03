package com.afrid.iscan.bean;

import java.util.List;

public class WashReceiveInfo {
	
	private XdSysUseArea area;
	private SpOffice dept;
	private OptUser user;
	private String barCode;
	private List<TagInfo> tagList;
	
	public WashReceiveInfo() {}
	
	
	public void setArea(final XdSysUseArea area) {
		this.area = area;
	}
	public XdSysUseArea getArea() {
		return area;
	}
	
	
	public void setDept(final SpOffice dept) {
		this.dept = dept;
	}
	public SpOffice getDept() {
		return dept;
	}
	
	
	public void setUser(final OptUser user) {
		this.user = user;
	}
	public OptUser getUser() {
		return user;
	}
	
	
	public void setBarCode(final String barCode) {
		this.barCode = barCode;
	}
	public String getBarCode() {
		return barCode;
	}
	
	
	public void setTags(final List<TagInfo> tagList) {
		this.tagList = tagList;
	}
	public List<TagInfo> getTags() {
		return tagList;
	}


}
