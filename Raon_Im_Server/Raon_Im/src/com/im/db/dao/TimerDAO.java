package com.im.db.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Date;
import java.util.ArrayList;
import java.util.List;

import com.im.vo.TimerVO;

/**
 * Data Access Object to access and control TimerTable
 * @since 	2016. 01. 16.
 * @version 1.0
 * @author 	Yoon JiSoo
 */
public class TimerDAO {
	private Connection conn = null;
	private ConnectionManager cm = new ConnectionManager();
	private PreparedStatement pstmt = null;
	
	/**
	 * modify the tuple that fits the values of user ID and companyUrl of parameter(Modify Timer)
	 * @Method	update
	 * @param	VO that stores the value of TimerTable
	 * @return	boolean value that represents whethrer SQL query is success or not
	 */
	public boolean update(TimerVO vo) throws Exception {
		cm.connect();
		conn = cm.getConnection();
		
		String sql = "update TimerTable set timer=? where userID=? and companyUrl=?";
		
		try {
			pstmt = conn.prepareStatement(sql);
			pstmt.setDate(1, vo.getTimer());
			pstmt.setString(2, vo.getUserID());
			pstmt.setString(3, vo.getCompanyUrl());
			pstmt.executeUpdate();
		} catch(SQLException e) {
			System.out.println("TimerDAO : [ update error ]");
			e.printStackTrace();
			return false;
		}
		finally {
			cm.disconnect();
		}
		return true;
	}
	
	/**
	 * delete the tuple that represents parameter
	 * @Method	delete
	 * @param	userID and companyUrl of the tuple to delete
	 * @return	boolean value that represents whethrer SQL query is success or not
	 */
	public boolean delete(String userID, String companyUrl) throws Exception {
		cm.connect();
		conn = cm.getConnection();
		
		String sql = "delete from TimerTable where userID=? and companyUrl=?";
		
		try {
			pstmt = conn.prepareStatement(sql);
			pstmt.setString(1, userID);
			pstmt.setString(2, companyUrl);
			pstmt.executeUpdate();
		} catch(SQLException e) {
			System.out.println("TimerDAO : [ delete error ]");
			e.printStackTrace();
			return false;
		} finally {
			cm.disconnect();
		}
		return true;
	}

	/**
	 * insert tuple by using the values of the parameter
	 * @Method	create
	 * @param	VO that stores the data of tuple to insert
	 * @return	boolean value that represents whethrer SQL query is success or not
	 */
	public boolean create(TimerVO vo) throws Exception {
		cm.connect();
		conn = cm.getConnection();
		
		String sql = "insert ignore into TimerTable(userID, companyUrl, " 
				 + "timer) values(?, ?, ?)";
		
		try {
			pstmt = conn.prepareStatement(sql);
			pstmt.setString(1, vo.getUserID());
			pstmt.setString(2, vo.getCompanyUrl());
			pstmt.setDate(3, vo.getTimer());
			pstmt.executeUpdate();
		} catch(SQLException e) {
			System.out.println("TimerDAO : [ create error ]");
			e.printStackTrace();
			return false;
		} finally {
			cm.disconnect();
		}
		return true;
	}
	
	/**
	 * search the tuple that fits parameter and return
	 * @Method	search
	 * @param	userID of the tuple to search
	 * @return	VO that stores the values of the tuple to search 
	 */
	public TimerVO search(String userID) throws Exception {
		cm.connect();
		conn = cm.getConnection();
		
		String sql = "select * from TimerTable where userID=?";
		TimerVO vo = new TimerVO();
		
		try {
			pstmt = conn.prepareStatement(sql);
			pstmt.setString(1, userID);
			ResultSet rs = pstmt.executeQuery();
			
			rs.next();
			vo.setUserID(rs.getString("userID"));
			vo.setCompanyUrl(rs.getString("companyUrl"));
			vo.setTimer(rs.getDate("timer"));
			rs.close();
		} catch(SQLException e) {
			System.out.println("TimerDAO : [ search fail ]");
			e.printStackTrace();
		} finally {
			cm.disconnect();
		}
		return vo;
	}
	
	/**
	 * search all of the tuples that represent parameter and return
	 * @Method	searchList
	 * @param	userID of the tuples to search 
	 * @return	VO List that stores the searched values of the tuples
	 */
	public List<TimerVO> searchList(String userID) throws Exception {
		cm.connect();
		conn = cm.getConnection();
		List<TimerVO> datas = new ArrayList<TimerVO>();
	
		String sql = "select * from TimerTable where userID=?";
		try {
			pstmt = conn.prepareStatement(sql);
			pstmt.setString(1, userID);
			ResultSet rs = pstmt.executeQuery();
			while(rs.next()) {
				TimerVO vo = new TimerVO();
				
				vo.setUserID(rs.getString("userID"));
				vo.setCompanyUrl(rs.getString("companyUrl"));
				vo.setTimer(rs.getDate("timer"));
				datas.add(vo);
			}
			rs.close();
			
		} catch(SQLException e) {
			System.out.println("TimerDAO : [ searchList error ]");
			e.printStackTrace();
		} finally {
			cm.disconnect();
		}
		return datas;
	}
	
	/**
	 * search all of the tuples that represent parameter and return
	 * @Method	searchExpiration
	 * @param	the timer that represents the tuples
	 * @return	VO that stores the searched values of the tuples 
	 */
	public List<TimerVO> searchExpiration(Date expirationDate) throws Exception {
		cm.connect();
		conn = cm.getConnection();
		List<TimerVO> datas = new ArrayList<TimerVO>();
	
		String sql = "select * from TimerTable where timer=?";
		try {
			pstmt = conn.prepareStatement(sql);
			pstmt.setDate(1, expirationDate);
			ResultSet rs = pstmt.executeQuery();
			while(rs.next()) {
				TimerVO vo = new TimerVO();
				
				vo.setUserID(rs.getString("userID"));
				vo.setCompanyUrl(rs.getString("companyUrl"));
				vo.setTimer(rs.getDate("timer"));
				datas.add(vo);
			}
			rs.close();
			
		} catch(SQLException e) {
			System.out.println("TimerDAO : [ searchExpiration error ]");
			e.printStackTrace();
		} finally {
			cm.disconnect();
		}
		return datas;
	}
}
