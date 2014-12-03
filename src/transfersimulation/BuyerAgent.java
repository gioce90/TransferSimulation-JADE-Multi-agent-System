package transfersimulation;

import java.io.IOException;
import java.sql.Date;
import java.util.ArrayList;
import java.util.List;
import java.util.Vector;

import transfersimulation.model.goods.Goods;
import jade.core.AID;
import jade.core.Agent;
import jade.core.behaviours.CyclicBehaviour;
import jade.core.behaviours.OneShotBehaviour;
import jade.domain.DFService;
import jade.domain.FIPAException;
import jade.domain.FIPANames;
import jade.domain.FIPAAgentManagement.DFAgentDescription;
import jade.domain.FIPAAgentManagement.FailureException;
import jade.domain.FIPAAgentManagement.NotUnderstoodException;
import jade.domain.FIPAAgentManagement.RefuseException;
import jade.domain.FIPAAgentManagement.ServiceDescription;
import jade.lang.acl.ACLMessage;
import jade.lang.acl.MessageTemplate;
import jade.lang.acl.UnreadableException;
import jade.proto.ContractNetInitiator;
import jade.proto.ContractNetResponder;

public class BuyerAgent extends Agent implements BuyerInterface {
	private static final long serialVersionUID = 3399019455702807074L;
	
	BuyerAgentGUI myGUI;
	Vector<Goods> goods;
	
	@Override
	protected void setup() {
		super.setup();
		
		// Inserisco una serie di merci:
		goods = new Vector<Goods>();
		
		Goods m = new Goods();
		m.setCodice("Cod1");
		m.setDescrizione("Sabbia");
		m.setDimensione("x*y*z");
		m.setPericolosa(false);
		m.setQuantità(100);
		m.setTipo("solida");
		m.setVolume(200);
		m.setLocationStart("Bari");
		m.setLocationEnd("Lecce");
		m.setDateStart(Date.valueOf("2014-10-22"));
		m.setDateLimit(5);
		goods.add(m);
		
		m = new Goods();
		m.setCodice("Cod2");
		m.setDescrizione("Petrolio");
		m.setDimensione("x*y*z");
		m.setPericolosa(true);
		m.setQuantità(100);
		m.setTipo("liquida");
		m.setVolume(200);
		m.setLocationStart("Lecce");
		m.setLocationEnd("Roma");
		m.setDateStart(Date.valueOf("2014-10-22"));
		m.setDateLimit(6);
		goods.add(m);
		
		m = new Goods();
		m.setCodice("Cod3");
		m.setDescrizione("Cibo refrigerato");
		m.setDimensione("x*y*z");
		m.setPericolosa(true);
		m.setQuantità(100);
		m.setTipo("solida");
		m.setVolume(200);
		m.setLocationStart("Lecce");
		m.setLocationEnd("Roma");
		m.setDateStart(Date.valueOf("2014-10-22"));
		m.setDateLimit(6);
		goods.add(m);
		
		/*
		Transport t1 = new Transport(m1, "x", "y", Date.valueOf("2014-10-22"), 5);
		Transport t2 = new Transport(m2, "y", "z", Date.valueOf("2014-10-22"), 3);
		transports = new Vector<Transport>();
		transports.add(t1);
		*/
		//final Object[] ordini = {t1, t2};
		
		// GRAFICA E PRESENTAZIONE
		myGUI = new BuyerAgentGUI(this);
		myGUI.showGui();
		
		// Printout a welcome message
		System.out.println("Ciao! Shipper Agent "+getAID().getName()+" pronto!");
		
		// Pubblica sulle Pagine Gialle il proprio servizio
		publishService();
		
		//addBehaviour(new CFPRequests());
		
		
		MessageTemplate template = MessageTemplate.and(
				MessageTemplate.MatchProtocol(FIPANames.InteractionProtocol.FIPA_CONTRACT_NET),
				MessageTemplate.MatchPerformative(ACLMessage.CFP) );
		
		addBehaviour(new ReplyContratto(this, template));
	}
	


	private void publishService() {
		// Registra il servizio di Trasporto presso il servizio di Pagine Gialle
		DFAgentDescription dfd = new DFAgentDescription();
		dfd.setName(getAID());
		ServiceDescription sd = new ServiceDescription();
		sd.setName("JADE-trasporto-merci");
		sd.setType("buyer");
		dfd.addServices(sd);
		try {
			DFService.register(this, dfd);
			System.out.println("Registrazione sulle pagine gialle... avvenuta con successo");
		} catch (FIPAException fe) {
			fe.printStackTrace();
		}
	}
	
	@Override
	protected void takeDown() {
		super.takeDown();
		try {
			DFService.deregister(this); // deregister from the yellow pages
		} catch (FIPAException fe) {fe.printStackTrace();}
		
		myGUI.dispose();// Close the GUI
		
		// Printout a dismissal message:
		System.out.println("Buyer Agent "+getAID().getName()+" terminato.");
	}
	
	////////////////////////////////////////////////////////////////////////////////
	
	
	protected AID[] searchShippers() {
		ServiceDescription sd = new ServiceDescription();
		sd.setName("JADE-trasporto-merci");
		sd.setType("shipper");
		DFAgentDescription template = new DFAgentDescription();
		template.addServices(sd);
		AID[] shippers = null;
		try {
			DFAgentDescription[] result = DFService.search(this, template);
			shippers = new AID[result.length];
			for (int i=0; i<result.length; ++i) 
				shippers[i] = result[i].getName();
		} catch (FIPAException fe) {
			fe.printStackTrace();
		}
		return shippers;
	}
	
	
	//TODO
	private void matching(AID[] shippers, Object[] ordini) {
		System.out.println("risultato del Matching... inserire l'algoritmo");
		
		
	}
	
	
	

	////////////////////////////////////////////////////////////////////////////////
	
	private class ReplyContratto extends ContractNetResponder{
		private static final long serialVersionUID = 1L;

		public ReplyContratto(Agent a, MessageTemplate mt) {
			super(a, mt);
		}
		
		@Override
		protected ACLMessage handleCfp(ACLMessage cfp)
				throws RefuseException, FailureException, NotUnderstoodException  {
			System.out.println("Agente "+getLocalName()+": ricevuta CFP da "
				+cfp.getSender().getName()+". Chiede '"+cfp.getContent()+"'");
			ACLMessage reply = cfp.createReply();
			
			if (goods!=null && !goods.isEmpty()){
				System.out.println("Agente "+getLocalName()+": invio PROPOSE ");
				reply.setPerformative(ACLMessage.PROPOSE);
				try {
					reply.setContentObject(goods);
				} catch (IOException e) {
					e.printStackTrace();
				}
			} else {
				System.out.println("Agente "+getLocalName()+": invio REFUSE ");
				reply.setPerformative(ACLMessage.REFUSE);
				reply.setContent("not-available");
			}
			return reply;
		}
		
		@Override
		protected ACLMessage handleAcceptProposal(ACLMessage cfp, ACLMessage propose, ACLMessage accept)
				throws FailureException {
			System.out.println("Agente "+getLocalName()+": la proposta e' stata accettata. Perform...");
			ACLMessage reply = accept.createReply();
			try {
				Vector<Goods> l = (Vector<Goods>) propose.getContentObject();
				
				//TODO devo fare il matching
				/*
				for (Goods goods : l) {
					if (BuyerAgent.this.goods.contains(goods)){
						System.out.println("Beni '"+goods.toString()+"' disponibili! Prenotato.");
						reply.setPerformative(ACLMessage.INFORM);
					} else {
						System.out.println("Beni '"+goods.toString()+"' non più disponibili!");
						reply.setPerformative(ACLMessage.FAILURE);
					}
				}
				*/
				reply.setPerformative(ACLMessage.INFORM);
				
			} catch (UnreadableException e) {
				e.printStackTrace(); 
			}
			return reply;
		}
		
		@Override
		protected void handleRejectProposal(ACLMessage cfp, ACLMessage propose, ACLMessage reject) {
			System.out.println("Agente "+getLocalName()+": proposta rifiutata");
		}
		
		
		
	}
	
	
	
	
	
	
	
	
	
	////////////////////////////////////////////////////////////////////////////////
	
	/**
	   Inner class CFPRequests Behaviour
	   TODO: fare il controllo nel caso la merce sia stata presa da altri shipper nel frattempo
	 */
	private class CFPRequests extends CyclicBehaviour {
		private static final long serialVersionUID = 1L;

		public void action() {
			MessageTemplate mt = MessageTemplate.MatchPerformative(ACLMessage.CFP);
			ACLMessage msg = myAgent.receive(mt);
			
			if (msg != null) {
				// CFP Message received. Process it
				ACLMessage reply = msg.createReply();
				
				if (goods!=null && !goods.isEmpty()){
					reply.setPerformative(ACLMessage.PROPOSE);
					try {
						reply.setContentObject(goods);
					} catch (IOException e) { e.printStackTrace(); }
				} else {
					reply.setContent("not-available");
					reply.setPerformative(ACLMessage.REFUSE);
				}
				
				/*
				Integer price = (Integer) catalogue.get(title);
				if (price != null) {
					// The requested book is available for sale. Reply with the price
					reply.setPerformative(ACLMessage.PROPOSE);
					reply.setContent(String.valueOf(price.intValue()));
				}
				else {
					// The requested book is NOT available for sale.
					reply.setPerformative(ACLMessage.REFUSE);
					reply.setContent("not-available");
				}
				*/
				myAgent.send(reply);
				
				addBehaviour(new ProposeRequests(msg.getSender()));
				
			} else {
				block();
			}
		}
	}  // End of inner class CFPRequests
	
	
	
	/**
	   Inner class ProposeRequests Behaviour
	   TODO: fare il controllo nel caso la merce sia stata presa da altri shipper nel frattempo
	   TODO: fare il matching
	 */
	private class ProposeRequests extends CyclicBehaviour {
		private static final long serialVersionUID = 1L;
		
		AID sender;
		
		public ProposeRequests(AID sender) {
			this.sender = sender;
		}

		@Override
		public void action() {
			
			MessageTemplate mt = MessageTemplate.and(
					MessageTemplate.MatchSender(sender),
					MessageTemplate.or(
							MessageTemplate.MatchPerformative(ACLMessage.ACCEPT_PROPOSAL),
							MessageTemplate.MatchPerformative(ACLMessage.REJECT_PROPOSAL)
			));
			
			ACLMessage msg = myAgent.receive(mt);
			
			if (msg!=null){
				ACLMessage reply = msg.createReply();
				
				if (msg.getPerformative()==ACLMessage.ACCEPT_PROPOSAL){
					try {
						ArrayList<Goods> l = (ArrayList<Goods>) msg.getContentObject();
						
						System.out.println("Controlla... ");
						//TODO devo fare il matching
						for (Goods goods : l) {
							if (BuyerAgent.this.goods.contains(goods)){
								System.out.println("Beni '"+goods.toString()+"' disponibili! Prenotato.");
								reply.setPerformative(ACLMessage.INFORM);
							} else {
								System.out.println("Beni '"+goods.toString()+"' non più disponibili!");
								reply.setPerformative(ACLMessage.FAILURE);
							}
						}
						
					} catch (UnreadableException e) {
						e.printStackTrace(); 
					}
					
					myAgent.send(reply);
				} else { // REJECT_PROPOSAL
					// TODO dovrei fare inviare qualcosa?
				}
				removeBehaviour(this);
			} else { // se non si riceve una risposta dallo Shipper:
				
				block();
				System.out.println("ooohohohoho");
			}
		}
	} // End of inner class ProposeRequests



	@Override
	public void addGoods(Goods g) {
		goods.add(g);
	}
	

	@Override
	public void removeGoods(Goods g) {
		goods.remove(g);
	}

	@Override
	public List<Goods> getGoods(){
		return goods;
	}
}