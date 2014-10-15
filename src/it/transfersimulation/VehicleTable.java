package it.transfersimulation;

import it.transfersimulation.model.*;
import it.transfersimulation.VehicleTableModel.COLUMNS;

import java.awt.Dimension;

import javax.swing.ComboBoxModel;
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
		this.setRowSelectionAllowed(true);
		this.setShowHorizontalLines(true);
		this.setRowHeight(28);
		this.setPreferredScrollableViewportSize(new Dimension(700,150));
		this.setFillsViewportHeight(true);
		
		////////////////////////////////////
		// Now I set the columns features:
		int flag=-1;
		TableColumn column;
		
		// Icon Column:
		flag = vehicleModel.findColumn(COLUMNS.IMAGE_COLUMN);
		if (flag!=-1){
			column = this.getColumnModel().getColumn(flag);
			column.setMinWidth(80);
			column.setMaxWidth(80);
		}
		
		// Targa Column:
		flag = vehicleModel.findColumn(COLUMNS.TARGA_COLUMN);
		if (flag!=-1){
			column = this.getColumnModel().getColumn(flag);
			column.setMinWidth(100);
			column.setMaxWidth(150);
		}
		
		// Tipo veicolo Column
		flag = vehicleModel.findColumn(COLUMNS.TYPE_COLUMN);
		if (flag!=-1){
			column = this.getColumnModel().getColumn(flag);
			/* TODO se si volesse cambiare tipo?
			column.setCellEditor(new DefaultCellEditor(
					...
			));
			*/
			column.setMinWidth(150);
			column.setMaxWidth(150);
		}
		
		// MARCA Column:
		/* Non necessaria
		flag = vehicleModel.findColumn(COLUMNS.MARCA_COLUMN);
		if (flag!=-1){
			column = this.getColumnModel().getColumn(flag);
			column.setMinWidth(150);
			column.setMaxWidth(150);
		}
		*/
		
		// Stato veicolo Column
		flag = vehicleModel.findColumn(COLUMNS.STATE_COLUMN);
		if (flag!=-1){
			column = this.getColumnModel().getColumn(flag);
			column.setCellEditor(new DefaultCellEditor(
					new JComboBox<Stato>(Stato.values())));
			column.setMinWidth(150);
			column.setMaxWidth(150);
		}
		
		// PTT Column:
		flag = vehicleModel.findColumn(COLUMNS.PTT_COLUMN);
		if (flag!=-1){
			column = this.getColumnModel().getColumn(flag);
			column.setMinWidth(50);
			column.setMaxWidth(50);
		}
		
	}
	
	
	
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
