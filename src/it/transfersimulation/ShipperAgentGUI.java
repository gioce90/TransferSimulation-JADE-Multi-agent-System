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
import javax.swing.JButton;

import java.util.Vector;

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
	private JTable parcoMezziTable;
	
	
	private DefaultTableModel dtModelParcoMezzi =
			new DefaultTableModel(null, Vehicle.getHeader().toArray()){
		
		public Class<?> getColumnClass(int columnIndex) {
			return getValueAt(0, columnIndex).getClass();
		};
		
		
	};
	
	private DefaultTableModel dtModelMezziDisponibili =
			new DefaultTableModel(null, Vehicle.getHeader().toArray());
	
	protected ShipperAgent shipperAgent;
	
	//private ShipperInterface sInterface;
	
	
	////////////////////////////////////////////////////
	// COSTRUTTORE
	//
	ShipperAgentGUI(ShipperAgent agent) {
		shipperAgent = agent;
		
		/*
		// Contatto con l'agente - Riempimento dati
		Object[] veicoli = shipperAgent.getVehicles();
		for (int i=0; i<veicoli.length;i++){
			Object[] veicolo = (Object[]) veicoli[i];
			dtModelParcoMezzi.addRow(veicolo);
			if ( veicolo[3] == Stato.DISPONIBILE ) //TODO
				dtModelMezziDisponibili.addRow((Object[]) veicoli[i]);
		}
		*/
		
		dtModelParcoMezzi.addRow(new Object[] {"dd", new JComboBox<Object>(), new Boolean(false),"",""  });
		
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
		
		
		// Cambio di grafica
		try {
			UIManager.setLookAndFeel("javax.swing.plaf.nimbus.NimbusLookAndFeel");
			// UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
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

		parcoMezziTable = new JTable();
		parcoMezziTable.setModel(dtModelParcoMezzi);
		parcoMezziTable.setPreferredScrollableViewportSize(new Dimension(500,70));
		parcoMezziTable.setFillsViewportHeight(true);
		panel_1.add(new JScrollPane(parcoMezziTable));

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
		mezziDisponibiliTable.setModel(dtModelMezziDisponibili);
		mezziDisponibiliTable.setPreferredScrollableViewportSize(
				new Dimension(500, 70));
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

		// TODO Inserisco i listener alle tabelle
		dtModelParcoMezzi.addTableModelListener(dtmParcoMezziListener);
		dtModelMezziDisponibili.addTableModelListener(dtmMezziDisponibiliListener);
		
		showGui();

		/*
		 * // Ricevo un input da tastiera che inserisce una nuova riga try {
		 * dtModelParcoMezzi.addRow( new Object[] { System.in.read() } ); }
		 * catch (IOException e1) { e1.printStackTrace(); }
		 */
	}

	
	/*
	public ShipperAgentGUI(ShipperInterface si, ShipperAgent agent) {
		this(agent);
		sInterface = si;
	}
	*/
	
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
			new VehicleInsertJDialog(this, dtModelParcoMezzi);
		} break;
			
		case "-parco": {
			int selectedRow = parcoMezziTable.getSelectedRow();
			if (selectedRow != -1)
				removeVehicle(dtModelParcoMezzi, selectedRow);
		} break;
		
		case "+disponibili": {
			int selectedRow = parcoMezziTable.getSelectedRow();
			if (selectedRow != -1){
				addVehicle(dtModelMezziDisponibili,
						String.valueOf(dtModelParcoMezzi.getValueAt(selectedRow, 0)),
						String.valueOf(dtModelParcoMezzi.getValueAt(selectedRow, 1)),
						String.valueOf(dtModelParcoMezzi.getValueAt(selectedRow, 2)),
						String.valueOf(dtModelParcoMezzi.getValueAt(selectedRow, 3)),
						String.valueOf(dtModelParcoMezzi.getValueAt(selectedRow, 4))
				);
			}
		} break;
		
		case "-disponibili": {
			int selectedRow = mezziDisponibiliTable.getSelectedRow();
			if (selectedRow != -1)
				removeVehicle(dtModelMezziDisponibili, selectedRow);
		} break;
		
		default:
			System.out.println("Imprevisto in actionPerformed()");
			break;
		}
	}

	
	// /////////////////////////////////////
	// Add/Remove vehicles methods
	
	public void addVehicle(final DefaultTableModel model, final String targa,
			final String tipo, final String marca, final String stato, final String peso) {
		Runnable addV = new Runnable() {
			@Override
			public void run() {
				model.addRow(new Object[] { targa, tipo, marca, stato, peso });
			} 
		};
		SwingUtilities.invokeLater(addV);
		
		//TODO mettere la comunicazione verso l'agente qui?
		if (model.equals(dtModelParcoMezzi))
			shipperAgent.newTruck(targa);
		else if (model.equals(dtModelMezziDisponibili))
			shipperAgent.activateTruck(targa); //TODO
	}
	
	
	public void removeVehicle(final DefaultTableModel model, final int index) {
		Vector row = (Vector) model.getDataVector().elementAt(index);
		
		if (row!=null){
			String targa = row.elementAt(0).toString();
			
			Runnable removeV = new Runnable() {
				@Override
				public void run() {
					model.removeRow(index);
				}
			};
			SwingUtilities.invokeLater(removeV);
			
			if (model.equals(dtModelParcoMezzi))
				shipperAgent.removeTruck(targa);
			else if (model.equals(dtModelMezziDisponibili))
				shipperAgent.deactivateTruck(targa); //TODO
		}
	}
	
	
	// //////////////////////////////////////////
	// LISTENER:
	
	TableModelListener dtmParcoMezziListener = new TableModelListener() {
		//TODO pare non convenga centralizzare la comunicazione verso l'agente da qui a causa della DELETE
		public void tableChanged(TableModelEvent e) {
			switch (e.getType()) {
				case (TableModelEvent.INSERT): System.out.println("un inserimento in corso!"); break;
				case (TableModelEvent.DELETE): {
					System.out.println("una cancellazione in corso!");
					// TODO se una riga viene cancellata, ed era presente
					// anche in MEZZI DISPONIBILI, cancellare anche da li quella riga
				} break;
				case (TableModelEvent.UPDATE): System.out.println("un aggiornamento in corso!"); break;
			}
		}
	};
	
	TableModelListener dtmMezziDisponibiliListener = new TableModelListener() {
		public void tableChanged(TableModelEvent e) {
			switch (e.getType()) {
				case (TableModelEvent.INSERT): System.out.println("un inserimento in corso!"); break;
				case (TableModelEvent.DELETE): System.out.println("una cancellazione in corso!"); break;
				case (TableModelEvent.UPDATE): System.out.println("un aggiornamento in corso!"); break;
			}
		}
	};
	
	
	
}
