package it.transfersimulation;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

import jade.core.AID;
import jade.core.Agent;
import jade.core.behaviours.CyclicBehaviour;
import jade.domain.DFService;
import jade.domain.FIPAException;
import jade.domain.FIPAAgentManagement.DFAgentDescription;
import jade.domain.FIPAAgentManagement.ServiceDescription;

public class BuyerAgent extends Agent {
	private static final long serialVersionUID = 3399019455702807074L;
	
	@Override
	protected void setup() {
		super.setup();
		
		publishService();
		
		Goods m1 = new Goods("Merce1", "Dimensioni x*y*z", 10);
		Goods m2 = new Goods("Merce2", "Dimensioni x*y*z", 5);
		Goods m22 = new Goods("Merce2", "Dimensioni x*y*z", 2);
		
		Transport t1 = new Transport(m1, "x");
		Transport t2 = new Transport(m2, "x");
		Transport t3 = new Transport(m22, "y");
		
		final Object[] trasporti = {t1, t2, t3};
		
		
		addBehaviour(new CyclicBehaviour() {
			
			@Override
			public void action() {
				System.out.println("\nInserire:\n"
						+ "0 per chiudere l'agente\n"
						+ "1 per visualizzare le aziende disponibili");
				
				BufferedReader leggi=new BufferedReader(new InputStreamReader(System.in));
				try{
					int tuoInt = Integer.parseInt(leggi.readLine());
					if (tuoInt==1){
						System.out.println("Le aziende di trasporto disponibili sono:");
						searchShippers(trasporti);
					} else if(tuoInt==0)
						this.myAgent.doDelete();
				} catch (IOException e1) {
					e1.printStackTrace();
				}
			}
		});
		
		
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
	
	
	// Put agent clean-up operations here
	@Override
	protected void takeDown() {
		try {
			DFService.deregister(this); // deregister from the yellow pages
		} catch (FIPAException fe) {fe.printStackTrace();}
		//myGUI.dispose();// Close the GUI
		// Printout a dismissal message:
		System.out.println("Buyer Agent "+getAID().getName()+" terminato.");
	}
	
	////////////////////////////////////////////////////////////////////////////////
	
	private void searchShippers(Object[] trasporti) {
		ServiceDescription sd = new ServiceDescription();
		sd.setName("JADE-trasporto-merci");
		sd.setType("shipper");
		DFAgentDescription template = new DFAgentDescription();
		template.addServices(sd);
		try {
			DFAgentDescription[] result = DFService.search(this, template);
			//System.out.println("Trovati i seguenti clienti: ");
			AID[] shipperAgents;
			shipperAgents = new AID[result.length];
			
			for (int i = 0; i < result.length; ++i) {
				shipperAgents[i] = result[i].getName();
				System.out.println(shipperAgents[i].getName());
			}
		}
		catch (FIPAException fe) {
			fe.printStackTrace();
		}
	}
	
}
