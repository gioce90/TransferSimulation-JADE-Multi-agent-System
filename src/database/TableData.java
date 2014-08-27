package database;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import database.TableSchema.Column;

/**
 *  <p>Title: TableData</p>
 *  <p>Copyright: Copyright (c) 2014</p>
 *  <p>Class description: Classe che modella una tabella di pattern, importandola dal database.</p>
 *  @author Gioacchino Piazzolla
 *  @version 1.0
 */
public class TableData {
	
	/**
	 *  <p>Title: TupleData</p>
	 *  <p>Class description: Inner-class di TableData che modella una lista di tuple.
	 *  @author Gioacchino Piazzolla
	 *  @version 1.0
	 */
	public class TupleData {
		public List<Object> tuple = new ArrayList<Object>();
		
		/**
		 * Metodo che pubblica tutti i valori assunti dalle tuple
		 */
		public String toString(){
			String value="";
			Iterator<Object> it = tuple.iterator();
			while(it.hasNext())
				value+= (it.next().toString() + " ");
			return value;
		}
	}
	
	
	public TableData() {}
	
	
	/**
	 * @return Restituisce l'elenco dei database
	 * @throws SQLException
	 */
	public static ResultSet getDatabases() throws SQLException {
		Statement s = DbAccess.getConnection().createStatement();
		ResultSet ris = s.executeQuery("SHOW DATABASES");
		return ris;
	}
	
	
	/**
	 * Restituisce l'elenco delle tabelle di un particolare database
	 * @param db Nome del database
	 * @return Elenco databases
	 * @throws SQLException
	 */
	public static ResultSet getTables(String db) throws SQLException{
		Statement s = DbAccess.getConnection(db).createStatement();
		ResultSet ris = s.executeQuery("SHOW TABLES");
		return ris;
	}
	
	
	/**
	 * Restituisce le transazioni di una specifica tabella sottoforma di lista di tuple
	 * @param table Nome della tabella del database
	 * @return Lista di tuple
	 * @throws SQLException
	 */
	public List<TupleData> getTransazioni(String table) throws SQLException  {
		List<TupleData> lista_tuple = new ArrayList<TupleData>();
		TableSchema schema = new TableSchema(table);
		Statement s = DbAccess.getConnection().createStatement();
		
		ResultSet ris = s.executeQuery("SELECT * FROM " + table);
		
		TupleData tupla;
		while(ris.next()){ //ciclo sulle tuple
			tupla = new TupleData();
			for (int i=0; i<schema.getNumberOfAttributes(); i++){//ciclo sulle colonne
				if(schema.getColumn(i).isNumber())//se la colonna i-esima contiene un valore numerico
					tupla.tuple.add(ris.getFloat(schema.getColumn(i).getColumnName()));//aggiungi alla tupla corrente il valore numerico
				else//la colonna contiene valori stringa
					tupla.tuple.add(ris.getString(schema.getColumn(i).getColumnName())); //aggiungi alla tupla corrente il valore stringa
			} //fine for
				lista_tuple.add(tupla);//Aggiungi la tupla appena scandita
		}
		ris.close();
		s.close();
		
		return lista_tuple; //ritorna la lista di tuple
	}
	
	/**
	 * Restituisce lista ordinata di distinti valori che può assumere un attributo
	 * @param table Nome tabella in cui ricercare
	 * @param column Colonna in cui ricercare
	 * @return Lista ordinata
	 * @throws SQLException
	 */
	public List<Object>getDistinctColumnValues(String table, Column column) throws SQLException{
		List<Object> lista_valori = new ArrayList<Object>();
		Statement s = DbAccess.getConnection().createStatement();
		ResultSet ris = s.executeQuery("SELECT DISTINCT " + column.getColumnName() + " FROM " + table + " ORDER BY " +  column.getColumnName());
		
		if(column.isNumber())
			while(ris.next())
				lista_valori.add(ris.getFloat(column.getColumnName()));
		else
			while(ris.next())
				lista_valori.add(ris.getString(column.getColumnName()));
		
		ris.close();
		s.close();
		return lista_valori;
	}
	
	/**
	 * Restituisce il valore aggregato
	 * @param table Nome tabella in cui ricercare
	 * @param column Colonna in cui ricercare
	 * @param aggregate Min o Max
	 * @return valore aggregato
	 * @throws SQLException
	 * @throws NoValueException Se il resultset è vuoto o il valore calcolato è pari a null
	 */
	public Object getAggregateColumnValue(String table, Column column, QUERY_TYPE aggregate)
	throws SQLException, NoValueException {
		Object o = new Object();
		Statement s = DbAccess.getConnection().createStatement();
		ResultSet ris = s.executeQuery("SELECT " + aggregate + "(" + column.getColumnName() + ") AS "
				+ column.getColumnName() + " FROM " + table);
		
		if (ris == null || aggregate == null)
			throw new NoValueException("Nessun valore");
		
		ris.first();
		if(column.isNumber())
			o = ris.getFloat(column.getColumnName());
		else
			o = ris.getString(column.getColumnName());
		
		ris.close();
		s.close();
		
		return o;
	}
	
}
