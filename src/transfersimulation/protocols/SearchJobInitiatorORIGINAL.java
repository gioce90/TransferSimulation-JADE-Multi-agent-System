package transfersimulation.protocols;

import jade.core.AID;
import jade.core.Agent;
import jade.core.behaviours.Behaviour;
import jade.domain.FIPANames;
import jade.lang.acl.ACLMessage;
import jade.lang.acl.MessageTemplate;
import jade.lang.acl.UnreadableException;
import jade.proto.ContractNetInitiator;

import java.io.IOException;
import java.util.Vector;

import transfersimulation.AgentUtility;
import transfersimulation.model.goods.Goods;
import transfersimulation.table.GoodsChoiceBox;

public class SearchJobInitiatorORIGINAL extends ContractNetInitiator {
	private static final long serialVersionUID = 1L;

	public SearchJobInitiatorORIGINAL(Agent a, ACLMessage cfp) {
		super(a, cfp);
		registerHandlePropose(new HandlePropose());
	}

	@Override
	protected Vector<?> prepareCfps(ACLMessage cfp) {
		cfp.setConversationId("contrattazione-by-shipper-" + myAgent.getAID().getLocalName());
		cfp.setContent("Fammi delle proposte di lavoro");
		cfp.setProtocol(FIPANames.InteractionProtocol.FIPA_CONTRACT_NET);
		// cfp.setReplyWith("cfp"+System.currentTimeMillis()) inutile settarlo,
		// viene ignorato.

		System.out.println("Agente " + myAgent.getLocalName() + ": invio CFP a "
				+ AgentUtility.listReceiversToString(cfp));
		// AgentUtility.aclToString(cfp,"cfp");

		return super.prepareCfps(cfp);
	}

	@Override
	protected void handleRefuse(ACLMessage refuse) {
		System.out.println("Refuse");
		// AgentUtility.aclToString(refuse,"refuse");
	}

	@Override
	protected void handleOutOfSequence(ACLMessage msg) {
		System.out.println("Out of Sequence:");
		AgentUtility.aclToString(msg, "msg");
		reinit();
	}

	public class HandlePropose extends Behaviour {
		private static final long serialVersionUID = 1L;
		// private Boolean finished = false;
		private int count;
		private Vector<ACLMessage> acceptances;
		private ACLMessage propose;

		public void onStart() {
			// if (ds.containsKey(REPLY_KEY)){
			propose = (ACLMessage) getDataStore().get(REPLY_KEY);
			acceptances = (Vector<ACLMessage>) getDataStore().get(ALL_ACCEPTANCES_KEY);
			System.out.println("Agente " + myAgent.getLocalName() + ": ricevuta PROPOSE da "
					+ propose.getSender().getLocalName());
			// AgentUtility.aclToString(propose,"propose");

			count++;

			GoodsChoiceBox gcb = new GoodsChoiceBox(myAgent, this, propose); // fill
																				// the
																				// JTable
			gcb.setVisible(true);
		}

		public void action() {
			MessageTemplate mt = MessageTemplate.and(MessageTemplate.MatchSender(myAgent.getAID()),
					MessageTemplate.and(MessageTemplate.MatchReplyWith("response" + propose.getReplyWith()),
							MessageTemplate.or(MessageTemplate.MatchPerformative(ACLMessage.ACCEPT_PROPOSAL),
									MessageTemplate.MatchPerformative(ACLMessage.REJECT_PROPOSAL))));

			ACLMessage decisionFromGUI = myAgent.receive(mt); // Read data from
																// GUI: the user
																// accept or
																// reject
			if (decisionFromGUI != null) {
				ACLMessage reply = propose.createReply();
				reply.setPerformative(decisionFromGUI.getPerformative());
				try {
					Vector<Goods> list = (Vector<Goods>) decisionFromGUI.getContentObject();
					if (list != null)
						reply.setContentObject(list);
				} catch (IOException | UnreadableException e) {
					e.printStackTrace();
				}

				// AgentUtility.aclToString(reply,"decision");

				acceptances.add(reply);
				count--;
				// finished = true;
			} else {
				block();
			}
		}

		public boolean done() {
			return count == 0;
		}
	}

	@Override
	protected void handleFailure(ACLMessage failure) {
		System.out.println("Agente " + myAgent.getLocalName() + ": l'agente "
				+ failure.getSender().getLocalName() + " ha fallito nell'eseguire l'azione");

		if (failure.getContent().equals("change")) {
			ACLMessage newCfp = new ACLMessage(ACLMessage.CFP);
			newCfp.addReceiver(failure.getSender());
			myAgent.addBehaviour(new SearchJobInitiatorORIGINAL(myAgent, newCfp));
		}
		// AgentUtility.aclToString(failure,"failure");
	}

	@Override
	protected void handleInform(ACLMessage inform) {
		System.out.println("Agente " + myAgent.getLocalName() + ": l'agente "
				+ inform.getSender().getLocalName() + " ha eseguito l'azione con successo");
		// AgentUtility.aclToString(inform,"inform");
	}
}
