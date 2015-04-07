package StartAgents;

import jade.core.MainContainerChecker;
import jade.core.Runtime;
import jade.core.Profile;
import jade.core.ProfileImpl;
import jade.util.leap.Properties;
import jade.wrapper.*;
import jade.wrapper.PlatformController.Listener;

import java.awt.Color;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;

import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.ImageIcon;
import javax.swing.JTextField;
import javax.swing.SwingConstants;
import javax.swing.ButtonGroup;
import javax.swing.JFrame;
import javax.swing.JButton;
import javax.swing.JPanel;
import javax.swing.SwingUtilities;
import javax.swing.Timer;
import javax.swing.UIManager;
import javax.swing.UnsupportedLookAndFeelException;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;



public class StartSystem extends JFrame {
	
	private static final long serialVersionUID = 1L;
	
	private ButtonGroup agentBtnGroup = new ButtonGroup();
	private JLabel lblOnoff = new JLabel();
	private JLabel newsLabel = new JLabel(" ");
	
	private Runtime runTime;
	private Profile mainProfile;
	private ContainerController mainContainer; // l'interfaccia ContainerController è nuova, ma AgentConbtainer gestisce meglio le eccezioni
	private	PlatformController platform;
	
	Properties props = new Properties();
	
	
	// CONSTRUCTOR
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
		
		// centerPanel
		JPanel btnPanel = new JPanel();
		GridBagConstraints gbc = new GridBagConstraints();
		gbc.gridx = 0;
		gbc.gridy = 0;
		centerPanel.add(btnPanel, gbc);
		
		{
			final JButton shipperBtn = new JButton("Shipper");
			agentBtnGroup.add(shipperBtn);
			shipperBtn.setToolTipText("Avvia un agente Shipper, per le aziende di trasporto");
			shipperBtn.setIcon(new ImageIcon(getClass().getResource("/images/truck-green-icon.png")));
			shipperBtn.setVerticalTextPosition(SwingConstants.BOTTOM);
			shipperBtn.setHorizontalTextPosition(SwingConstants.CENTER);
			shipperBtn.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent e) {
					ChooseAgentName c = new ChooseAgentName(shipperBtn, "Shipper", "Shipper1");
					c.setVisible(true);
				}
			});
			
			final JButton buyerBtn = new JButton("Buyer");
			agentBtnGroup.add(buyerBtn);
			buyerBtn.setToolTipText("Avvia un agente buyer, per le aziende che hanno merci");
			buyerBtn.setIcon(new ImageIcon(getClass().getResource("/images/boxes-brown-icon.png")));
			buyerBtn.setVerticalTextPosition(SwingConstants.BOTTOM);
			buyerBtn.setHorizontalTextPosition(SwingConstants.CENTER);
			buyerBtn.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent arg0) {
					ChooseAgentName c = new ChooseAgentName(buyerBtn, "Buyer", "Buyer1");
					c.setVisible(true);
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
					if(!isPlatformActivated())
						startPlatform();
					else
						showNews("Piattaforma già online");
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
		startPlatform();
	}
	
	
	// TODO 
	private void startPlatform() {
		runTime = Runtime.instance(); 	// Get a hold on JADE runtime
		
		if (!isPlatformActivated()){
			showNews("Accensione piattaforma");
			mainProfile = new ProfileImpl(true);
			mainContainer = runTime.createMainContainer(mainProfile);
			
		} else {
			showNews("Piattaforma già online");
			mainProfile = new ProfileImpl();
			mainProfile.setParameter(Profile.DETECT_MAIN, "true");
			mainProfile.setParameter(Profile.PLATFORM_ID, "*");
			mainContainer = runTime.createAgentContainer(mainProfile);
		}
		
		lblOnoff.setText("Online");
		lblOnoff.setFont(new Font(null, Font.BOLD, 11));
		lblOnoff.setForeground(Color.GREEN);
		lblOnoff.setBackground(Color.BLACK);

		try {
			platform = mainContainer.getPlatformController();
			platform.addPlatformListener(listener);
		} catch (ControllerException e) {
			System.out.println("getPlatformController non funziona");
		}
		
	}
	
	
	Listener listener = new Listener() {

		@Override
		public void startedPlatform(PlatformEvent anEvent) {
			lblOnoff.setText("Online");
			lblOnoff.setFont(new Font(null, Font.BOLD, 11));
			lblOnoff.setForeground(Color.GREEN);
			lblOnoff.setBackground(Color.BLACK);
			System.out.println("AGAIN!!");
		}
		
		@Override
		public void killedPlatform(PlatformEvent anEvent) {
			showNews("Spegnimento piattaforma");
			lblOnoff.setText("Offline");
			lblOnoff.setFont(new Font(null, Font.BOLD, 11));
			lblOnoff.setBackground(Color.BLACK);
			lblOnoff.setForeground(Color.RED);
		}
		
		@Override
		public void suspendedPlatform(PlatformEvent anEvent) {}
		@Override
		public void resumedPlatform(PlatformEvent anEvent) {}
		@Override
		public void deadAgent(PlatformEvent anEvent) {}
		@Override
		public void bornAgent(PlatformEvent anEvent) {}
	};
	
	
	
	private void stopPlatform() {
		if (isPlatformActivated()){
			Thread t = new Thread(new Runnable() {
				public void run() {
					try {
						platform.kill();
					} catch (ControllerException e) {
						showNews("Piattaforma già offline!");
					}
				}
			});
			t.start();
		} else {
			showNews("Piattaforma già offline");
		}
	}
	
	
	
	private boolean isPlatformActivated() {
		// mainProfile.getBootProperties() // verificare con questo
		//props.setProperty(Profile.MAIN, "true");
		return MainContainerChecker.check(props);
	}
	
	
	private void showNews(final String news){
		SwingUtilities.invokeLater(new Runnable() {
			@Override
			public void run() {
				newsLabel.setText(news);
				
				final Timer t = new Timer(0, new ActionListener() {
					@Override
					public void actionPerformed(ActionEvent e) {
						newsLabel.setText(" "+newsLabel.getText()+".");
					}
				});
				t.setDelay(1500);
				t.start();
				
				Timer t2 = new Timer(4000, new ActionListener() {
					@Override
					public void actionPerformed(ActionEvent e) {
						t.stop();
						newsLabel.setText(" ");
					}
				});
				t2.setRepeats(false);
				t2.start();
			}
		});
	}
	
	
	private void startTool(String agentType){
		
		if (isPlatformActivated()){
			
			String name="",location="",exception="";
			Object[] args = null;
			
			switch (agentType) {
				case "rma":	
					name = "RMA";
					location = "jade.tools.rma.rma";
					exception = "RMA già attivo";
					break;
				case "sniffer":
					name = "mySniffer";
					location = "jade.tools.sniffer.Sniffer";
					args = new Object[]{"Buyer*;Shipper*"};
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
	
	
	private void startAgent(final String typeAgent, final String nameAgent) {
		Thread agentThread = new Thread(new Runnable() {
			@Override
			public void run() {
				if (isPlatformActivated()){
					Profile anotherProfile = new ProfileImpl(false);
					ContainerController anotherContainer = runTime.createAgentContainer(anotherProfile);
					String location="";
					
					switch (typeAgent){
						case "Shipper":
							location = "transfersimulation.ShipperAgent";
							break;
						case "Buyer":
							location = "transfersimulation.BuyerAgent";
							break;
					}
					
					try {
						AgentController agent =	anotherContainer.createNewAgent(
								nameAgent, location, new Object[0]);
						agent.start();
					} catch (StaleProxyException e) {
						showNews("Nome occupato: sceglierne un altro");
						try {
							anotherContainer.kill();
						} catch (StaleProxyException e1) {
							e1.printStackTrace();
						}
					}
					
				} else {
					showNews("La piattaforma non è attiva");
				}
			}
		});
		agentThread.start();
	}
	
	
	@Override
	public void dispose() {
		super.dispose();
		try {
			platform.kill();
		} catch (ControllerException e) {
			e.printStackTrace();
		}
	}

	
	public static void main(String args[]) throws InterruptedException, StaleProxyException {
		StartSystem ss = new StartSystem();
		ss.setVisible(true);
	}

	
	
	
	private class ChooseAgentName extends JDialog {
		private static final long serialVersionUID = 1L;
		private JTextField textField;
		private JButton jButton;
		
		public ChooseAgentName(JButton externalBtn, final String agentType) {
			jButton = externalBtn;
			externalBtn.setEnabled(false);
			
			setTitle("Start a "+agentType);
			setAlwaysOnTop(true);
			setModal(true);
			setModalityType(ModalityType.DOCUMENT_MODAL);
			setDefaultCloseOperation(DISPOSE_ON_CLOSE);;
			
			getContentPane().setLayout(new FlowLayout());
			
			textField = new JTextField();
			final JButton btnStartAgent = new JButton("Start");
			btnStartAgent.setEnabled(false);
			
			textField.setColumns(20);
			textField.getDocument().addDocumentListener(new DocumentListener() {
				public void changedUpdate(DocumentEvent e) 	{changed();}
				public void removeUpdate(DocumentEvent e) 	{changed();}
				public void insertUpdate(DocumentEvent e) 	{changed();}
				
				public void changed() {
					if (textField.getText().equals("")){
						btnStartAgent.setEnabled(false);
					} else {
						btnStartAgent.setEnabled(true);
					}

				}
			});
			
			btnStartAgent.addActionListener(new ActionListener() {
				@Override
				public void actionPerformed(ActionEvent e) {
					startAgent(agentType, getAgentName());
					dispose();
				}
			});
			
			getContentPane().add(textField);
			getContentPane().add(btnStartAgent);
			
			pack();
			setLocationRelativeTo(null);
		}
		
		public ChooseAgentName(JButton externalBtn, final String agentType, String suggestName) {
			this(externalBtn, agentType);
			textField.setText(suggestName);
		}
		
		
		@Override
		public void dispose() {
			jButton.setEnabled(true);
			super.dispose();
		}
		
		
		private String getAgentName(){
			return textField.getText();
		}
		
	} // close JDialog

	
}
