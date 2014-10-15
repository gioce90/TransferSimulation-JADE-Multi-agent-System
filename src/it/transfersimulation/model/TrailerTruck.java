package it.transfersimulation.model;

public class TrailerTruck extends Vehicle {

	// TRAILER TRUCK: autocarro
	
	// DrivingPart: Car, Van, Truck
	// Trailer (the DRIVEN part is always a trailer)
	
	String plateFront;
	String plateTrailer;
	
	public TrailerTruck(DrivingPart frontVehicle, Trailer trailerVehicle) {
		plateFront=frontVehicle.getPlate();
		plateTrailer=trailerVehicle.getPlate();
		setPlate(plateFront+" - "+plateTrailer);
	}
	
	
}
