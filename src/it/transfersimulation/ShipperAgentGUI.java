package it.transfersimulation;

import it.transfersimulation.VehicleTableModel.COLUMNS;
import it.transfersimulation.model.Vehicle;

import javax.swing.ImageIcon;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.SwingUtilities;
import javax.swing.UIManager;
import javax.swing.UnsupportedLookAndFeelException;
import javax.swing.BoxLayout;
import javax.swing.event.TableModelEvent;
import javax.swing.event.TableModelListener;
import javax.swing.JButton;
import javax.swing.SwingConstants;

import java.util.Iterator;
import java.util.Vector;
import java.awt.FlowLayout;
import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.Toolkit;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;
import java.awt.ComponentOrientation;


@SuppressWarnings("serial")
public class ShipperAgentGUI extends JFrame implements ActionListener {

	// Variabili di classe
	private JPanel masterPanel;
	private JButton btnPM_plus;
	private JButton btnPM_meno;
	private JButton btnMD_plus;
	private JButton btnMD_meno;
	 
	// Headers, TableModels, JTables and Coordinators for the tables
	private COLUMNS[] parkModelHeader = {COLUMNS.IMAGE_COLUMN, COLUMNS.TARGA_COLUMN,
		COLUMNS.CAR_TYPE_COLUMN, COLUMNS.MARCA_COLUMN, COLUMNS.STATE_COLUMN, COLUMNS.PTT_COLUMN };
	private COLUMNS[] availablesModelHeader = {COLUMNS.IMAGE_COLUMN, COLUMNS.TARGA_COLUMN,
		COLUMNS.CAR_TYPE_COLUMN, COLUMNS.MARCA_COLUMN };
	
	private VehicleTableModel parkModel = new VehicleTableModel(parkModelHeader);
	private VehicleTableModel availablesModel = new VehicleTableModel(availablesModelHeader);
	
	private VehicleTable parkTable;
	private VehicleTable availablesTable;
	
	//private Coordinator parkCoordinator;
	//private Coordinator availablesCoordinator;
	
	// My third-part software, a JADE agent:
	protected ShipperAgent shipperAgent;
	
	
	// --------------------------------------------------------------------------
	
	////////////////////////////////////////////////////
	// COSTRUTTORE
	
	ShipperAgentGUI(ShipperAgent agent) {
		
		// Valorizza l'agente corrispondente
		shipperAgent = agent;
		
		///////////////////////////////////////////////////////////////////////
		// Graphics:
		
		setTitle("Shipper Agent: "+agent.getLocalName());
		setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		
		try {
			UIManager.setLookAndFeel("javax.swing.plaf.nimbus.NimbusLookAndFeel");
			//UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
		} catch (ClassNotFoundException | InstantiationException
				| IllegalAccessException | UnsupportedLookAndFeelException e) {
			e.printStackTrace();
		}
		
		// MasterPanel
		masterPanel = new JPanel();
		masterPanel.setLayout(new BoxLayout(masterPanel, BoxLayout.Y_AXIS));
		masterPanel.setComponentOrientation(ComponentOrientation.LEFT_TO_RIGHT);
		getContentPane().add(masterPanel, BorderLayout.CENTER);
		
		// Park Panel
		JPanel parkPanel = new JPanel();
		parkPanel.setLayout(new BoxLayout(parkPanel, BoxLayout.Y_AXIS));
		masterPanel.add(parkPanel);
		
		JPanel pnlHeaderParkPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
		JLabel parkLabel = new JLabel("Parco auto:");
		pnlHeaderParkPanel.add(parkLabel);
		parkPanel.add(pnlHeaderParkPanel);
		
		JPanel pnlTableParkPanel = new JPanel();
		pnlTableParkPanel.setLayout(new BoxLayout(pnlTableParkPanel, BoxLayout.X_AXIS));
		parkPanel.add(pnlTableParkPanel);
		
		// Park Table
		parkTable = new VehicleTable(parkModel);
		JScrollPane parkScrollPane = new JScrollPane(parkTable);
		pnlTableParkPanel.add(parkScrollPane);

		JPanel pnlBtnParkPanel = new JPanel();
		pnlTableParkPanel.add(pnlBtnParkPanel);
		pnlBtnParkPanel.setLayout(new BoxLayout(pnlBtnParkPanel, BoxLayout.Y_AXIS));

		// JButtons: add/remove vehicle in Park Table
		btnPM_plus = new JButton();
		btnPM_plus.setToolTipText("Aggiungi mezzo");
		btnPM_plus.setIcon(new ImageIcon(ShipperAgentGUI.class.getResource("/images/lorry-add.png")));
		btnPM_plus.setActionCommand("+parco");
		btnPM_plus.addActionListener(this);
		pnlBtnParkPanel.add(btnPM_plus);

		btnPM_meno = new JButton();
		btnPM_meno.setToolTipText("Rimuovi mezzo");
		btnPM_meno.setIcon(new ImageIcon(ShipperAgentGUI.class.getResource("/images/lorry-delete.png")));
		btnPM_meno.setActionCommand("-parco");
		btnPM_meno.addActionListener(this);
		pnlBtnParkPanel.add(btnPM_meno);
		
		
		// Arrow Panel
		JPanel arrowPanel = new JPanel();
		masterPanel.add(arrowPanel);

		// JButtons: available or not vehicle
		btnMD_plus = new JButton();
		btnMD_plus.setToolTipText("Rendi disponibile il mezzo selezionato");
		btnMD_plus.setIcon(new ImageIcon(ShipperAgentGUI.class.getResource("/images/arrow-green-down.png")));
		arrowPanel.add(btnMD_plus);
		btnMD_plus.setActionCommand("+disponibili");
		btnMD_plus.addActionListener(this);

		btnMD_meno = new JButton();
		btnMD_meno.setToolTipText("Rendi indisponibile il mezzo selezionato");
		btnMD_meno.setIcon(new ImageIcon(ShipperAgentGUI.class.getResource("/images/arrow-red-up.png")));
		arrowPanel.add(btnMD_meno);
		btnMD_meno.setActionCommand("-disponibili");
		btnMD_meno.addActionListener(this);

		
		// Availables Panel
		JPanel availablesPanel = new JPanel();
		masterPanel.add(availablesPanel);
		availablesPanel.setLayout(new BoxLayout(availablesPanel, BoxLayout.Y_AXIS));
		
		JPanel pnlHeaderAvailablesPanel = new JPanel();
		FlowLayout fl_pnlHeaderAvailablesPanel = (FlowLayout) pnlHeaderAvailablesPanel.getLayout();
		fl_pnlHeaderAvailablesPanel.setAlignment(FlowLayout.LEFT);
		availablesPanel.add(pnlHeaderAvailablesPanel);
		JLabel label_1 = new JLabel("Disponibili:");
		pnlHeaderAvailablesPanel.add(label_1);
		label_1.setHorizontalAlignment(SwingConstants.LEFT);

		// Available Table
		availablesTable = new VehicleTable(availablesModel);
		JScrollPane availablesScrollPane = new JScrollPane(availablesTable);
		availablesPanel.add(availablesScrollPane);
		
		// Search Panel
		JPanel searchPanel = new JPanel();
		masterPanel.add(searchPanel);
		JButton btnSearch = new JButton("Search");
		searchPanel.add(btnSearch);

		// End of graphics init
		///////////////////////////////////
		
		
		/////////////////////////////////////////////////////////////////////
		// Coordinators (ispirati al Mediator pattern)
		
		
		
		
		/////////////////////////////////////////////////////
		// Contatto con l'agente - Riempimento dati
		
		Vector<Vehicle> veicoli = shipperAgent.getVehicles();
		
		Iterator<Vehicle> I = veicoli.iterator();
		while (I.hasNext()){
			addVehicle(parkCoordinator, I.next());
		}
		
		
		////////////////////////////
		// Show GUI
		showGui();
	}
	
	
	
	///////////////////////////////////////////////////////////////////////
	// Methods
	///////////////////////////////////////////////////////////////////////
	
	
	public void showGui() {
		pack();
		Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
		int centerX = (int) screenSize.getWidth() / 2;
		int centerY = (int) screenSize.getHeight() / 2;
		setLocation(centerX - getWidth() / 2, centerY - getHeight() / 2);
		super.setVisible(true);
	}
	
	
	//////////////////////////////////////////////
	// actionPerformed method

	@Override
	public void actionPerformed(ActionEvent e) {
		switch (e.getActionCommand()) {
		case "+parco": {
			new VehicleInsertJDialog(this, parkCoordinator);
		} break;

		case "-parco": {
			int selectedRow = parkTable.getSelectedRow();
			if (selectedRow != -1)
				removeVehicle(parkCoordinator, selectedRow);
		} break;

		case "+disponibili": {
			int selectedRow = parkTable.getSelectedRow();
			if (selectedRow != -1){
				addVehicle(availablesCoordinator, parkModel.getVehicleAt(selectedRow));	
			}
		} break;

		case "-disponibili": {
			int selectedRow = availablesTable.getSelectedRow();
			if (selectedRow != -1)
				removeVehicle(availablesCoordinator, selectedRow);
		} break;

		default:
			System.out.println("Imprevisto in actionPerformed()");
			break;
		}
	}
	
	
	// /////////////////////////////////////
	// Add/Remove vehicles methods
	
	private void addVehicle(Coordinator coordinator, Vehicle v) {
		coordinator.notifyAndAddRow(v);
	}
	
	public void removeVehicle(Coordinator coordinator, Vehicle v) {
		int row = coordinator.indexOf(v);
		if (row!=-1)
			coordinator.notifyAndDeleteRow(row);
	}
	
	public void removeVehicle(Coordinator coordinator, int index) {
		coordinator.notifyAndDeleteRow(index);
	}
	
	
	// on dispose, delete the agent
	public void dispose() {
		super.dispose();
		shipperAgent.doDelete(); 
	}
	
	
	
	
	///////////////////////////////////////
	// INNER CLASS
	///////////////////////////////////////
	
	protected abstract class Coordinator {
		private VehicleTableModel tableModel;

		public Coordinator(VehicleTableModel tm) {
			tableModel = tm;
			notifyRowUpdated();
		}

		public abstract void notifyAndAddRow(Vehicle vehicle);
		public abstract void notifyAndDeleteRow(int rowIndex);
		public abstract void notifyRowUpdated();
		
		// TODO attenzione forse c'è un dupplicato in un'altra classe

		public int indexOf(Vehicle v) {
			return tableModel.indexOf(v);
		}
		
		boolean vehicleExists(Vehicle vehicle){
			int bool = indexOf(vehicle);
			if (bool==-1) return false;
			else return true;
		}
		
		
	}
	
	
	// Coordinatore della tabella park
	Coordinator parkCoordinator = new Coordinator(parkModel) {
		
		@Override
		public void notifyAndAddRow(final Vehicle vehicle) {
			if (!vehicleExists(vehicle)){
				// TODO comunica all'agente
				shipperAgent.newTruck(vehicle.getPlate());
				SwingUtilities.invokeLater(new Runnable() {
					@Override
					public void run() {
						parkModel.addRow(vehicle);
						if (vehicle.getState().equals(Stato.DISPONIBILE))
							availablesModel.addRow(vehicle);
					}
				});
			}
		}
		
		@Override
		public void notifyAndDeleteRow(final int rowIndex) {
			final Vehicle v = parkModel.getVehicleAt(rowIndex);
			
			//availablesCoordinator.notifyAndDeleteRow(rowIndex); // Rimuove anche dai veicoli disponibili
			removeVehicle(availablesCoordinator, v);
			
			// comunica all'agente
			shipperAgent.removeTruck(v.getPlate());
			SwingUtilities.invokeLater(new Runnable() {
				@Override
				public void run() {
					parkModel.removeRow(rowIndex);
				}
			});
		}
		
		@Override
		public void notifyRowUpdated() {
			parkModel.addTableModelListener(new TableModelListener() {
				public void tableChanged(TableModelEvent e) {
					switch (e.getType()) {
						case (TableModelEvent.INSERT): System.out.println("un inserimento in parkModel");
							break;
						case (TableModelEvent.DELETE): System.out.println("una cancellazione in parkModel");
							parkTable.repaint();
							break;
						case (TableModelEvent.UPDATE): System.out.println("un aggiornamento in parkModel");
							int row = e.getLastRow();
							Vehicle v = parkModel.getVehicleAt(row);
							if (v.getState().equals(Stato.DISPONIBILE)){
								addVehicle(availablesCoordinator, v);
								availablesTable.repaint();
							} else
								removeVehicle(availablesCoordinator, v);
							parkTable.repaint();
							break;
					}
				}
			});
		}
	};
	
	
	// Coordinatore della tabella availables
	Coordinator availablesCoordinator = new Coordinator(availablesModel) {
		
		@Override
		public void notifyAndAddRow(final Vehicle vehicle) {
			if (!vehicleExists(vehicle)){
				vehicle.setStato(Stato.DISPONIBILE);
				parkTable.repaint();
				shipperAgent.activateTruck(vehicle.getPlate());
				SwingUtilities.invokeLater(new Runnable() {
					@Override
					public void run() {
						availablesModel.addRow(vehicle);
					}
				});
			}
		}
		
		@Override
		public void notifyAndDeleteRow(final int rowIndex) {
			Vehicle v = availablesModel.getVehicleAt(rowIndex);
			if (v!=null){
				v.setStato(Stato.NON_DISPONIBILE); // TODO attenzione
				shipperAgent.deactivateTruck(v.getPlate());
				
				SwingUtilities.invokeLater(new Runnable() {
					@Override
					public void run() {
						availablesModel.removeRow(rowIndex);
					}
				});
			}
		}

		@Override
		public void notifyRowUpdated() {
			availablesModel.addTableModelListener(new TableModelListener() {
				public void tableChanged(TableModelEvent e) {
					switch (e.getType()) {
					case (TableModelEvent.INSERT): System.out.println("un inserimento in availablesModel");
						break;
					case (TableModelEvent.DELETE): System.out.println("una cancellazione in availablesModel");
						parkTable.repaint();
						break;
					case (TableModelEvent.UPDATE): System.out.println("un aggiornamento in availablesModel");
						parkTable.repaint();
						break;
					}
				}
			});
		}
	};
	
}
