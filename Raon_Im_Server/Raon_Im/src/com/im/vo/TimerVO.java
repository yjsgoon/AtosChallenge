package com.im.vo;

import java.sql.Date;

/**
 * The value object for TimerTable
 * @since 	2016. 01. 16.
 * @version 1.0
 * @author 	Yoon JiSoo
 */
public class TimerVO {
	private String userID;
	private String companyUrl;
	private Date timer;
	
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
	public Date getTimer() {
		return timer;
	}
	public void setTimer(Date timer) {
		this.timer = timer;
	}
	
	@Override
	public String toString() {
		return "TimerVO [userID=" + userID + ", companyIP=" + companyUrl
				+ ", timer=" + timer + "]";
	}
	
}
