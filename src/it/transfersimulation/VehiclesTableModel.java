package it.transfersimulation;


import it.transfersimulation.Vehicle.TipoVeicolo;

import javax.swing.ImageIcon;
import javax.swing.JComboBox;
import javax.swing.table.DefaultTableModel;

public class VehiclesTableModel extends DefaultTableModel {
	
	public enum COLUMNS {
			IMAGE_COLUMN,
			TARGA_COLUMN,
			CAR_TYPE_COLUMN,
			MARCA_COLUMN,		
			STATE_COLUMN,			
			PTT_COLUMN,
	};
	
	
	public VehiclesTableModel(Object[] headerTable) {
		super(null, headerTable);
	}
    
    
    @Override
	public Class<?> getColumnClass(int columnIndex) {
    	Class<?> c;
    	if (super.getColumnName(columnIndex).equals(COLUMNS.IMAGE_COLUMN.toString()))
    		c = ImageIcon.class;
    	else if (super.getColumnName(columnIndex).equals(COLUMNS.CAR_TYPE_COLUMN.toString()))
    		c =  JComboBox.class;
    	else if (super.getColumnName(columnIndex).equals(COLUMNS.STATE_COLUMN.toString()))
    		c =  JComboBox.class;
    	else c = super.getColumnClass(columnIndex);
    	return c;
    }
    
    
    @Override
    public String getColumnName(int col) {
    	if (super.getColumnName(col).equals(COLUMNS.IMAGE_COLUMN.toString()))
    		return " ";
    	else if (super.getColumnName(col).equals(COLUMNS.TARGA_COLUMN.toString()))
    		return "Targa";
    	else if (super.getColumnName(col).equals(COLUMNS.CAR_TYPE_COLUMN.toString()))
    		return "Tipo veicolo";
    	else if (super.getColumnName(col).equals(COLUMNS.MARCA_COLUMN.toString()))
    		return "Marca";
    	else if (super.getColumnName(col).equals(COLUMNS.STATE_COLUMN.toString()))
    		return "Stato";
    	else if (super.getColumnName(col).equals(COLUMNS.PTT_COLUMN.toString()))
    		return "PTT";
    	return super.getColumnName(col);
    };
	
    
    @Override
    public void setValueAt(Object value, int row, int col) {
        /*if (col == 0) {
            ImageIcon icon = findImageByColumnCarType(value);
            super.setValueAt(icon, row, 0);
        } else */
    	super.setValueAt(value, row, col);
    }
	
    
	private ImageIcon findImageByColumnCarType(Object value) {
		ImageIcon i = null;
		if (value.equals(TipoVeicolo.AUTO))
			i = new ImageIcon(ShipperAgentGUI.class.getResource("/images/lorry-add.png"));
		else if (value.equals(TipoVeicolo.AUTOARTICOLATO))
			i = new ImageIcon(ShipperAgentGUI.class.getResource("/images/lorry-delete.png"));
		else if (value.equals(TipoVeicolo.AUTOCARRO))
			i = new ImageIcon(ShipperAgentGUI.class.getResource("/images/lorry-icon.png"));
		else if (value.equals(TipoVeicolo.FURGONE))
			i = new ImageIcon(ShipperAgentGUI.class.getResource("/images/lorry-icon.png"));
		else if (value.equals(TipoVeicolo.MINIVAN))
			i = new ImageIcon(ShipperAgentGUI.class.getResource("/images/lorry-icon.png"));
		return i;
	}
	
	
}
