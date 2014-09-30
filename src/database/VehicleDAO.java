package database;

import it.transfersimulation.Vehicle;

import java.util.List;

public interface VehicleDAO {
	public List<Vehicle> getAllVehicles();
	public Vehicle getVehicle(int vehicle);
	public void insertVehicle(Vehicle vehicle);
	public void updateVehicle(Vehicle vehicle);
	public void deleteVehicle(Vehicle vehicle);
	
	// aggiungi quelli che vuoi...
	
	/*
	public Province findById(int addetto_ID) throws DAOException;
	public int insert(Province addetto) throws DAOException;
	public void update(Province addetto) throws DAOException;
	public void delete(int addetto_ID) throws DAOException;
	public boolean existCF(String value);
	public boolean existCF(int id, String value);
	public List<Province> getAddetti() throws DAOException;
	public void insertTurno(int id, boolean turno[]) throws DAOException;
	public void updateTurno(int id, boolean turno[]) throws DAOException;
	*/
	
	
}
