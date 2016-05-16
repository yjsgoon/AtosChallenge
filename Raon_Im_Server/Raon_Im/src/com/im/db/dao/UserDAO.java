package com.im.db.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import com.im.vo.UserVO;

/**
 * Data Access Object to access and control UserTable
 * @since 	2016. 01. 16.
 * @version 1.0
 * @author 	Yoon JiSoo
 */
public class UserDAO {
	private Connection conn = null;
	private ConnectionManager cm = new ConnectionManager();
	private PreparedStatement pstmt = null;
	
	/**
	 * modify the tuple that represents userUrl of parameter
	 * @Method	update
	 * @param	VO that stores the values of UserTable
	 * @return	boolean value that represents whethrer SQL query is success or not
	 */
	public boolean update(UserVO vo) throws Exception {
		cm.connect();
		conn = cm.getConnection();
		
		String sql = "update UserTable set userPW=?, gcmID=? where userID=?";
		
		try {
			pstmt = conn.prepareStatement(sql);
			pstmt.setString(1, vo.getUserPW());
			pstmt.setString(2, vo.getGcmID());
			pstmt.setString(5, vo.getUserID());
			pstmt.executeUpdate();
		} catch(SQLException e) {
			System.out.println("UserDAO : [ update error ]");
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
	 * @param	userID of the tuple to delete
	 * @return	boolean value that represents whethrer SQL query is success or not
	 */
	public boolean delete(String userID) throws Exception {
		cm.connect();
		conn = cm.getConnection();
		
		String sql = "delete from UserTable where userID=?";
		
		try {
			pstmt = conn.prepareStatement(sql);
			pstmt.setString(1,  userID);
			pstmt.executeUpdate();
		} catch(SQLException e) {
			System.out.println("UserDAO : [ delete error ]");
			e.printStackTrace();
			return false;
		} finally {
			cm.disconnect();
		}
		return true;
	}

	/**
	 * insert tuple using the values of the parameter
	 * @Method	create
	 * @param	VO that stores the data of the tuple
	 * @return	boolean value that represents whethrer SQL query is success or not
	 */
	public boolean create(UserVO vo) throws Exception {
		cm.connect();
		conn = cm.getConnection();
		
		String sql = "insert ignore into UserTable(userID, userPW, gcmID) "
				+ "values(?, ?, ?)";
		
		try {
			pstmt = conn.prepareStatement(sql);
			pstmt.setString(1, vo.getUserID());
			pstmt.setString(2, vo.getUserPW());
			pstmt.setString(3, vo.getGcmID());
			pstmt.executeUpdate();
		} catch(SQLException e) {
			System.out.println("UserDAO : [ create error ]");
			e.printStackTrace();
			return false;
		} finally {
			cm.disconnect();
		}
		return true;
	}
	
	/**
	 * search the tuple that represents parameter and return
	 * @Method	search
	 * @param	userID to search tuple
	 * @return	VO that stores the value of the tuple searched
	 */
	public UserVO search(String userID) throws Exception {
		cm.connect();
		conn = cm.getConnection();
		
		String sql = "select * from UserTable where userID=?";
		UserVO vo = new UserVO();
		
		try {
			pstmt = conn.prepareStatement(sql);
			pstmt.setString(1, userID);
			ResultSet rs = pstmt.executeQuery();
			
			rs.next();
			vo.setUserID(rs.getString("userID"));
			vo.setUserPW(rs.getString("userPW"));
			vo.setGcmID(rs.getString("gcmID"));
			rs.close();
		} catch(SQLException e) {
			System.out.println("UserDAO : [ search error ]");
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
	 * @return	VO List that stores searched values of the tuple
	 */
	public List<UserVO> searchList(String userID) throws Exception {
		cm.connect();
		conn = cm.getConnection();
		List<UserVO> datas = new ArrayList<UserVO>();
	
		String sql = "select * from UserTable";
		try {
			pstmt = conn.prepareStatement(sql);
			ResultSet rs = pstmt.executeQuery();
			while(rs.next()) {
				UserVO vo = new UserVO();
				
				vo.setUserID(rs.getString("userID"));
				vo.setUserPW(rs.getString("userPW"));
				vo.setGcmID(rs.getString("gcmID"));
				datas.add(vo);
			}
			rs.close();
			
		} catch(SQLException e) {
			System.out.println("UserDAO : [ searchList error ]");
			e.printStackTrace();
		} finally {
			cm.disconnect();
		}
		return datas;
	}
}
