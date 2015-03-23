package test;

import jade.core.Profile;
import jade.core.ProfileImpl;
import jade.core.Runtime;
import jade.wrapper.AgentContainer;
import jade.wrapper.AgentController;
import jade.wrapper.StaleProxyException;
import test.common.testSuite.TestSuiteAgent;

public class GUITest {
	
	public static void main(String[] args) throws StaleProxyException, InterruptedException {
		
		//TestSuiteAgent tsa = new TestSuiteAgent();
		
		Runtime runTime = Runtime.instance();
		Profile anotherProfile = new ProfileImpl(false);
		AgentContainer anotherContainer = runTime.createAgentContainer(anotherProfile);
		AgentController agent = anotherContainer.createNewAgent("TSA", "test.common.testSuite.TestSuiteAgent", new Object[0]);
		agent.start();
		Thread.sleep(500);
		
		
	}
	
}
