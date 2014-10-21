package transfersimulation;

import jade.core.AID;
import jade.core.Agent;
import jade.core.behaviours.OneShotBehaviour;
import jade.core.behaviours.TickerBehaviour;
import jade.domain.DFService;
import jade.domain.FIPAException;
import jade.domain.FIPAAgentManagement.DFAgentDescription;
import jade.domain.FIPAAgentManagement.ServiceDescription;
import jade.lang.acl.ACLMessage;

public class CompanyAgent extends Agent {
	
	private static final long serialVersionUID = 1L;
	private String goods;
	private String quantity;
	private String location;
	private AID[] shipperAgents;
	
	
	@Override
	protected void setup() {
		// Printout a welcome message
		System.out.println("Ciao! Company Agent "+getAID().getName()+" pronto!");
		
		// Determina la merce
		whichGoods();
		
		// Add a TickerBehaviour that schedules a request to Shipper agents every 10 sec
		addBehaviour(new TickerBehaviour(this, 10000) {
			@Override
			protected void onTick() {
				// TODO Auto-generated method stub
				// Aggiorna la lista degli shipper agents
				DFAgentDescription template = new DFAgentDescription();
				ServiceDescription sd = new ServiceDescription();
				sd.setType("trasporto-merci");
				template.addServices(sd);
				try {
					DFAgentDescription[] result = DFService.search(myAgent,template);
					System.out.println("Trovati i seguenti Shipper Agent:");
					shipperAgents = new AID[result.length];
					for (int i = 0; i < result.length; ++i) {
						shipperAgents[i] = result[i].getName();
						System.out.println(shipperAgents[i].getName());
					}
				} catch (FIPAException fe) {
					fe.printStackTrace();
				}
				
				// Perform the request
				if (shipperAgents.length!=0){
					myAgent.addBehaviour(new RequestPerformer());
					//myAgent.addBehaviour(new RequestPerformer());
				}
			}
		});
	}
	
	
	private void whichGoods() {
		Object[] args = getArguments();
		if (args != null && args.length == 3) {
			goods = (String) args[0];
			quantity = (String) args[1];
			location = (String) args[2];
			
			System.out.println("L'azienda vuole trasferire "+quantity+"tn di "+goods+" da "+location);
		} else {
			// Make the agent terminate
			System.out.println("Dati non specificati");
			doDelete();
		}
	}
	
	
	
	private class RequestPerformer extends OneShotBehaviour {
		@Override
		public void action() {
			// Invia a ogni Shipper Agent una propose:
			ACLMessage propose = new ACLMessage(ACLMessage.PROPOSE);
			for (int i = 0; i < shipperAgents.length; ++i) {
				propose.addReceiver(shipperAgents[i]);
			}
			propose.setContent(goods+" "+quantity+" "+location);
			propose.setConversationId("trasporto-merci");
			propose.setReplyWith("propose"+System.currentTimeMillis()); // Unique value
			myAgent.send(propose);
		}
	}
	
	
}
