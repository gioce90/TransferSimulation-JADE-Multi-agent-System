package database;

public class DAOException extends Exception{
	private static final long serialVersionUID = 1L;

	DAOException(){}
	
	public DAOException(String msg){
		super(msg);
	}
	
	DAOException(String msg, Exception e){
		super(msg);
		e.printStackTrace();
	}
	
}
