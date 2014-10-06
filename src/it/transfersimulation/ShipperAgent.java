package it.transfersimulation;

import it.transfersimulation.Vehicle.Stato;
import it.transfersimulation.Vehicle.TipoVeicolo;

import java.util.Vector;

import jade.core.AID;
import jade.core.Agent;
import jade.core.behaviours.Behaviour;
import jade.core.behaviours.CyclicBehaviour;
import jade.core.behaviours.OneShotBehaviour;
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
		
		vehicles.add(new Vehicle("AAA1", TipoVeicolo.AUTO, "Peugeot", Stato.DISPONIBILE, (float) 1.5));
		vehicles.add(new Vehicle("AAA2", TipoVeicolo.AUTOARTICOLATO, "SCANIA", Stato.IN_VIAGGIO, (float) 3.5));
		vehicles.add(new Vehicle("AAA3", TipoVeicolo.AUTOCARRO, "SCANIA", Stato.NON_DISPONIBILE, (float) 3.5));
		
		
		// GRAFICA E PRESENTAZIONE
		myGUI = new ShipperAgentGUI(this);
		myGUI.showGui();
		
		// Printout a welcome message
		System.out.println("Ciao! Shipper Agent "+getAID().getName()+" pronto!");
		
		
		// SVILUPPO FUTURO:
		
		// Determina la tipologia di automezzo
		//whichVehicle();
		
		// Pubblica sulle Pagine Gialle il proprio servizio
		publishService();
		
		// Trova le aziende che necessitano di un trasporto
		//searchJob();
		//addBehaviour(new HandlePropose());
	}
	
	
	
	////////////////////
	// METODI         //
	////////////////////
	
	public Vector<Vehicle> getVehicles(){
		return vehicles;
	}
	
	
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
	
	
	
	// Put agent clean-up operations here
	protected void takeDown() {
		// Deregister from the yellow pages
		try {
			DFService.deregister(this);
		} catch (FIPAException fe) {
			fe.printStackTrace();
		}
		
		// Close the GUI
		//myGui.dispose();
		
		// Printout a dismissal message
		System.out.println("Shipper Agent "+getAID().getName()+" terminato.");
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
	public void newCiao(String prima) {
		System.out.println(prima + " e ciaooo!");
	}
	
	@Override
	public void newBye(String dopo) {
		System.out.println("ciao e " + dopo);
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
	
	@Override
	public void doDelete() {
		super.doDelete();
	}
}
