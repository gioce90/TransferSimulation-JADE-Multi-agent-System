package StartAgents;

import jade.core.MainContainerChecker;
import jade.core.Runtime;
import jade.core.Profile;
import jade.core.ProfileImpl;
import jade.util.leap.Properties;
import jade.wrapper.*;

import java.awt.Color;
import java.awt.Font;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;

import javax.swing.JLabel;
import javax.swing.ImageIcon;
import javax.swing.SwingConstants;
import javax.swing.ButtonGroup;
import javax.swing.JFrame;
import javax.swing.JButton;
import javax.swing.JPanel;
import javax.swing.Timer;
import javax.swing.UIManager;
import javax.swing.UnsupportedLookAndFeelException;




public class StartSystem extends JFrame {
	
	private static final long serialVersionUID = 1L;
	
	private ButtonGroup agentBtnGroup = new ButtonGroup();
	private JLabel lblOnoff = new JLabel();
	private JLabel newsLabel = new JLabel(" ");
	
	private Runtime runTime;
	private Profile mainProfile;
	private AgentContainer mainContainer; // l'interfaccia ContainerController è nuova, ma AgentConbtainer gestisce meglio le eccezioni
	
	
	public StartSystem() {
		
		setTitle("TransferSimulation 1.0");
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		
		try {
			UIManager.setLookAndFeel("javax.swing.plaf.nimbus.NimbusLookAndFeel");
			//UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
		} catch (ClassNotFoundException | InstantiationException
				| IllegalAccessException | UnsupportedLookAndFeelException e) {
			e.printStackTrace();
		}
		
		
		////////////////////////////////////
		
		GridBagLayout gbl_centerPanel = new GridBagLayout();
		gbl_centerPanel.rowWeights = new double[]{0.0, 0.0, 0.0, 1.0};
		gbl_centerPanel.columnWeights = new double[]{1.0};
		JPanel centerPanel = new JPanel(new GridBagLayout());
		getContentPane().add(centerPanel);
		
		////////////////////////////////////
		
		
		// centerPanel
		JPanel btnPanel = new JPanel();
		GridBagConstraints gbc = new GridBagConstraints();
		gbc.gridx = 0;
		gbc.gridy = 0;
		centerPanel.add(btnPanel, gbc);
		
		{
			JButton shipperBtn = new JButton("Shipper");
			agentBtnGroup.add(shipperBtn);
			shipperBtn.setToolTipText("Avvia un agente Shipper, per le aziende di trasporto");
			shipperBtn.setIcon(new ImageIcon(StartSystem.class.getResource("/images/truck-green-icon.png")));
			shipperBtn.setVerticalTextPosition(SwingConstants.BOTTOM);
			shipperBtn.setHorizontalTextPosition(SwingConstants.CENTER);
			shipperBtn.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent e) {
					startShipperAgent();
				}
			});
			
			JButton buyerBtn = new JButton("Buyer");
			agentBtnGroup.add(buyerBtn);
			buyerBtn.setToolTipText("Avvia un agente buyer, per le aziende che hanno merci");
			buyerBtn.setIcon(new ImageIcon(StartSystem.class.getResource("/images/boxes-brown-icon.png")));
			buyerBtn.setVerticalTextPosition(SwingConstants.BOTTOM);
			buyerBtn.setHorizontalTextPosition(SwingConstants.CENTER);
			buyerBtn.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent arg0) {
					startBuyerAgent();
				}
			});
			
			btnPanel.add(shipperBtn);
			btnPanel.add(buyerBtn);
		}
		
		///////////////////////////////////////
		
		
		// platformPanel:
		JPanel platformPanel = new JPanel();
		gbc = new GridBagConstraints();
		gbc.gridx = 0;
		gbc.gridy = 1;
		centerPanel.add(platformPanel, gbc);
		
		{
			JLabel lblSystemJade = new JLabel("La piattaforma attualmente \u00E8: ");
			platformPanel.add(lblSystemJade);
			lblOnoff.setOpaque(true);
			platformPanel.add(lblOnoff);
			
			startPlatform();
		}
		
		///////////////////////////////////////
		
		
		JPanel RmaPanel = new JPanel();
		gbc = new GridBagConstraints();
		gbc.gridx = 0;
		gbc.gridy = 2;
		centerPanel.add(RmaPanel, gbc);
		
		{
			final JButton btnRMA = new JButton("Avvia RMA");
			btnRMA.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent arg0) {
					startTool("rma");
				}
			});
			RmaPanel.add(btnRMA);
			
			final JButton btnSniffer = new JButton("Avvia Sniffer");
			btnSniffer.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent arg0) {
					startTool("sniffer");
				}
			});
			RmaPanel.add(btnSniffer);
			
			final JButton btnIntrospector = new JButton("Avvia Introspector");
			btnIntrospector.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent arg0) {
					startTool("introspector");
				}
			});
			RmaPanel.add(btnIntrospector);
		}

		///////////////////////////////////////
		

		JPanel anotherPanel = new JPanel();
		gbc = new GridBagConstraints();
		gbc.gridx = 0;
		gbc.gridy = 3;
		centerPanel.add(anotherPanel, gbc);
		
		{
			JButton btnOnline = new JButton("Avvia piattaforma");
			btnOnline.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent arg0) {
					startPlatform();
				}
			});
			anotherPanel.add(btnOnline);
			
			
			JButton btnKill = new JButton("Kill platform");
			btnKill.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent arg0) {
					stopPlatform();
				}
			});
			anotherPanel.add(btnKill);
		}

		///////////////////////////////////////
		
		
		JPanel newsPanel = new JPanel();
		gbc = new GridBagConstraints();
		gbc.fill = GridBagConstraints.BOTH;
		gbc.gridx = 0;
		gbc.gridy = 4;
		centerPanel.add(newsPanel, gbc);

		newsPanel.add(newsLabel);
		
		
		//////////////////////////////////////
		
		pack();
		setLocationRelativeTo(null);
		
	}
	
	
	
	// TODO to modify in future
	private void startPlatform() {
		runTime = Runtime.instance(); 	// Get a hold on JADE runtime
		//runTime.setCloseVM(true); 		// Exit the JVM when there are no more containers around
		
		if (!isPlatformActivated()){
			mainProfile = new ProfileImpl(true);
			mainContainer = runTime.createMainContainer(mainProfile);
			
			showNews("Accensione piattaforma...");
			
			lblOnoff.setText("Online");
			lblOnoff.setFont(new Font(null, Font.BOLD, 11));
			lblOnoff.setForeground(Color.GREEN);
			lblOnoff.setBackground(Color.BLACK);
			
			/* //ManagerAgent manager = new ManagerAgent(this);
			try {
				Object[] args = {this};
				Profile anotherProfile = new ProfileImpl(false);
				AgentContainer anotherContainer =  Runtime.instance().createAgentContainer(anotherProfile);
				AgentController a = anotherContainer.createNewAgent("mmanager", "transfersimulation.ManagerAgent", args);
				a.start();
			} catch (StaleProxyException e1) {
				e1.printStackTrace();
			}
			*/
		} else {
			showNews("Piattaforma già online");
		}
		
	}
	
	private void stopPlatform() {
		if (isPlatformActivated()){
			try {
				mainContainer.kill();

				showNews("Spegnimento piattaforma...");
				
				lblOnoff.setText("Offline");
				lblOnoff.setFont(new Font(null, Font.BOLD, 11));
				lblOnoff.setBackground(Color.BLACK);
				lblOnoff.setForeground(Color.RED);
				
			} catch (StaleProxyException e) {
				showNews("Piattaforma già offline");
			}
		} else {
			showNews("Piattaforma già offline");
		}
	}
	
	
	Properties props = new Properties();
	private boolean isPlatformActivated() {
		// mainProfile.getBootProperties() // TODO verificare con questo
		//props.setProperty(Profile.MAIN, "false");
		return MainContainerChecker.check(props);
	}
	
	
	private void showNews(final String news){
		newsLabel.setText(news);
		Timer t = new Timer(5000, taskPerformer);
		t.setInitialDelay(2000);
		t.setRepeats(false);
		t.start();
	}
	
	
	private ActionListener taskPerformer = new ActionListener(){		
		public void actionPerformed(ActionEvent evt){
			newsLabel.setText(" ");
		}
	};
	
	
	private void startTool(String agentName){
		
		if (isPlatformActivated()){
			
			String name = "";
			String location = "";
			Object[] args = null;
			String exception = "";
			
			switch (agentName) {
				case "rma":	
					name = "RMA";
					location = "jade.tools.rma.rma";
					exception = "RMA già attivo";
					break;
				case "sniffer":
					name = "mySniffer";
					location = "jade.tools.sniffer.Sniffer";
					args = new Object[]{"BuyerAgent1;BuyerAgent2;ShipperAgent1;ShipperAgent2"};
					exception = "Sniffer già attivo";
					break;
				case "introspector":
					name = "myIntrospector";
					location = "jade.tools.introspector.Introspector";
					exception = "Introspector già attivo";
					break;
			}
			
			try {
				AgentController toolAgent = mainContainer.createNewAgent(name, location, args);
				toolAgent.start();
			} catch (StaleProxyException e1) {
				showNews(exception);
			}
		
		} else {
			showNews("La piattaforma non è attiva");
		}
	}
	
	
	
	
	
	private void startBuyerAgent() {
		Profile anotherProfile = new ProfileImpl(false);
		AgentContainer anotherContainer =  runTime.createAgentContainer(anotherProfile);
		
		if (isPlatformActivated()){
			try {
				AgentController agent = anotherContainer.createNewAgent(
						"BuyerAgent1", "transfersimulation.BuyerAgent", new Object[0]);
				System.out.println("Starting up a BuyerAgent...");
				agent.start();
			} catch (StaleProxyException e) {
				showNews("Buyer già attivo");  // TODO e se ne voglio di più?
			}
		} else {
			showNews("La piattaforma non è attiva");
		}
	}
	
	
	private void startShipperAgent() {
		Profile anotherProfile = new ProfileImpl(false);
		AgentContainer anotherContainer =  runTime.createAgentContainer(anotherProfile);
		
		if (isPlatformActivated()){
			try {
				AgentController agent =	anotherContainer.createNewAgent(
						"ShipperAgent1", "transfersimulation.ShipperAgent", new Object[0]);
				System.out.println("Starting up a ShipperAgent...");
				agent.start();
			} catch (StaleProxyException e) {
				showNews("Shipper già attivo"); // TODO e se ne voglio di più?
			}
		} else {
			showNews("La piattaforma non è attiva");
		}
	}
	
	//TODO startAgent(String name, Agent agentType, String property?){ ...
	
	
	public static void main(String args[]) throws InterruptedException, StaleProxyException {
		StartSystem ss = new StartSystem();
		ss.setVisible(true);
	}

}


















/*
AMSSubscriber ams = new AMSSubscriber() {

	@SuppressWarnings("unchecked")
	@Override
	protected void installHandlers(Map handlersTable) {
		handlersTable.put(IntrospectionVocabulary.SHUTDOWNPLATFORMREQUESTED, new EventHandler() { 
			@Override
			public void handle(Event ev) {
				System.out.println(ev.toString());	
			} 
		}); 
	}
};
*/

