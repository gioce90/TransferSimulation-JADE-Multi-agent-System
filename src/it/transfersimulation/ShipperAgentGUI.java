package it.transfersimulation;

import it.transfersimulation.Vehicle.Stato;
import it.transfersimulation.Vehicle.TipoVeicolo;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.Toolkit;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;
import java.awt.ComponentOrientation;

import javax.swing.DefaultCellEditor;
import javax.swing.DefaultComboBoxModel;
import javax.swing.ImageIcon;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.SwingUtilities;
import javax.swing.UIManager;
import javax.swing.UnsupportedLookAndFeelException;
import javax.swing.BoxLayout;
import javax.swing.JTable;
import javax.swing.event.TableModelEvent;
import javax.swing.event.TableModelListener;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableColumn;
import javax.swing.JButton;

import java.awt.FlowLayout;

import javax.swing.SwingConstants;


@SuppressWarnings("serial")
public class ShipperAgentGUI extends JFrame implements ActionListener {

	// Variabili di classe
	private JPanel masterPanel;
	private JButton btnPM_plus;
	private JButton btnPM_meno;
	private JButton btnMD_plus;
	private JButton btnMD_meno;
	private JTable availablesTable;
	private JTable parkTable;

	private Object[] parkModelHeader = { "" , "TARGA", "TIPO VEICOLO", "MARCA", "STATO", "PTT" };
	private Object[] availablesModelHeader = { "", "TARGA", "TIPO VEICOLO", "MARCA", "STATO", "PTT" };
	
	private DefaultTableModel parkModel = new DefaultTableModel(null, parkModelHeader){
		public Class<?> getColumnClass(int columnIndex) {
			return getValueAt(0, columnIndex).getClass();
		};
	};// per aggiungere jCheckBox, jComboBox e ImageIcon
	
	private DefaultTableModel availablesModel =	new DefaultTableModel(null, availablesModelHeader){
		public Class<?> getColumnClass(int columnIndex) {
			return getValueAt(0, columnIndex).getClass();
		}
	};// per aggiungere jCheckBox, jComboBox e ImageIcon
	
	// My third-part software: a JADE agent:
	protected ShipperAgent shipperAgent;
	
	private Coordinator parkCoordinator;
	private Coordinator availablesCoordinator;
	

	////////////////////////////////////////////////////
	// COSTRUTTORE

	ShipperAgentGUI(ShipperAgent agent) {
		
		// Valorizza l'agente corrispondente
		shipperAgent = agent;
		
		
		///////////////////////////////////////////////////////////////////////
		// Graphics:
		//
		
		setTitle("Shipper Agent: "+agent.getLocalName());
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		
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
		parkTable = new JTable();
		parkTable.setModel(parkModel);
		parkTable.setPreferredScrollableViewportSize(new Dimension(500,100));
		parkTable.setFillsViewportHeight(true);
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
		availablesTable = new JTable();
		availablesTable.setModel(availablesModel);
		availablesTable.setPreferredScrollableViewportSize(new Dimension(500, 100));
		availablesTable.setFillsViewportHeight(true);
		JScrollPane availablesScrollPane = new JScrollPane(availablesTable);
		availablesPanel.add(availablesScrollPane);
		getContentPane().add(masterPanel, BorderLayout.CENTER);
		
		// Search Panel
		JPanel searchPanel = new JPanel();
		masterPanel.add(searchPanel);
		JButton btnSearch = new JButton("Search");
		searchPanel.add(btnSearch);

		// End of graphics init
		///////////////////////////////////
		
		
		//////////////////////////////////////
		// Editor delle colonne delle tabelle
		// TODO
		JComboBox<TipoVeicolo> tipoVeicoloComboBox = new JComboBox<TipoVeicolo>();
		tipoVeicoloComboBox.setModel(new DefaultComboBoxModel<TipoVeicolo>(TipoVeicolo.values()));
		JComboBox<Stato> statoComboBox = new JComboBox<Stato>();
		statoComboBox.setModel(new DefaultComboBoxModel<Stato>(Stato.values()));
		
		TableColumn tipoVeicoloColumn = parkTable.getColumnModel().getColumn(2);
		TableColumn statoColumn = parkTable.getColumnModel().getColumn(4);

		tipoVeicoloColumn.setCellEditor(new DefaultCellEditor(tipoVeicoloComboBox));
		statoColumn.setCellEditor(new DefaultCellEditor(statoComboBox));

		
		/////////////////////////////////////////////////////////////////////
		// Coordinators (ispirati al Mediator pattern)
		
		parkCoordinator = new Coordinator(shipperAgent, parkModel) {
			@Override
			public void notifyAndAddRow(final Object[] rowData) {
				shipperAgent.newTruck((String) rowData[0]);

				SwingUtilities.invokeLater(new Runnable() {
					@Override
					public void run() {
						tableModel.addRow(rowData);
					}
				});
			}

			@Override
			public void notifyAndDeleteRow(final int rowIndex) {
				final String truck = (String)this.tableModel.getValueAt(rowIndex, 0);
				int flag=search(availablesCoordinator.tableModel, truck);
				if (flag!=-1)
					removeVehicle(availablesCoordinator, flag);
				shipperAgent.removeTruck(truck);

				SwingUtilities.invokeLater(new Runnable() {
					@Override
					public void run() {
						tableModel.removeRow(rowIndex);
					}
				});
			}
		};


		availablesCoordinator = new Coordinator(shipperAgent, availablesModel) {
			@Override
			public void notifyAndAddRow(final Object[] rowData) {
				shipperAgent.activateTruck((String) rowData[0]);

				SwingUtilities.invokeLater(new Runnable() {
					@Override
					public void run() {
						tableModel.addRow(rowData);
					}
				});
			}

			@Override
			public void notifyAndDeleteRow(final int rowIndex) {
				String truck = (String)this.tableModel.getValueAt(rowIndex, 1);
				shipperAgent.deactivateTruck(truck);

				SwingUtilities.invokeLater(new Runnable() {
					@Override
					public void run() {
						tableModel.removeRow(rowIndex);
					}
				});
			}
		};


		/////////////////////////////////////////////////////
		// Listeners:
		
		parkModel.addTableModelListener(parcoListener);
		availablesModel.addTableModelListener(mezziDisponibiliListener);
		
		
		/////////////////////////////////////////////////////
		// Contatto con l'agente - Riempimento dati
		// TODO
		Object[] veicoli = shipperAgent.getVehicles();
		for (int i=0; i<veicoli.length;i++){
			Object[] info = (Object[]) veicoli[i];
			Object[] veicolo = new Object[info.length+1];
			
			veicolo[0] = new ImageIcon(ShipperAgentGUI.class.getResource("/images/lorry-icon.png"));
			
			for (int j=1;j<info.length+1;j++){
				veicolo[j]=info[j-1];
			}
			
			parkModel.addRow(veicolo);
			
			if ( veicolo[4] == Stato.DISPONIBILE )
				availablesModel.addRow(veicolo);
		}
		
		////////////////////////////
		// Show GUI
		showGui();
	}


	public void showGui() {
		pack();
		Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
		int centerX = (int) screenSize.getWidth() / 2;
		int centerY = (int) screenSize.getHeight() / 2;
		setLocation(centerX - getWidth() / 2, centerY - getHeight() / 2);
		super.setVisible(true);
	}


	//////////////////////////////////////////////
	// actionPerformed

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
				//TODO controlla la consistenza
				addVehicle(availablesCoordinator,
						String.valueOf(parkModel.getValueAt(selectedRow, 0)),
						String.valueOf(parkModel.getValueAt(selectedRow, 1)),
						String.valueOf(parkModel.getValueAt(selectedRow, 2)),
						String.valueOf(parkModel.getValueAt(selectedRow, 3)),
						String.valueOf(parkModel.getValueAt(selectedRow, 4))
						);

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

	public void addVehicle(Coordinator coordinator,
			String targa, String tipo, String marca, String stato, String peso) {
		coordinator.notifyAndAddRow(new Object[]{targa, tipo, marca, stato, peso});
	}

	public void removeVehicle(Coordinator coordinator, int index) {
		coordinator.notifyAndDeleteRow(index);
	}


	// //////////////////////////////////////////
	// LISTENER:

	TableModelListener parcoListener = new TableModelListener() {
		public void tableChanged(TableModelEvent e) {
			switch (e.getType()) {
			case (TableModelEvent.INSERT):
				System.out.println("un inserimento in corso!"); break;
			case (TableModelEvent.DELETE):
				System.out.println("una cancellazione in corso!"); break;
			case (TableModelEvent.UPDATE):
				System.out.println("un aggiornamento in corso!"); break;
			}
		}
	};

	TableModelListener mezziDisponibiliListener = new TableModelListener() {
		public void tableChanged(TableModelEvent e) {
			switch (e.getType()) {
			case (TableModelEvent.INSERT):
				System.out.println("un inserimento in corso!"); break;
			case (TableModelEvent.DELETE):
				System.out.println("una cancellazione in corso!"); break;
			case (TableModelEvent.UPDATE):
				System.out.println("un aggiornamento in corso!"); break;
			}
		}
	};



	private int search(DefaultTableModel tableModel, String targa) {
		int flag = -1;
		for (int i=0; i<tableModel.getRowCount(); i++) 
			if (tableModel.getValueAt(i, 0).equals(targa))
				flag=i;
		return flag;
	}






	///////////////////////////////////////
	// INNER CLASS
	///////////////////////////////////////

	protected abstract class Coordinator {

		/*
		 * protected class members so subclasses can access these directly
		 */

		protected ShipperAgent shipperAgent;
		protected DefaultTableModel tableModel;

		public Coordinator(ShipperAgent sa, DefaultTableModel tm) {
			shipperAgent = sa;
			tableModel = tm;
		}

		public abstract void notifyAndAddRow(Object[] rowData);

		public abstract void notifyAndDeleteRow(int rowIndex);
	}

}
