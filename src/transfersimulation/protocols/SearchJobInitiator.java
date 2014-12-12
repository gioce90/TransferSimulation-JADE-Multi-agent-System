package transfersimulation.protocols;

import jade.core.Agent;
import jade.core.behaviours.Behaviour;
import jade.domain.FIPANames;
import jade.lang.acl.ACLMessage;
import jade.proto.ContractNetInitiator;

import java.io.IOException;
import java.util.Vector;

import transfersimulation.AgentUtility;
import transfersimulation.model.goods.Goods;
import transfersimulation.table.GoodsChoiceBox;

public class SearchJobInitiator extends ContractNetInitiator {
	private static final long serialVersionUID = 1L;
	
	public SearchJobInitiator(Agent a, ACLMessage cfp) {
		super(a, cfp);
		registerHandlePropose(new HandlePropose());
	}
	
	@Override
	protected Vector<?> prepareCfps(ACLMessage cfp) {
		cfp.setConversationId("contrattazione-by-shipper-"+myAgent.getAID().getLocalName());
		cfp.setContent("Fammi delle proposte di lavoro");
		cfp.setProtocol(FIPANames.InteractionProtocol.FIPA_CONTRACT_NET);
		//cfp.setReplyWith("cfp"+System.currentTimeMillis()) inutile settarlo, viene ignorato.
		
		System.out.println("Agente "+myAgent.getLocalName()
				+": invio CFP a "+AgentUtility.listReceiversToString(cfp));
		//AgentUtility.aclToString(cfp,"cfp");
		
		return super.prepareCfps(cfp);
	}

	
	
	@Override
	protected void handleRefuse(ACLMessage refuse) {
		System.out.println("Refuse");
		//AgentUtility.aclToString(refuse,"refuse");
	}
	
	@Override
	protected void handleOutOfSequence(ACLMessage msg) {
		System.out.println("Out of Sequence:");
		AgentUtility.aclToString(msg,"msg");
		reinit();
	}
	
	
	public class HandlePropose extends Behaviour {
		private static final long serialVersionUID = 1L;
		private ACLMessage propose;
		private Vector<ACLMessage> acceptances;
		
		
		public void onStart() {
			propose = (ACLMessage) getDataStore().get(REPLY_KEY);
			acceptances = (Vector<ACLMessage>) getDataStore().get(ALL_ACCEPTANCES_KEY);
			System.out.println("Agente "+myAgent.getLocalName()
					+": ricevuta PROPOSE da "+propose.getSender().getLocalName());
			count++;
			GoodsChoiceBox gcb = new GoodsChoiceBox(myAgent, this, propose); // fill the JTable
			gcb.setVisible(true);
		}
		
		private int count;
		
		public void action() {
			if (count!=0)
				block();
		}
		
		public boolean done() {
			return count==0;
		}
		
		public void handleChoice(ACLMessage propose, boolean accept, Vector<Goods> selectedGoods) {
			ACLMessage acceptance;
	        acceptance = propose.createReply();
	        acceptance.setPerformative(accept ? ACLMessage.ACCEPT_PROPOSAL : ACLMessage.REJECT_PROPOSAL);
	        if (selectedGoods!=null){
		        try {
		        	acceptance.setContentObject(selectedGoods);
		        } catch (IOException e) {
					e.printStackTrace();
				}
	        }
	        acceptances.add(acceptance);
	        count--;
	        restart();
		}
	}
	
	
	
	@Override
	protected void handleFailure(ACLMessage failure) {
		System.out.println("Agente "+myAgent.getLocalName()
				+": l'agente "+failure.getSender().getLocalName()
				+" ha fallito nell'eseguire l'azione");
		
		if (failure.getContent().equals("change")){
			ACLMessage newCfp = new ACLMessage(ACLMessage.CFP);
			newCfp.addReceiver(failure.getSender());
			myAgent.addBehaviour(new SearchJobInitiator(myAgent, newCfp));
		}
		//AgentUtility.aclToString(failure,"failure");
	}
	
	
	@Override
	protected void handleInform(ACLMessage inform) {
		System.out.println("Agente "+myAgent.getLocalName()
				+": l'agente "+inform.getSender().getLocalName()
				+" ha eseguito l'azione con successo");
		//AgentUtility.aclToString(inform,"inform");
	}
	
	
	
	
	
	
	
}
