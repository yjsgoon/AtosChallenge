package com.im.vo;

/**
 * The value object for UserTable
 * @since 	2016. 01. 16.
 * @version 1.0
 * @author 	Yoon JiSoo
 */
public class UserVO {
	private String userID;
	private String userPW;
	private String gcmID;
	
	public String getUserID() {
		return userID;
	}
	public void setUserID(String userID) {
		this.userID = userID;
	}
	public String getUserPW() {
		return userPW;
	}
	public void setUserPW(String userPW) {
		this.userPW = userPW;
	}
	public String getGcmID() {
		return gcmID;
	}
	public void setGcmID(String gcmID) {
		this.gcmID = gcmID;
	}
	
	@Override
	public String toString() {
		return "UserVO [userID=" + userID + ", userPW=" + userPW + ", gcmID=" + gcmID + "]";
	}
}
