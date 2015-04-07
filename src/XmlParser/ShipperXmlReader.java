package XmlParser;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.Vector;

import org.jdom2.Document;
import org.jdom2.Element;
import org.jdom2.JDOMException;
import org.jdom2.input.SAXBuilder;

import transfersimulation.model.vehicle.*;
import transfersimulation.model.vehicle.Vehicle.Stato;


public class ShipperXmlReader {
	
	HashMap<String, Vehicle> map = new HashMap<String, Vehicle>();
	Vector<Vehicle> vehicles = new Vector<Vehicle>();
	
	
	public ShipperXmlReader(String file) throws JDOMException, IOException {
	
			
			// Creo un SAXBuilder e con esco costruisco un document 
			SAXBuilder builder = new SAXBuilder(); 
			Document document = builder.build(new File(file+".xml")); 
			
			// Prendo la radice 
			Element root = document.getRootElement();
			
			simpleVehicles(root);
			complexVehicles(root);
	}
	
	
	
	private void simpleVehicles (Element fleet){
		for (Element vehicleElem : fleet.getChildren()) {
			String kindVehicle = vehicleElem.getName();
			String plate = vehicleElem.getChildText("plate");
			Vehicle v = null;
			switch (kindVehicle){
				case "Car":
					v = new Car(plate);
					break;
				case "Van":
					v = new Van(plate);
					break;
				case "Truck":
					v = new Truck(plate);
					break;
				case "Trailer":
					v = new Trailer(plate);
					break;
				case "RoadTractor":
					v = new RoadTractor(plate);
					break;
				case "SemiTrailer":
					v = new SemiTrailer(plate);
					break;
			} // chiusura switch
			if (v!=null){
				String id = vehicleElem.getAttributeValue("id");
				settingVehicle(vehicleElem, v);
				map.put(id, v);
				vehicles.add(v);
			}
		} // chiusura ciclo for
	}
	
	
	
	private void complexVehicles(Element fleet){
		
		// TrailerTruck
		for (Element trailerTruckElement : fleet.getChildren("TrailerTruck")) {
			Element drivingElement = trailerTruckElement.getChildren().get(0);
			Vehicle drivingVehicle = null;
			
			switch (drivingElement.getName()){
				case "Car":
					drivingVehicle = (Car) map.get(drivingElement.getAttributeValue("refid"));
					break;
				case "Van":
					drivingVehicle = (Van) map.get(drivingElement.getAttributeValue("refid"));
					break;
				case "Truck":
					drivingVehicle = (Truck) map.get(drivingElement.getAttributeValue("refid"));
					break;
			}
			
			Element drivenElement = trailerTruckElement.getChildren().get(1);
			Trailer drivenVehicle = (Trailer) map.get(drivenElement.getAttributeValue("refid"));
			
			if (drivingVehicle!=null&&drivenVehicle!=null){
				String id = trailerTruckElement.getAttributeValue("id");
				TrailerTruck trailerTruckVehicle = new TrailerTruck((DrivingPart) drivingVehicle, drivenVehicle);
				map.put(id, trailerTruckVehicle);
				vehicles.add(trailerTruckVehicle);
			}
		}
		
		
		// SemiTrailerTruck
		for (Element semiTrailerTruckElement : fleet.getChildren("SemiTrailerTruck")) {
			Element drivingElement = semiTrailerTruckElement.getChild("RoadTractor");
			RoadTractor drivingVehicle = (RoadTractor) map.get(drivingElement.getAttributeValue("refid"));
			Element drivenElement = semiTrailerTruckElement.getChild("SemiTrailer");
			SemiTrailer drivenVehicle = (SemiTrailer) map.get(drivenElement.getAttributeValue("refid"));
			
			if (drivingVehicle!=null&&drivenVehicle!=null){
				String id = semiTrailerTruckElement.getAttributeValue("id");
				SemiTrailerTruck semiTrailerTruckVehicle = new SemiTrailerTruck(drivingVehicle, drivenVehicle);
				map.put(id, semiTrailerTruckVehicle);
				vehicles.add(semiTrailerTruckVehicle);
			}
		}
		
	}
	
	
	
	private void settingVehicle(Element element, Vehicle vehicle){
		vehicle.setPlate(element.getChildText("plate"));
		vehicle.setMark(element.getChildText("mark"));
		vehicle.setModel(element.getChildText("model"));
		vehicle.setTrim(element.getChildText("trim"));
		vehicle.setLocazioneAttuale(element.getChildText("locazioneAttuale"));
		vehicle.setAllestimento(element.getChildText("allestimento"));
		
		if (element.getChildText("stato").equals("DISPONIBILE"))
			vehicle.setStato(Stato.DISPONIBILE);
		else vehicle.setStato(Stato.NON_DISPONIBILE);

		vehicle.setCarryingCapacity(Float.valueOf(
				element.getChildText("carryingCapacity")));
		vehicle.setPtt(Float.valueOf(
				element.getChildText("ptt")));
		vehicle.setWeight(Float.valueOf(
				element.getChildText("weight")));
		vehicle.setVolume(Float.valueOf(
				element.getChildText("volume")));
		vehicle.setLength(Float.valueOf(
				element.getChildText("length")));
		vehicle.setHeight(Float.valueOf(
				element.getChildText("height")));
		vehicle.setWidth(Float.valueOf(
				element.getChildText("width")));
	}
	
	
	
	public Vector<Vehicle> getVehicles() {
		return vehicles;
	}
	
	
	
	public static void main(String[] args) throws JDOMException, IOException { 
		ShipperXmlReader reader = new ShipperXmlReader("Shipper1");
	}
	
	
	
}