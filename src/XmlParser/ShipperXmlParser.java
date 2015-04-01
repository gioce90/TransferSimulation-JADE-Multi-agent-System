package XmlParser;

import java.io.File;
import java.io.IOException;
import java.util.Iterator;
import java.util.List;
import java.util.Vector;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import org.jdom2.Document;
import org.jdom2.Element;
import org.jdom2.input.SAXBuilder;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

import transfersimulation.model.vehicle.*;


public class ShipperXmlParser {
	
	Vector<Vehicle> vehicles = new Vector<Vehicle>();
	
	public ShipperXmlParser(String file) {
		DocumentBuilderFactory docBuilderFactory = DocumentBuilderFactory.newInstance();
	    DocumentBuilder docBuilder;
	    Document doc;
	    
	    //Element rootElement = new Element("ROOT"); 
	    //Document document = new Document(rootElement);
	    
	    try {
	    	
	        //Creo un SAXBuilder e con esco costruisco un document 
	        SAXBuilder builder = new SAXBuilder(); 
	        Document document = builder.build(new File("src/XmlParser/"+file+".xml")); 
	        
	         //Prendo la radice 
	        Element root = document.getRootElement(); 
	        //Estraggo i figli dalla radice 
	        List children = root.getChildren(); 
	        Iterator iterator = children.iterator(); 
	        
	         //Per ogni figlio 
	        while(iterator.hasNext()){ 
	           //Mostro il valore dell'elemento figlio "DESCR" e degli 
	           //attributi "importanza", "perc_completamento", e "completata" 
	           //sullo standard output 
	           Element item = (Element)iterator.next(); 
	           Element description = item.getChild("DESCR"); 
	           System.out.println("*" + description.getText()); 
	           System.out.println("\tImportanza: " + item.getAttributeValue("importanza")); 
	           System.out.println("\tCompletamento: " + item.getAttributeValue("perc_completamento") + "%");
	           System.out.println("\tItem copmletata: " + item.getAttributeValue("completata")+"\n"); 
	        } 
	      }  
	      catch (Exception e) { 
	        System.err.println("Errore durante la lettura dal file"); 
	        e.printStackTrace(); 
	      } 
	    
	    
	    
	    
	    
	}
	    
	    
	    /*
	    try {
	    	docBuilder = docBuilderFactory.newDocumentBuilder();
	    	doc = docBuilder.parse(new File("src/XmlParser/"+file+".xml"));
	    	doc.getDocumentElement().normalize();
	    	
	    	System.out.println(doc.getDocumentElement());
	    	System.out.println(doc.getAttributes());
	    	System.out.println(doc.getNextSibling());
	    	
	    	NodeList nodeList = doc.getElementsByTagName("VehicleType");
	    */
	    	
	    	/*
	    	for (int i=0; i<nodeList.getLength(); i++) {
	    		Node node = nodeList.item(i);
	    		
	    		if (node.getNodeType() == Node.ELEMENT_NODE) {
	    			Element element = (Element) node;
	    			Vehicle v = null;
	    			
	    			System.out.println(i);
	    			System.out.println(node);
	    			System.out.println(node.getLocalName());
	    			System.out.println(node.getNodeName());
	    			System.out.println(node.getNodeValue());
	    			
	    			System.out.println(element);
	    			System.out.println(element.getLocalName());
	    			System.out.println(element.getNodeName());
	    			System.out.println(element.getNodeValue());
	    			
	    			}
	    	}
	    	 */

	    	/*
		    Node n1 = doc.getFirstChild();
		    System.out.println(n1);

		    Node n2 = n1.getFirstChild();
		    System.out.println(n2);

		    NodeList fleet = doc.getElementsByTagName("Fleet");
		    System.out.println("Total no of Vehicles : " + fleet.getLength());
		    
		    Node listOfVehicles = fleet.item(0);
		    System.out.println(listOfVehicles.getTextContent());
	    	 */
		    //System.out.println(listOfVehicles.item(0).getAttributes().getNamedItem("kind"));

/*
	    } catch (ParserConfigurationException e) {
	    	e.printStackTrace();
	    } catch (SAXException e) {
	    	e.printStackTrace();
	    } catch (IOException e) {
	    	e.printStackTrace();
	    }
	}
*/
	
	public Vector<Vehicle> getVehicles() {
		return vehicles;
	}
	
	
	public static void main(String[] args) {
		ShipperXmlParser parser = new ShipperXmlParser("Shipper1");
	}
	
}







/*
// Create XPathFactory e XPath
XPathFactory xpathFactory = XPathFactory.newInstance();
XPath xpath = xpathFactory.newXPath();
*/


/*
String kindVehicle = element.getAttribute("kind");
switch (kindVehicle){
case "car": 		v = new Car(""); break;
case "van": 		v = new Van(""); break;
case "truck": 		v = new Truck(""); break;
case "trailer": 	v = new Trailer(""); break;
case "roadtractor": v = new RoadTractor(""); break;
case "semitrailer": v = new SemiTrailer(""); break;
*/

/*
case "trailertruck":
	DrivingPart driving1;
	
	v = new TrailerTruck(driving1, new Trailer(""));
	break;
case "semitrailertruck": 
	DrivingPart driving2;
	v = new TrailerTruck(driving2, new Trailer(""));
	break;
*/



/*
	Node elementNode = element.getElementsByTagName("plate").item(0);
	if (elementNode!=null) v.setPlate(elementNode.getTextContent());

	elementNode = element.getElementsByTagName("allestimento").item(0);
	if (elementNode!=null) v.setAllestimento(elementNode.getTextContent());

	elementNode = element.getElementsByTagName("model").item(0);
	if (elementNode!=null) v.setModel(elementNode.getTextContent());

	elementNode = element.getElementsByTagName("mark").item(0);
	if (elementNode!=null) v.setMark(elementNode.getTextContent());

	elementNode = element.getElementsByTagName("trim").item(0);
	if (elementNode!=null) v.setTrim(elementNode.getTextContent());

	elementNode = element.getElementsByTagName("locazioneAttuale").item(0);
	if (elementNode!=null) v.setLocazioneAttuale(elementNode.getTextContent());

	elementNode = element.getElementsByTagName("carryingCapacity").item(0);
	if (elementNode!=null) v.setCarryingCapacity((float)Double.parseDouble(elementNode.getTextContent()));

	elementNode = element.getElementsByTagName("ptt").item(0);
	if (elementNode!=null) v.setPtt((float)Double.parseDouble(elementNode.getTextContent()));

	elementNode = element.getElementsByTagName("volume").item(0);
	if (elementNode!=null) v.setVolume((float)Double.parseDouble(elementNode.getTextContent()));

	elementNode = element.getElementsByTagName("length").item(0);
	if (elementNode!=null) v.setLength((float)Double.parseDouble(elementNode.getTextContent()));

	elementNode = element.getElementsByTagName("height").item(0);
	if (elementNode!=null) v.setHeight((float)Double.parseDouble(elementNode.getTextContent()));

	elementNode = element.getElementsByTagName("weight").item(0);
	if (elementNode!=null) v.setWeight((float)Double.parseDouble(elementNode.getTextContent()));

	elementNode = element.getElementsByTagName("width").item(0);
	if (elementNode!=null) v.setWidth((float)Double.parseDouble(elementNode.getTextContent()));

	elementNode = element.getElementsByTagName("stato").item(0);
	if (elementNode!=null && elementNode.getTextContent().equals("disponibile"))
		v.setStato(Stato.DISPONIBILE);
	else v.setStato(Stato.NON_DISPONIBILE);
	
	vehicles.add(v);
	
	
    	if (elementNode!=null) list.add(elementNode.getTextContent());
    	elementNode  = element.getElementsByTagName("allestimento").item(0);
    	if (elementNode!=null) list.add(elementNode.getTextContent());
    	elementNode  = element.getElementsByTagName("model").item(0);
    	if (elementNode!=null) list.add(elementNode.getTextContent());
    	elementNode  = element.getElementsByTagName("mark").item(0);
    	if (elementNode!=null) list.add(elementNode.getTextContent());
    	elementNode  = element.getElementsByTagName("locazioneAttuale").item(0);
    	if (elementNode!=null) list.add(elementNode.getTextContent());
    	elementNode  = element.getElementsByTagName("carryingCapacity").item(0);
    	if (elementNode!=null) list.add(elementNode.getTextContent());
    	elementNode  = element.getElementsByTagName("ptt").item(0);
    	if (elementNode!=null) list.add(elementNode.getTextContent());
    	elementNode  = element.getElementsByTagName("trim").item(0);
    	if (elementNode!=null) list.add(elementNode.getTextContent());
    	elementNode  = element.getElementsByTagName("volume").item(0);
    	if (elementNode!=null) list.add(elementNode.getTextContent());
    	elementNode  = element.getElementsByTagName("length").item(0);
    	if (elementNode!=null) list.add(elementNode.getTextContent());
    	elementNode  = element.getElementsByTagName("height").item(0);
    	if (elementNode!=null) list.add(elementNode.getTextContent());
    	elementNode  = element.getElementsByTagName("weight").item(0);
    	if (elementNode!=null) list.add(elementNode.getTextContent());
    	elementNode  = element.getElementsByTagName("width").item(0);
    	if (elementNode!=null) list.add(elementNode.getTextContent());
	 
	list.add(element.getElementsByTagName("plate").item(0).getTextContent());
        list.add(element.getElementsByTagName("allestimento").item(0).getTextContent());
        list.add(element.getElementsByTagName("model").item(0).getTextContent());
        list.add(element.getElementsByTagName("mark").item(0).getTextContent());
        list.add(element.getElementsByTagName("locazioneAttuale").item(0).getTextContent());
       	list.add(element.getElementsByTagName("carryingCapacity").item(0).getTextContent());
        list.add(element.getElementsByTagName("ptt").item(0).getTextContent());
        list.add(element.getElementsByTagName("trim").item(0).getTextContent());
        list.add(element.getElementsByTagName("volume").item(0).getTextContent());
        list.add(element.getElementsByTagName("length").item(0).getTextContent());
        list.add(element.getElementsByTagName("height").item(0).getTextContent());
        list.add(element.getElementsByTagName("weight").item(0).getTextContent());
        list.add(element.getElementsByTagName("width").item(0).getTextContent());
}
*/
