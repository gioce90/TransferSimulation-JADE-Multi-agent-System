package it.transfersimulation;

import it.transfersimulation.model.Vehicle;

public interface ShipperInterface {
	void newTruck(String targa);
	void removeTruck(String targa);
	void activateTruck(String targa);
	void deactivateTruck(String targa);
	
	void newTruck(Vehicle vehicle);
	void removeTruck(Vehicle vehicle);
	void activateTruck(Vehicle vehicle);
	void deactivateTruck(Vehicle vehicle);
}
