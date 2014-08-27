package database2.mysql;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

import database2.*;

public class MySqlProvinciaDAO implements ProvinciaDAO {
	protected static final String FIELDS_INSERT =
			"citta, regione, sigla";
	
	protected static final String FIELDS_RETURN = FIELDS_INSERT;
	
	protected static final String INSERT_SQL =
			"insert into Province ( " + FIELDS_INSERT +
			" ) " + "values ( ?, ?, ?) ";
	
	protected static final String SELECT_SQL =
			"select " + FIELDS_RETURN + " from Province where citta = ? ";
	
	protected static final String UPDATE_SQL =
			"update Province set citta = ?, regione = ?, sigla = ?" +
			"where citta = ? ";
	
	protected static final String DELETE_SQL =
			"delete from Province where citta = ? ";
	
	
	// *** METODI ***
	
	public MySqlProvinciaDAO() throws DAOException{}
	
	
	
	
	/*
	public Province findById(int id) throws DAOException {
		Province Province = null;
		try{
			Connection conn = getConnection();
			PreparedStatement prepStmt = conn.prepareStatement(SELECT_SQL);
			prepStmt.setInt(1, id);
			ResultSet rs = prepStmt.executeQuery();
			if (rs.next()){
				Province = new Province();
				Province.setID(id);
				Province.setCF(rs.getString(2));
				Province.setCognome(rs.getString(3));
				Province.setNome(rs.getString(4));
				Province.setQualifica(String.valueOf(rs.getString(5)));
				Province.setAutomunito(rs.getBoolean(6));
				prepStmt = conn.prepareStatement(SELECT_GIORNILIBERI_SQL);
				prepStmt.setInt(1, id);
				ResultSet rs2 = prepStmt.executeQuery();
				if (rs2.next()){
					Province.setGiorniLiberi(rs2.getBoolean(1),
											rs2.getBoolean(2),
											rs2.getBoolean(3),
											rs2.getBoolean(4),
											rs2.getBoolean(5),
											rs2.getBoolean(6));
				}
				rs2.close();
			} else
				throw new DAOException("Search Error. Province ID: " + id + " inesistente");
			rs.close();
		} catch (SQLException e){
			e.printStackTrace();
		}
		return Province;
	}
	
	
	public int insert(Province Province) throws DAOException{
		int ID = 0;
		try {
			Connection conn = getConnection();
			PreparedStatement prepStmt = conn.prepareStatement(INSERT_SQL, Statement.RETURN_GENERATED_KEYS);
			prepStmt.setString(1, Province.getCF());
			prepStmt.setString(2, Province.getCognome());
			prepStmt.setString(3, Province.getNome());
			prepStmt.setString(4, String.valueOf(Province.getQualifica()));
			prepStmt.setBoolean(5, Province.isAutomunito());
			prepStmt.execute();
			ResultSet rs = prepStmt.getGeneratedKeys();
			if (rs.next()){
				ID = rs.getInt(1);
				insertTurno(ID, Province.getGiorniLiberi());
			}
			rs.close();
			prepStmt.close();
			conn.close();
		} catch (SQLException e){
			e.printStackTrace();
		}
		return ID;
	}
	
	
	public void update(Province Province) throws DAOException{ // oppure ProvinceTO ???
		try{
			Connection conn = getConnection();
			PreparedStatement prepStmt = conn.prepareStatement(UPDATE_SQL);
			prepStmt.setString(1, Province.getCF());
			prepStmt.setString(2, Province.getCognome());
			prepStmt.setString(3, Province.getNome());
			prepStmt.setString(4, String.valueOf(Province.getQualifica()));
			prepStmt.setBoolean(5, Province.isAutomunito());
			prepStmt.setInt(6, Province.getID());
			int rowCount = prepStmt.executeUpdate();
			prepStmt.close();
			conn.close();
			if (rowCount == 0)
				throw new DAOException("Update Error. Province ID: " + Province.getID());
			//GiorniLiberi e qui?
		} catch (SQLException e){
			e.printStackTrace();
		}
	}
	
	
	public void delete(int id) throws DAOException{
		try {
			Connection conn = getConnection();
			PreparedStatement prepStmt = conn.prepareStatement(DELETE_SQL);
			prepStmt.setInt(1, id);
			prepStmt.executeUpdate();
			prepStmt.close();
			conn.close();
		} catch (SQLException e){
			throw new DAOException("Delete Error. Province ID: " + id);
		}
	}
	
	public boolean existCF(String value){
		return MySqlDAOFactory.exist("Province", "CF", value);
	}
	
	public boolean existCF(int id, String value){
		return MySqlDAOFactory.exist("Province", id, "CF", value);
	}
	
	
	public List<Province> getAddetti() throws DAOException{
		List<Province> lista_addetti=null;
		try {
			Connection conn = getConnection();
			PreparedStatement prepStmt = conn.prepareStatement("Select id from Province order by id");
			ResultSet rs = prepStmt.executeQuery();
			lista_addetti = new ArrayList<Province>();
			while (rs.next())
				lista_addetti.add(findById(rs.getInt(1)));
			rs.close();
			prepStmt.close();
			conn.close();
		} catch (SQLException e){
			e.printStackTrace();
		}
		return lista_addetti;
	}
	
	
	public void insertTurno(int id, boolean turno[]) throws DAOException{
		try {
			Connection conn = getConnection();
			PreparedStatement prepStmt = conn.prepareStatement(INSERT_GIORNILIBERI_SQL);
			prepStmt.setInt(1, id);
			prepStmt.setBoolean(2, turno[0]);
			prepStmt.setBoolean(3, turno[1]);
			prepStmt.setBoolean(4, turno[2]);
			prepStmt.setBoolean(5, turno[3]);
			prepStmt.setBoolean(6, turno[4]);
			prepStmt.setBoolean(7, turno[5]);
			prepStmt.execute();
			prepStmt.close();
			conn.close();
		} catch (SQLException e){
			e.printStackTrace();
		}
	}
	
	
	public void updateTurno(int id, boolean turno[]) throws DAOException{
		try{
			Connection conn = getConnection();
			PreparedStatement prepStmt = conn.prepareStatement(UPDATE_GIORNILIBERI_SQL);
			prepStmt.setBoolean(1, turno[0]);
			prepStmt.setBoolean(2, turno[1]);
			prepStmt.setBoolean(3, turno[2]);
			prepStmt.setBoolean(4, turno[3]);
			prepStmt.setBoolean(5, turno[4]);
			prepStmt.setBoolean(6, turno[5]);
			prepStmt.setInt(7, id);
			int rowCount = prepStmt.executeUpdate();
			prepStmt.close();
			conn.close();
			if (rowCount == 0)
				throw new DAOException("Update Error. Province ID: " + id);
		} catch (SQLException e){
			e.printStackTrace();
		}
	}
	
	
	public Connection getConnection(){
		return DAOFactory.getDefaultDAOFactory().getConnection();
	}
	
	*/
	
	
	
	public static void main(String args[]){
		
		
		try {
			MySqlProvinciaDAO a_dao = new MySqlProvinciaDAO();
			/*
			 * Province a1, a2, a3, a4;
			
			a1 = a_dao.findById(000001);
			System.out.println(a1);
			a2 = a_dao.findById(000002);
			System.out.println(a2);
			a3 = a_dao.findById(000003);
			System.out.println(a3);
			a4 = a_dao.findById(000004);
			System.out.println(a4);
			//a4 = a_dao.find(000005);
			//System.out.println(a4);
			
			a1.setCognome("SEEEE");
			a_dao.update(a1);
			System.out.println("\n"+a_dao.findById(000001));
			a1.setCognome("Doronzo");
			a_dao.update(a1);
			System.out.println(a_dao.findById(000001));
			*/
			
			//System.out.println(a_dao.getProvince());
			
		} catch (DAOException e) {
			e.printStackTrace();
		}
		
	}
	
	
	
	
}