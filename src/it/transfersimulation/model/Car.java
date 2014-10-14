package it.transfersimulation.model;

public class Car extends Vehicle {

	public Car(String plate) {
		super(plate);
		//setTipoVeicolo(Car.class);
	}
	
	@Override
	public String toString() {
	  return "CAR"; // assumes nombre is a string
	}
}
