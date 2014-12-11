package transfersimulation;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Vector;

import jade.core.Agent;
import jade.core.behaviours.Behaviour;
import jade.core.behaviours.OneShotBehaviour;
import jade.lang.acl.ACLMessage;
import jade.lang.acl.UnreadableException;
import jade.proto.ContractNetInitiator;
import transfersimulation.ShipperAgent.IniziaContratto.HandlePropose;
import transfersimulation.ShipperAgent.IniziaContratto.NewHandlePropose;
import transfersimulation.model.goods.Goods;
import transfersimulation.table.GoodsChoiceBox;

public class Contract extends ContractNetInitiator {
	public Contract(Agent a, ACLMessage cfp) {
		super(a, cfp);
		HandlePropose h2 = new HandlePropose();
		registerHandlePropose(h2);
	}
	/*... ...*/

	private class HandlePropose extends Behaviour {
		private Boolean finished = false;
		private Vector acceptances;
		private ACLMessage propose;
		
		public void onStart() {
			propose = (ACLMessage) getDataStore().get(REPLY_KEY);
			acceptances = (Vector) getDataStore().get(ALL_ACCEPTANCES_KEY);
			System.out.println("Agente "+getLocalName()
					+": ricevuta PROPOSE da "+propose.getSender().getLocalName());
			GoodsChoiceBox gcb = new GoodsChoiceBox(myAgent, propose); // fill the JTable
			gcb.setVisible(true);
		}
		
		public void action() {
			ACLMessage decisionMsg = receive(); // Read data from GUI
			if (decisionMsg != null) {
				ACLMessage reply = propose.createReply();
				reply.setPerformative(decisionMsg.getPerformative());
				try {
					ArrayList<Goods> list = (ArrayList<Goods>) decisionMsg.getContentObject();
					if (list!=null)
						reply.setContentObject(list);
				} catch (IOException | UnreadableException e) {
					e.printStackTrace();
				}
				acceptances.add(reply);
				finished = true;
			} else {
				block();
			}
		}
		
		public boolean done() {
			return finished;
		}
		
	}// closes HandlePropose
	
} // closes Contract








public GoodsChoiceBox(final Agent agent, final ACLMessage propose){
	this(); //graphic stuff
	//fill JTable
	btnEsegui.addActionListener(new ActionListener() {
		public void actionPerformed(ActionEvent arg0) {
			agent.addBehaviour(new OneShotBehaviour() {
				public void action() {
					ACLMessage reply = new ACLMessage(ACLMessage.ACCEPT_PROPOSAL);
					System.out.println("Agent "+agent.getLocalName()
						+": send ACCEPT PROPOSAL to "+propose.getSender().getLocalName());
					reply.addReceiver(agent.getAID());
					agent.send(reply);
					dispose();
				}
			});
		}
	});
	//btnAnnulla.addActionListener(... equal... 
}

