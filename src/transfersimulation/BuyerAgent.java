package transfersimulation;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.sql.Date;
import java.util.List;
import java.util.Vector;

import transfersimulation.model.goods.Goods;
import jade.core.AID;
import jade.core.Agent;
import jade.core.behaviours.CyclicBehaviour;
import jade.domain.DFService;
import jade.domain.FIPAException;
import jade.domain.FIPAAgentManagement.DFAgentDescription;
import jade.domain.FIPAAgentManagement.ServiceDescription;
import jade.lang.acl.ACLMessage;

public class BuyerAgent extends Agent implements BuyerInterface {
	private static final long serialVersionUID = 3399019455702807074L;
	
	BuyerAgentGUI myGUI;
	Vector<Goods> goods;
	
	@Override
	protected void setup() {
		super.setup();
		
		publishService();

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
		
			try {
				ACLMessage acl = blockingReceive();
				ACLMessage reply = acl.createReply();
				reply.setPerformative(ACLMessage.INFORM);
				reply.setContentObject(goods);
				this.send(reply);
			} catch (IOException e) {
				e.printStackTrace();
			}
		
		
		/*
		// TODO da rimuovere
		addBehaviour(new CyclicBehaviour() {
			private static final long serialVersionUID = 1L;
			
			@Override
			public void action() {
				System.out.println("\nInserire:\n"
						//+ "0 per chiudere l'agente\n"
						+ "1 per visualizzare le aziende disponibili\n"
						+ "2 per richiedere un trasporto\n"
						+ "3 per renderti disponibile per un trasporto"
				);
				
				BufferedReader leggi=new BufferedReader(new InputStreamReader(System.in));
				try{
					int tuoInt = Integer.parseInt(leggi.readLine());
					switch (tuoInt){
					case 1: {
						System.out.println("Le aziende di trasporto disponibili sono:");
						//AID[] shippers = searchShippers(ordini);
						//for (AID i:shippers)
						//	System.out.println(i.getName());
					} break;
					case 2: {
						System.out.println("Richiesta di un trasporto");
						//AID[] shippers = searchShippers(ordini);
						
					} break;
					case 3: {
						System.out.println("Renditi disponibile per un trasporto");
						//System.out.println("Matching...");
						//matching(shippers, ordini);
						ACLMessage acl = blockingReceive();
						ACLMessage reply = acl.createReply();
						reply.setPerformative(ACLMessage.INFORM);
						reply.setContentObject(goods);
						myAgent.send(reply);
					} break;	
					}
				} catch (IOException e1) {
					e1.printStackTrace();
				}
			}
		});*/
		
		
	}
	
	
	//TODO
	private void matching(AID[] shippers, Object[] ordini) {
		System.out.println("risultato del Matching... inserire l'algoritmo");
		
		
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
	
	public List<Goods> getGoods(){
		return goods;
	}
	
	
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
		}
		catch (FIPAException fe) {
			fe.printStackTrace();
		}
		return shippers;
	}
	
}
