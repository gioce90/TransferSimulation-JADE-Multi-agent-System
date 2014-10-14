package it.transfersimulation;

import it.transfersimulation.model.Car;
import it.transfersimulation.model.RoadTractor;
import it.transfersimulation.model.SemiTrailer;
import it.transfersimulation.model.SemiTrailerTruck;
import it.transfersimulation.model.Trailer;
import it.transfersimulation.model.TrailerTruck;
import it.transfersimulation.model.Truck;
import it.transfersimulation.model.Van;
import it.transfersimulation.model.Vehicle;

import javax.swing.ImageIcon;

public class VehicleUtils {
	
	
	// found the right image
	protected static ImageIcon findImageByColumnCarType(Class<? extends Vehicle> type) {
		ImageIcon i = null;
		if (type.equals(Car.class))						// auto
			i = new ImageIcon(VehicleTableModel.class.getResource("/images/vehicles/car_32.png"));
		else if (type.equals(Van.class))				// furgone
			i = new ImageIcon(VehicleTableModel.class.getResource("/images/vehicles/van_32.png"));
		else if (type.equals(Truck.class))				// autocarro
			i = new ImageIcon(VehicleTableModel.class.getResource("/images/vehicles/truck_32.png"));
		else if (type.equals(TrailerTruck.class))		// autotreno
			i = new ImageIcon(VehicleTableModel.class.getResource("/images/vehicles/trailertruck_32.png"));
		else if (type.equals(SemiTrailer.class))		// semirimorchio
			i = new ImageIcon(VehicleTableModel.class.getResource("/images/vehicles/semitrailer_32.png"));
		else if (type.equals(RoadTractor.class))		// trattore stradale
			i = new ImageIcon(VehicleTableModel.class.getResource("/images/vehicles/roadtractor_32.png"));
		
		else if (type.equals(SemiTrailerTruck.class))	// autoarticolato
			i = new ImageIcon(VehicleTableModel.class.getResource("/images/vehicles/semitrailertruck_32.png"));
		else if (type.equals(Trailer.class))			// rimorchio
			i = new ImageIcon(VehicleTableModel.class.getResource("/images/vehicles/semitrailer_32.png"));
		
		return i;
	}
	
	
	protected static String findStringByColumnCarType(Class<? extends Vehicle> type) {
		String i = "?";
		if (type.equals(Car.class))
			i = "Automobile";
		else if (type.equals(Van.class))
			i = "Furgone";
		else if (type.equals(Truck.class))
			i = "Autocarro";
		else if (type.equals(TrailerTruck.class))
			i = "Autotreno"; 
		else if (type.equals(SemiTrailerTruck.class))
			i = "Autoarticolato";
		else if (type.equals(RoadTractor.class))
			i = "Trattore stradale";
		else if (type.equals(Trailer.class))
			i = "Rimorchio";
		else if (type.equals(SemiTrailer.class))
			i = "Semirimorchio";
		return i;
	}
	
}
