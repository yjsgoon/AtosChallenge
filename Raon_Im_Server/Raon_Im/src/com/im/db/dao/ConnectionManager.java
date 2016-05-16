package com.im.db.dao;

import java.sql.Connection;
import java.sql.SQLException;

import javax.naming.Context;
import javax.naming.InitialContext;
import javax.sql.DataSource;

/**
 * Create the necessary objects to the DataBase access
 * @since 	2016. 01. 18.
 * @version 1.0
 * @author 	Yoon JiSoo
 */
public class ConnectionManager {
	private Connection conn = null;
	
	/**
	 * initialize the configuration for connection pool, and create and acquire the connection object.
	 * @Method	connect
	 */
	public void connect() throws Exception {
		try {
			Context initContext = new InitialContext();
			Context envContext = (Context)initContext.lookup("java:/comp/env");
			DataSource ds = (DataSource)envContext.lookup("jdbc/mysql");
			conn = ds.getConnection();
		} catch(SQLException e) {
			System.out.println("ConnectionManager : [ connect error ]");
			e.printStackTrace();
		}
	}
	
	/**
	 * finish the connection
	 * @Method	disconnect
	 */
	public void disconnect() throws Exception {
		try {
			if(conn != null)
				conn.close();
		} catch(SQLException e) {
			System.out.println("ConnectionManager : [ disconnect error ]");
			e.printStackTrace();
		}
	}
	
	/**
	 * acquire(return) the connection object
	 * @Method	getConnection
	 */
	public Connection getConnection() throws Exception {
		return conn;
	}
}
