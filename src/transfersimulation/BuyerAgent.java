package transfersimulation;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.sql.Date;
import java.util.List;
import java.util.Vector;

import transfersimulation.model.goods.Goods;
import transfersimulation.model.goods.Transport;
import jade.core.AID;
import jade.core.Agent;
import jade.core.behaviours.CyclicBehaviour;
import jade.domain.DFService;
import jade.domain.FIPAException;
import jade.domain.FIPAAgentManagement.DFAgentDescription;
import jade.domain.FIPAAgentManagement.ServiceDescription;

public class BuyerAgent extends Agent {
	private static final long serialVersionUID = 3399019455702807074L;
	
	BuyerAgentGUI myGUI;
	Vector<Goods> goods;
	Vector<Transport> transports;
	
	@Override
	protected void setup() {
		super.setup();
		
		publishService();
		
		Goods m1 = new Goods("Cod1", "Merce1", "Dimensioni x*y*z", 10, 0.2);
		Goods m2 = new Goods("Cod2", "Merce2", "Dimensioni x*y*z", 5, 1);
		Goods m3 = new Goods("Cod3", "Merce2", "Dimensioni x*y*z", 2, 3);
		
		goods = new Vector<Goods>();
		goods.add(m1); goods.add(m2); goods.add(m3);
		
		Transport t1 = new Transport(m1, "x", "y", Date.valueOf("2014-10-22"));
		Transport t2 = new Transport(m2, "y", "z", Date.valueOf("2014-10-22"));
		
		final Object[] ordini = {t1, t2};
		
		
		// GRAFICA E PRESENTAZIONE
		myGUI = new BuyerAgentGUI(this);
		myGUI.showGui();
		
		
		
		// TODO da rimuovere
		addBehaviour(new CyclicBehaviour() {
			private static final long serialVersionUID = 1L;

			@Override
			public void action() {
				System.out.println("\nInserire:\n"
						+ "0 per chiudere l'agente\n"
						+ "1 per visualizzare le aziende disponibili\n"
						+ "2 per richiedere un trasporto"
				);
				
				BufferedReader leggi=new BufferedReader(new InputStreamReader(System.in));
				try{
					int tuoInt = Integer.parseInt(leggi.readLine());
					switch (tuoInt){
					case 0:
						doDelete();
						break;
					case 1: {
						System.out.println("Le aziende di trasporto disponibili sono:");
						AID[] shippers = searchShippers(ordini);
						for (AID i:shippers)
							System.out.println(i.getName());
					} break;
					case 2: {
						System.out.println("Richiesta di un trasporto");
						AID[] shippers = searchShippers(ordini);
						System.out.println("Matching...");
						matching(shippers, ordini);
						
					} break;
					case 3:
						break;
							
					}
				} catch (IOException e1) {
					e1.printStackTrace();
				}
			}

		});
		
		
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
	
	public List<Transport> getTransports(){
		return transports;
	}
	
	public List<Goods> getGoods(){
		return goods;
	}
	
	protected AID[] searchShippers(Object[] trasporti) {
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
