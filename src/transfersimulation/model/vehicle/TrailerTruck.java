package transfersimulation.model.vehicle;


public class TrailerTruck extends Vehicle {
	
	// TRAILER TRUCK: autocarro
	
	// DrivingPart: Car, Van, Truck
	// Trailer (the DRIVEN part is always a trailer)
	
	
	DrivingPart drivingVehicle;
	Trailer trailerVehicle;
	
	public TrailerTruck(DrivingPart drivingVehicle, Trailer trailerVehicle) {
		this.drivingVehicle=drivingVehicle;
		this.trailerVehicle=trailerVehicle;
	}
	
	@Override
	public String getPlate() {
		return drivingVehicle.getPlate()+" - "+trailerVehicle.getPlate();
	}
	
	@Override
	public String getAllestimento() {
		String s="";
		if (drivingVehicle.getAllestimento()!=""){
			s+=drivingVehicle.getAllestimento();
			if (trailerVehicle.getAllestimento()!="")
				s+=", "+trailerVehicle.getAllestimento();
		} else if (trailerVehicle.getAllestimento()!="")
			s+=trailerVehicle.getAllestimento();
		return s;
	}
	
	@Override
	public float getCarryingCapacity() {
		return ((Vehicle) drivingVehicle).getCarryingCapacity()+trailerVehicle.getCarryingCapacity();
	}
	
	@Override
	public float getPtt() {
		return ((Vehicle) drivingVehicle).getPtt()+trailerVehicle.getPtt();
	}
	
	@Override
	public float getVolume() {
		return ((Vehicle) drivingVehicle).getVolume()+trailerVehicle.getVolume();
	}
	
	@Override
	public String getMark() {
		return ((Vehicle) drivingVehicle).getMark()+" - "+trailerVehicle.getMark();
	}
	
	@Override
	public Stato getState() {
		if ( ((Vehicle)drivingVehicle).getState().equals(Stato.DISPONIBILE)
		&& ((Vehicle)trailerVehicle).getState().equals(Stato.DISPONIBILE))
		return Stato.DISPONIBILE;
		else return Stato.NON_DISPONIBILE;
	}
	
	
	@Override
	public String toString() {
	  return "TRAILERTRUCK"; // assumes nombre is a string
	}
	
}
