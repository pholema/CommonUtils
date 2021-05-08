package com.pholema.tool.utils.db;

import java.io.PrintWriter;
import java.io.StringWriter;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import org.apache.log4j.Logger;

public class DbUtils {
	final static Logger logger = Logger.getLogger(DbUtils.class);
	
	public static ResultSet load(String server ,String database ,String username ,String password ,String sql){
		try {
			Class.forName("net.sourceforge.jtds.jdbc.Driver");
			String sqlserver_connect_string="jdbc:jtds:sqlserver://"+server+":1433/"+database;
			Connection connMSSQL;
			connMSSQL = DriverManager.getConnection(sqlserver_connect_string, username, password);
			return query(connMSSQL ,sql);
		}catch (Exception e) {
			StringWriter sw = new StringWriter();
			e.printStackTrace(new PrintWriter(sw));
			logger.error(sw.toString());
			return null;
		}
	}
	
	public static ResultSet query(Connection connMSSQL ,String sql){
		try {
			Statement stmt = (Statement) connMSSQL.createStatement();
			return stmt.executeQuery(sql);
		}catch (SQLException e) {
			logger.error(e.getMessage().toString());
			return null;
		}
		
	}
	
	
}
