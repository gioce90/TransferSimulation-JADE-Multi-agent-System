package it.transfersimulation.model;

public class SemiTrailerTruck extends Vehicle {

	String plateRoadTruck;
	String plateSemiTrailer;
	
	public SemiTrailerTruck(RoadTractor roadTruck, SemiTrailer semiTrailer) {
		plateRoadTruck=roadTruck.getPlate();
		plateSemiTrailer=semiTrailer.getPlate();
		//super(plate);
		//setTipoVeicolo(TypeVehicle.SemiTrailerTruck);
		
	}

	// TODO tutti i settaggi automatici dovuti alla unione di due veicoli.
}
