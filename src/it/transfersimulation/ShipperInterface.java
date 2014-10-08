package it.transfersimulation;

public interface ShipperInterface {
	void newTruck(String targa);
	void removeTruck(String targa);
	void activateTruck(String targa);
	void deactivateTruck(String targa);
}
