package com.util;

import org.apache.commons.dbcp2.BasicDataSource;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class DbUtil {
	private static final BasicDataSource dataSource = new BasicDataSource();

	static {
		dataSource.setDriverClassName("com.mysql.jdbc.Driver");
		dataSource.setUrl("jdbc:mysql://192.168.0.100:3500/db_sales?characterEncoding=utf8");
		dataSource.setUsername("root");
		dataSource.setPassword("amxers");
	}
//	private static String dburl="jdbc:mysql://192.168.0.100:3500/db_productiontest?characterEncoding=utf8";
//	private static String dbUserName="root";
//	private static String dbPassword="amxers";
//	private static String jdbcName="com.mysql.jdbc.Driver";
	
public static Connection getCon()throws SQLException{

//	System.out.println("创建了一个Connection....");
//	Connection con= null;
//	try {
//		Class.forName(jdbcName);
//		con = DriverManager.getConnection(dburl, dbUserName, dbPassword);
//	}catch (Exception e){
//		e.printStackTrace();
//	}

	return dataSource.getConnection();
}
public void closeCon(Connection con)throws Exception {
	if(con!=null){
		con.close();
	}

}

}