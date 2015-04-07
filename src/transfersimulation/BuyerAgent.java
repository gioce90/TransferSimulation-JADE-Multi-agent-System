package transfersimulation;

import java.io.File;
import java.io.IOException;
import java.io.Serializable;
import java.util.Vector;

import org.jdom2.JDOMException;

import XmlParser.BuyerXmlReader;
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
		

		BuyerXmlReader buyerParser;
		
		try {
			buyerParser = new BuyerXmlReader(getAID().getLocalName());
			goods = buyerParser.getGoods();
		} catch (JDOMException | IOException e) {
			try {
				new File(getAID().getLocalName()+".xml").createNewFile();
			} catch (IOException e1) {
				e1.printStackTrace();
			}
		}
		
		
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