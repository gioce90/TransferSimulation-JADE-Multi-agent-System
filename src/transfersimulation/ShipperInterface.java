package transfersimulation;

import java.util.List;

import transfersimulation.model.vehicle.Vehicle;

public interface ShipperInterface {
	List<Vehicle> getVehicles(); 
	
	void newTruck(Vehicle vehicle);
	void removeTruck(Vehicle vehicle);
	void activateTruck(Vehicle vehicle);
	void deactivateTruck(Vehicle vehicle);
	
	
}
