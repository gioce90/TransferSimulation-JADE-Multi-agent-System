package transfersimulation;

import java.util.Map;

import StartAgents.StartSystem;
import jade.core.Agent;
import jade.domain.introspection.AMSSubscriber;
import jade.domain.introspection.Event;
import jade.domain.introspection.IntrospectionVocabulary;

public class ManagerAgent extends Agent {

	private static final long serialVersionUID = 1L;
	
	StartSystem system;
	
	public ManagerAgent() {
		//system=(StartSystem) getArguments()[0];
	}
	
	@Override
	protected void setup() {
		super.setup();
		AMSSubscriber ams = new AMSSubscriber() {

			@SuppressWarnings("unchecked")
			@Override
			protected void installHandlers(Map handlersTable) {
				
				handlersTable.put(IntrospectionVocabulary.SHUTDOWNPLATFORMREQUESTED, new EventHandler() { 
					private static final long serialVersionUID = 1L;
					@Override
					public void handle(Event ev) {
						//system.setOffline();
						System.out.println("ehichez");
					} 
				});
				
				handlersTable.put(IntrospectionVocabulary.PLATFORMDESCRIPTION, new EventHandler() { 
					private static final long serialVersionUID = 1L;
					@Override
					public void handle(Event ev) {
						//system.setOnline();
						System.out.println("uchez");
					} 
				});
			}
		};
		addBehaviour(ams);
		
	}
}



/*

handlersTable.put(IntrospectionVocabulary.KILLCONTAINERREQUESTED_CONTAINER, new EventHandler() { 
	private static final long serialVersionUID = 1L;
	@Override
	public void handle(Event ev) {
		System.out.println("BUHAHAHHA 1");	
	} 
});

handlersTable.put(IntrospectionVocabulary.KILLCONTAINERREQUESTED, new EventHandler() { 
	private static final long serialVersionUID = 1L;
	@Override
	public void handle(Event ev) {
		System.out.println("BUHAHAHHA 2");	
	} 
}); 

*/