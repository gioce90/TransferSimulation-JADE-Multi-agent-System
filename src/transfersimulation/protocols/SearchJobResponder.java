package transfersimulation.protocols;

import jade.core.Agent;
import jade.domain.FIPAAgentManagement.FailureException;
import jade.domain.FIPAAgentManagement.NotUnderstoodException;
import jade.domain.FIPAAgentManagement.RefuseException;
import jade.lang.acl.ACLMessage;
import jade.lang.acl.UnreadableException;
import jade.proto.SSContractNetResponder;

import java.io.IOException;
import java.sql.Date;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.Vector;

import transfersimulation.AgentUtility;
import transfersimulation.BuyerAgent;
import transfersimulation.model.goods.Goods;

public class SearchJobResponder extends SSContractNetResponder {
	private static final long serialVersionUID = 1L;

	static BuyerAgent agent;
	
	
	public SearchJobResponder(Agent a, ACLMessage cfp) {
		super(a, cfp);
		agent=(BuyerAgent) a;
	}
	
	
	@Override
	protected ACLMessage handleCfp(ACLMessage cfp)
			throws RefuseException, FailureException, NotUnderstoodException  {
		System.out.println("Agente "+myAgent.getLocalName()
				+": ricevuta CFP da "+cfp.getSender().getLocalName());
		//AgentUtility.aclToString(cfp,"cfp");
		
		ACLMessage reply = cfp.createReply();
		
		Vector<Goods> goods = agent.getGoods();
		if (goods!=null && !goods.isEmpty()){
			System.out.println("Agente "+myAgent.getLocalName()
					+": invio PROPOSE all'agente "+cfp.getSender().getLocalName());
			reply.setPerformative(ACLMessage.PROPOSE);
			try {
				reply.setContentObject(goods);
			} catch (IOException e) {
				e.printStackTrace();
			}
		} else {
			System.out.println("Agente "+myAgent.getLocalName()
					+": invio REFUSE all'agente "+cfp.getSender().getLocalName());
			reply.setPerformative(ACLMessage.REFUSE);
			reply.setContent("not-available");
		}
		
		//AgentUtility.aclToString(reply,"decision(propose/refuse)");
		return reply;
	}
	
	
	@Override
	protected ACLMessage handleAcceptProposal(ACLMessage cfp, ACLMessage propose, ACLMessage accept)
			throws FailureException {
		System.out.println("Agente "+myAgent.getLocalName()+
				": l'agente "+accept.getSender().getLocalName()+" ha accettato la proposta. Perform...");
		ACLMessage reply = accept.createReply();
		
		//AgentUtility.aclToString(accept,"accept");
		
		try {
			//Vector<Goods> goodsProposed = (Vector<Goods>) propose.getContentObject();
			Vector<Goods> selectedGoods = (Vector<Goods>) accept.getContentObject();
			System.out.println(selectedGoods.toString());
			
			//TODO devo fare il matching:
			/*
			 * verificherò che tutte le merci scelte dall'utente in accept siano ancora presenti in goods.
			 * Il matchig va fatto tra le merci in goodsModel e accept.
			 * Se sono tutte presenti invierò una reply INFORM
			 * Se una o più merci sono mancanti, la reply sarà una FAILURE
			 * 
			 */
			
			boolean matchResult = matching(agent.getGoods(),selectedGoods);
			
			if (matchResult){
				reply.setPerformative(ACLMessage.INFORM);
				removeGoodsFromModel(selectedGoods);
			} else {
				throw new FailureException("change");
			}
			
		} catch (UnreadableException e) {
			e.printStackTrace(); 
		}
		
		//AgentUtility.aclToString(reply,"inform");
		
		return reply;
	}
	
	
	private void removeGoodsFromModel(Vector<Goods> selectedGoods) {
		for (Goods goods : selectedGoods) {
			agent.removeGoods(goods);
		}
	}


	@Override
	protected void handleRejectProposal(ACLMessage cfp, ACLMessage propose, ACLMessage reject) {
		System.out.println("Agente "+myAgent.getLocalName()
				+": l'agente "+reject.getSender().getLocalName()+" ha rifiutato la proposta");
		//AgentUtility.aclToString(reject,"reject");
	}
	
	
	/////////////////////////////////////////////////////
	// Utility
	
	private boolean matching(Vector<Goods> borsaMerci, Vector<Goods> selectedGoods) {
		boolean bool=true;
		for (Goods s : selectedGoods) {
			if (!matchingGoodsToGoods(borsaMerci, s)){
				bool=false;
				System.out.println("La merce "+s.getCodice()+" NON è più presente nella borsa merci");
			} else System.out.println("La merce "+s.getCodice()+" è presente nella borsa merci");
		}
		if (bool)
			System.out.println("Corrispondenza al 100%");
		else
			System.out.println("Qualcosa è cambiato");
		return bool;
	}
	
	private boolean matchingGoodsToGoods(Vector<Goods> borsaMerci, Goods selectedGoods) {
		boolean bool=false;
		for (Goods goods : borsaMerci) {
			if (goods.equals(selectedGoods))
				bool=true;
		}
		return bool;
	}
	
}
