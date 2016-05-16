package com.im.vo;

/**
 * The value object for WaitingTable
 * @since 	2016. 3. 29.
 * @version 1.0	
 * @author 	Yoon JiSoo
 */
public class WaitingVO {
	private String userID;
	private String companyUrl;
	
	public String getUserID() {
		return userID;
	}
	public void setUserID(String userID) {
		this.userID = userID;
	}
	public String getCompanyUrl() {
		return companyUrl;
	}
	public void setCompanyUrl(String companyUrl) {
		this.companyUrl = companyUrl;
	}
	
	@Override
	public String toString() {
		return "WaitingVO [userID=" + userID + ", companyUrl=" + companyUrl + "]";
	}
}
