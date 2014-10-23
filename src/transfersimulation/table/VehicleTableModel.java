package transfersimulation.table;

import java.util.ArrayList;
import java.util.List;

import javax.swing.ImageIcon;
import javax.swing.JComboBox;
import javax.swing.table.AbstractTableModel;

import transfersimulation.model.vehicle.*;
import transfersimulation.model.vehicle.Vehicle.Stato;

public class VehicleTableModel extends AbstractTableModel {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	private ArrayList<Vehicle> vehicles;
	private COLUMNS[] header;
	
	
	// possible column names:
	public enum COLUMNS {
		IMAGE_COLUMN,
		TARGA_COLUMN,
		TYPE_COLUMN,
		MARK_COLUMN,		
		STATE_COLUMN,			
		PTT_COLUMN,
		LOCATION_COLUMN
	};
	
	
	
	///////////////////////////////////////////////////////
	// Constructors:
	
	public VehicleTableModel(COLUMNS[] headerTable) {
		this.vehicles = new ArrayList<Vehicle>();
		this.header = headerTable;
	}
	
	
	///////////////////////////////////////////////////////
	// obligatory override methods (from AbstractTableModel):
	
	@Override
	public int getColumnCount() {
		return header.length;
	}

	@Override
	public int getRowCount() {
		return vehicles.size();
	}
	
	@Override
	public Object getValueAt(int row, int col) {
		Object value = "?";
        Vehicle v = vehicles.get(row);
        if (v!=null) {
        	COLUMNS column = header[col];
	        switch (column) {
	            case IMAGE_COLUMN:
	                value = VehicleUtils.findImageByColumnCarType(v.getType());
	                break;
	            case TARGA_COLUMN:
	                value = v.getPlate();
	                break;
	            case TYPE_COLUMN:
	                value = VehicleUtils.findStringByColumnCarType(v.getType());
	                break;
	            case MARK_COLUMN:
	                value = v.getMark();
	                break;
	            case STATE_COLUMN:
	                value = v.getState();
	                break;
	            case PTT_COLUMN:
	                value = v.getPtt();
	                break;
	            case LOCATION_COLUMN:
	            	value = v.getLocazioneAttuale();
	        }
        }
        return value;
	}
	
	
	
	///////////////////////////////////////////////////////
	// My methods:
	
	public void addRow(Vehicle vehicle) {
		int rowIndex = vehicles.size();
		vehicles.add(vehicle);
		fireTableRowsInserted(rowIndex, rowIndex); // TODO attenzione
	}
	
	public void removeRow(int row) {
		vehicles.remove(row);
		fireTableRowsDeleted(row, row);
	}
	
	public Vehicle getVehicleAt(int row) {
        return vehicles.get(row);
    }
	
	public int indexOf(Vehicle v){
		return vehicles.indexOf(v);
	}
	
	// found the corresponding column index
	public int findColumn(COLUMNS columnName) {
		for (int i=0; i<getColumnCount(); i++)
			if (columnName.equals(header[i])) 
		        return i;
		return -1;
	}

	
	// TODO
	// controlla se un valore in un campo già esiste in tutte le righe
	private boolean controllIfExist(Object value, int col) {
		boolean bool = false;
		for (int i=0; i<getRowCount();i++){
			if (value.equals(getValueAt(i, col))){
				bool=true;
				break;
			}
		}
		return bool;
	}
	
	public int getColumnIndex(COLUMNS column){
		for(int i=0;i<header.length;i++){
			if (column.equals(header[i])){
				return i;
			}
		}
		return -1;
	}
	
	
	
	///////////////////////////////////////////////////////
	// other methods (from AbstractTableModel) to override:
	
	
    @Override
	public Class<?> getColumnClass(int col) {
    	Class<?> c;
    	COLUMNS column = header[col];
    	if (column.equals(COLUMNS.IMAGE_COLUMN))
    		c = ImageIcon.class;
    	else if (column.equals(COLUMNS.STATE_COLUMN))
    		c =  JComboBox.class;
    	else c = super.getColumnClass(col);
    	return c;
    }
    
    
    @Override
    public String getColumnName(int col) {
    	COLUMNS column = header[col];
    	if (column.equals(COLUMNS.IMAGE_COLUMN))
    		return " ";
    	else if (column.equals(COLUMNS.TARGA_COLUMN))
    		return "Targa";
    	else if (column.equals(COLUMNS.TYPE_COLUMN))
    		return "Tipo veicolo";
    	else if (column.equals(COLUMNS.MARK_COLUMN))
    		return "Marca";
    	else if (column.equals(COLUMNS.STATE_COLUMN))
    		return "Stato";
    	else if (column.equals(COLUMNS.PTT_COLUMN))
    		return "PTT";
    	else if (column.equals(COLUMNS.LOCATION_COLUMN))
    		return "Locazione attuale";
    	return super.getColumnName(col);
    };
	
    
	@Override
	public boolean isCellEditable(int row, int col) {
		return true;
	}
	
	
    @Override
    public void setValueAt(Object value, int row, int col) {
    	Vehicle v = vehicles.get(row);
    	boolean flag = false;
    	if (v!=null) {
        	COLUMNS column = header[col];
	        switch (column) {
	            case TARGA_COLUMN:
	            	if (!v.getPlate().equals(value)){
	            		if (!controllIfExist(value, col)){
	            			v.setPlate((String) value);
	            			flag = true;
	            		}
	            	}
	            	break;
	            case MARK_COLUMN:
	            	if (!v.getMark().equals(value)){
	            		v.setMark((String) value);
	            		flag = true;
	            	}
	            	break;
	            case STATE_COLUMN:
	            	if (!v.getState().equals(value)){
	            		v.setStato((Stato) value);
	            		flag = true;
	            	}
	            	break;
	            case PTT_COLUMN:
	            	if (v.getPtt()!=Float.valueOf((String) value)){
	            		v.setPtt(Float.valueOf((String)value));
	            		flag = true;
	            	}
	            	break;
	            case LOCATION_COLUMN:
	            	if (!v.getLocazioneAttuale().equals(value)){
		            		v.setLocazioneAttuale((String) value);
		            		flag = true;
		            }
			default:
				break;
	        }
	        // Aggiorna solo se ci sono state modifiche
	        if (flag)
	        	fireTableCellUpdated(row, col);
        }
    }
}
