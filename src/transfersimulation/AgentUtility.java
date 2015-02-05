package transfersimulation;

import java.util.Iterator;

import jade.core.AID;
import jade.lang.acl.ACLMessage;

public class AgentUtility{
	
	protected static void aclToString(ACLMessage msg, String nome){
		
		String sender = "n/d";
		String receivers = "n/d";
		
		if (msg.getSender()!=null)
			sender = msg.getSender().getLocalName();
		
		Iterator i = msg.getAllReceiver();
		if (i.hasNext()){
			receivers=((AID) i.next()).getLocalName();
			while(i.hasNext())
				receivers+=", "+((AID) i.next()).getLocalName();
		}
		
		System.out.println("ACL MESSAGE from "+sender+" to "+receivers
				+"\n	"+nome+"	'conversation-id':	"+msg.getConversationId()
				+"\n	"+nome+"	'performative':		"+ACLMessage.getPerformative(msg.getPerformative())
				+"\n	"+nome+"	'reply-by' (date):	"+msg.getReplyByDate()
				+"\n	"+nome+"	'in-reply-to':		"+msg.getInReplyTo()
				+"\n	"+nome+"	'reply-with':		"+msg.getReplyWith()
		);
	}
	
	public static String listReceiversToString(ACLMessage msg){
		String receivers = "n/d";
		Iterator i = msg.getAllReceiver();
		if (i.hasNext()){
			receivers=((AID) i.next()).getLocalName();
			while(i.hasNext())
				receivers+=", "+((AID) i.next()).getLocalName();
		}
		return receivers;
	}
	
	
	public static void main(String[] args) {
		ACLMessage m = new ACLMessage(ACLMessage.ACCEPT_PROPOSAL);
		m.setSender(new AID("CECCO@QUESTOPC", AID.ISGUID));
		m.addReceiver(new AID("MEMT@QUESTOPC", AID.ISGUID));
		m.addReceiver(new AID("MEMT2@QUESTOPC", AID.ISGUID));
		m.setConversationId("Conversazione 01");
		m.setInReplyTo("proposta0001");
		m.setReplyWith("accetto_a_questi_termini");
		
		aclToString(m, "accept");
	}
	
	
	
}
