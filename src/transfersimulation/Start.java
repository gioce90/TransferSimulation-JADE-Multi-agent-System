package transfersimulation;

import jade.core.Agent;
import jade.core.Runtime;
import jade.core.Profile;
import jade.core.ProfileImpl;
import jade.wrapper.*;

public class Start extends Agent {

	
	
	public static void main(String args[]) {

		try {
			// Get a hold on JADE runtime
			Runtime rt = Runtime.instance();
			
			// Exit the JVM when there are no more containers around
			rt.setCloseVM(true);
			
			// Check whether a '-container' flag was given
			/*if(args.length > 0) {
				if(args[0].equalsIgnoreCase("-container")) {
					// Create a default profile
					Profile p = new ProfileImpl(false);
					//p.setParameter(Profile.MAIN, "false");

					// Create a new non-main container, connecting to the default
					// main container (i.e. on this host, port 1099)
					System.out.println("Launching the agent container ..."+p);
					AgentContainer ac = rt.createAgentContainer(p);

					// Create a new agent, a DummyAgent
					//AgentController dummy = ac.createNewAgent("BuyerAgent1", "/transfersimulationrepository/src/transfersimulation/BuyerAgent.java", new Object[0]);
					AgentController dummy = ac.createNewAgent("BuyerAgent1", "transfersimulation.BuyerAgent", new Object[0]);
					
					// Fire up the agent
					System.out.println("Starting up a DummyAgent...");
					dummy.start();

					// Wait for 20 seconds
					Thread.sleep(20000);

					// Kill the DummyAgent
					System.out.println("Killing DummyAgent...");
					dummy.kill();

					// Create another peripheral container within the same JVM
					// NB. Two containers CAN'T share the same Profile object!!! -->
					// Create a new one.
					p = new ProfileImpl(false);
					//p.putProperty(Profile.MAIN, "false");
					AgentContainer another = rt.createAgentContainer(p);
					
					// Launch the Mobile Agent example
					// and pass it 2 arguments: a String and an object reference
					AgentController mobile = another.createNewAgent("ShipperAgent1", "transfersimulation.ShipperAgent", new Object[0]);
					mobile.start();

					return;
				}
			}*/

			// Launch a complete platform on the 8888 port
			// create a default Profile 
			/*
			Profile pMain = new ProfileImpl(null, 8888, null);
			
			System.out.println("Launching a whole in-process platform..."+pMain);
			AgentContainer mc = rt.createMainContainer(pMain);
			
			// set now the default Profile to start a container
			ProfileImpl pContainer = new ProfileImpl(null, 8888, null);
			System.out.println("Launching the agent container ..."+pContainer);
			
			AgentContainer cont = rt.createAgentContainer(pContainer);
			System.out.println("Launching the agent container after ..."+pContainer);
			
			System.out.println("Launching the rma agent on the main container ...");
			*/
			
			//AgentController rma = mc.createNewAgent("rma", "jade.tools.rma.rma", null);
			//rma.start();

			// Launch a custom agent, taking an object via the
			// object-to-agent communication channel. Notice how an Object
			// is passed to the agent, to achieve a startup synchronization:
			// this Object is used as a POSIX 'condvar' or a Win32
			// 'EventSemaphore' object...

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
		}
		catch(Exception e) {
			e.printStackTrace();
		}

	}

}
