package transfersimulation.table;

import java.awt.Dimension;

import javax.swing.DefaultCellEditor;
import javax.swing.JComboBox;
import javax.swing.JTable;
import javax.swing.ListSelectionModel;
import javax.swing.table.TableColumn;

import transfersimulation.model.vehicle.Vehicle.Stato;
import transfersimulation.table.VehicleTableModel.COLUMNS;


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
		this.setPreferredScrollableViewportSize(new Dimension(650,150));
		this.setFillsViewportHeight(true);
		
		////////////////////////////////////
		// Now I set the columns features:
		int flag=-1;
		TableColumn column;
		
		// Icon Column:
		flag = vehicleModel.findColumn(COLUMNS.IMAGE_COLUMN);
		if (flag!=-1){
			column = this.getColumnModel().getColumn(flag);
			column.setMinWidth(70);
			column.setMaxWidth(75);
		}
		
		// Targa Column:
		flag = vehicleModel.findColumn(COLUMNS.TARGA_COLUMN);
		if (flag!=-1){
			column = this.getColumnModel().getColumn(flag);
			column.setMinWidth(80);
			column.setMaxWidth(115);
		}
		
		// Tipo veicolo Column
		flag = vehicleModel.findColumn(COLUMNS.TYPE_COLUMN);
		if (flag!=-1){
			column = this.getColumnModel().getColumn(flag);
			column.setMinWidth(90);
			column.setMaxWidth(100);
		}
		
		// MARCA Column:
		flag = vehicleModel.findColumn(COLUMNS.MARK_COLUMN);
		if (flag!=-1){
			column = this.getColumnModel().getColumn(flag);
			column.setMinWidth(70);
			column.setMaxWidth(115);
		}
		
		// ALLESTIMENTO Column:
		flag = vehicleModel.findColumn(COLUMNS.SETTINGUP_COLUMN);
		if (flag!=-1){
			column = this.getColumnModel().getColumn(flag);
			column.setMinWidth(80);
			//column.setMaxWidth(100);
		}
		
		// Stato veicolo Column
		flag = vehicleModel.findColumn(COLUMNS.STATE_COLUMN);
		if (flag!=-1){
			column = this.getColumnModel().getColumn(flag);
			column.setCellEditor(new DefaultCellEditor(
					new JComboBox<Stato>(Stato.values())));
			column.setMinWidth(128);
			column.setMaxWidth(128);
		}
		
		// PTT Column:
		flag = vehicleModel.findColumn(COLUMNS.PTT_COLUMN);
		if (flag!=-1){
			column = this.getColumnModel().getColumn(flag);
			column.setMinWidth(55);
			column.setMaxWidth(55);
		}
		
		// Actual Location Column:
		flag = vehicleModel.findColumn(COLUMNS.LOCATION_COLUMN);
		if (flag!=-1){
			column = this.getColumnModel().getColumn(flag);
			column.setMinWidth(100);
			//column.setMaxWidth(140);
		}
		
		
	}
	
	
	
	/*	TODO: se in futuro si volesse fare l'ordinamento da colonna
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
