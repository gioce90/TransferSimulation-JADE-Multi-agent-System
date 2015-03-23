package databaseEX;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;


/**
 *  <p>Title: DbAccess</p>
 *  <p>Copyright: Copyright (c) 2014</p>
 *  <p>Class description: Classe che modella l'accesso a un database mysql.
 *  @author Gioacchino Piazzolla
 *  @version 1.0
 */
public class DbAccess {
	static String DRIVER_CLASS_NAME = "org.gjt.mm.mysql.Driver"; /* Per utilizzare questo Driver scaricare e aggiungere al classpath
	 																il connettore mysql connector)*/
	private static final String DBMS = "jdbc:mysql";
	private static final String SERVER="localhost"; // identificativo del server su cui risiede il DB
	private static String DATABASE = "STSN_MAIN"; 		// nome della base di dati (di default AprioriDB)
	private static final String PORT = "3306"; 		// porta sulla quale il DBMS MySQL accetta le connessioni
	private static final String USER_ID = "AdminUser"; 	// contiene il nome dell’utente per l’accesso al DB
	private static final String PASSWORD = "psw"; 	// contiene la password di autenticazione
	private static Connection conn; 				// gestisce una connessione al DB
	
	
	/**
	 * Inizializza la connessione al database
	 * @throws DatabaseConnectionException È sollevata nel caso:
	 * <BR> - Non sia trovato il driver manager
	 * <BR> - Errore di connessione al db
	 */
	public static void initConnection() throws DatabaseConnectionException{
		try {
			Class.forName(DRIVER_CLASS_NAME);
			String URL = DBMS+"://" + SERVER + ":" + PORT + "/" + DATABASE;
			conn = DriverManager.getConnection(URL, USER_ID, PASSWORD);
		} catch (ClassNotFoundException e) {
			throw new DatabaseConnectionException("Driver Manager non trovato");
		} catch (SQLException e) {
			throw new DatabaseConnectionException("Errore di connessione (comandi SQL errati");
		}
	}
	
	/**
	 * Inizializza la connessione a un database scelto dall'utente
	 * @param db Nome database scelto dall'utente
	 * @throws DatabaseConnectionException È sollevata nel caso:
	 * <BR> - Non sia trovato il driver manager
	 * <BR> - Errore di connessione al db
	 */
	public static void initConnection(String db) throws DatabaseConnectionException{
			DATABASE = db;
			initConnection();
	}
	
	/**
	 * @return Restituisce la connessione al database di default
	 */
	public static Connection getConnection(){
		try {
			DbAccess.initConnection();
		} catch (DatabaseConnectionException e) {
			e.printStackTrace();
		}
		return conn;
	}
	
	/**
	 * @param db Nome database scelto dall'utente
	 * @return Restituisce la connessione al database scelto
	 */
	public static Connection getConnection(String db){
		try {
			DbAccess.initConnection(db);
		} catch (DatabaseConnectionException e) {
			e.printStackTrace();
		}
		return conn;
	}
	
	/**
	 * Chiude la connessione
	 */
	public static void closeConnection(){
		try {
			conn.close();
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}
	
}
