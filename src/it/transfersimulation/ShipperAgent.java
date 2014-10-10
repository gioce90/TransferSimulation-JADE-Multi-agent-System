package it.transfersimulation;

import it.transfersimulation.model.*;

import java.util.Vector;

import jade.core.AID;
import jade.core.Agent;
import jade.core.behaviours.Behaviour;
import jade.core.behaviours.CyclicBehaviour;
import jade.domain.DFService;
import jade.domain.FIPAException;
import jade.domain.FIPAAgentManagement.DFAgentDescription;
import jade.domain.FIPAAgentManagement.ServiceDescription;
import jade.lang.acl.ACLMessage;
import jade.lang.acl.MessageTemplate;

public class ShipperAgent extends Agent implements ShipperInterface {
	
	private static final long serialVersionUID = 1L;
	
	private String vehicle;
	private String weight;

	private AID[] companyAgents;

	private ShipperAgentGUI myGUI;
	
	private Vector<Vehicle> vehicles = new Vector<Vehicle>();
	
	@Override
	protected void setup() {
		
		// TODO: li dovrà prendere da un database
		/*
		vehicles.add(new Vehicle("AAA1", TipoVeicolo.AUTO, "Peugeot", Stato.DISPONIBILE, (float) 1.5));
		vehicles.add(new Vehicle("AAA2", TipoVeicolo.AUTOARTICOLATO, "SCANIA", Stato.IN_VIAGGIO, (float) 3.5));
		vehicles.add(new Vehicle("AAA3", TipoVeicolo.AUTOCARRO, "SCANIA", Stato.NON_DISPONIBILE, (float) 3.5));
		*/
		Vehicle c1 = new Car("AAA1");
		c1.setMark("Peugeot");
		c1.setModel("206");
		c1.setPtt(2);
		c1.setStato(Stato.DISPONIBILE);
		
		Vehicle c2 = new Van("AAA2");
		c2.setMark("Volvo");
		c2.setModel("xxx");
		c2.setPtt(3);
		c2.setStato(Stato.DISPONIBILE);
		
		Vehicle c3 = new Truck("AAA3");
		c3.setMark("Scania");
		c3.setModel("xxx");
		c3.setPtt(3);
		c3.setStato(Stato.DISPONIBILE);
		
		Vehicle c4 = new TrailerTruck("AAA4"); // TODO
		c4.setMark("Scania");
		c4.setModel("yyy");
		c4.setPtt(15);
		c4.setStato(Stato.DISPONIBILE);
		
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
		
		Vehicle c8 = new Trailer("AAA8");
		c8.setMark("Menci");
		c8.setModel("yyy");
		c8.setPtt(15);
		c8.setStato(Stato.DISPONIBILE);
		
		vehicles.add(c1);
		vehicles.add(c2);
		vehicles.add(c3);
		vehicles.add(c4);
		vehicles.add(c5);
		vehicles.add(c6);
		vehicles.add(c7);
		vehicles.add(c8);
		
		// GRAFICA E PRESENTAZIONE
		myGUI = new ShipperAgentGUI(this);
		myGUI.showGui();
		
		// Printout a welcome message
		System.out.println("Ciao! Shipper Agent "+getAID().getName()+" pronto!");
		
		
		
		// SVILUPPO FUTURO:
		
		// Pubblica sulle Pagine Gialle il proprio servizio
		publishService();
		
		// Trova le aziende che necessitano di un trasporto
		//searchJob();
		//addBehaviour(new HandlePropose());
	}
	
	
	
	////////////////////
	// METODI         //
	////////////////////
	
	
	private void publishService() {
		// Registra il servizio di Trasporto presso il servizio di Pagine Gialle
		DFAgentDescription dfd = new DFAgentDescription();
		dfd.setName(getAID());
		ServiceDescription sd = new ServiceDescription();
		sd.setName("JADE-trasporto-merci");
		sd.setType("trasporto-merci"); // cos'è?
		dfd.addServices(sd);
		try {
			DFService.register(this, dfd);
			System.out.println("Registrazione sulle pagine gialle... avvenuta con successo");
		} catch (FIPAException fe) {
			fe.printStackTrace();
		}
	}
	
	
	// Put agent clean-up operations here
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
		System.out.println("Shipper Agent "+getAID().getName()+" terminato.");
	}
	
	
	///////////////////////////////////////////
	// Comunication with agent methods
	
	public Vector<Vehicle> getVehicles(){
		return vehicles;
	}
	
	
	
	
	///////////////////////////////////////////
	
	private void searchJob() {
		addBehaviour(new Behaviour() {
			@Override
			public void action() {
				DFAgentDescription template = new DFAgentDescription();
				ServiceDescription sd = new ServiceDescription();
				//sd.setName("trasporto-merci");
				sd.setType("trasporto-merci");
				template.addServices(sd);
				try {
					DFAgentDescription[] result = DFService.search(myAgent, template); 
					System.out.println("Trovate le seguenti company agents:");
					companyAgents = new AID[result.length];
					for (int i = 0; i < result.length; ++i) {
						companyAgents[i] = result[i].getName();
						System.out.println(companyAgents[i].getName());
					}
				}
				catch (FIPAException fe) {
					fe.printStackTrace();
				}
			}
			
			@Override
			public boolean done() {
				return false;
			}
		});
	}
	
	
	
	
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
	
	
	
	
	
	
	
	
	private class HandlePropose extends CyclicBehaviour {
		@Override
		public void action() {
			MessageTemplate mt = MessageTemplate.MatchPerformative(ACLMessage.PROPOSE);
			ACLMessage msg = myAgent.receive(mt);
			if (msg != null) {
				// PROPOSE Message received. Process it
				String[] items = msg.getContent().split(" ");
				ACLMessage reply = msg.createReply();
				
				if (items != null) {
					String tipoMerce = items[0];
					String pesoMerce = items[1];
					
					System.out.println("eee");
					
					// TODO Se il tipo di merce corrisponde a quella trasportabile
					// e se il peso della merce è inferiore a quello trasportabile
					// allora accetta di prendere il carico
					if (tipoMerce.equals("oro"))
						//if (Integer.getInteger(pesoMerce)<=Integer.getInteger(weight)) {
							reply.setPerformative(ACLMessage.ACCEPT_PROPOSAL);
							System.out.println("accetta la proposta");
						
				}
				else {
					// Rifiuta il trasporto
					reply.setPerformative(ACLMessage.REFUSE);
					reply.setContent("not-available");
					System.out.println("rifiuto");
				}
				myAgent.send(reply);
			}
			else {
				block();
			}
		}
		
	}
	
	
	
	@Override
	public void newTruck(String targa) {
		System.out.println("ShipperAgent "+getLocalName()+": Nuovo camion targato \""+targa+"\"");
	}

	@Override
	public void removeTruck(String targa) {
		System.out.println("ShipperAgent "+getLocalName()+": Rimosso camion targato \""+targa+"\"");
	}
	 
	@Override
	public void activateTruck(String targa) {
		System.out.println("ShipperAgent "+getLocalName()+": Il camion targato \""+targa+"\" è stato reso disponibile");
	}
	
	@Override
	public void deactivateTruck(String targa) {
		System.out.println("ShipperAgent "+getLocalName()+": Il camion targato \""+targa+"\" non è più disponibile");
	}
	
}
