package transfersimulation.protocols;

import jade.core.Agent;
import jade.core.behaviours.Behaviour;
import jade.domain.FIPANames;
import jade.lang.acl.ACLMessage;
import jade.lang.acl.MessageTemplate;
import jade.lang.acl.UnreadableException;
import jade.proto.ContractNetInitiator;

import java.io.IOException;
import java.util.Date;
import java.util.Vector;

import transfersimulation.AgentUtility;
import transfersimulation.ShipperAgent;
import transfersimulation.model.goods.Goods;
import transfersimulation.table.GoodsChoiceBox;

public class SearchJobInitiator extends ContractNetInitiator {
	private static final long serialVersionUID = 1L;
	
	ShipperAgent shipperAgent;
	
	public SearchJobInitiator(ShipperAgent a, ACLMessage cfp) {
		super(a, cfp);
		shipperAgent= a;
		registerHandlePropose(new HandlePropose());
	}
	
	@Override
	protected Vector<?> prepareCfps(ACLMessage cfp) {
		/* 
		 * Lo slot conversationId serve a rendere unica la conversazione,
		 * sia in base all'agente shipper che la comincia
		 * e sia in base al momento in cui la comincia
		 */
		long now = System.currentTimeMillis();
		cfp.setConversationId("contractNet-by-shipper-"
				+shipperAgent.getAID().getLocalName()+now);
		
		cfp.setContent("Fammi delle proposte di lavoro");
		cfp.setProtocol(FIPANames.InteractionProtocol.FIPA_CONTRACT_NET);
		
		/* 
		 * Gli agenti buyers hanno 10 secondi per rispondere, o il protocollo si interrompe.
		 * Non è un problema perché la prima risposta è automatica: i buyer
		 * rispondono immediatamente con una PROPOSE/REFUSE.
		 */
		cfp.setReplyByDate(new Date(now+10000));
		
		/* 
		 * È inutile settare lo slot replyWith, perché viene sovrascritto
		 * al termine di questo metodo.
		 */
		//cfp.setReplyWith("cfp"+System.currentTimeMillis())
		
		shipperAgent.myGUI.insertInfo("Invio CFP a "+AgentUtility.listReceiversToString(cfp));
		
		return super.prepareCfps(cfp);
	}
	
	@Override
	protected void handleRefuse(ACLMessage refuse) {
		shipperAgent.myGUI.insertInfo("Ricevuta REFUSE da "+refuse.getSender().getLocalName());
	}
	
	@Override
	protected void handleOutOfSequence(ACLMessage msg) {
		shipperAgent.myGUI.insertInfo("Messaggio Out-of-Sequence ricevuto da "+msg.getSender().getLocalName());
	}
	
	/**
	 * 
	 * @author Cecco
	 *
	 */
	public class HandlePropose extends Behaviour {
		private static final long serialVersionUID = 1L;
		private ACLMessage propose;
		private Vector<ACLMessage> acceptances;
		private int count;
		
		public void onStart() {
			//in caso di classi statiche:
			//propose = (ACLMessage) getDataStore().get(((ContractNetInitiator) getParent()).REPLY_KEY);
			//acceptances = (Vector<ACLMessage>) getDataStore().get(((ContractNetInitiator) getParent()).ALL_ACCEPTANCES_KEY);
			propose = (ACLMessage) getDataStore().get(REPLY_KEY);
			acceptances = (Vector<ACLMessage>) getDataStore().get(ALL_ACCEPTANCES_KEY);
			shipperAgent.myGUI.insertInfo("Ricevuta PROPOSE da "+propose.getSender().getLocalName());
			count++;
			GoodsChoiceBox gcb = new GoodsChoiceBox(shipperAgent, this, propose); // fill the JTable
			gcb.setVisible(true);
		}
		
		public void action() {
			MessageTemplate mt = MessageTemplate.and(
					MessageTemplate.MatchSender(shipperAgent.getAID()),
					MessageTemplate.and(
						MessageTemplate.MatchReplyWith("response"+propose.getReplyWith()),
						MessageTemplate.or(
							MessageTemplate.MatchPerformative(ACLMessage.ACCEPT_PROPOSAL),
							MessageTemplate.MatchPerformative(ACLMessage.REJECT_PROPOSAL)
			) ) );
			
			ACLMessage decisionFromGUI = shipperAgent.receive(mt); // Read data from GUI: the user accept or reject
			if (decisionFromGUI != null) {
				ACLMessage reply = propose.createReply();
				reply.setPerformative(decisionFromGUI.getPerformative());
				reply.setReplyByDate(new Date(System.currentTimeMillis()+10000));
				
				try {
					Vector<Goods> list = (Vector<Goods>) decisionFromGUI.getContentObject();
					if (list!=null)
						reply.setContentObject(list);
				} catch (IOException | UnreadableException e) {
					e.printStackTrace();
				}
				
				if (System.currentTimeMillis()<=propose.getReplyByDate().getTime())
					acceptances.add(reply);
				else
					shipperAgent.myGUI.insertInfo("La proposta di "+propose.getSender().getLocalName()+" è scaduta ");
				
				count--;
			} else {
				block();
			}
		}

		public boolean done() {
			return (count==0);
		}
		
		public void handleChoice(ACLMessage propose, boolean bool, Vector<Goods> selectedGoods) {
			ACLMessage reply;
			if (bool){
				reply = new ACLMessage(ACLMessage.ACCEPT_PROPOSAL);
				try {
					reply.setContentObject(selectedGoods);
				} catch (IOException e) {
					e.printStackTrace();
				}
				shipperAgent.myGUI.insertInfo("Inviata ACCEPT PROPOSAL a "
						+propose.getSender().getLocalName());
			} else {
				reply = new ACLMessage(ACLMessage.REJECT_PROPOSAL);
				shipperAgent.myGUI.insertInfo("Inviata REJECT PROPOSAL a "
						+propose.getSender().getLocalName());
			}
			reply.addReceiver(shipperAgent.getAID());
			reply.setReplyWith("response"+propose.getReplyWith());
			shipperAgent.send(reply);
		}
		
	}
	
	
	@Override
	protected void handleFailure(ACLMessage failure) {
		if (failure.getContent().equals("change")){
			ACLMessage newCfp = new ACLMessage(ACLMessage.CFP);
			newCfp.addReceiver(failure.getSender());
			shipperAgent.addBehaviour(new SearchJobInitiator(shipperAgent, newCfp));
			shipperAgent.myGUI.insertInfo("Il buyer "+failure.getSender().getLocalName()
					+" ha una nuova proposta");
		} else {
			shipperAgent.myGUI.insertInfo("Il buyer "+failure.getSender().getLocalName()
					+" ha fallito nell'eseguire l'azione");
		}
		//AgentUtility.aclToString(failure,"failure");
	}
	
	
	@Override
	protected void handleInform(ACLMessage inform) {
		shipperAgent.myGUI.insertInfo("Accordo raggiunto con "+inform.getSender().getLocalName());
		//AgentUtility.aclToString(inform,"inform");
	}
	
	
}
