package database;

import java.sql.Connection;

import database.mysql.MySqlDAOFactory;


public abstract class DAOFactory {
	
	// List of DAO types supported by the factory
	public static final int MYSQL = 0;
	public static final int CLOUDSCAPE = 1;
	public static final int ORACLE = 2;
	public static final int XML = 3;
	//...
	
	
	// There will be a method for each DAO that can be created.
	// The concrete factories will have to implement these methods.
	public abstract VehicleDAO getVehicleDAO();
	public abstract DriverDAO getDriverDAO();
	//...
	
	
	public abstract Connection getConnection();
	
	
	public static DAOFactory getDAOFactory(int whichFactory) {
		switch (whichFactory) {
			case MYSQL:
				return new MySqlDAOFactory();
			case CLOUDSCAPE:
				//return new CloudscapeDAOFactory();
			case ORACLE    : 
				//return new OracleDAOFactory();
			case XML    : 
				//return new XMLDAOFactory();
			default           : 
				return null;
		}
	}
	
	
	public static DAOFactory getDefaultDAOFactory(){
		///////////////////////////////////////////////////////////////
		// Se si volesse cambiare tipo di database, cambiare qui sotto.
		// Di default usiamo MySQL
		return getDAOFactory(MYSQL);
	}
	
	
}
