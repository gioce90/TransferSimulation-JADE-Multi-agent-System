package it.transfersimulation.model;

public class Car extends Vehicle implements DrivingPart {

	public Car(String plate) {
		super(plate);
	}
	
	@Override
	public String toString() {
	  return "CAR"; // assumes nombre is a string
	}
	
	@Override
	public String getPlate() {
		return super.getPlate();
	}
	
}
