package transfersimulation;

import jade.util.leap.Serializable;

import javax.swing.ImageIcon;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.SwingUtilities;
import javax.swing.UIManager;
import javax.swing.UnsupportedLookAndFeelException;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.SwingConstants;
import javax.swing.event.TableModelEvent;
import javax.swing.event.TableModelListener;
import javax.swing.text.DefaultCaret;

import transfersimulation.model.goods.Goods;
import transfersimulation.model.vehicle.Vehicle;
import transfersimulation.model.vehicle.Vehicle.Stato;
import transfersimulation.table.DataObjectTableModel;
import transfersimulation.table.InsertVehicleJDialog;
import transfersimulation.table.VehicleTable;
import transfersimulation.table.VehicleTableModel;
import transfersimulation.table.VehicleTableModel.COLUMNS;

import java.awt.FlowLayout;
import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.Toolkit;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;
import java.awt.ComponentOrientation;

import javax.swing.JTextArea;
import javax.swing.JTabbedPane;
import javax.swing.JTable;

import java.awt.Component;
import java.util.ArrayList;
import java.util.List;


@SuppressWarnings("serial")
public class ShipperAgentGUI extends JFrame implements ActionListener, Serializable {

	// Variabili di classe
	private JButton btnPM_plus;
	private JButton btnPM_meno;
	private JButton btnMD_plus;
	private JButton btnMD_meno;
	
	// Headers, TableModels, JTables and Coordinators for the tables
	private COLUMNS[] parkModelHeader = {COLUMNS.IMAGE_COLUMN, COLUMNS.TARGA_COLUMN,
		COLUMNS.TYPE_COLUMN, COLUMNS.MARK_COLUMN, COLUMNS.SETTINGUP_COLUMN, COLUMNS.STATE_COLUMN, COLUMNS.PTT_COLUMN, COLUMNS.LOCATION_COLUMN };
	private COLUMNS[] availablesModelHeader = {COLUMNS.IMAGE_COLUMN, COLUMNS.TARGA_COLUMN,
		COLUMNS.TYPE_COLUMN, COLUMNS.MARK_COLUMN, COLUMNS.SETTINGUP_COLUMN, COLUMNS.LOCATION_COLUMN };
	
	private VehicleTableModel parkModel = new VehicleTableModel(parkModelHeader);
	private VehicleTableModel availablesModel = new VehicleTableModel(availablesModelHeader);
	
	private VehicleTable parkTable;
	private VehicleTable availablesTable;
	
	private Coordinator parkCoordinator;
	private Coordinator availablesCoordinator;
	
	// My third-part software, a JADE agent:
	private ShipperAgent shipperAgent;
	
	// For visual interaction with user
	private JTextArea communicationTextArea;
	private DataObjectTableModel<Goods> goodsModel;
	
	
	// --------------------------------------------------------------------------
	
	////////////////////////////////////////////////////
	// CONSTRUCTOR
	
	ShipperAgentGUI(ShipperAgent agent) {
		
		// Valorizza l'agente corrispondente
		shipperAgent = agent;
		
		///////////////////////////////////////////////////////////////////////
		// Graphics:
		
		setTitle("Shipper agent: "+agent.getLocalName());
		setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		
		try {
			UIManager.setLookAndFeel("javax.swing.plaf.nimbus.NimbusLookAndFeel");
			//UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
		} catch (ClassNotFoundException | InstantiationException
				| IllegalAccessException | UnsupportedLookAndFeelException e) {
			e.printStackTrace();
		}
		
		JPanel masterPanel = new JPanel();
		getContentPane().add(masterPanel, BorderLayout.CENTER);

		// Primo TAB: Veicoli
		JPanel vehiclesPanel = new JPanel();
		{
			// MasterPanel
			vehiclesPanel.setLayout(new BoxLayout(vehiclesPanel, BoxLayout.Y_AXIS));
			vehiclesPanel.setComponentOrientation(ComponentOrientation.LEFT_TO_RIGHT);
	
			// Park Panel
			JPanel parkPanel = new JPanel();
			parkPanel.setLayout(new BoxLayout(parkPanel, BoxLayout.Y_AXIS));
			vehiclesPanel.add(parkPanel);
	
			JPanel pnlHeaderParkPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
			JLabel parkLabel = new JLabel("Flotta:");
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
			vehiclesPanel.add(arrowPanel);
	
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
			masterPanel.setLayout(new BoxLayout(masterPanel, BoxLayout.PAGE_AXIS));
	
			// Availables Panel
			JPanel availablesPanel = new JPanel();
			availablesPanel.setLayout(new BoxLayout(availablesPanel, BoxLayout.Y_AXIS));
			vehiclesPanel.add(availablesPanel);
	
			JPanel pnlHeaderAvailablesPanel = new JPanel();
			FlowLayout fl_pnlHeaderAvailablesPanel = (FlowLayout) pnlHeaderAvailablesPanel.getLayout();
			fl_pnlHeaderAvailablesPanel.setAlignment(FlowLayout.LEFT);
			availablesPanel.add(pnlHeaderAvailablesPanel);
			JLabel label_1 = new JLabel("Mezzi disponibili:");
			pnlHeaderAvailablesPanel.add(label_1);
			label_1.setHorizontalAlignment(SwingConstants.LEFT);
	
			// Available Table
			availablesTable = new VehicleTable(availablesModel);
			JScrollPane availablesScrollPane = new JScrollPane(availablesTable);
			availablesPanel.add(availablesScrollPane);
		}

		// Secondo TAB: Merci
		JPanel goodsPanel = new JPanel();
		{
			goodsPanel.setLayout(new BoxLayout(goodsPanel, BoxLayout.Y_AXIS));
	
			JLabel lblMerciPrenotate = new JLabel("Merci prenotate");
			lblMerciPrenotate.setAlignmentX(Component.CENTER_ALIGNMENT);
			goodsPanel.add(lblMerciPrenotate);
			
			final ArrayList<String> colonne = new ArrayList<String>();
			colonne.add("Descrizione");
			colonne.add("Dimensioni x*y*z");
			colonne.add("Q.tà");
			colonne.add("Volume");
			colonne.add("Tipo");
			colonne.add("Pericolosa");
			colonne.add("Necessità");
			colonne.add("Partenza");
			colonne.add("Destinazione");
			colonne.add("Dal");
			colonne.add("Entro");
			colonne.add("Cliente");
			// TODO affidato a... colonne.add("Veicolo di riferimento");
			
			goodsModel = new DataObjectTableModel<Goods>(colonne) {
				private static final long serialVersionUID = 1L;

				@Override
				public Object getValueAt(int rowIndex, int columnIndex) {
					//Prenotazione p = (Prenotazione) goodsModel.getDataObject(rowIndex);
					//Goods g = p.goods;
					Goods g = goodsModel.getDataObject(rowIndex);
					String s = "?";
					switch (columnIndex){
						case 0: s= g.getDescrizione(); break;
						case 1: s= g.getDimensione(); break;
						case 2: s= String.valueOf(g.getQuantità()); break;
						case 3: s= String.valueOf(g.getVolume()); break;
						case 4: s= g.getTipo(); break;
						case 5: s= String.valueOf(g.isPericolosa()); break;
						case 6: s= g.getNecessità(); break;
						case 7: s= g.getLocationStart(); break;
						case 8: s= g.getLocationEnd(); break;
						case 9: s= String.valueOf(g.getDateStart()); break;
						case 10: s= String.valueOf(g.getDateLimit())+" gg"; break;
						case 11: s= g.getBuyer(); break;
					}
					return s;
				}
			};
			
			JTable goodsTable = new JTable(goodsModel);
			goodsTable.setFillsViewportHeight(true);
			JScrollPane goodsScrollPane = new JScrollPane(goodsTable);
			goodsPanel.add(goodsScrollPane);
		}
		
		
		
		// TabbedPane
		JTabbedPane tabbedPane = new JTabbedPane(JTabbedPane.TOP);
		masterPanel.add(tabbedPane);
		tabbedPane.addTab("Veicoli", null, vehiclesPanel, null);
		tabbedPane.addTab("Merci", null, goodsPanel, null);


		// Communication Panel
		JPanel communicationPanel = new JPanel();
		masterPanel.add(communicationPanel);
		communicationPanel.setLayout(new BoxLayout(communicationPanel, BoxLayout.Y_AXIS));

		JPanel pnlHeaderCommunicationPanel = new JPanel();
		FlowLayout fl_pnlHeaderCommunicationPanel = (FlowLayout) pnlHeaderCommunicationPanel.getLayout();
		fl_pnlHeaderCommunicationPanel.setAlignment(FlowLayout.LEFT);
		communicationPanel.add(pnlHeaderCommunicationPanel);

		JLabel lblComunicazioni = new JLabel("Comunicazioni:");
		pnlHeaderCommunicationPanel.add(lblComunicazioni);

		communicationTextArea = new JTextArea(5,0);
		communicationTextArea.setLineWrap(true);
		((DefaultCaret)communicationTextArea.getCaret()).setUpdatePolicy(DefaultCaret.ALWAYS_UPDATE);
		JScrollPane scrollPane = new JScrollPane(communicationTextArea);
		communicationPanel.add(scrollPane);


		// Search Panel
		JPanel searchPanel = new JPanel();
		masterPanel.add(searchPanel);
		JButton btnSearch = new JButton("Search");
		btnSearch.setActionCommand("search");
		btnSearch.addActionListener(this);
		searchPanel.add(btnSearch);

		// End of graphics init
		///////////////////////////////////

		
		/////////////////////////////////////////////////////
		// Contatto con l'agente
		
		parkCoordinator = new Coordinator(parkModel) {
			
			void init(){
				for (Vehicle v: shipperAgent.getVehicles())
					parkModel.addRow(v);
			}
			
			@Override
			public void notifyAndAddRow(final Vehicle vehicle) {
				if (!vehicleExists(vehicle)){
					shipperAgent.newTruck(vehicle); // comunica all'agente
					SwingUtilities.invokeLater(new Runnable() {
						@Override
						public void run() {
							parkModel.addRow(vehicle);
							if (vehicle.getState().equals(Stato.DISPONIBILE))
								addVehicle(availablesCoordinator, vehicle);
						}
					});
				}
			}
			
			@Override
			public void notifyAndDeleteRow(final int rowIndex) {
				final Vehicle vehicle = parkModel.getVehicleAt(rowIndex);
				// Rimuove anche dai veicoli disponibili:
				removeVehicle(availablesCoordinator, vehicle);
				// comunica all'agente
				shipperAgent.removeTruck(vehicle);
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
							case (TableModelEvent.INSERT):
								//System.out.println("un inserimento in availablesModel");
								availablesTable.repaint();
								break;
							case (TableModelEvent.DELETE):
								//System.out.println("una cancellazione in availablesModel");
								availablesTable.repaint();
								break;
							case (TableModelEvent.UPDATE):
								//System.out.println("un aggiornamento in parkModel");
								int row = e.getLastRow();
								Vehicle vehicle = parkModel.getVehicleAt(row);
								if (vehicle.getState().equals(Stato.DISPONIBILE)){
									addVehicle(availablesCoordinator, vehicle);
									availablesTable.repaint();
								} else
									removeVehicle(availablesCoordinator, vehicle);
								break;
						}
					}
				});
			}
		};
		
		
		availablesCoordinator = new Coordinator(availablesModel) {

			@Override
			void init() {
				for (Vehicle v: shipperAgent.getVehicles())
					if (v!=null){
						if (v.getState()!= null){
							if (v.getState().equals(Stato.DISPONIBILE))
								availablesModel.addRow(v);
						} else v.setStato(Stato.NON_DISPONIBILE);
					}
			}
			
			@Override
			public void notifyAndAddRow(final Vehicle vehicle) {
				if (!vehicleExists(vehicle)){
					vehicle.setStato(Stato.DISPONIBILE);
					
					shipperAgent.activateTruck(vehicle);
					
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
				Vehicle vehicle = availablesModel.getVehicleAt(rowIndex);
				if (vehicle!=null){
					vehicle.setStato(Stato.NON_DISPONIBILE);
					shipperAgent.deactivateTruck(vehicle);
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
						parkTable.repaint();
					}
				});
			}
		};
		
		////////////////////////////
		// Show GUI
		showGui();
	}
	
	
	///////////////////////////////////////////////////////////////////////
	// Methods
	///////////////////////////////////////////////////////////////////////
	
	
	public void showGui() {
		Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
		int centerX = (int) screenSize.getWidth() / 2;
		int centerY = (int) screenSize.getHeight() / 2;
		setLocation(centerX - getWidth() / 2, centerY - getHeight() / 2);
		pack();
		super.setVisible(true);
	}
	
		
	// on dispose, delete the agent
	public void dispose() {
		//if (isActive())
			super.dispose();
		shipperAgent.doDelete();
	}
	
	//////////////////////////////////////////////
	// actionPerformed method

	@Override
	public void actionPerformed(ActionEvent e) {
		switch (e.getActionCommand()) {
		case "+parco": {
			new InsertVehicleJDialog(this, parkCoordinator);
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
		
		case "search": {
			if (availablesModel.getRowCount()>0)
				shipperAgent.searchJob();
			else
				shipperAgent.myGUI.insertInfo("Nessun veicolo disponibile!");
		} break;

		default:
			System.out.println("Imprevisto in actionPerformed()");
			break;
		}
	}
	
	
	// /////////////////////////////////////
	// Add/Remove vehicle methods
	
	public void addVehicle(Coordinator coordinator, Vehicle v) {
		coordinator.notifyAndAddRow(v);
	}
	
	void removeVehicle(Coordinator coordinator, Vehicle v) {
		int row = coordinator.indexOf(v);
		if (row!=-1)
			coordinator.notifyAndDeleteRow(row);
	}
	
	void removeVehicle(Coordinator coordinator, int row) {
		coordinator.notifyAndDeleteRow(row);
	}
	
	///////////////////////////////////////
	// Add goods methods
	/*public void addGoods(Goods g) {
		goodsModel.addRow(g);
	}*/
	
	public void addGoods(List<Goods> g) {
		/*for (Goods goods : g) {
			Prenotazione p = new Prenotazione(v, goods);
			goodsModel.addRow(p);
		}*/
		goodsModel.addRows(g);
	}
	
	public void insertInfo(String info){
		communicationTextArea.append(info+"\n");
		System.out.println("Agent "+shipperAgent.getLocalName()+": "+info);
	}  
	
	
	
	///////////////////////////////////////
	// INNER CLASS
	///////////////////////////////////////
	
	public abstract class Coordinator implements Serializable {
		private VehicleTableModel tableModel;

		public Coordinator(VehicleTableModel tm) {
			tableModel = tm;
			init();
			notifyRowUpdated();
		}
		
		abstract void init();
		public abstract void notifyAndAddRow(Vehicle vehicle);
		public abstract void notifyAndDeleteRow(int rowIndex);
		public abstract void notifyRowUpdated();
		
		public int indexOf(Vehicle v) {
			return tableModel.indexOf(v);
		}
		
		
		// inoltre non effettua alcun controllo sulle targhe
		protected boolean vehicleExists(Vehicle vehicle){
			if (indexOf(vehicle)==-1)
				return false;
			else
				return true;
		}	
		
	}
	
}
