package com.afrid.iscan.bean;

import java.io.Serializable;

public class TagInfo  implements Serializable{
	private String tid;
	private String objID;
	private String objName;
	private String orgID;
	private String orgName;
	private String normalWashPrice;
	private String specialWashPrice;
	private String returnWashPrice;
	private String washTimes;
	private String businessType;	// 0����ͨ��1������
	private String xdDataDetailID;	// �ӱ�ID
	private String date;
	private String deptPrimaryKey;
	private String barCode;
	

	public TagInfo() {
	}

	public void setTID(final String tid) {
		this.tid = tid;
	}
	public String getTID() {
		return tid;
	}
	
	
	public void setObjID(final String objID) {
		this.objID = objID;
	}
	public String getObjID() {
		return objID;
	}
	
	
	public void setObjName(final String objName) {
		this.objName = objName;
	}
	public String getObjName() {
		return objName;
	}
	
	
	public void setOrgID(final String orgID) {
		this.orgID = orgID;
	}
	public String getOrgID() {
		return orgID;
	}
	

	public void setOrgName(final String orgName) {
		this.orgName = orgName;
	}
	public String getOrgName() {
		return orgName;
	}

	
	public void setNormalWashPrice(final String normalWashPrice) {
		this.normalWashPrice = normalWashPrice;
	}
	public String getNormalWashPrice() {
		return normalWashPrice;
	}

	
	public void setSpecialWashPrice(final String specialWashPrice) {
		this.specialWashPrice = specialWashPrice;
	}
	public String getSpecialWashPrice() {
		return specialWashPrice;
	}
	
	
	public void setReturnWashPrice(final String returnWashPrice) {
		this.returnWashPrice = returnWashPrice;
	}
	public String getReturnWashPrice() {
		return returnWashPrice;
	}
	
	
	public void setWashTimes(final String washTimes) {
		this.washTimes = washTimes;
	}
	public String getWashTimes() {
		return washTimes;
	}
	
	
	public void setBusinessType(final String businessType) {
		this.businessType = businessType;
	}
	public String getBusinessType() {
		return businessType;
	}
	
	
	public void setXdDataDetailID(final String xdDataDetailID) {
		this.xdDataDetailID = xdDataDetailID;
	}
	public String getXdDataDetailID() {
		return xdDataDetailID;
	}
	
	
	public void setDate(final String date) {
		this.date = date;
	}
	public String getDate() {
		return date;
	}
	
	
	public void setDeptPrimaryKey(final String deptPrimaryKey) {
		this.deptPrimaryKey = deptPrimaryKey;
	}
	public String getDeptPrimaryKey() {
		return deptPrimaryKey;
	}
	
	
	public void setBarCode(final String barCode) {
		this.barCode = barCode;
	}
	public String getBarCode() {
		return barCode;
	}
}

