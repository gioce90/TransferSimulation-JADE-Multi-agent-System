package transfersimulation.model.vehicle;

public class Van extends Vehicle implements DrivingPart {

	public Van(String plate) {
		super(plate);
	}
	
	@Override
	public String getPlate() {
		return super.getPlate();
	}
	
	
	@Override
	public String toString() {
	  return "VAN"; // assumes nombre is a string
	}
}
