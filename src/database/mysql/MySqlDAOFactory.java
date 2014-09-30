package database.mysql;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import com.mysql.jdbc.jdbc2.optional.MysqlDataSource;

import database.DAO;
import database.DAOFactory;
import database.DriverDAO;
import database.VehicleDAO;


public class MySqlDAOFactory extends DAOFactory {
	
	private static Connection conn; // gestisce una connessione
	private static String DB_NAME = "trasportiDB";
	private static String PASSWORD = "experis";
	private static String USER = "root";
	
	
	public Connection getConnection(){
		MysqlDataSource source = new MysqlDataSource();
		try {
			source.setDatabaseName(DB_NAME);
			source.setPassword(PASSWORD);
			conn = source.getConnection(USER, PASSWORD);
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return conn;
	}
	
	
	// Crea l'oggetto DAO corrispondente alla classe
	// Ad esempio:
	// Input: MySqlDriverDAO
	// Output: DriverDAO
	private static Object createDAO(Class<?> classObj){
		Object daoObject = null;
		try {
			daoObject = classObj.newInstance();
		} catch (InstantiationException e) {
			e.printStackTrace();
		} catch (IllegalAccessException e) {
			e.printStackTrace();
		}
		return daoObject;
	}

	
	
	
	//////////////////////////////////////////
	// Lista dei get per le varie interfacce:
	
	@Override
	public VehicleDAO getVehicleDAO() {
		return (VehicleDAO) createDAO(MySqlVehicleDAO.class);
	}


	@Override
	public DriverDAO getDriverDAO() {
		return (DriverDAO) createDAO(MySqlDriverDAO.class);
	}
	
	
	// ...
	
	
	
	
	
	
	// Altre funzioni utili...
	
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
	
	
}
