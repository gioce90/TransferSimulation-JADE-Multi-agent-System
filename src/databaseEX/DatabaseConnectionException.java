package databaseEX;

/**
 *  <p>Title: DatabaseConnectionException</p>
 *  <p>Copyright: Copyright (c) 2014</p>
 *  <p>Class description: Classe che modella l'eccezione DatabaseConnectionException.
 *  <BR>È sollevata nel caso la connessione con il database fallisse</p>
 *  @author Gioacchino Piazzolla
 *  @version 1.0
 */
@SuppressWarnings("serial")
public class DatabaseConnectionException extends Exception{
	
	public DatabaseConnectionException(){}
	
	public DatabaseConnectionException(String msg){
		super(msg);
	}
	
}
