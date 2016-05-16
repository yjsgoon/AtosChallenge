package com.im.db.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import com.im.vo.CompanyVO;

/**
 * Data Access Object to access and control CompanyTable
 * @since 	2016. 01. 16.
 * @version 1.0
 * @author 	Yoon JiSoo
 */
public class CompanyDAO {
	private Connection conn = null;
	private ConnectionManager cm = new ConnectionManager();
	private PreparedStatement pstmt = null;
	
	/**
 	 * modify the tuple that represents companyUrl of parameter
	 * @Method	update
	 * @param	VO that stores the value of CompanyTable
	 * @return	boolean value that represents whethrer SQL query is success or not
	 */
	public boolean update(CompanyVO vo) throws Exception {
		cm.connect();
		conn = cm.getConnection();
		
		String sql = "update CompanyTable set alias=?, "
				+ "requestData=? where companyUrl=?";
		
		try {
			pstmt = conn.prepareStatement(sql);
			pstmt.setString(1, vo.getAlias());
			pstmt.setString(2, vo.getRequestData());
			pstmt.setString(3, vo.getCompanyUrl());
			pstmt.executeUpdate();
		} catch(SQLException e) {
			System.out.println("CompanyDAO : [ update error ]");
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
	 * @param	companyUrl of the tuple to delete
	 * @return	boolean value that represents whethrer SQL query is success or not
	 */
	public boolean delete(String companyUrl) throws Exception {
		cm.connect();
		conn = cm.getConnection();
		
		String sql = "delete from CompanyTable where companyUrl=?";
		
		try {
			pstmt = conn.prepareStatement(sql);
			pstmt.setString(1, companyUrl);
			pstmt.executeUpdate();
		} catch(SQLException e) {
			System.out.println("CompanyDAO : [ delete error ]");
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
	public boolean create(CompanyVO vo) throws Exception {
		cm.connect();
		conn = cm.getConnection();
		
		String sql = "insert ignore into CompanyTable(companyUrl, alias, " 
				 + "requestData) values(?, ?, ?)";
		
		try {
			pstmt = conn.prepareStatement(sql);
			pstmt.setString(1, vo.getCompanyUrl());
			pstmt.setString(2, vo.getAlias());
			pstmt.setString(3, vo.getRequestData());
			pstmt.executeUpdate();
		} catch(SQLException e) {
			System.out.println("CompanyDAO : [ create error ]");
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
	 * @param	companyUrl to search tuple
	 * @return	VO that stores the value of the tuple searched
	 */
	public CompanyVO search(String companyUrl) throws Exception {
		cm.connect();
		conn = cm.getConnection();
		
		String sql = "select * from CompanyTable where companyUrl=?";
		CompanyVO vo = new CompanyVO();
		
		try {
			pstmt = conn.prepareStatement(sql);
			pstmt.setString(1, companyUrl);
			ResultSet rs = pstmt.executeQuery();
			
			rs.next();
			vo.setCompanyUrl(rs.getString("companyUrl"));
			vo.setAlias(rs.getString("alias"));
			vo.setRequestData(rs.getString("requestData"));
			rs.close();
		} catch(SQLException e) {
			System.out.println("CompanyDAO : [ search error ]");
			e.printStackTrace();
		} finally {
			cm.disconnect();
		}
		return vo;
	}
	
	/**
	 * search the tuple that represents parameter and return 
	 * @Method	searchUrl
	 * @param	alias of tuple that has companyUrl to search
	 * @return	VO that stores the value of the tuple searched
	 */
	public CompanyVO searchUrl(String alias) throws Exception {
		cm.connect();
		conn = cm.getConnection();
		
		String sql = "select * from CompanyTable where alias=?";
		CompanyVO vo = new CompanyVO();
		
		try {
			pstmt = conn.prepareStatement(sql);
			pstmt.setString(1, alias);
			ResultSet rs = pstmt.executeQuery();
			
			rs.next();
			vo.setCompanyUrl(rs.getString("companyUrl"));
			vo.setAlias(rs.getString("alias"));
			vo.setRequestData(rs.getString("requestData"));
			rs.close();
		} catch(SQLException e) {
			System.out.println("CompanyDAO : [ search error ]");
			e.printStackTrace();
		} finally {
			cm.disconnect();
		}
		return vo;
	}
	
	/**
	 * search all of the tuples that represent parameter and return
	 * @Method	searchList
	 * @param	companyUrl of the tuples to search
	 * @return	VO List that stores searched values of the tuple
	 */
	public List<CompanyVO> searchList(String companyUrl) throws Exception {
		cm.connect();
		conn = cm.getConnection();
		List<CompanyVO> datas = new ArrayList<CompanyVO>();
	
		String sql = "select * from CompanyTable";
		try {
			pstmt = conn.prepareStatement(sql);
			ResultSet rs = pstmt.executeQuery();
			while(rs.next()) {
				CompanyVO vo = new CompanyVO();
				
				vo.setCompanyUrl(rs.getString("companyUrl"));
				vo.setAlias(rs.getString("alias"));
				vo.setRequestData(rs.getString("requestData"));
				datas.add(vo);
			}
			rs.close();
			
		} catch(SQLException e) {
			System.out.println("CompanyDAO : [ searchList error ]");
			e.printStackTrace();
		} finally {
			cm.disconnect();
		}
		return datas;
	}
}
