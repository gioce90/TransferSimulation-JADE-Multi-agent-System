package transfersimulation.protocols;

import jade.core.behaviours.Behaviour;
import jade.domain.FIPANames;
import jade.lang.acl.ACLMessage;
import jade.lang.acl.MessageTemplate;
import jade.lang.acl.UnreadableException;
import jade.proto.ContractNetInitiator;

import java.io.IOException;
import java.util.Date;
import java.util.Iterator;
import java.util.List;
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
		shipperAgent=a;
		registerHandleAllResponses(new HandleProposes());
	}
	
	@Override
	protected Vector<?> prepareCfps(ACLMessage cfp) {
		/* 
		 * Lo slot conversationId serve a rendere unica la conversazione,
		 * sia in base all'agente shipper che la comincia
		 * e sia in base al momento in cui la comincia
		 */
		long now = System.currentTimeMillis();
		cfp.setConversationId("contractNet-by-"
				+shipperAgent.getAID().getLocalName()+now);
		
		/* 
		 * In precedenza sono già stati settati come contentObject
		 * tutti i veicoli disponibili. 
		 * Il loro allestimento indicherà il tipo di merci trasportabili
		 * Questo comporterà un allegerimento del filtraggio.
		 */
		
		
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
	 * @author Cecco
	 */
	public class HandleProposes extends Behaviour {
		private static final long serialVersionUID = 1L;
		private Vector<ACLMessage> proposes;
		private Vector<ACLMessage> acceptances;
		private int numberOfProposes;
		
		public void onStart() {
			proposes = (Vector<ACLMessage>) getDataStore().get(ALL_RESPONSES_KEY);
			acceptances = (Vector<ACLMessage>) getDataStore().get(ALL_ACCEPTANCES_KEY);
			
			numberOfProposes=proposes.size();
			
			for (Iterator I=proposes.iterator(); I.hasNext();) {
				ACLMessage propose = (ACLMessage) I.next();
				if (propose.getPerformative()==ACLMessage.PROPOSE)
					myAgent.addBehaviour(new HandleSinglePropose(propose, acceptances));
				else
					numberOfProposes--;
			}
			
		}
		
		public void action() {
			if (!done())
				block();
		}
		
		public boolean done() {
			return (acceptances.size()==numberOfProposes);
		}
		
		
		
		public class HandleSinglePropose extends Behaviour {
			private static final long serialVersionUID = 1L;
			private ACLMessage propose;
			private Vector<ACLMessage> acceptances;
			private boolean finish=false;
			
			public HandleSinglePropose (ACLMessage propose, Vector<ACLMessage> acceptances) {
				this.propose=propose;
				this.acceptances=acceptances;
				
				shipperAgent.myGUI.insertInfo("Ricevuta PROPOSE da "+propose.getSender().getLocalName());
				GoodsChoiceBox gcb = new GoodsChoiceBox(shipperAgent, this, propose); // fill the JTable
				gcb.setVisible(true);
			}
			
			@Override
			public void action() {
				MessageTemplate mt = MessageTemplate.and(
						MessageTemplate.MatchSender(shipperAgent.getAID()),
						MessageTemplate.and(
							MessageTemplate.MatchReplyWith("response"+propose.getReplyWith()),
							MessageTemplate.or(
								MessageTemplate.MatchPerformative(ACLMessage.ACCEPT_PROPOSAL),
								MessageTemplate.MatchPerformative(ACLMessage.REJECT_PROPOSAL)
				) ) ) ;
				
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
					
					finish=true;
					HandleProposes.this.restart();
				} else {
					block();
				}
			}
			
			public boolean done() {
				return finish;
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
			
		} // closes HandleSinglePropose
	
	} // closes HandleProposes
	
	
	
	
	/*
	 * NON CANCELLARE
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
	*/
	
	@Override
	protected void handleFailure(ACLMessage failure) {
		if (failure.getContent().equals("change")){
			ACLMessage newCfp = new ACLMessage(ACLMessage.CFP);
			newCfp.addReceiver(failure.getSender());
			try {
				newCfp.setContentObject(shipperAgent.getAvailableVehicles());
			} catch (IOException e) {
				e.printStackTrace();
			}
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
		try {
			List<Goods> beni = (List<Goods>) inform.getContentObject();
			
			if (!beni.isEmpty())
				shipperAgent.myGUI.addGoods(beni);
			
		} catch (UnreadableException e) {
			e.printStackTrace();
		}
	}
	
	
}
