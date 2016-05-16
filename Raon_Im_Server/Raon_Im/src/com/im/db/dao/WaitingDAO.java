package com.im.db.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import com.im.vo.WaitingVO;

/**
 * Data Access Object to access and control WaitingTable
 * @since 	2016. 3. 29.
 * @version 1.0	
 * @author 	Yoon JiSoo
 */
public class WaitingDAO {
	private Connection conn = null;
	private ConnectionManager cm = new ConnectionManager();
	private PreparedStatement pstmt = null;
	
	/**
	 * insert tuple using the values of the parameter
	 * @Method	create
	 * @param	VO that stores the data of the tuple
	 * @return	boolean value that represents whethrer SQL query is success or not
	 */
	public boolean create(WaitingVO vo) throws Exception {
		cm.connect();
		conn = cm.getConnection();
		
		String sql = "insert ignore into WaitingTable(userID, companyUrl) values(?, ?)";
		
		try {
			pstmt = conn.prepareStatement(sql);
			pstmt.setString(1, vo.getUserID());
			pstmt.setString(2, vo.getCompanyUrl());
			pstmt.executeUpdate();
		} catch(SQLException e) {
			System.out.println("WaitingDAO : [ create error ]");
			e.printStackTrace();
			return false;
		} finally {
			cm.disconnect();
		}
		return true;
	}
	
	/**
	 * delete the tuple that represents parameter
	 * @Method	delete
	 * @param	companyUrl of the tuple to delete
	 * @return	boolean value that represents whethrer SQL query is success or not
	 */
	public boolean delete(String userID, String companyUrl) throws Exception {
		cm.connect();
		conn = cm.getConnection();
		
		String sql = "delete from WaitingTable where userID=? and companyUrl=?";
		
		try {
			pstmt = conn.prepareStatement(sql);
			pstmt.setString(1,  userID);
			pstmt.setString(2,  companyUrl);
			pstmt.executeUpdate();
		} catch(SQLException e) {
			System.out.println("WaitingDAO : [ delete error ]");
			e.printStackTrace();
			return false;
		} finally {
			cm.disconnect();
		}
		return true;
	}
	
	/**
	 * search all of the tuples that represent parameter and return
	 * @Method	searchList
	 * @param	userID of the tuples to search
	 * @return	VO List that stores searched values of the tuple
	 */
	public List<WaitingVO> searchList(String userID) throws Exception {
		cm.connect();
		conn = cm.getConnection();
		List<WaitingVO> datas = new ArrayList<WaitingVO>();
	
		String sql = "select * from WaitingTable where userID=?";
		try {
			pstmt = conn.prepareStatement(sql);
			pstmt.setString(1, userID);
			ResultSet rs = pstmt.executeQuery();
			while(rs.next()) {
				WaitingVO vo = new WaitingVO();
				
				vo.setUserID(rs.getString("userID"));
				vo.setCompanyUrl(rs.getString("companyUrl"));
				datas.add(vo);
			}
			rs.close();
			
		} catch(SQLException e) {
			System.out.println("WaitingDAO : [ searchList error ]");
			e.printStackTrace();
		} finally {
			cm.disconnect();
		}
		return datas;
	}
}
