package transfersimulation;

import java.util.List;

import transfersimulation.model.goods.Transport;
import jade.core.Agent;

public class MatcherAgent extends Agent {
	private static final long serialVersionUID = 1L;
	
	
	public MatcherAgent() {
		
	}
	
	
	public MatcherAgent(BuyerAgent buyer) {
		List<Transport> transport = buyer.getTransports();
		for (Transport t:transport)
			t.getDateStart();
		
	}
	
	
	
	
	
	
	
	
	
	
	
}
