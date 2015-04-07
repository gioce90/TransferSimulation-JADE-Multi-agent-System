package transfersimulation;

import java.io.File;
import java.io.IOException;
import java.io.Serializable;
import java.util.List;
import java.util.Vector;

import org.jdom2.JDOMException;

import XmlParser.ShipperXmlReader;
import transfersimulation.model.vehicle.*;
import transfersimulation.protocols.SearchJobInitiator;
import jade.core.AID;
import jade.core.Agent;
import jade.core.behaviours.OneShotBehaviour;
import jade.domain.DFService;
import jade.domain.FIPAException;
import jade.domain.FIPAAgentManagement.DFAgentDescription;
import jade.domain.FIPAAgentManagement.ServiceDescription;
import jade.lang.acl.ACLMessage;


public class ShipperAgent extends Agent implements ShipperInterface, Serializable {
	
	private static final long serialVersionUID = 1L;
	public ShipperAgentGUI myGUI;
	private Vector<Vehicle> vehicles = new Vector<Vehicle>();
	
	@Override
	protected void setup() {
		
		ShipperXmlReader shipperParser;
		
		try {
			shipperParser = new ShipperXmlReader(getAID().getLocalName());
			vehicles = shipperParser.getVehicles();
		} catch (JDOMException | IOException e) {
			try {
				new File(getAID().getLocalName()+".xml").createNewFile();
			} catch (IOException e1) {
				e1.printStackTrace();
			}
		}
		
		///////////////////////////////////////////////////
		
		// GRAFICA E PRESENTAZIONE
		myGUI = new ShipperAgentGUI(this);
		myGUI.showGui();
		
		// Printout a welcome message
		myGUI.insertInfo("Ciao! Shipper Agent "+getAID().getName()+" pronto!");
		
		// Pubblica sulle Pagine Gialle il proprio servizio
		publishService();
		
	}
	
	
	
	////////////////////
	// METODI         //
	////////////////////
	
	/** 
	 * Registra il servizio di Trasporto presso le Pagine Gialle
	 */
	private void publishService() {
		DFAgentDescription dfd = new DFAgentDescription();
		dfd.setName(getAID());
		ServiceDescription sd = new ServiceDescription();
		sd.setName("JADE-trasporto-merci");
		sd.setType("shipper");
		dfd.addServices(sd);
		try {
			DFService.register(this, dfd);
			myGUI.insertInfo("Registrazione sulle pagine gialle... avvenuta con successo");
		} catch (FIPAException fe) {
			fe.printStackTrace();
		}
	}
	
	
	/**
	 *  Put agent clean-up operations here
	 */
	protected void takeDown() {
		// Deregister from the yellow pages
		try {
			DFService.deregister(this);
		} catch (FIPAException fe) {
			fe.printStackTrace();
		}
		
		// Printout a dismissal message
		myGUI.insertInfo("Shipper Agent "+getAID().getName()+" terminato.");

		// Close the GUI
		myGUI.dispose();
	}
	
	
	
	
	///////////////////////////////////////////
	// Communication with agent methods
	
	
	/**
	 * Trova i buyer attivi nella rete
	 */
	public AID[] searchBuyers(){
		ServiceDescription sd = new ServiceDescription();
		sd.setName("JADE-trasporto-merci");
		sd.setType("buyer");
		DFAgentDescription template = new DFAgentDescription();
		template.addServices(sd);
		AID[] buyerAgents = null;
		try {
			DFAgentDescription[] result = DFService.search(this, template);
			buyerAgents = new AID[result.length];
			String list = "";
			for (int i = 0; i < result.length; ++i) {
				buyerAgents[i] = result[i].getName();
				list+=buyerAgents[i].getLocalName();
				if (!(i==result.length-1))
					list+=", ";
			}
			myGUI.insertInfo("Trovati i seguenti clienti: "+list);
		}
		catch (FIPAException fe) {
			fe.printStackTrace();
		}
		return buyerAgents;
	}
	
	
	
	/**
	 * Invia una CFP a tutti i buyer agent attivi,
	 * e richiede la lista delle merci
	 */
	void searchJob() {
		ACLMessage cfp = new ACLMessage(ACLMessage.CFP);
		// Trova tutti i buyer, itera su di essi e invia la CFP
		AID[] buyerAgents = searchBuyers();
		for (AID buyer : buyerAgents)
			cfp.addReceiver(buyer);
		
		try {
			cfp.setContentObject(getAvailableVehicles());
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		addBehaviour(new SearchJobInitiator(this, cfp));
	}
	
	
	
	////////////////////////////////////////////////////////
	// Metodi ereditati dall'interfaccia ShipperInterface:

	@Override
	public List<Vehicle> getVehicles(){
		return vehicles;
	}
	
	@Override
	public Vector<Vehicle> getAvailableVehicles(){
		Vector<Vehicle> availableVehicles = new Vector<Vehicle>();
		for (Vehicle vehicle : vehicles) {
			if (vehicle.getState()==Vehicle.Stato.DISPONIBILE)
				availableVehicles.add(vehicle);
		}
		return availableVehicles;
	}
	
	@Override
	public void newTruck(final Vehicle vehicle) {
		addBehaviour(new OneShotBehaviour() {
			private static final long serialVersionUID = 1L;
			@Override
			public void action() {
				myGUI.insertInfo("Nuovo veicolo targato '"+vehicle.getPlate()+"'");
			}
		});
		
	}
	
	@Override
	public void removeTruck(final Vehicle vehicle) {
		addBehaviour(new OneShotBehaviour() {
			private static final long serialVersionUID = 1L;
			@Override
			public void action() {
				myGUI.insertInfo("Rimosso veicolo targato '"+vehicle.getPlate()+"'");
			}
		});
		
	}

	@Override
	public void activateTruck(final Vehicle vehicle) {
		addBehaviour(new OneShotBehaviour() {
			private static final long serialVersionUID = 1L;
			@Override
			public void action() {
				myGUI.insertInfo("Il veicolo targato '"+vehicle.getPlate()+"' è disponibile");
			}
		});
	}
	
	@Override
	public void deactivateTruck(final Vehicle vehicle) {
		addBehaviour(new OneShotBehaviour() {
			private static final long serialVersionUID = 1L;
			@Override
			public void action() {
				myGUI.insertInfo("Il veicolo targato '"+vehicle.getPlate()+"' non è più disponibile");
			}
		});
	}
	

}




////////////////////////////


/*
private String vehicle;
private String weight;
private void whichVehicle() {
	Object[] args = getArguments();
	if (args!=null && args.length==2){
		vehicle = (String) getArguments()[0];
		weight = (String) getArguments()[1];
		
		System.out.println("Tipo di automezzo: " + vehicle +
				".\nPeso massimo trasportabile: "+weight+"tn");
	} else
		System.out.println("Tipo di automezzo non definito");
}
*/

// Utility: lista dei concorrenti
/*
private void searchCompetitors() {
	addBehaviour(new OneShotBehaviour() {
		private static final long serialVersionUID = 1L;
		@Override
		public void action() {
			ServiceDescription sd = new ServiceDescription();
			sd.setName("JADE-trasporto-merci");
			sd.setType("shipper");
			DFAgentDescription template = new DFAgentDescription();
			template.addServices(sd);
			try {
				DFAgentDescription[] result = DFService.search(myAgent, template);
				System.out.println("Trovate le seguenti aziende: ");
				
				AID[] customerAgents;
				customerAgents = new AID[result.length];
				
				for (int i = 0; i < result.length; ++i) {
					customerAgents[i] = result[i].getName();
					System.out.println(customerAgents[i].getName());
				}
				
			}
			catch (FIPAException fe) {
				fe.printStackTrace();
			}
		}
	});
}
*/

