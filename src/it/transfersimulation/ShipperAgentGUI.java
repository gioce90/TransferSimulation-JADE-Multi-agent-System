package it.transfersimulation;

import it.transfersimulation.Vehicle.Stato;
import it.transfersimulation.Vehicle.TipoVeicolo;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.Toolkit;
import java.awt.Panel;
import java.awt.Button;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;
import java.awt.ComponentOrientation;

import javax.swing.DefaultCellEditor;
import javax.swing.DefaultComboBoxModel;
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


@SuppressWarnings("serial")
public class ShipperAgentGUI extends JFrame implements ActionListener {

	private JPanel panel = new JPanel();
	private Panel panel_1;
	private Panel parcoPanel;
	private Panel panel_2;
	private Button btnPM_plus;
	private Button btnPM_meno;
	private Panel disponibiliPane;
	private Panel panel_3;
	private Panel panel_4;
	private Button btnMD_plus;
	private Button btnMD_meno;
	private JTable mezziDisponibiliTable;
	private JTable parcoTable;
	
	private DefaultTableModel parcoModel =
			new DefaultTableModel(null, Vehicle.getHeader());/*
	{
		
		public Class<?> getColumnClass(int columnIndex) {
			return Vehicle.getHeaderType().get(columnIndex);
		};
	};//TODO per aggiungere checkbox e altro  */
	
	
	private DefaultTableModel mezziDisponibiliModel =
			new DefaultTableModel(null, Vehicle.getHeader());
	
	protected ShipperAgent shipperAgent;
	private Coordinator parcoCoordinator;
	private Coordinator mezziDisponibiliCoordinator;
	//private ShipperInterface sInterface;
	
	
	////////////////////////////////////////////////////
	// COSTRUTTORE
	
	ShipperAgentGUI(ShipperAgent agent) {
		shipperAgent = agent;
		
		// TODO
		// Contatto con l'agente - Riempimento dati
		Object[] veicoli = shipperAgent.getVehicles();
		for (int i=0; i<veicoli.length;i++){
			Object[] veicolo = (Object[]) veicoli[i];
			parcoModel.addRow(veicolo);
			if ( veicolo[3] == Stato.DISPONIBILE )
				mezziDisponibiliModel.addRow((Object[]) veicoli[i]);
		}
		
		
		/*
		dtModelParcoMezzi.addRow(new Object[] {"aa", "shhiiiish", true,"",""  });
		dtModelParcoMezzi.addRow(new Object[] {"dd", "shhiiiish", false,"",""  });
		*/
		
		//dtModelParcoMezzi = new DefaultTableModel(dtModelParcoMezzi.getDataVector(), Vehicle.getHeader())
		
		/*{
			
			public Class<?> getColumnClass(int columnIndex) {
				return getValueAt(0, columnIndex).getClass();
			};
		};
		
		/*
		Object[] veicoli = shipperAgent.getVehicles();
		for (int i=0; i<veicoli.length;i++){
			Vehicle veicolo = (Vehicle) veicoli[i];
			Object[] newRow = new Object[] {
					veicolo.getTarga(),
					veicolo.getTipoVeicolo(),
					veicolo.getMarca(),
					veicolo.getStato(),
					veicolo.getPtt()
			};
			dtModelParcoMezzi.addRow(newRow);
		}
		*/
		
		/*
		 * // Ricevo un input da tastiera che inserisce una nuova riga try {
		 * parcoModel.addRow( new Object[] { System.in.read() } ); }
		 * catch (IOException e1) { e1.printStackTrace(); }
		 */
		
		
		
		// Grafica:
		try {
			UIManager.setLookAndFeel("javax.swing.plaf.nimbus.NimbusLookAndFeel");
			//UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
		} catch (ClassNotFoundException | InstantiationException
				| IllegalAccessException | UnsupportedLookAndFeelException e) {
			e.printStackTrace();
		}
		
		setTitle("Shipper Agent: "+agent.getLocalName());
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		panel.setComponentOrientation(ComponentOrientation.LEFT_TO_RIGHT);
		panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));

		parcoPanel = new Panel();
		panel.add(parcoPanel);

		panel_1 = new Panel();
		parcoPanel.add(panel_1);
		panel_1.setLayout(new BoxLayout(panel_1, BoxLayout.Y_AXIS));
		JLabel label = new JLabel("Parco auto:");
		panel_1.add(label);

		parcoTable = new JTable();
		parcoTable.setModel(parcoModel);
		parcoTable.setPreferredScrollableViewportSize(new Dimension(500,100));
		parcoTable.setFillsViewportHeight(true);
		panel_1.add(new JScrollPane(parcoTable));

		panel_2 = new Panel();
		parcoPanel.add(panel_2);
		panel_2.setLayout(new BoxLayout(panel_2, BoxLayout.Y_AXIS));

		btnPM_plus = new Button("+");
		btnPM_plus.setActionCommand("+parco");
		btnPM_plus.addActionListener(this);
		panel_2.add(btnPM_plus);

		btnPM_meno = new Button("-");
		btnPM_meno.setActionCommand("-parco");
		btnPM_meno.addActionListener(this);
		panel_2.add(btnPM_meno);

		disponibiliPane = new Panel();
		panel.add(disponibiliPane);

		panel_3 = new Panel();
		disponibiliPane.add(panel_3);
		panel_3.setLayout(new BoxLayout(panel_3, BoxLayout.Y_AXIS));
		JLabel label_1 = new JLabel("Disponibili:");
		panel_3.add(label_1);

		mezziDisponibiliTable = new JTable();
		mezziDisponibiliTable.setModel(mezziDisponibiliModel);
		mezziDisponibiliTable.setPreferredScrollableViewportSize(new Dimension(500, 100));
		mezziDisponibiliTable.setFillsViewportHeight(true);
		panel_3.add(new JScrollPane(mezziDisponibiliTable));

		panel_4 = new Panel();
		disponibiliPane.add(panel_4);
		panel_4.setLayout(new BoxLayout(panel_4, BoxLayout.Y_AXIS));

		btnMD_plus = new Button("+");
		btnMD_plus.setActionCommand("+disponibili");
		btnMD_plus.addActionListener(this);
		panel_4.add(btnMD_plus);

		btnMD_meno = new Button("-");
		btnMD_meno.setActionCommand("-disponibili");
		btnMD_meno.addActionListener(this);
		panel_4.add(btnMD_meno);
		getContentPane().add(panel, BorderLayout.CENTER);

		JButton btnSearch = new JButton("Search");
		panel.add(btnSearch);
		
		
		
		// Coordinators (ispirati al Mediator pattern)
		
		parcoCoordinator = new Coordinator(shipperAgent, parcoModel) {
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
		        int flag=search(mezziDisponibiliCoordinator.tableModel, truck);
		        if (flag!=-1)
    				removeVehicle(mezziDisponibiliCoordinator, flag);
    			shipperAgent.removeTruck(truck);
    			
		        SwingUtilities.invokeLater(new Runnable() {
	        		@Override
	    			public void run() {
	        			tableModel.removeRow(rowIndex);
	    			}
		        });
		    }
		};
		
		
		mezziDisponibiliCoordinator = new Coordinator(shipperAgent, mezziDisponibiliModel) {
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
		        String truck = (String)this.tableModel.getValueAt(rowIndex, 0);
		        shipperAgent.deactivateTruck(truck);
		        
		        SwingUtilities.invokeLater(new Runnable() {
		        		@Override
		    			public void run() {
		        			tableModel.removeRow(rowIndex);
		    			}
		        });
		    }
		};
		
		
		
		// Listeners:
		parcoModel.addTableModelListener(parcoListener);
		mezziDisponibiliModel.addTableModelListener(mezziDisponibiliListener);
		
		
		
		// TODO Editor delle colonne:
		// dovrei generalizzare un metodo?
		
		TableColumn tipoVeicoloColumn = parcoTable.getColumnModel().getColumn(1);
		JComboBox<TipoVeicolo> tipoVeicoloComboBox = new JComboBox<TipoVeicolo>();
		tipoVeicoloComboBox.setModel(new DefaultComboBoxModel<TipoVeicolo>(TipoVeicolo.values()));
		
		
		TableColumn statoColumn = parcoTable.getColumnModel().getColumn(3);
		JComboBox<Stato> statoComboBox = new JComboBox<Stato>();
		statoComboBox.setModel(new DefaultComboBoxModel<Stato>(Stato.values()));
		
		tipoVeicoloColumn.setCellEditor(new DefaultCellEditor(tipoVeicoloComboBox));
		statoColumn.setCellEditor(new DefaultCellEditor(statoComboBox));
		
		
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
			new VehicleInsertJDialog(this, parcoCoordinator);
		} break;
			
		case "-parco": {
			int selectedRow = parcoTable.getSelectedRow();
			if (selectedRow != -1)
				removeVehicle(parcoCoordinator, selectedRow);
		} break;
		
		case "+disponibili": {
			int selectedRow = parcoTable.getSelectedRow();
			if (selectedRow != -1){
				
				//TODO controlla la consistenza
				addVehicle(mezziDisponibiliCoordinator,
						String.valueOf(parcoModel.getValueAt(selectedRow, 0)),
						String.valueOf(parcoModel.getValueAt(selectedRow, 1)),
						String.valueOf(parcoModel.getValueAt(selectedRow, 2)),
						String.valueOf(parcoModel.getValueAt(selectedRow, 3)),
						String.valueOf(parcoModel.getValueAt(selectedRow, 4))
				);
				
			}
		} break;
		
		case "-disponibili": {
			int selectedRow = mezziDisponibiliTable.getSelectedRow();
			if (selectedRow != -1)
				removeVehicle(mezziDisponibiliCoordinator, selectedRow);
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
	
	
	
	
	
	 public void setUpColumn(JTable table, TableColumn column, int i) {
		 
		 
		 
		 
		 
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
