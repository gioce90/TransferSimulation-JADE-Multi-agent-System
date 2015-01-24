package transfersimulation.protocols;

import jade.domain.FIPAAgentManagement.FailureException;
import jade.domain.FIPAAgentManagement.NotUnderstoodException;
import jade.domain.FIPAAgentManagement.RefuseException;
import jade.lang.acl.ACLMessage;
import jade.lang.acl.UnreadableException;
import jade.proto.SSContractNetResponder;

import java.io.IOException;
import java.util.Date;
import java.util.Vector;

import transfersimulation.BuyerAgent;
import transfersimulation.model.goods.Goods;
import transfersimulation.model.vehicle.Vehicle;

public class SearchJobResponder extends SSContractNetResponder {
	private static final long serialVersionUID = 1L;

	BuyerAgent buyerAgent;
	
	public SearchJobResponder(BuyerAgent a, ACLMessage cfp) {
		super(a, cfp);
		buyerAgent = a;
	}
	
	
	@Override
	protected ACLMessage handleCfp(ACLMessage cfp)
			throws RefuseException, FailureException, NotUnderstoodException  {
		buyerAgent.myGUI.insertInfo("Ricevuta CFP da "+cfp.getSender().getLocalName());
		
		ACLMessage reply = cfp.createReply();
		reply.setReplyWith(buyerAgent.getName()+System.currentTimeMillis());
		
		Vector<Goods> goods = buyerAgent.getGoods();
		
		Vector<Vehicle> availableVehicles = null;
		try {
			availableVehicles = (Vector<Vehicle>) cfp.getContentObject();
		} catch (UnreadableException e1) {
			e1.printStackTrace();
		}
		
		/* Matching tra i beni del buyer e i veicoli dello Shipper */
		goods = matchingGoodsAndVehicles(goods, availableVehicles);
		
		if (goods!=null && !goods.isEmpty()){
			buyerAgent.myGUI.insertInfo("Invio PROPOSE a "+cfp.getSender().getLocalName());
			reply.setPerformative(ACLMessage.PROPOSE);
			try {
				reply.setContentObject(goods);
			} catch (IOException e) {
				e.printStackTrace();
			}
			
			/*
			 * L'utente dello shipper agent ha 120 secondi per 
			 * rispondere alla PROPOSE, o il protocollo si interrompe
			*/
			reply.setReplyByDate(new Date(System.currentTimeMillis()+120000)); //TODO
			
		} else {
			buyerAgent.myGUI.insertInfo("Invio REFUSE a "+cfp.getSender().getLocalName());
			reply.setPerformative(ACLMessage.REFUSE);
			reply.setContent("not-available");
		}
		
		return reply;
	}
	

	@Override
	protected ACLMessage handleAcceptProposal(ACLMessage cfp, ACLMessage propose, ACLMessage accept)
			throws FailureException {
		ACLMessage reply = accept.createReply();
		
		try {
			Vector<Goods> selectedGoods = (Vector<Goods>) accept.getContentObject();
			System.out.println(selectedGoods.toString());
			
			/*
			 * verifico che tutte le merci scelte dall'utente in accept siano ancora presenti.
			 * Il matchig va fatto tra le merci in goodsModel e accept.
			 * Se sono tutte presenti invierò una reply INFORM
			 * Se una o più merci sono mancanti, la reply sarà una FAILURE e
			 * la comunicazione ricomincerà
			 */
			
			boolean matchResult = matching(buyerAgent.getGoods(),selectedGoods);
			
			if (matchResult){
				reply.setPerformative(ACLMessage.INFORM);
				reply.setContentObject(selectedGoods);
				buyerAgent.myGUI.insertInfo("Proposta accettata da "+accept.getSender().getLocalName());
				for (Goods goods : selectedGoods)
					buyerAgent.removeGoods(goods);
			} else {
				buyerAgent.myGUI.insertInfo("Fallimento nella contrattazione con "+cfp.getSender().getLocalName());
				throw new FailureException("change");
			}
			
		} catch (UnreadableException | IOException e) {
			e.printStackTrace(); 
		}
		/*
		try {
			Thread.sleep(11000);
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		*/
		return reply;
	}
	


	@Override
	protected void handleRejectProposal(ACLMessage cfp, ACLMessage propose, ACLMessage reject) {
		if (reject!=null)
			buyerAgent.myGUI.insertInfo("Proposta rifiutata da "+reject.getSender().getLocalName());
	}
	
	
	@Override
	protected void handleOutOfSequence(ACLMessage msg) {
		super.handleOutOfSequence(msg);
		buyerAgent.myGUI.insertInfo("Messaggio Out-of-Sequence ricevuto da "+msg.getSender().getLocalName());
		//AgentUtility.aclToString(msg,"msg");
	}
	
	/////////////////////////////////////////////////////
	// Utility
	
	private boolean matching(Vector<Goods> borsaMerci, Vector<Goods> selectedGoods) {
		boolean bool=true;
		for (Goods s : selectedGoods) {
			if (!matchingGoodsToGoods(borsaMerci, s)){
				bool=false;
				//System.out.println("La merce "+s.getCodice()+" NON è più presente nella borsa merci");
			} //else System.out.println("La merce "+s.getCodice()+" è presente nella borsa merci");
		}
		/*
		if (bool)
			System.out.println("Agente "+buyerAgent.getLocalName()+": Corrispondenza al 100%");
		else
			System.out.println("Agente "+buyerAgent.getLocalName()+": Qualcosa è cambiato");
		*/
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
	
	/**
	 * Certi beni possono essere trasportati solo da un certo tipo di
	 * veicoli, dotati di un certo tipo di allestimenti e accorgimenti.
	 * In questa versione, il matching si limita solo al tipo di allestimento.
	 * In futuro raccomandiamo di sostituire questo algoritmo e impiegare
	 * un motore inferenziale
	 * @param goods
	 * @param availableVehicles
	 * @return
	 */
	private Vector<Goods> matchingGoodsAndVehicles(Vector<Goods> goods, Vector<Vehicle> availableVehicles) {
		Vector<Goods> proposta = new Vector<Goods>();
		if (availableVehicles!=null){
			if (goods!=null){
				for (Goods g : goods) {
					String necessità = g.getNecessità();
					if (necessità!=null){
						for (Vehicle vehicle : availableVehicles) {
							String allestimento = vehicle.getAllestimento();
							if (allestimento!=null && allestimento.equals(necessità)){
								proposta.add(g);
								break;
							}
						}
					} else
						proposta.add(g);
				}
			}
		}
		return proposta;
	}

	
}
