package transfersimulation.table;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import javax.swing.table.AbstractTableModel;


/**
 * Abstract base class which extends from {@code AbstractTableModel} and 
 * provides an API to work with user-defined POJO's as table rows. Sub-classes 
 * extending from {@code DataObjectTableModel} must implement 
 * {@code getValueAt(row, column)} method. 
 * 
 * By default cells are not editable. If those are intended to be editable then 
 * sub-classes should override both {@code isCellEditable(row, column)} and 
 * {@code setValueAt(row, column)} methods.
 * 
 * Finally, it is not mandatory but highly recommended to override 
 * {@code getColumnClass(column)} method, in order to return the appropriate 
 * column class: default implementation always returns {@code Object.class}.
 * 
 * @param <T> The data object's class handled by this TableModel.
 */
public abstract class DataObjectTableModel<T> extends AbstractTableModel implements Serializable {

	private static final long serialVersionUID = 1L;
	private final List<String> columnNames;
    private final List<T> data;

    public DataObjectTableModel() {
        this.data = new ArrayList<>();
        this.columnNames = new ArrayList<>();
    }

    public DataObjectTableModel(List<String> columnIdentifiers) {
        this();
        if (columnIdentifiers != null) {
            this.columnNames.addAll(columnIdentifiers);
        }
    }

    @Override
    public int getRowCount() {
        return this.data.size();
    }

    @Override
    public int getColumnCount() {
        return this.columnNames.size();
    }

    @Override
    public String getColumnName(int columnIndex) {
        return this.columnNames.get(columnIndex);
    }

    public void setColumnNames(List<String> columnNames) {
        if (columnNames != null) {
            this.columnNames.clear();
            this.columnNames.addAll(columnNames);
            fireTableStructureChanged();
        }
    }

    public List<String> getColumnNames() {
        return Collections.unmodifiableList(this.columnNames);
    }

    public void addRow(T dataObject) {
        int rowIndex = this.data.size();
        this.data.add(dataObject);
        fireTableRowsInserted(rowIndex, rowIndex);
    }

    public void addRows(List<T> dataObjects) {
        if (!dataObjects.isEmpty()) {
            int firstRow = data.size();
            this.data.addAll(dataObjects);
            int lastRow = data.size() - 1;
            fireTableRowsInserted(firstRow, lastRow);
        }
    }

    public void insertRow(T dataObject, int rowIndex) {
        this.data.add(rowIndex, dataObject);
        fireTableRowsInserted(rowIndex, rowIndex);
    }

    public void deleteRow(int rowIndex) {
        if (this.data.remove(this.data.get(rowIndex))) {
            fireTableRowsDeleted(rowIndex, rowIndex);
        }
    }
    
    public void deleteRow(T object) {
        if (this.data.remove(object)) {
            fireTableDataChanged();
        }
    }

    public T getDataObject(int rowIndex) {
        return this.data.get(rowIndex);
    }

    public List<T> getDataObjects(int firstRow, int lastRow) {
        List<T> subList = this.data.subList(firstRow, lastRow);
        return Collections.unmodifiableList(subList);
    }

    public List<T> getDataObjects() {
        return Collections.unmodifiableList(this.data);
    }

    public void clearTableModelData() {
        if (!this.data.isEmpty()) {
            int lastRow = data.size() - 1;
            this.data.clear();
            fireTableRowsDeleted(0, lastRow);
        }
    }
    
    
}