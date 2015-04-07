package transfersimulation.model.vehicle;

public class Trailer extends Vehicle {

	public Trailer(String plate) {
		super(plate);
		//setTipoVeicolo(TypeVehicle.Trailer);
	}
	

	@Override
	public String toString() {
	  return "TRAILER"; // assumes nombre is a string
	}
	
}
