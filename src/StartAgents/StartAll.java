package StartAgents;

import transfersimulation.ShipperAgent;
import transfersimulation.protocols.SearchJobInitiator;
import jade.core.Agent;
import jade.core.Runtime;
import jade.core.Profile;
import jade.core.ProfileImpl;
import jade.core.behaviours.Behaviour;
import jade.core.behaviours.OneShotBehaviour;
import jade.lang.acl.ACLMessage;
import jade.wrapper.*;

public class StartAll {
	
	public static void main(String args[]) throws InterruptedException, StaleProxyException {
		// Get a hold on JADE runtime
		final Runtime runTime = Runtime.instance();
		
		// Exit the JVM when there are no more containers around
		runTime.setCloseVM(true);
		
		// Create a profile and the main container and start RMA
		Profile mainProfile = new ProfileImpl(true);
		AgentContainer mainContainer = runTime.createMainContainer(mainProfile);
		AgentController rma = mainContainer.createNewAgent("rma", "jade.tools.rma.rma", null);
		rma.start();
		Thread.sleep(900);
		
		// Creo uno Sniffer
		AgentController sniffer = mainContainer.createNewAgent(
				"mySniffer", "jade.tools.sniffer.Sniffer",
				new Object[]{"BuyerAgent1;BuyerAgent2;ShipperAgent1;ShipperAgent2"});
		sniffer.start();
		Thread.sleep(900);
		
		// Creo un Introspector
		AgentController introspector = mainContainer.createNewAgent(
				"myIntrospector", "jade.tools.introspector.Introspector",
				null);
		introspector.start();
		Thread.sleep(900);
		
		/////////////////////////////////////////
		// Prepare for create and fire new agents:
		
		/* 	Create a new profile and a new non-main container, connecting to the
			default main container (i.e. on this host, port 1099)
			NB. Two containers CAN'T share the same Profile object: create a new one. */
		Profile anotherProfile;
		AgentContainer anotherContainer;
		AgentController agent;
		
		anotherProfile = new ProfileImpl(false);
		anotherContainer = runTime.createAgentContainer(anotherProfile);
		System.out.println("Starting up a BuyerAgent...");
		agent = anotherContainer.createNewAgent("BuyerAgent1", "transfersimulation.BuyerAgent", new Object[0]);
		agent.start();
		Thread.sleep(900);
		
		anotherProfile = new ProfileImpl(false);
		anotherContainer = runTime.createAgentContainer(anotherProfile);
		System.out.println("Starting up a BuyerAgent...");
		agent = anotherContainer.createNewAgent("BuyerAgent2", "transfersimulation.BuyerAgent", new Object[0]);
		agent.start();
		Thread.sleep(900);
		
		anotherProfile = new ProfileImpl(false);
		anotherContainer = runTime.createAgentContainer(anotherProfile);
		System.out.println("Starting up a ShipperAgent...");
		agent = anotherContainer.createNewAgent("ShipperAgent1", "transfersimulation.ShipperAgent", new Object[0]);
		agent.start();
		Thread.sleep(900);
		
		anotherProfile = new ProfileImpl(false);
		anotherContainer = runTime.createAgentContainer(anotherProfile);
		System.out.println("Starting up a ShipperAgent...");
		agent = anotherContainer.acceptNewAgent("ShipperAgent2", new ShipperAgent());
		agent.start();
		Thread.sleep(900);
		
		/*
		 * TEST:
		anotherProfile = new ProfileImpl(false);
		anotherContainer = runTime.createAgentContainer(anotherProfile);
		System.out.println("Starting up a Test Suite...");
		agent = anotherContainer.createNewAgent("TSA", "test.common.testSuite.TestSuiteAgent", new Object[0]);
		agent.start();
		Thread.sleep(500);
		*/
		
		
		/*
		// Indica le operazioni di termiazione TODO
		runTime.invokeOnTermination(new Runnable() {
			public void run() {
				runTime.shutDown();
			}
		});
		*/
		
		return;
	}
	
	
}


			// Kill the BuyerAgent1
			//System.out.println("Killing BuyerAgent...");
			//buyer1.kill();
			
			/*
			CondVar startUpLatch = new CondVar();

			AgentController custom = mc.createNewAgent("customAgent", CustomAgent.class.getName(), new Object[] { startUpLatch });
			custom.start();

			// Wait until the agent starts up and notifies the Object
			try {
				startUpLatch.waitOn();
			}
			catch(InterruptedException ie) {
				ie.printStackTrace();
			}

			// Put an object in the queue, asynchronously
			System.out.println("Inserting an object, asynchronously...");
			custom.putO2AObject("Message 1", AgentController.ASYNC);
			System.out.println("Inserted.");

			// Put an object in the queue, synchronously
			System.out.println("Inserting an object, synchronously...");
			custom.putO2AObject(mc, AgentController.SYNC);
			System.out.println("Inserted.");
			*/
		