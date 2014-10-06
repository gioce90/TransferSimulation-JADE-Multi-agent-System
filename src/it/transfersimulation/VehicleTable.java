package it.transfersimulation;

import it.transfersimulation.Vehicle.Stato;
import it.transfersimulation.Vehicle.TipoVeicolo;
import it.transfersimulation.VehicleTableModel.COLUMNS;

import java.awt.Dimension;

import javax.swing.DefaultCellEditor;
import javax.swing.DefaultComboBoxModel;
import javax.swing.JComboBox;
import javax.swing.JTable;
import javax.swing.ListSelectionModel;
import javax.swing.table.TableColumn;


@SuppressWarnings("serial")
public class VehicleTable extends JTable {
	
	public VehicleTable(VehicleTableModel vehicleModel) {
		super(vehicleModel); 
		this.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
		this.setColumnSelectionAllowed(false);
		this.setCellSelectionEnabled(false);
		this.setShowHorizontalLines(true);
		this.setRowHeight(25);
		this.setPreferredScrollableViewportSize(new Dimension(700,150));
		this.setFillsViewportHeight(true);
		
		
		int flag=-1;
		
		// Icon Column:
		flag = vehicleModel.findColumn(COLUMNS.IMAGE_COLUMN);
		if (flag!=-1){
			TableColumn iconColumn = this.getColumnModel().getColumn(flag);
			iconColumn.setMinWidth(80);
			iconColumn.setMaxWidth(80);
		}
		
		// Tipo veicolo Column
		flag = vehicleModel.findColumn(COLUMNS.CAR_TYPE_COLUMN);
		if (flag!=-1){
			TableColumn tipoVeicoloColumn = this.getColumnModel().getColumn(flag);
			tipoVeicoloColumn.setCellEditor(new DefaultCellEditor(
					new JComboBox<TipoVeicolo>(TipoVeicolo.values())));
		}
		
		// Stato veicolo Column
		flag = vehicleModel.findColumn(COLUMNS.STATE_COLUMN);
		if (flag!=-1){
			TableColumn statoColumn = this.getColumnModel().getColumn(flag);
			statoColumn.setCellEditor(new DefaultCellEditor(
					new JComboBox<Stato>(Stato.values())));
		}
		
	}
	
	
	
	
	/*
	public void setJComboBoxTipoVeicoloColumn(int i){
		JComboBox<TipoVeicolo> tipoVeicoloComboBox = new JComboBox<TipoVeicolo>();
		tipoVeicoloComboBox.setModel(new DefaultComboBoxModel<TipoVeicolo>(TipoVeicolo.values()));
		TableColumn tipoVeicoloColumn = this.getColumnModel().getColumn(i);
		tipoVeicoloColumn.setCellEditor(new DefaultCellEditor(tipoVeicoloComboBox));
	}
	
	
	public void setJComboBoxStatoColumn(int i){
		JComboBox<Stato> statoComboBox = new JComboBox<Stato>();
		statoComboBox.setModel(new DefaultComboBoxModel<Stato>(Stato.values()));
		TableColumn statoColumn = this.getColumnModel().getColumn(i);
		statoColumn.setCellEditor(new DefaultCellEditor(statoComboBox));
	}
	*/
	
	
	
	/*
	TODO: se in futuro si volesse fare l'ordinamento da colonna
	tableHeader = parkTable.getTableHeader();
	tableHeader.addMouseListener( new MouseAdapter() {
		   public void mouseClicked(MouseEvent e) {
		      int x = e.getX();
		      int y = e.getY();
		      int columnIndex = tableHeader.columnAtPoint( new Point(x,y) );
		      
		      System.out.println("Si ordina per la colonna "+columnIndex);
		      //sort( columnIndex );
		   }
	} );
	*/
	
}




