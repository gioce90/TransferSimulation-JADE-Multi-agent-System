package XmlParser;

import java.io.File;
import java.util.Iterator;
import java.util.List;

import org.jdom2.Content;
import org.jdom2.Document;
import org.jdom2.Element;
import org.jdom2.input.SAXBuilder;

import transfersimulation.model.vehicle.Vehicle;

public class ShipperXmlReader {
	
	public ShipperXmlReader(String file) {
		try { 
			// Creo un SAXBuilder e con esco costruisco un document 
			SAXBuilder builder = new SAXBuilder(); 
			Document document = builder.build(new File("src/XmlParser/"+file+".xml")); 
			
			// Prendo la radice 
			Element root = document.getRootElement(); 
			System.out.println("ROOT: "+document.getRootElement());
			
			// Estraggo i figli dalla radice 
			List vehicles = root.getChildren(); 
			System.out.println("LISTA VEICOLI: "+vehicles);
			
			List carList = root.getChildren("Car");
			List vanList = root.getChildren("Van");
			List truckList = root.getChildren("Truck");
			List trailerList = root.getChildren("Trailer");
			List roadTractorList = root.getChildren("RoadTractor");
			List semiTrailerList = root.getChildren("SemiTrailer");
			List trailerTruckList = root.getChildren("TrailerTruck");
			List SemiTrailerTruckList = root.getChildren("SemiTrailerTruck");
			
			
			System.out.println(
					"Car: "				+carList+"\n"+
					"Van: "				+vanList+"\n"+
					"Truck: "			+truckList+"\n"+
					"Trailer: "			+trailerList+"\n"+
					"RoadTractor: "		+roadTractorList+"\n"+
					"SemiTrailer: "		+semiTrailerList+"\n"+
					"TrailerTruck: "	+trailerTruckList+"\n"+
					"SemiTrailerTruck: "+SemiTrailerTruckList
				);
			
			// Veicolo uno per uno
			Iterator iterator = vehicles.iterator(); 
			
			while(iterator.hasNext()){ 
				Element v = (Element)iterator.next();
				System.out.println(v.getName());
				
				carList = v.getChildren("Car");
				vanList = v.getChildren("Van");
				
				
				List<Content> list = v.getContent();
				
			}
			

			/*
			//Per ogni figlio 
			while(iterator.hasNext()){ 
				//Mostro il valore dell'elemento figlio "DESCR" e degli 
				//attributi "importanza", "perc_completamento", e "completata" 
				//sullo standard output 
				Element item = (Element)iterator.next(); 
				
				//System.out.println("*" + item.getTextNormalize()); 
				
				Element description = item.getChild("Car"); 
				System.out.println("*" + description.getTextNormalize()); 
				
				System.out.println("\tImportanza: " + item.getAttributeValue("importanza")); 
				System.out.println("\tCompletamento: " + item.getAttributeValue("perc_completamento") + "%");
				System.out.println("\tItem copmletata: " + item.getAttributeValue("completata")+"\n"); 
			}
			*/
		}
		catch (Exception e) { 
			System.err.println("Errore durante la lettura dal file"); 
			e.printStackTrace(); 
		}
	}
	
	
	public static void main(String[] args) { 
		ShipperXmlReader reader = new ShipperXmlReader("Shipper1");
	}
	
}