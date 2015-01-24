package transfersimulation;

import java.io.IOException;
import java.io.Serializable;
import java.util.List;
import java.util.Vector;

import transfersimulation.model.vehicle.*;
import transfersimulation.model.vehicle.Vehicle.Stato;
import transfersimulation.protocols.SearchJobInitiator;
import jade.core.AID;
import jade.core.Agent;
import jade.core.behaviours.OneShotBehaviour;
import jade.domain.DFService;
import jade.domain.FIPAException;
import jade.domain.FIPAAgentManagement.DFAgentDescription;
import jade.domain.FIPAAgentManagement.ServiceDescription;
import jade.lang.acl.ACLMessage;


public class ShipperAgent extends Agent implements ShipperInterface {
	
	private static final long serialVersionUID = 1L;
	public ShipperAgentGUI myGUI;
	private Vector<Vehicle> vehicles = new Vector<Vehicle>();
	
	@Override
	protected void setup() {
		
		// TODO: li dovrà prendere da un database
		
		Vehicle c1 = new Car("AAA1");
		c1.setMark("Peugeot");
		c1.setModel("206");
		c1.setPtt(2);
		c1.setStato(Stato.NON_DISPONIBILE);
		c1.setLocazioneAttuale("Bari");
		
		Vehicle c2 = new Van("AAA2");
		c2.setMark("Volvo");
		c2.setModel("xxx");
		c2.setPtt(3);
		c2.setStato(Stato.DISPONIBILE);
		c2.setLocazioneAttuale("Andria");
		
		Vehicle c3 = new Truck("AAA3");
		c3.setMark("Scania");
		c3.setModel("xxx");
		c3.setPtt(3);
		c3.setStato(Stato.DISPONIBILE);
		c3.setAllestimento("Frigo");
		c3.setLocazioneAttuale("Foggia");
		
		Vehicle c4 = new Truck("AAA4");
		c4.setMark("Scania");
		c4.setModel("xxx");
		c4.setPtt(3);
		c4.setStato(Stato.DISPONIBILE);
		c4.setAllestimento("Cisterna");
		c4.setLocazioneAttuale("Foggia");
		
		/*
		Vehicle c5 = new SemiTrailerTruck("AAA5");
		c5.setMark("DAF");
		c5.setModel("yyy");
		c5.setPtt(15);
		c5.setStato(Stato.DISPONIBILE);
		
		Vehicle c6 = new SemiTrailer("AAA6");
		c6.setMark("Menci");
		c6.setModel("yyy");
		c6.setPtt(15);
		c6.setStato(Stato.DISPONIBILE);
		
		Vehicle c7 = new RoadTractor("AAA7");
		c7.setMark("DAF");
		c7.setModel("yyy");
		c7.setPtt(15);
		c7.setStato(Stato.DISPONIBILE);
		*/
		
		////////////////////////////////////////////////
		// Autotreno: car + trailer/
			Vehicle t1 = new Trailer("TTT1");
			t1.setMark("Menci");
			t1.setModel("yyy");
			t1.setPtt(15);
			t1.setStato(Stato.DISPONIBILE);
			t1.setLocazioneAttuale("Roma");
			
			
			Vehicle tt1 = new TrailerTruck((Car) c1, (Trailer) t1);
			tt1.setMark("Scania");
			tt1.setModel("yyy");
			tt1.setPtt(15);
			tt1.setStato(Stato.DISPONIBILE);
			tt1.setLocazioneAttuale("Bari");
			
		////////////////////////////////////////////////
		// Autotreno: van + trailer/
			Vehicle tt2 = new TrailerTruck((Van) c2, (Trailer) t1);
			tt2.setMark("Scania");
			tt2.setModel("yyy");
			tt2.setPtt(15);
			tt2.setStato(Stato.DISPONIBILE);
			
		////////////////////////////////////////////////
		// Autotreno: truck + trailer/
			Vehicle tt3 = new TrailerTruck((Truck) c3, (Trailer) t1);
			tt3.setMark("Scania");
			tt3.setModel("yyy");
			tt3.setPtt(15);
			tt3.setStato(Stato.DISPONIBILE);
		
		
		vehicles.add(c1);
		vehicles.add(c2);
		vehicles.add(c3);
		vehicles.add(c4);
		
		vehicles.add(t1);
		
		//vehicles.add(tt1);
		//vehicles.add(tt2);
		vehicles.add(tt3);
		
		
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
		
		// Close the GUI
		myGUI.dispose();
		
		// Printout a dismissal message
		myGUI.insertInfo("Shipper Agent "+getAID().getName()+" terminato.");
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
			myGUI.insertInfo("Trovati i seguenti clienti: "+list);//TODO probabilmente è da spostare
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
				//vehicles.add(vehicle);
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
				//vehicles.remove(vehicle);
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
				//vehicles.get(vehicles.indexOf(vehicle)).setStato(Stato.DISPONIBILE);
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
				//vehicles.get(vehicles.indexOf(vehicle)).setStato(Stato.NON_DISPONIBILE);
			}
		});
	}
	

}