package com.afrid.iscan.bean;

import java.util.List;

public class LinenDepartureInfo {
	
	private XdSysUseArea area;
	private OptUser user;
	private List<TagInfo> tagList;
	
	
	public LinenDepartureInfo() {}
	
	
	public void setArea(final XdSysUseArea area) {
		this.area = area;
	}
	public XdSysUseArea getArea() {
		return area;
	}
	
	
	public void setUser(final OptUser user) {
		this.user = user;
	}
	public OptUser getUser() {
		return user;
	}
	
	
	public void setTags(final List<TagInfo> tagList) {
		this.tagList = tagList;
	}
	public List<TagInfo> getTags() {
		return tagList;
	}

}
