package transfersimulation.model.vehicle;

public class RoadTractor extends Vehicle {

	public RoadTractor(String plate) {
		super(plate);
		//setTipoVeicolo(TypeVehicle.RoadTractor);
	}
	
	@Override
	public String toString() {
	  return "ROADTRACTOR"; // assumes nombre is a string
	}
}
