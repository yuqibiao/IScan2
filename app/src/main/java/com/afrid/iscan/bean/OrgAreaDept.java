package com.afrid.iscan.bean;

import java.util.List;

public class OrgAreaDept {
	private List<HotelSource> hsList;
	private List<XdSysUseArea> areaList;
	private List<SpOffice> deptList;
	
	public OrgAreaDept() {}
	
	public OrgAreaDept(final List<HotelSource> hsList, final List<XdSysUseArea> areaList, final List<SpOffice> deptList) {
		this.hsList = hsList;
		this.areaList = areaList;
		this.deptList = deptList;
	}
	
	
	public void setOrgs(final List<HotelSource> hsList) {
		this.hsList = hsList;
	}
	public List<HotelSource> getOrgs() {
		return hsList;
	}
	
	public void setAreas(final List<XdSysUseArea> areaList) {
		this.areaList = areaList;
	}
	public List<XdSysUseArea> getAreas() {
		return areaList;
	}
	
	public void setDepts(final List<SpOffice> deptList) {
		this.deptList = deptList;
	}
	public List<SpOffice> getDepts() {
		return deptList;
	}

}
