package database2;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

public class DAO {
	
	private Connection conn;
	private Statement stmt;
	
	
	public static DAOFactory getDAO() {
		return DAOFactory.getDefaultDAOFactory();
	}
	
	
	public boolean connetti(String driverString, String connString){
		boolean connesso = false;
		try {
			Class.forName(driverString);
			try {
				this.conn = DriverManager.getConnection(connString);
				connesso = (!conn.isClosed());
			} catch (SQLException e){ e.printStackTrace(); }
		} catch (ClassNotFoundException e){
			e.printStackTrace();
		}
		return connesso; 
	}
	
	
	public ResultSet leggi(String query){
		ResultSet rs = null;
		try{
			this.stmt = conn.createStatement();
			rs = stmt.executeQuery(query);
		} catch (SQLException e){ e.printStackTrace(); }
		return rs;
	}
	
	
	public boolean chiudi(ResultSet rs){
		boolean chiuso = false;
		try {
			rs.close();
			//chiuso = rs.isClosed(); // TODO l'ho aggiunto io..
		} catch (SQLException e){ e.printStackTrace(); }
		return chiuso;
	}
	
}
