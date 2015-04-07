package transfersimulation.model.vehicle;

public class SemiTrailer extends Vehicle {

	public SemiTrailer(String plate) {
		super(plate);
		//setTipoVeicolo(TypeVehicle.SemiTrailer);
	}
	
	@Override
	public String toString() {
	  return "SEMITRAILER"; // assumes nombre is a string
	}
	
}
