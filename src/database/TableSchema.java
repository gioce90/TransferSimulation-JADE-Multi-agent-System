package database;

import java.sql.Connection;
import java.sql.DatabaseMetaData;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 *  <p>Title: TableSchema</p>
 *  <p>Copyright: Copyright (c) 2014</p>
 *  <p>Class description: Classe che modella lo schema della tabella</p>
 *  @author Gioacchino Piazzolla
 *  @version 1.0
 */
public class TableSchema {
	
	/**
	 *  <p>Title: Column</p>
	 *  <p>Copyright: Copyright (c) 2014</p>
	 *  <p>Class description: Classe interna che modella una colonna della tabella</p>
	 *  @author Gioacchino Piazzolla
	 *  @version 1.0
	 */
	public class Column {
		private String name;
		private String type;
		
		/**
		 * Costruttore della colonna
		 * @param name Nome della colonna
		 * @param type Tipo di colonna (number o string)
		 */
		Column(String name, String type){
			this.name=name;
			this.type=type;
		}
		
		/**
		 * @return Nome della colonna
		 */
		public String getColumnName(){
			return name;
		}
		
		/**
		 * Ritorna TRUE se la colonna è di tipo numerico, FALSE altrimenti
		 * @return TRUE o FALSE
		 */
		public boolean isNumber(){
			return type.equals("number");
		}
		
		/**
		 * Pubblica "nome_colonna: tipo_colonna"
		 */
		public String toString(){
			return name + ": " + type;
		}
	}
	
	List<Column> tableSchema = new ArrayList<Column>();
	
	/**
	 * Costruttore dello schema della tabella. 
	 * <BR>Modella una lista di colonne della tabella.
	 * @param tableName Nome tabella
	 * @throws SQLException
	 */
	public TableSchema(String tableName) throws SQLException{
		HashMap<String,String> mapSQL_JAVATypes=new HashMap<String, String>();
		//http://java.sun.com/j2se/1.3/docs/guide/jdbc/getstart/mapping.html
		mapSQL_JAVATypes.put("CHAR","string");
		mapSQL_JAVATypes.put("VARCHAR","string");
		mapSQL_JAVATypes.put("LONGVARCHAR","string");
		mapSQL_JAVATypes.put("BIT","string");
		mapSQL_JAVATypes.put("SHORT","number");
		mapSQL_JAVATypes.put("INT","number");
		mapSQL_JAVATypes.put("LONG","number");
		mapSQL_JAVATypes.put("FLOAT","number");
		mapSQL_JAVATypes.put("DOUBLE","number");
		
		Connection con = DbAccess.getConnection();
		DatabaseMetaData meta = con.getMetaData();
	    ResultSet res = meta.getColumns(null, null, tableName, null);
		 
	    while (res.next()) {
	    	if (mapSQL_JAVATypes.containsKey(res.getString("TYPE_NAME")))
	    		tableSchema.add(new Column(
	    						res.getString("COLUMN_NAME"),
	    						mapSQL_JAVATypes.get(res.getString("TYPE_NAME"))
	    					)
	    		);
	     }
	     
	     res.close();
	}
	
	/**
	 * @return Numero attributi dello schema
	 */
	public int getNumberOfAttributes(){
		return tableSchema.size();
	}
	
	/**
	 * Restituisce una colonna indicando l'indice
	 * @param index Indice colonna
	 * @return Column
	 */
	public Column getColumn(int index){
		return tableSchema.get(index);
	}
	
}