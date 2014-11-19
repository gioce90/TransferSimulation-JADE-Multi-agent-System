package transfersimulation;

import jade.core.AID;

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
import java.util.Iterator;
import java.util.List;
import java.awt.Component;

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
		colonne.add("Descrizione");
		colonne.add("Dimensioni x*y*z");
		colonne.add("Q.tà");
		colonne.add("Volume");
		colonne.add("Tipo");
		colonne.add("Pericolosa");
		colonne.add("Partenza");
		colonne.add("Destinazione");
		colonne.add("Dal");
		colonne.add("Entro");
		
		
		goodsModel = new DataObjectTableModel<Goods>(colonne) {
			private static final long serialVersionUID = 1L;

			@Override
			public Object getValueAt(int rowIndex, int columnIndex) {
				Goods g = goodsModel.getDataObject(rowIndex);
				String s = "?";
				switch (columnIndex){
					case 0: s= g.getDescrizione(); break;
					case 1: s= g.getDimensione(); break;
					case 2: s= String.valueOf(g.getQuantità()); break;
					case 3: s= String.valueOf(g.getVolume()); break;
					case 4: s= g.getTipo(); break;
					case 5: s= String.valueOf(g.isPericolosa()); break;
					case 6: s= g.getLocationStart(); break;
					case 7: s= g.getLocationStart(); break;
					case 8: s= String.valueOf(g.getDateStart()); break;
					case 9: s= String.valueOf(g.getDateLimit())+" gg"; break;
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
		
		JPanel panel_1 = new JPanel();
		pnlTableGoodsPanel.add(panel_1);
		
		goodsTable = new JTable(goodsModel);
		goodsTable.setPreferredScrollableViewportSize(new Dimension(999, 100));
		goodsTable.setFillsViewportHeight(true);
		JScrollPane goodsScrollPane = new JScrollPane(goodsTable);
		panel_1.add(goodsScrollPane);
		
		JPanel pnlBtnGoodsPanel = new JPanel();
		pnlTableGoodsPanel.add(pnlBtnGoodsPanel);
		pnlBtnGoodsPanel.setLayout(new BoxLayout(pnlBtnGoodsPanel, BoxLayout.Y_AXIS));
		
		btnPM_plus = new JButton(" + ");
		btnPM_plus.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
			//	new GoodsInsertJDialog(merciTable); TODO
			}
		});
		pnlBtnGoodsPanel.add(btnPM_plus);
		
		btnPM_meno = new JButton(" - ");
		btnPM_meno.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				int selectedRow = goodsTable.getSelectedRow();
				if (selectedRow != -1)
					goodsModel.deleteRow(selectedRow);
			}
		});
		pnlBtnGoodsPanel.add(btnPM_meno);
		
		JPanel pnlBtn = new JPanel();
		goodsPanel.add(pnlBtn);
		
		JButton btnFoundShipper = new JButton("Trova aziende di trasporto");
		btnFoundShipper.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent paramActionEvent) {
				AID[] shippers = buyerAgent.searchShippers();
				for (int i = 0; i < shippers.length; i++)
					System.out.println(shippers[i].getName());
			}
		});
		pnlBtn.add(btnFoundShipper);
		
		JButton btnSearch = new JButton("Richiedi un trasporto"); //TODO
		pnlBtn.add(btnSearch);
		
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
		buyerAgent.doDelete();
		super.dispose();
	}
	
}