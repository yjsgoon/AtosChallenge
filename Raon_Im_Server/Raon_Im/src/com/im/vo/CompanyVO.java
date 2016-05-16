package com.im.vo;

/**
 * The value object for CompanyTable
 * @since 	2016. 01. 16.
 * @version 1.0
 * @author 	Yoon JiSoo
 */
public class CompanyVO {
	private String companyUrl;
	private String alias;
	private String requestData;
	
	public String getCompanyUrl() {
		return companyUrl;
	}
	public void setCompanyUrl(String companyUrl) {
		this.companyUrl = companyUrl;
	}
	public String getAlias() {
		return alias;
	}
	public void setAlias(String alias) {
		this.alias = alias;
	}
	public String getRequestData() {
		return requestData;
	}
	public void setRequestData(String requestData) {
		this.requestData = requestData;
	}
	
	@Override
	public String toString() {
		return "CompanyVO [companyUrl=" + companyUrl + ", alias="
				+ alias + ", requestData=" + requestData + "]";
	}
}
