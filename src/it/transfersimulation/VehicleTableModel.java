package it.transfersimulation;

import it.transfersimulation.Vehicle.Stato;
import it.transfersimulation.Vehicle.TipoVeicolo;

import java.util.ArrayList;
import java.util.EventListener;
import java.util.List;

import javax.swing.ImageIcon;
import javax.swing.JComboBox;
import javax.swing.table.AbstractTableModel;

public class VehicleTableModel extends AbstractTableModel {
	
	
	// private objects
	private ArrayList<Vehicle> vehicles ; //= new ArrayList<Vehicle>();
	private COLUMNS[] header;
	
	
	// possible column names:
	public enum COLUMNS {
		IMAGE_COLUMN,
		TARGA_COLUMN,
		CAR_TYPE_COLUMN,
		MARCA_COLUMN,		
		STATE_COLUMN,			
		PTT_COLUMN,
	};
	
	
	
	///////////////////////////////////////////////////////
	// Constructors:
	
	public VehicleTableModel(List<Vehicle> vehicles, COLUMNS[] headerTable) {
		this.vehicles = (ArrayList<Vehicle>) vehicles;
		this.header = headerTable;
	}
	
	public VehicleTableModel(COLUMNS[] headerTable) {
		this(new ArrayList<Vehicle>(), headerTable);
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
	            	int i = findColumn(COLUMNS.CAR_TYPE_COLUMN);
	            	Object tipo = getValueAt(row, i); 
	                value = (ImageIcon)findImageByColumnCarType(tipo);
	                break;
	            case TARGA_COLUMN:
	                value = v.getTarga();
	                break;
	            case CAR_TYPE_COLUMN:
	                value = v.getTipoVeicolo();
	                break;
	            case MARCA_COLUMN:
	                value = v.getMarca();
	                break;
	            case STATE_COLUMN:
	                value = v.getStato();
	                break;
	            case PTT_COLUMN:
	                value = v.getPtt();
	                break;
	        }
        }
        return value;
	}
	
	
	
	///////////////////////////////////////////////////////
	// My methods:
	
	
	public void addRow(Vehicle vehicle) {
		vehicles.add(vehicle);
	}
	
	public void removeRow(int row) {
		vehicles.remove(row);
	}
	
	// forse da rinominare in removeVehicle
	public boolean removeRow(Vehicle vehicle) {
		return vehicles.remove(vehicle);
	}
	
	
	public Vehicle getVehicleAt(int row) {
        return vehicles.get(row);
    }
	
	// found the corresponding column index
	public int findColumn(COLUMNS columnName) {
		for (int i=0; i<getColumnCount(); i++)
			if (columnName.equals(header[i])) 
		        return i;
		return -1;
	}
	
	// found the right image
	protected static ImageIcon findImageByColumnCarType(Object value) {
		ImageIcon i = null;
		if (value.equals(TipoVeicolo.AUTO))
			i = new ImageIcon(VehicleTableModel.class.getResource("/images/Car-icon_32.png"));
		else if (value.equals(TipoVeicolo.AUTOARTICOLATO))
			i = new ImageIcon(VehicleTableModel.class.getResource("/images/City-Truck-blue-icon_32.png"));
		else if (value.equals(TipoVeicolo.AUTOCARRO))
			i = new ImageIcon(VehicleTableModel.class.getResource("/images/lorry-icon.png"));
		else if (value.equals(TipoVeicolo.FURGONE))
			i = new ImageIcon(VehicleTableModel.class.getResource("/images/truck-icon-autocarro_32.png"));
		return i;
	}

	
	
	///////////////////////////////////////////////////////
	// other methods (from AbstractTableModel) to ovveride:
	
	
    @Override
	public Class<?> getColumnClass(int col) {
    	Class<?> c;
    	COLUMNS column = header[col];
    	if (column.equals(COLUMNS.IMAGE_COLUMN))
    		c = ImageIcon.class;
    	else if (column.equals(COLUMNS.CAR_TYPE_COLUMN))
    		c =  JComboBox.class;
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
    	else if (column.equals(COLUMNS.CAR_TYPE_COLUMN))
    		return "Tipo veicolo";
    	else if (column.equals(COLUMNS.MARCA_COLUMN))
    		return "Marca";
    	else if (column.equals(COLUMNS.STATE_COLUMN))
    		return "Stato";
    	else if (column.equals(COLUMNS.PTT_COLUMN))
    		return "PTT";
    	return super.getColumnName(col);
    };
	
    
	@Override
	public boolean isCellEditable(int row, int col) {
		return true;
	}
	
	
    @Override
    public void setValueAt(Object value, int row, int col) {
    	System.out.println("Siamo in setValueAt("+value+") !!!");
    	Vehicle v = vehicles.get(row);
    	if (v!=null) {
        	COLUMNS column = header[col];
	        switch (column) {
	            case TARGA_COLUMN:
	                v.setTarga((String) value);
	                break;
	            case CAR_TYPE_COLUMN:
	                v.setTipoVeicolo((TipoVeicolo) value);
	                break;
	            case MARCA_COLUMN:
	                v.setMarca((String) value);
	                break;
	            case STATE_COLUMN:
	                v.setStato((Stato) value);
	                break;
	            case PTT_COLUMN:
	                v.setPtt((float) value);
	                break;
	        }
        }
		fireTableRowsUpdated(row, col);
    }
    
	@Override
	public <T extends EventListener> T[] getListeners(Class<T> paramClass) {
		return super.getListeners(paramClass);
	}
	
	
	@Override
	public int findColumn(String paramString) {
		return super.findColumn(paramString);
	}

	
}
