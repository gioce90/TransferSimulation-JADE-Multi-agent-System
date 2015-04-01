package transfersimulation;

import java.io.Serializable;
import java.sql.Date;
import java.util.Vector;

import transfersimulation.model.goods.Goods;
import transfersimulation.protocols.SearchJobResponder;
import jade.core.AID;
import jade.core.Agent;
import jade.core.behaviours.Behaviour;
import jade.domain.DFService;
import jade.domain.FIPAException;
import jade.domain.FIPANames;
import jade.domain.FIPAAgentManagement.DFAgentDescription;
import jade.domain.FIPAAgentManagement.ServiceDescription;
import jade.lang.acl.ACLMessage;
import jade.lang.acl.MessageTemplate;
import jade.proto.SSResponderDispatcher;
 

public class BuyerAgent extends Agent implements BuyerInterface, Serializable {
	private static final long serialVersionUID = 3399019455702807074L;
	
	public BuyerAgentGUI myGUI;
	private Vector<Goods> goods;
	
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
		m.setBuyer(this.getLocalName());
		goods.add(m);
		
		Goods m1 = new Goods();
		m1.setCodice("Cod2");
		m1.setDescrizione("Petrolio");
		m1.setDimensione("x*y*z");
		m1.setPericolosa(true);
		m1.setQuantità(100);
		m1.setTipo("liquida");
		m1.setVolume(200);
		m1.setLocationStart("Lecce");
		m1.setLocationEnd("Roma");
		m1.setDateStart(Date.valueOf("2014-10-22"));
		m1.setDateLimit(6);
		m1.setBuyer(this.getLocalName());
		m1.setNecessità("Cisterna");
		goods.add(m1);
		
		Goods m2 = new Goods();
		m2.setCodice("Cod3");
		m2.setDescrizione("Cibo refrigerato");
		m2.setDimensione("x*y*z");
		m2.setPericolosa(true);
		m2.setQuantità(100);
		m2.setTipo("solida");
		m2.setVolume(200);
		m2.setLocationStart("Lecce");
		m2.setLocationEnd("Roma");
		m2.setDateStart(Date.valueOf("2014-10-22"));
		m2.setDateLimit(6);
		m2.setBuyer(this.getLocalName());
		m2.setNecessità("Frigo");
		goods.add(m2);
		
		Goods m3 = new Goods();
		m3.setCodice("Cod4");
		m3.setDescrizione("Pietra");
		m3.setDimensione("x*y*z");
		m3.setPericolosa(true);
		m3.setQuantità(100);
		m3.setTipo("solida");
		m3.setVolume(200);
		m3.setLocationStart("Milano");
		m3.setLocationEnd("Roma");
		m3.setDateStart(Date.valueOf("2014-10-22"));
		m3.setDateLimit(6);
		m3.setBuyer(this.getLocalName());
		goods.add(m3);
		
		Goods m4 = new Goods();
		m4.setCodice("Cod5");
		m4.setDescrizione("Olio");
		m4.setDimensione("x*y*z");
		m4.setPericolosa(true);
		m4.setQuantità(100);
		m4.setTipo("liquida");
		m4.setVolume(200);
		m4.setLocationStart("Milano");
		m4.setLocationEnd("Roma");
		m4.setDateStart(Date.valueOf("2014-10-22"));
		m4.setDateLimit(6);
		m4.setNecessità("Cisterna");
		m4.setBuyer(this.getLocalName());
		
		goods.add(m4);
		
		/*
		Transport t1 = new Transport(m1, "x", "y", Date.valueOf("2014-10-22"), 5);
		Transport t2 = new Transport(m2, "y", "z", Date.valueOf("2014-10-22"), 3);
		transports = new Vector<Transport>();
		transports.add(t1);
		*/
		//final Object[] ordini = {t1, t2};
		
		// GRAFICA E PRESENTAZIONE
		myGUI = new BuyerAgentGUI(this);
		
		// Printout a welcome message
		myGUI.insertInfo("Ciao! Buyer Agent "+getAID().getName()+" pronto!");
		
		
		// Pubblica sulle Pagine Gialle il proprio servizio
		publishService();
		
		
		// MessageTemplate per filtrare i messaggi:
		final MessageTemplate template = MessageTemplate.and(
			MessageTemplate.MatchProtocol(FIPANames.InteractionProtocol.FIPA_CONTRACT_NET),
			MessageTemplate.MatchPerformative(ACLMessage.CFP) );
		
		// SSResponderDispatcher per gestire le varie CFP degli ShipperAgents:
		SSResponderDispatcher dispatcher = new SSResponderDispatcher(this, template) {
			private static final long serialVersionUID = 1L;
			BuyerAgent b = (BuyerAgent) this.myAgent;
			protected Behaviour createResponder(ACLMessage initiationMsg) {
				// SearchJobResponder per singola cfp:
				return new SearchJobResponder(b, initiationMsg);
			}
		};
		addBehaviour(dispatcher);
		
		
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
			myGUI.insertInfo("Registrazione sulle pagine gialle... avvenuta con successo");
		} catch (FIPAException fe) {
			fe.printStackTrace();
		}
	}
	
	
	@Override
	protected void takeDown() {
		super.takeDown();
		try {
			DFService.deregister(this); // deregister from the yellow pages
		} catch (FIPAException fe) {
			fe.printStackTrace();
		}
		
		// Printout a dismissal message:
		myGUI.insertInfo("Buyer Agent "+getAID().getName()+" terminato.");
		
		myGUI.dispose();// Close the GUI
		
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
	
	
	////////////////////////////////////////////////////////////////////////////////
	
	
	@Override
	public void addGoods(Goods g) {
		goods.add(g);
		myGUI.insertInfo("Nuova merce inserita");
	}
	
	  
	@Override
	public void removeGoods(Goods g) {
		goods.remove(g);
		myGUI.goodsModel.deleteRow(g); //Potrebbe dare problemi in futuro
		myGUI.insertInfo("Merce ("+g.getCodice()+") rimossa");
	}
	
	
	@Override
	public Vector<Goods> getGoods(){
		return goods;
	}
	
	
}