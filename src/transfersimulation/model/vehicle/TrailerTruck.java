package transfersimulation.model.vehicle;

public class TrailerTruck extends Vehicle {
	
	// TRAILER TRUCK: autocarro
	
	// DrivingPart: Car, Van, Truck
	// Trailer (the DRIVEN part is always a trailer)
	
	String plateFront;
	String plateTrailer;
	
	DrivingPart drivingVehicle;
	Trailer trailerVehicle;
	
	public TrailerTruck(DrivingPart drivingVehicle, Trailer trailerVehicle) {
		plateFront=drivingVehicle.getPlate();
		plateTrailer=trailerVehicle.getPlate();
		setPlate(plateFront+" - "+plateTrailer);
		
		this.drivingVehicle=drivingVehicle;
		this.trailerVehicle=trailerVehicle;
	}
	
	@Override
	public String getAllestimento() {
		return drivingVehicle.getAllestimento()
				+", "+trailerVehicle.getAllestimento();
	}
	
	@Override
	public float getCarryingCapacity() {
		return trailerVehicle.getCarryingCapacity();
	}
	
}
