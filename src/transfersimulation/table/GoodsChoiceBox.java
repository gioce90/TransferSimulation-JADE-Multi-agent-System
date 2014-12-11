package transfersimulation.table;


import jade.core.Agent;
import jade.core.behaviours.OneShotBehaviour;
import jade.lang.acl.ACLMessage;
import jade.lang.acl.UnreadableException;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Vector;

import javax.swing.DefaultCellEditor;
import javax.swing.JCheckBox;
import javax.swing.JFrame;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JPanel;
import javax.swing.table.AbstractTableModel;
import javax.swing.table.TableColumn;
import javax.swing.JButton;

import transfersimulation.AgentUtility;
import transfersimulation.model.goods.Goods;

import java.awt.Dimension;
import java.awt.BorderLayout;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;
import java.io.IOException;


public class GoodsChoiceBox extends JFrame  {
	
	private static final long serialVersionUID = 1L;
	private GoodsTableModel<Goods> goodsModel;
	private JButton btnEsegui;
	private JButton btnAnnulla;
	
	public GoodsChoiceBox() {
		setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		//setType(Type.POPUP);
		setAlwaysOnTop(true);
		
		
		List<String> colonne = new ArrayList<String>();
		colonne.add("SELEZIONA");
		colonne.add("Descrizione");		colonne.add("Dimensioni x*y*z");
		colonne.add("Q.tà");			colonne.add("Volume");
		colonne.add("Tipo");			colonne.add("Pericolosa");
		colonne.add("Partenza");		colonne.add("Destinazione");
		colonne.add("Dal");				colonne.add("Entro");
		
		/**
		 * COSTRUTTORE
		 */
		goodsModel = new GoodsTableModel<Goods>(colonne) {
			private static final long serialVersionUID = 1L;
			
			@Override
			public Object getValueAt(int rowIndex, int columnIndex) {
				Goods g = goodsModel.getDataObject(rowIndex);
				Object s = "?";
				switch (columnIndex){
					case 0: s= goodsModel.isSelected(rowIndex); break;
					case 1: s= g.getDescrizione(); break;
					case 2: s= g.getDimensione(); break;
					case 3: s= String.valueOf(g.getQuantità()); break;
					case 4: s= String.valueOf(g.getVolume()); break;
					case 5: s= g.getTipo(); break;
					case 6: s= g.isPericolosa(); break;
					case 7: s= g.getLocationStart(); break;
					case 8: s= g.getLocationStart(); break;
					case 9: s= String.valueOf(g.getDateStart()); break;
					case 10: s= String.valueOf(g.getDateLimit())+" gg"; break;
				}
				return s;
			}
			
			@Override
			public Class<?> getColumnClass(int columnIndex) {
				Class<?> c = String.class;
		    	if (columnIndex==0)
		    		c = Boolean.class;
		    	//else c = super.getColumnClass(columnIndex);
		    	return c;
			}
			
		    @Override
		    public boolean isCellEditable(int row, int column) {
		    	return (column==0);
		    }
		    
		};
		JPanel jp = new JPanel();
		jp.setLayout(new BorderLayout(0, 0));
		
		JTable jTable = new JTable(goodsModel);
		jTable.setFillsViewportHeight(true);
		JScrollPane goodsScrollPane = new JScrollPane(jTable);
		jp.add(goodsScrollPane);
		
		TableColumn column = jTable.getColumnModel().getColumn(0);
		
		JPanel btnPanel = new JPanel();
		jp.add(btnPanel, BorderLayout.SOUTH);
		
		btnAnnulla = new JButton("Annulla");
		btnAnnulla.setActionCommand("REJECT");
		btnPanel.add(btnAnnulla);
		
		btnEsegui = new JButton("Esegui");
		btnEsegui.setActionCommand("ACCEPT");
		btnPanel.add(btnEsegui);
		
		this.setContentPane(jp);
		this.setSize(new Dimension(600, 250));
		JCheckBox jcb = new JCheckBox();
	    column.setCellEditor(new DefaultCellEditor(jcb));
		
	}
	
	
	public GoodsChoiceBox(final Agent agent, final ACLMessage propose){
		this();
		setTitle("Merci disponibili da: "+propose.getSender().getLocalName()
				+" per "+agent.getLocalName());
		try {
			Vector<Goods> goods = (Vector<Goods>) propose.getContentObject();
			if (goods!=null)
				for (Goods good : goods)
					goodsModel.addRow(good);
		} catch (UnreadableException e) {
			e.printStackTrace();
		}
		
		btnEsegui.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				agent.addBehaviour(new OneShotBehaviour() {
					private static final long serialVersionUID = 1L;
					public void action() {
						Vector<Goods> selectedGoods = (Vector<Goods>) getSelectedGoods();
						if (selectedGoods!=null && !selectedGoods.isEmpty()){
							acceptPropose(agent,propose,selectedGoods);
						} else {
							rejectPropose(agent, propose);
						}
					}
				});
			}
		});
		
		btnAnnulla.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				agent.addBehaviour(new OneShotBehaviour() {
					private static final long serialVersionUID = 1L;
					public void action() {
						rejectPropose(agent, propose);
					}
				});
			}
		});
		
	}
	
	
	private void rejectPropose(Agent agent, ACLMessage propose){
		ACLMessage reply = new ACLMessage(ACLMessage.REJECT_PROPOSAL);
		System.out.println("Agent "+agent.getLocalName()
			+": send REJECT PROPOSAL to "+propose.getSender().getLocalName());
		reply.addReceiver(agent.getAID());
		reply.setReplyWith("response"+propose.getReplyWith());
		agent.send(reply);
		dispose();
	}
	
	
	private void acceptPropose(Agent agent, ACLMessage propose, Vector<Goods> selectedGoods){
		ACLMessage reply = new ACLMessage(ACLMessage.ACCEPT_PROPOSAL);
		try {
			reply.setContentObject(selectedGoods);
		} catch (IOException e) {
			e.printStackTrace();
		}
		System.out.println("Agent "+agent.getLocalName()
			+": send ACCEPT PROPOSAL to "+propose.getSender().getLocalName());
		reply.addReceiver(agent.getAID());
		reply.setReplyWith("response"+propose.getReplyWith());
		agent.send(reply);
		dispose();
	}

	
	public Vector<Goods> getSelectedGoods(){
		return goodsModel.getSelectedDataObjects();
	}
	
	
	public static void main(String[] args) {
		new GoodsChoiceBox().setVisible(true);
	}
	
	
	
	
	/**
	 * @class GoodsTableModel
	 * @author Cecco
	 * @param <T>
	 */
	protected abstract class GoodsTableModel<T> extends AbstractTableModel {
		
		private static final long serialVersionUID = 1L;
		
		private class Row<T> {
			protected T dato;
			private boolean selected;
			
			public Row(T dato) {
				this.dato = dato;
				this.selected = false;
			}
		}
		
		
	    private final List<String> columnNames;
	    private final ArrayList<Row<T>> data;
	    
	    public GoodsTableModel() {
	    	this.data = new ArrayList<>();
	        this.columnNames = new ArrayList<>();
	    }

	    public GoodsTableModel(List<String> columnIdentifiers) {
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

	    
	    
	    // ATTENZIONE:
	    
	    public void addRow(T dataObject) {
	        int rowIndex = this.data.size();
	        this.data.add(new Row<T>(dataObject));
	        fireTableRowsInserted(rowIndex, rowIndex);
	    }

	    /*
	    public void addRows(List<T> dataObjects) {
	        if (!dataObjects.isEmpty()) {
	            int firstRow = data.size();
	            
	            this.data.addAll(dataObjects);
	            int lastRow = data.size() - 1;
	            fireTableRowsInserted(firstRow, lastRow);
	        }
	    }
	    */
	    
	    public void insertRow(T dataObject, int rowIndex) {
	        this.data.add(rowIndex, new Row<T>(dataObject));
	        fireTableRowsInserted(rowIndex, rowIndex);
	    }

	    public void deleteRow(int rowIndex) {
	        if (this.data.remove(this.data.get(rowIndex))) {
	            fireTableRowsDeleted(rowIndex, rowIndex);
	        }
	    }

	    public T getDataObject(int rowIndex) {
	        return this.data.get(rowIndex).dato;
	    }

	    /*
	    public List<T> getDataObjects(int firstRow, int lastRow) {
	        List<Row<T>> subList = this.data.subList(firstRow, lastRow);
	        return Collections.unmodifiableList(subList);
	    }
	    
	    public List<T> getDataObjects() {
	    	List<Row<T>> c = Collections.unmodifiableList(this.data);
	    	List<T> t = new ArrayList<T>();
	    	for (Row<T> row : c) {
				t.add(row.dato);
			}
	        return t;
	    } */
	    
	    public Vector<T> getSelectedDataObjects() {
	    	Vector<T> item = new Vector<T>();
	    	for (Row<T> row : this.data) 
				if (row.selected)
					item.add(row.dato);
	    	return item;
		}
	    
	    
	    public void clearTableModelData() {
	        if (!this.data.isEmpty()) {
	            int lastRow = data.size() - 1;
	            this.data.clear();
	            fireTableRowsDeleted(0, lastRow);
	        }
	    }
	    
	    public boolean isSelected(int rowIndex) {
			return this.data.get(rowIndex).selected;
		}
	    
	    
		@Override
	    public void setValueAt(Object value, int row, int column) {
			if (value instanceof Boolean&&column==0) {
				this.data.get(row).selected = (boolean) value;
				fireTableCellUpdated(row, column);
			}
		}
		
	}


	
}
