package transfersimulation.table;

import java.io.Serializable;
import java.util.ArrayList;

import javax.swing.ImageIcon;
import javax.swing.JComboBox;
import javax.swing.table.AbstractTableModel;

import transfersimulation.model.vehicle.Car;
import transfersimulation.model.vehicle.RoadTractor;
import transfersimulation.model.vehicle.SemiTrailer;
import transfersimulation.model.vehicle.SemiTrailerTruck;
import transfersimulation.model.vehicle.Trailer;
import transfersimulation.model.vehicle.TrailerTruck;
import transfersimulation.model.vehicle.Truck;
import transfersimulation.model.vehicle.Van;
import transfersimulation.model.vehicle.Vehicle;
import transfersimulation.model.vehicle.Vehicle.Stato;

public class VehicleTableModel extends AbstractTableModel implements Serializable {
	
	private static final long serialVersionUID = 1L;
	private ArrayList<Vehicle> vehicles;
	private COLUMNS[] header;
	
	
	// possible column names:
	public enum COLUMNS implements Serializable {
		IMAGE_COLUMN,
		TARGA_COLUMN,
		TYPE_COLUMN,
		MARK_COLUMN,		
		STATE_COLUMN,			
		PTT_COLUMN,
		SETTINGUP_COLUMN,
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
	                value = findImageByColumnCarType(v.getType());
	                break;
	            case TARGA_COLUMN:
	                value = v.getPlate();
	                break;
	            case TYPE_COLUMN:
	                value = findStringByColumnCarType(v.getType());
	                break;
	            case MARK_COLUMN:
	                value = v.getMark();
	                break;
	            case SETTINGUP_COLUMN:
	                value = v.getAllestimento();
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
	protected int findColumn(COLUMNS columnName) {
		for (int i=0; i<getColumnCount(); i++)
			if (columnName.equals(header[i])) 
		        return i;
		return -1;
	}

	
	// TODO controlla se un valore in un campo già esiste in tutte le righe
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
	
	/*
	private int getColumnIndex(COLUMNS column){
		for(int i=0;i<header.length;i++){
			if (column.equals(header[i])){
				return i;
			}
		}
		return -1;
	}
	*/

	// found the right image
	private static ImageIcon findImageByColumnCarType(Class<? extends Vehicle> type) {
		ImageIcon i = null;
		if (type.equals(Car.class))						// auto
			i = new ImageIcon(VehicleTableModel.class.getResource("/images/vehicles/car_32.png"));
		else if (type.equals(Van.class))				// furgone
			i = new ImageIcon(VehicleTableModel.class.getResource("/images/vehicles/van_32.png"));
		else if (type.equals(Truck.class))				// autocarro
			i = new ImageIcon(VehicleTableModel.class.getResource("/images/vehicles/truck_32.png"));
		else if (type.equals(TrailerTruck.class))		// autotreno
			i = new ImageIcon(VehicleTableModel.class.getResource("/images/vehicles/trailertruck_32.png"));
		else if (type.equals(SemiTrailer.class))		// semirimorchio
			i = new ImageIcon(VehicleTableModel.class.getResource("/images/vehicles/semitrailer_32.png"));
		else if (type.equals(RoadTractor.class))		// trattore stradale
			i = new ImageIcon(VehicleTableModel.class.getResource("/images/vehicles/roadtractor_32.png"));
		
		else if (type.equals(SemiTrailerTruck.class))	// autoarticolato
			i = new ImageIcon(VehicleTableModel.class.getResource("/images/vehicles/semitrailertruck_32.png"));
		else if (type.equals(Trailer.class))			// rimorchio
			i = new ImageIcon(VehicleTableModel.class.getResource("/images/vehicles/semitrailer_32.png"));
		
		return i;
	}
	
	
	private static String findStringByColumnCarType(Class<? extends Vehicle> type) {
		String i = "?";
		if (type.equals(Car.class))
			i = "Automobile";
		else if (type.equals(Van.class))
			i = "Furgone";
		else if (type.equals(Truck.class))
			i = "Autocarro";
		else if (type.equals(TrailerTruck.class))
			i = "Autotreno"; 
		else if (type.equals(SemiTrailerTruck.class))
			i = "Autoarticolato";
		else if (type.equals(RoadTractor.class))
			i = "Trattore stradale";
		else if (type.equals(Trailer.class))
			i = "Rimorchio";
		else if (type.equals(SemiTrailer.class))
			i = "Semirimorchio";
		return i;
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
    	else if (column.equals(COLUMNS.SETTINGUP_COLUMN))
    		return "Allestimento";
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
	            case SETTINGUP_COLUMN:
	            	if (!v.getAllestimento().equals(value)){
	            		v.setAllestimento((String) value);
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
