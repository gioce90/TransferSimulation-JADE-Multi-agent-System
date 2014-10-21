package transfersimulation;

import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.UIManager;
import javax.swing.UnsupportedLookAndFeelException;
import javax.swing.JButton;
import javax.swing.BoxLayout;

import transfersimulation.model.goods.Goods;
import transfersimulation.table.DataObjectTableModel;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Toolkit;
import java.awt.ComponentOrientation;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;
import java.util.ArrayList;
import java.util.List;

public class BuyerAgentGUI extends JFrame {
	
	// Variabili di classe
	private JPanel masterPanel;
	private JButton btnPM_plus;
	private JButton btnPM_meno;
	private JTable goodsTable;
	private DataObjectTableModel<Goods> goodsModel;
	
	BuyerAgent buyerAgent;
	
	//////////////////////////////////////////////////////////////
	// CONSTRUCTOR:
	
	public BuyerAgentGUI(BuyerAgent buyer) {
		
		buyerAgent=buyer;
		
		//////////////////////////////////////////////////////////////
		// Tabella
		
		List<String> colonne = new ArrayList<String>();
		colonne.add("Nome");
		colonne.add("Dimensioni x*y*z");
		colonne.add("Q.tà");
		
		goodsModel = new DataObjectTableModel<Goods>(colonne) {
			private static final long serialVersionUID = 1L;

			@Override
			public Object getValueAt(int rowIndex, int columnIndex) {
				Goods g = goodsModel.getDataObject(rowIndex);
				String s = "?";
				switch (columnIndex){
					case 0: s= g.getNome(); break;
					case 1: s= g.getDimensione(); break;
					case 2: s= String.valueOf(g.getQuantità()); break;
				}
				return s;
			}
		};
		///////////////////////////////////////////////////////////////////////
		
		///////////////////////////////////////////////////////////////////////
		// Graphics:
		
		setTitle("Buyer: "+buyerAgent.getLocalName());
		setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		
		try {
			UIManager.setLookAndFeel("javax.swing.plaf.nimbus.NimbusLookAndFeel");
			//UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
		} catch (ClassNotFoundException | InstantiationException
			   | IllegalAccessException | UnsupportedLookAndFeelException e) {
			e.printStackTrace();
		}
		
		// Master Panel
		masterPanel = new JPanel();
		masterPanel.setLayout(new BoxLayout(masterPanel, BoxLayout.Y_AXIS));
		masterPanel.setComponentOrientation(ComponentOrientation.LEFT_TO_RIGHT);
		getContentPane().add(masterPanel, BorderLayout.CENTER);
		
		// Goods Panel
		JPanel goodsPanel = new JPanel();
		goodsPanel.setLayout(new BoxLayout(goodsPanel, BoxLayout.Y_AXIS));
		masterPanel.add(goodsPanel);
		
		JPanel pnlHeaderGoodsPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
		goodsPanel.add(pnlHeaderGoodsPanel);
		JLabel goodsLabel = new JLabel("Merci:");
		pnlHeaderGoodsPanel.add(goodsLabel);
		
		JPanel pnlTableGoodsPanel = new JPanel();
		pnlTableGoodsPanel.setLayout(new BoxLayout(pnlTableGoodsPanel, BoxLayout.X_AXIS));
		goodsPanel.add(pnlTableGoodsPanel);
		
		goodsTable = new JTable(goodsModel);
		goodsTable.setPreferredScrollableViewportSize(new Dimension(500, 100));
		goodsTable.setFillsViewportHeight(true);
		JScrollPane goodsScrollPane = new JScrollPane(goodsTable);
		pnlTableGoodsPanel.add(goodsScrollPane);
		
		JPanel pnlBtnGoodsPanel = new JPanel();
		pnlTableGoodsPanel.add(pnlBtnGoodsPanel);
		pnlBtnGoodsPanel.setLayout(new BoxLayout(pnlBtnGoodsPanel, BoxLayout.Y_AXIS));
		
		btnPM_plus = new JButton("+");
		btnPM_plus.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
			//	new GoodsInsertJDialog(merciTable);
			}
		});
		pnlBtnGoodsPanel.add(btnPM_plus);
		
		btnPM_meno = new JButton("-");
		btnPM_meno.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				int selectedRow = goodsTable.getSelectedRow();
				if (selectedRow != -1)
					goodsModel.deleteRow(selectedRow);
			}
		});
		pnlBtnGoodsPanel.add(btnPM_meno);
		
		JButton btnSearch = new JButton("Search");
		masterPanel.add(btnSearch);
		
		// Riempimento dati
		for (Goods g: buyerAgent.getGoods())
			goodsModel.addRow(g);
		
		showGui();
		
	}
	
	
	
	public void showGui() {
		pack();
		Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
		int centerX = (int) screenSize.getWidth() / 2;
		int centerY = (int) screenSize.getHeight() / 2;
		setLocation(centerX - getWidth() / 2, centerY - getHeight() / 2);
		super.setVisible(true);
	}
	
	// on dispose, delete the agent
	public void dispose() {
		super.dispose();
		buyerAgent.doSuspend();
		buyerAgent.doDelete();
	}
	
}