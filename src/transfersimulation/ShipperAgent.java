package transfersimulation;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Vector;

import transfersimulation.model.goods.Goods;
import transfersimulation.model.vehicle.*;
import transfersimulation.model.vehicle.Vehicle.Stato;
import transfersimulation.table.GoodsChoiceBox;
import jade.core.AID;
import jade.core.Agent;
import jade.core.behaviours.Behaviour;
import jade.core.behaviours.CyclicBehaviour;
import jade.core.behaviours.DataStore;
import jade.core.behaviours.OneShotBehaviour;
import jade.core.behaviours.SequentialBehaviour;
import jade.domain.DFService;
import jade.domain.FIPAException;
import jade.domain.FIPANames;
import jade.domain.FIPAAgentManagement.DFAgentDescription;
import jade.domain.FIPAAgentManagement.ServiceDescription;
import jade.lang.acl.ACLMessage;
import jade.lang.acl.MessageTemplate;
import jade.lang.acl.UnreadableException;
import jade.proto.ContractNetInitiator;


public class ShipperAgent extends Agent implements ShipperInterface {
	
	private static final long serialVersionUID = 1L;
	
	private String vehicle;
	private String weight;
	
	private ShipperAgentGUI myGUI;
	
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
		c3.setLocazioneAttuale("Foggia");
		
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
		
		vehicles.add(t1);
		
		//vehicles.add(tt1);
		//vehicles.add(tt2);
		vehicles.add(tt3);
		
		
		///////////////////////////////////////////////////
		
		// GRAFICA E PRESENTAZIONE
		myGUI = new ShipperAgentGUI(this);
		myGUI.showGui();
		
		// Printout a welcome message
		System.out.println("Ciao! Shipper Agent "+getAID().getName()+" pronto!");
		
		// Pubblica sulle Pagine Gialle il proprio servizio
		publishService();
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
		sd.setType("shipper");
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
	// Communication with agent methods
	
	
	/**
	 * Trova i buyer attivi
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
			//System.out.println("Trovati i seguenti clienti: ");
			buyerAgents = new AID[result.length];
			
			for (int i = 0; i < result.length; ++i) {
				buyerAgents[i] = result[i].getName();
			}
		}
		catch (FIPAException fe) {
			fe.printStackTrace();
		}
		return buyerAgents;
	}
	
	
	
	/**
	 * Invia una request a tutti i buyer agent attivi,
	 * e richiede la lista delle merci
	 */
	void searchJob() {
		//addBehaviour(new InviaCFP()); // NON CANCELLARE PER ORA
		
		ACLMessage cfp = new ACLMessage(ACLMessage.CFP);
		cfp.setContent("Fammi delle proposte di lavoro");
		cfp.setConversationId("contrattazione-by-shipper-"+this.getName());
		cfp.setProtocol(FIPANames.InteractionProtocol.FIPA_CONTRACT_NET);
		// Trova tutti i buyer, itera su di essi e invia la CFP
		AID[] buyerAgents = searchBuyers();
		for (AID buyer : buyerAgents)
			cfp.addReceiver(buyer);
		
		addBehaviour(new IniziaContratto(this, cfp));
	}
	
	
	private class IniziaContratto extends ContractNetInitiator {
		
		private static final long serialVersionUID = 1L;
		
		DataStore ds;
		Vector acceptances;
		
		public IniziaContratto(Agent a, ACLMessage cfp) {
			super(a, cfp);
			
			HandlePropose h1 = new HandlePropose();
			registerHandlePropose(h1);
		}
		
		@Override
		public void onStart() {
			super.onStart();
			ds=getDataStore();
			acceptances = (Vector) ds.get(ALL_ACCEPTANCES_KEY);
		}
		
		@Override
		protected void handleFailure(ACLMessage failure) {
			System.out.println("Failure");
		}
		
		@Override
		protected void handleInform(ACLMessage inform) {
			System.out.println("Agente "+inform.getSender().getLocalName()+": successfully performed the requested action");
		}
		
		@Override
		protected void handleRefuse(ACLMessage refuse) {
			System.out.println("Refuse");
		}
		
		@Override
		protected void handleOutOfSequence(ACLMessage msg) {
			System.out.println("Out of Sequence");
		}
		
		
		protected class HandlePropose extends SequentialBehaviour {
			private static final long serialVersionUID = 1L;
			
			protected GoodsChoiceBox gcb;
			ACLMessage propose;
			
			public HandlePropose() {
				addSubBehaviour(new ReceiveProposeAndShow());
				addSubBehaviour(new ShowTable());
				addSubBehaviour(new SendReply());
			}

			
			protected class ReceiveProposeAndShow extends OneShotBehaviour {
				private static final long serialVersionUID = 1L;
				
				public void action() {
					if (ds.containsKey(REPLY_KEY)){
						propose = (ACLMessage) ds.get(REPLY_KEY);
						try {
							System.out.println("Agente "+getLocalName()
									+": ricevuta PROPOSE da "+propose.getSender().getLocalName()
									+":\n         "+propose.getContentObject().toString()
							);
						} catch (UnreadableException e) {
							e.printStackTrace();
						}
					}
				}
			}
			
			protected class ShowTable extends OneShotBehaviour {
				public void action() {
					try {
						Vector<Goods> goods = (Vector<Goods>) propose.getContentObject();
						gcb = new GoodsChoiceBox(myAgent, propose, goods);
					} catch (UnreadableException e){
						e.printStackTrace();
					}
					gcb.getEsegui().addActionListener(new ActionListener() {
						@Override
						public void actionPerformed(ActionEvent e) {
							ArrayList<Goods> l = (ArrayList<Goods>) gcb.getSelectedGoods();
							ACLMessage reply = propose.createReply();
							if (l!=null && !l.isEmpty()){
								try {
									reply.setContentObject(l);
								} catch (IOException e1) {
									e1.printStackTrace();
								}
								reply.setPerformative(ACLMessage.ACCEPT_PROPOSAL);
								System.out.println("Agente "+getLocalName()
										+": ACCEPT PROPOSAL di "+propose.getSender().getLocalName());
							} else {
								reply.setPerformative(ACLMessage.REJECT_PROPOSAL);
								System.out.println("Agente "+getLocalName()
										+": REJECT PROPOSAL di "+propose.getSender().getLocalName());
							}
							acceptances.addElement(reply);
							gcb.dispose();
						}
					});
					
					gcb.getAnnulla().addActionListener(new ActionListener() {
						@Override
						public void actionPerformed(ActionEvent e) {
							ACLMessage reply = propose.createReply();
							reply.setPerformative(ACLMessage.REJECT_PROPOSAL);
							System.out.println("Agente "+getLocalName()
									+": REJECT PROPOSAL di "+propose.getSender().getLocalName());
							acceptances.addElement(reply);
						}
					});
					gcb.setVisible(true);
				}
			}
			
			protected class SendReply extends Behaviour {
				public void action() {}
				public boolean done() {
					return !(gcb.isVisible());
				}
			}
			
		} //close HandlePropose
		
	} // close contract
	
	
	
	
	
	
	
/*
	gcb.getEsegui().addActionListener(new ActionListener() {
		@Override
		public void actionPerformed(ActionEvent e) {
			ArrayList<Goods> l = (ArrayList<Goods>) gcb.getSelectedGoods();
			if (l!=null && !l.isEmpty()){
				try {
					reply.setContentObject(l);
				} catch (IOException e1) {
					e1.printStackTrace();
				}
				reply.setPerformative(ACLMessage.ACCEPT_PROPOSAL);
				System.out.println("Agente "+getLocalName()+": ACCEPT PROPOSAL di "+propose.getSender().getLocalName());
				//acceptances.addElement(reply);
				send(reply);
			} else {
				reply.setPerformative(ACLMessage.REJECT_PROPOSAL);
				System.out.println("Agente "+getLocalName()+": REJECT PROPOSAL di "+propose.getSender().getLocalName());
				//acceptances.addElement(reply);
				send(reply);
			}
		}
	});
	
	gcb.getAnnulla().addActionListener(new ActionListener() {
		@Override
		public void actionPerformed(ActionEvent e) {
			reply.setPerformative(ACLMessage.REJECT_PROPOSAL);
			System.out.println("Agente "+getLocalName()+": REJECT PROPOSAL di "+propose.getSender().getLocalName());
			//acceptances.addElement(reply);
			send(reply);
		}
	});
*/
	
	
	
	
	
	/*
	private class InviaCFP extends OneShotBehaviour {
		private static final long serialVersionUID = 1L;

		@Override
		public void action() {
			// crea la REQUEST
			ACLMessage cfp = new ACLMessage(ACLMessage.CFP);
			cfp.setContent("Fammi delle proposte di lavoro");
			cfp.setConversationId("contrattazione-by-shipper-"+getName());
			
			// Trova tutti i buyer, itera su di essi e invia la CFP
			AID[] buyerAgents = searchBuyers();
			for (AID buyer : buyerAgents) {
				cfp.addReceiver(buyer);
				addBehaviour(new ResponseToCFP(buyer, System.currentTimeMillis()));
			}
			myAgent.send(cfp);
		}
	} // close inner class InviaCFP
	
	
	private class ResponseToCFP extends Behaviour {
		private static final long serialVersionUID = 1L;
		
		long time;
		MessageTemplate mt;
		GoodsChoiceBox gcb;
		boolean done = false;
		
		public ResponseToCFP(AID sender, long time) {
			this.time = time;
			
			// Creo il template per filtrare le risposte
			mt = MessageTemplate.and(
				MessageTemplate.MatchSender(sender),
				MessageTemplate.or(
					MessageTemplate.MatchPerformative(ACLMessage.PROPOSE), 
					MessageTemplate.MatchPerformative(ACLMessage.REFUSE)));
			//MessageTemplate.MatchInReplyTo("order"+System.currentTimeMillis());
		}
		
		
		@Override
		public void action() {
			final ACLMessage proposal = receive(mt);
			
			if (proposal!=null){
				showGoodsTableAndReply(proposal);
				done = true;
			} else {
				block();
				// TODO il buyer ha 60 secondi per rispondere...
				// Dopodiché la richiesta scade. Da implementare!
			}
		} // close action()

		
		@Override
		public boolean done() {
			return done;
		}
		
		
		private void showGoodsTableAndReply(final ACLMessage proposal){
			try {
				final Vector<Goods> goods = (Vector<Goods>) proposal.getContentObject();
				
				Runnable addIt = new Runnable() {
					@Override
					public void run() {
						gcb = new GoodsChoiceBox(proposal.getSender().getName(), goods);
							
						gcb.getEsegui().addActionListener(new ActionListener() {
							@Override
							public void actionPerformed(ActionEvent e) {
								ArrayList<Goods> l = (ArrayList<Goods>) gcb.getSelectedGoods();
								ACLMessage reply = proposal.createReply();
								if (l!=null && !l.isEmpty()){
									try {
										reply.setContentObject(l);
									} catch (IOException e1) {
										e1.printStackTrace();
									}
									reply.setPerformative(ACLMessage.ACCEPT_PROPOSAL);
									addBehaviour(new WaitingResult(proposal.getSender()));
								} else {
									reply.setPerformative(ACLMessage.REJECT_PROPOSAL);
								}
								myAgent.send(reply);
							}
						});
						
						gcb.getAnnulla().addActionListener(new ActionListener() {
							@Override
							public void actionPerformed(ActionEvent e) {
								ACLMessage reply = proposal.createReply();
								reply.setPerformative(ACLMessage.REJECT_PROPOSAL);
								myAgent.send(reply);
							}
						});
						
						gcb.setVisible(true);
					}
				};
				SwingUtilities.invokeLater(addIt);
			} catch (UnreadableException e){
				e.printStackTrace();
			}
		}
		
		
		private class WaitingResult extends Behaviour {
			private static final long serialVersionUID = 1L;
			AID buyer;
			MessageTemplate mt;
			boolean done = false;
			
			public WaitingResult(AID buyer) {
				this.buyer=buyer;
				
				// Creo il template per filtrare le risposte
				mt = MessageTemplate.and(
						MessageTemplate.MatchSender(buyer),
						MessageTemplate.or(
							MessageTemplate.MatchPerformative(ACLMessage.INFORM), 
							MessageTemplate.MatchPerformative(ACLMessage.FAILURE)));
			}
			
			@Override
			public void action() {
				ACLMessage msg = receive(mt);
				if (msg!=null){
					if (msg.getPerformative()==ACLMessage.INFORM)
						System.out.println("Cliente: "+buyer.getName()+" ha detto OK!");
					else // caso FAILURE
						System.out.println("Cliente: "+buyer.getName()+" ha detto NO!");
					done=true;
				} else {
					block();
					//TODO dopo x secondi
					//System.out.println("Cliente: "+buyer.getName()+" non risponde!");
				}
			}
			
			@Override
			public boolean done() {
				return done;
			}
			
		} // close inner class WaitingResult


	} // close inner class ResponseToCFP
	
	
	*/
	
	
	
	
	
	
	
	
	
	
	
	
	
	
//////////////////
//////////////////
//////////////////
	//////////////////
	//////////////////
	//////////////////
//////////////////
//////////////////
//////////////////
	
	
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
	
	
	// TODO in futuro dovrà restituire una lista dei concorrenti
	public void searchCompetitors() {
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
	

	@Override
	public void newTruck(Vehicle vehicle) {
		System.out.println("ShipperAgent "+getLocalName()+": Nuovo veicolo targato \""+vehicle.getPlate()+"\"");
		//vehicles.add(vehicle);
	}

	
	@Override
	public void removeTruck(Vehicle vehicle) {
		System.out.println("ShipperAgent "+getLocalName()+": Rimosso veicolo targato \""+vehicle.getPlate()+"\"");
		//vehicles.remove(vehicle);
	}

	@Override
	public void activateTruck(final Vehicle vehicle) {
		addBehaviour(new OneShotBehaviour() {
			@Override
			public void action() {
				System.out.println("ShipperAgent "+getLocalName()+": Il veicolo targato \""+vehicle.getPlate()+"\" è stato reso disponibile");
			}
		});
		//System.out.println("ShipperAgent "+getLocalName()+": Il veicolo targato \""+vehicle.getPlate()+"\" è stato reso disponibile");
		//vehicles.get(vehicles.indexOf(vehicle)).setStato(Stato.DISPONIBILE);
	}
	
	@Override
	public void deactivateTruck(final Vehicle vehicle) {
		addBehaviour(new OneShotBehaviour() {
			@Override
			public void action() {
				System.out.println("ShipperAgent "+getLocalName()+": Il veicolo targato \""+vehicle.getPlate()+"\" non è più disponibile");
			}
		});
		//System.out.println("ShipperAgent "+getLocalName()+": Il veicolo targato \""+vehicle.getPlate()+"\" non è più disponibile");
		//vehicles.get(vehicles.indexOf(vehicle)).setStato(Stato.NON_DISPONIBILE);
	}
	
	@Override
	public List<Vehicle> getVehicles(){
		return vehicles;
	}
	

}