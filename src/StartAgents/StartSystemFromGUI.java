package StartAgents;

import jade.core.MainContainerImpl;
import jade.core.Runtime;
import jade.core.Profile;
import jade.core.ProfileImpl;
import jade.wrapper.*;

import java.awt.Dimension;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Point;
import java.awt.Toolkit;
import java.awt.Insets;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;

import javax.swing.JLabel;
import javax.swing.JToggleButton;
import javax.swing.ImageIcon;
import javax.swing.SwingConstants;
import javax.swing.ButtonGroup;
import javax.swing.JFrame;
import javax.swing.JButton;
import javax.swing.JPanel;





public class StartSystemFromGUI extends JFrame {
	
	Runtime runTime;
	private Profile mainProfile;
	
	public StartSystemFromGUI() {
		
		// ... graphics stuff...
		
		JButton shipperBtn = new JButton("AGENT 1");
		//...
		shipperBtn.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				startAgent1(); // if the JADE platform is running, works!
			}
		});
		
		//... other agents...
		
		startPlatform(); // works!
		
		// here I want a JLabel for detecte the JADE platform's state:
		// ... ? ...
		// and a Jbutton for activate/deactivate the JADE platform:
		// ... ? ...
		
	}
	
	

	
	// Works:
	private void startAgent1() {
		Profile anotherProfile = new ProfileImpl(false);
		AgentContainer anotherContainer =  runTime.createAgentContainer(anotherProfile);
		
		System.out.println("Starting up a Agent1...");
		try {
			AgentController agent = anotherContainer.
					createNewAgent("Agent1", "transfersimulation.Agent1", new Object[0]);
			agent.start();
		} catch (StaleProxyException e) {
			e.printStackTrace();
		}
	}
	
	
	// to modify in future...
	private void startPlatform() {
		runTime = Runtime.instance();
		runTime.setCloseVM(true);
		
		mainProfile = new ProfileImpl(true);
		
		AgentContainer mainContainer;
		try {
			mainContainer = runTime.createMainContainer(mainProfile);
			AgentController rma = mainContainer.createNewAgent("RMA", "jade.tools.rma.rma", null);
			rma.start();
		} catch (Exception e){
			System.out.println("eeeeee");
			mainProfile = new ProfileImpl(false);
			mainContainer = runTime.createAgentContainer(mainProfile);
			AgentController rma;
			try {
				rma = mainContainer.createNewAgent("RMA", "jade.tools.rma.rma", null);
				rma.start();
			} catch (StaleProxyException e1) {
				e1.printStackTrace();
			}
		}
		
	}
	

	
	// this doesn't works:
	protected void startRMA() {
		AgentContainer mainContainer = 
				runTime.createMainContainer(mainProfile);
		
		try {
			PlatformController platformController = mainContainer.getPlatformController();
			
			AgentController rma = platformController.getAgent("RMA");
			
			//rma.start();
		} catch (StaleProxyException e1) {
			e1.printStackTrace();
		} catch (ControllerException e1) {
			e1.printStackTrace();
		}
	}
	
	public static void main(String args[]) throws InterruptedException, StaleProxyException {
		StartSystemFromGUI ss = new StartSystemFromGUI();
		ss.setVisible(true);
	}
	
	
}