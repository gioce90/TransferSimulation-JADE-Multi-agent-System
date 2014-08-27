package database2.mysql;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import com.mysql.jdbc.jdbc2.optional.MysqlDataSource;

import database2.DAO;
import database2.DAOException;
import database2.DAOFactory;
import database2.ProvinciaDAO;


public class MySqlDAOFactory extends DAOFactory {
	
	private static Connection conn; // gestisce una connessione
	static String DATASOURCE_DB_NAME = "java:comp/env/jdbc/ADISysDB";
	
	public Connection getConnection(){
		MysqlDataSource source = new MysqlDataSource();
		try {
			source.setDatabaseName("STSN_MAIN");
			source.setPassword("experis");
			conn = source.getConnection("AdminUser", "psw");
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return conn;
	}
	
	
	public static boolean exist(String table, String field, String value){
		boolean bool = false;
		try{
			Connection conn = DAO.getDAO().getConnection();
			PreparedStatement prepStmt = conn.prepareStatement("Select * from " + table + " where " +
					field + " = '" + value + "'");
			ResultSet rs = prepStmt.executeQuery();
			if (rs.next())
				bool = true;
			rs.close();
			prepStmt.close();
			conn.close();
		} catch (SQLException e){
			e.printStackTrace();
		}
		return bool;
	}
	
	
	public static boolean exist(String table, int id, String field, String value){
		boolean bool = false;
		try{
			Connection conn = DAO.getDAO().getConnection();
			PreparedStatement prepStmt = conn.prepareStatement("Select * from "+table
					+" where id != '" + id + "' AND " + field + " = '" + value + "'");
			ResultSet rs = prepStmt.executeQuery();
			if (rs.next())
				bool = true;
			rs.close();
			prepStmt.close();
			conn.close();
		} catch (SQLException e){
			e.printStackTrace();
		}
		return bool;
	}
	
	
	private static Object createDAO(Class<?> classObj) throws DAOException{
		Object x = null;
		try {
			 x = classObj.newInstance();
		} catch (InstantiationException e) {
			e.printStackTrace();
		} catch (IllegalAccessException e) {
			e.printStackTrace();
		}
		return x;
	}

	
	// Lista dei get per le varie interfacce
	
	@Override
	public ProvinciaDAO getProvinciaDAO() throws DAOException {
		return (MySqlProvinciaDAO) createDAO(MySqlProvinciaDAO.class);
	}
	
	/*
	@Override
	public ProvinceDAO getAutistiDAO() throws DAOException {
		return (MySqlAutistiDAO) createDAO(MySqlAutistiDAO.class);
	}
	*/
	
	// ...
	
}
