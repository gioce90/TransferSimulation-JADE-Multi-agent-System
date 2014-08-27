package database;

/**
 *  <p>Title: NoValueException</p>
 *  <p>Copyright: Copyright (c) 2014</p>
 *  <p>Class description: Classe che modella l'eccezione NoValueException.
 *  <BR>È sollevata nel caso si tentasse di modellare un resultset privo di valori</p>
 *  @author Gioacchino Piazzolla
 *  @version 1.0
 */
@SuppressWarnings("serial")
public class NoValueException extends Exception{
	
	public NoValueException(){}
	
	public NoValueException(String msg){
		super(msg);
	}
	
}
