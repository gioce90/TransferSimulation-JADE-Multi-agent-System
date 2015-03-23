package database;

import it.transfersimulation.Driver;

import java.util.List;

public interface DriverDAO {
	
	public List<Driver> getAllDrivers();
	public Driver getDriver(int driver);
	public void insertDriver(Driver driver);
	public void updateDriver(Driver driver);
	public void deleteDriver(Driver driver);
	
	// aggiungi quelli che vuoi...
}
