package transfersimulation.model.vehicle;

public class Truck extends Vehicle implements DrivingPart {
	
	public Truck(String plate) {
		super(plate);
	}
	
	@Override
	public String getPlate() {
		return super.getPlate();
	}
	
	@Override
	public String toString() {
	  return "TRUCK"; // assumes nombre is a string
	}
}
