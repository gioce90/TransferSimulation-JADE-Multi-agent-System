package database2;

import java.sql.Connection;

import database2.mysql.MySqlDAOFactory;


public abstract class DAOFactory {
	
	public interface StorageType {
		//Lista di tipi di Database supportati:
		public static final int MYSQL = 1;
		public static final int ORACLE = 2;
		// ...
	}
	
	//static String DATASOURCE_DB_NAME = "java:comp/env/jdbc/STSN_MAIN";
	
	public static int DB_SCELTO = 1;
	
	public abstract Connection getConnection();
	
	public static DAOFactory getDAOFactory(int whichFactory){
		switch (whichFactory){
			case StorageType.MYSQL:
				return new MySqlDAOFactory();
			case StorageType.ORACLE:
				return null;
			// . . . altri?
			default: return new MySqlDAOFactory();
		}
	}
	
	public static DAOFactory getDefaultDAOFactory(){
		return DAOFactory.getDAOFactory(DB_SCELTO);
	}
	
	/*
	public static DAOFactory changeDefaultDAOFactory( int change ){
		DB_SCELTO = change;
		return DAOFactory.getDAOFactory(DB_SCELTO);
	}
	*/
	
	// Metodi astratti per creare i DAO.
	// Le factory concrete li implementaranno.
	public abstract ProvinciaDAO getProvinciaDAO() throws DAOException;
	// public abstract AutistaDAO getAutistaDAO() throws DAOException;
	//...
	
	
}
