package transfersimulation;

import jade.core.AID;

import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.UIManager;
import javax.swing.UnsupportedLookAndFeelException;
import javax.swing.JButton;
import javax.swing.BoxLayout;
import javax.swing.border.EmptyBorder;
import javax.swing.text.DefaultCaret;

import transfersimulation.model.goods.Goods;
import transfersimulation.table.DataObjectTableModel;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.GridLayout;
import java.awt.Toolkit;
import java.awt.ComponentOrientation;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;
import java.sql.Date;
import java.util.ArrayList;

public class BuyerAgentGUI extends JFrame {
	private static final long serialVersionUID = 1L;
	
	// Variabili di classe
	private JPanel masterPanel;
	private JButton btnPM_plus;
	private JButton btnPM_meno;
	private JTable goodsTable;
	protected DataObjectTableModel<Goods> goodsModel;
	
	BuyerAgent buyerAgent;

	private JTextArea communicationTextArea;
	
	//////////////////////////////////////////////////////////////
	// CONSTRUCTOR:
	
	public BuyerAgentGUI() {}
	
	public BuyerAgentGUI(BuyerAgent buyer) {
		
		buyerAgent=buyer;
		
		//////////////////////////////////////////////////////////////
		// Tabella
		
		final ArrayList<String> colonne = new ArrayList<String>();
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
		colonne.add("Necessità");
		
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
					case 7: s= g.getLocationEnd(); break;
					case 8: s= String.valueOf(g.getDateStart()); break;
					case 9: s= String.valueOf(g.getDateLimit())+" gg"; break;
					case 10: s= g.getNecessità(); break;
				}
				return s;
			}
		};
		///////////////////////////////////////////////////////////////////////
		
		///////////////////////////////////////////////////////////////////////
		// Graphics:
		
		setTitle("Buyer agent: "+buyerAgent.getLocalName());
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
		goodsTable.setPreferredScrollableViewportSize(new Dimension(800, 100));
		goodsTable.setFillsViewportHeight(true);
		JScrollPane goodsScrollPane = new JScrollPane(goodsTable);
		pnlTableGoodsPanel.add(goodsScrollPane);
		
		// Communication Panel
		JPanel communicationPanel = new JPanel();
		communicationPanel.setLayout(new BoxLayout(communicationPanel, BoxLayout.Y_AXIS));
		masterPanel.add(communicationPanel);
		
		JPanel pnlHeaderCommunicationPanel = new JPanel();
		FlowLayout fl_pnlHeaderCommunicationPanel = (FlowLayout) pnlHeaderCommunicationPanel.getLayout();
		fl_pnlHeaderCommunicationPanel.setAlignment(FlowLayout.LEFT);
		communicationPanel.add(pnlHeaderCommunicationPanel);
		
		JLabel lblComunicazioni = new JLabel("Comunicazioni:");
		pnlHeaderCommunicationPanel.add(lblComunicazioni);
		
		communicationTextArea = new JTextArea(5,0);
		communicationTextArea.setLineWrap(true);
		((DefaultCaret)communicationTextArea.getCaret()).setUpdatePolicy(DefaultCaret.ALWAYS_UPDATE);
		JScrollPane scrollPane = new JScrollPane(communicationTextArea);
		communicationPanel.add(scrollPane);
		
		
		// Pulsanti
		JPanel pnlBtnGoodsPanel = new JPanel();
		pnlTableGoodsPanel.add(pnlBtnGoodsPanel);
		pnlBtnGoodsPanel.setLayout(new BoxLayout(pnlBtnGoodsPanel, BoxLayout.Y_AXIS));
		
		btnPM_plus = new JButton("+");
		btnPM_plus.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				new InsertGoodsJDialog();
			}
		});
		pnlBtnGoodsPanel.add(btnPM_plus);
		
		btnPM_meno = new JButton("-");
		btnPM_meno.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				int selectedRow = goodsTable.getSelectedRow();
				if (selectedRow != -1){
					buyerAgent.removeGoods(goodsModel.getDataObject(selectedRow));
					//goodsModel.deleteRow(selectedRow);
				}
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
	
	
	////////////////////
	// METODI
	
	
	public void insertInfo(String info){
		communicationTextArea.append(info+"\n");
		System.out.println("Agent "+buyerAgent.getLocalName()+": "+info);
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
		buyerAgent.doDelete();
	}
	
	
	///////////////
	// INNER CLASS
	
	private class InsertGoodsJDialog extends JDialog {
		
		private static final long serialVersionUID = 1L;
		private JPanel contentPanel = new JPanel();
		private JButton okButton;
		
		public InsertGoodsJDialog(){
			setBounds(100, 100, 400, 230);
			getContentPane().setLayout(new BorderLayout());
			getContentPane().add(contentPanel, BorderLayout.CENTER);
			
			contentPanel.setBorder(new EmptyBorder(5, 5, 5, 5));
			contentPanel.setLayout(new GridLayout(0, 2, 0, 0));
			
			{
				JLabel lblCodice = new JLabel("Codice");
				final JTextField txtCodice = new JTextField(10);
				contentPanel.add(lblCodice);
				contentPanel.add(txtCodice);
				
				JLabel lblDescrizione = new JLabel("Descrizione");
				final JTextField txtDescrizione = new JTextField(10);
				contentPanel.add(lblDescrizione);
				contentPanel.add(txtDescrizione);
				
				JLabel lblDimensioni = new JLabel("Dimensioni x*y*z");
				final JTextField txtDimensioni = new JTextField(10);
				contentPanel.add(lblDimensioni);
				contentPanel.add(txtDimensioni);
				
				JLabel lblQtà = new JLabel("Q.tà");
				final JTextField txtQtà = new JTextField(10);
				contentPanel.add(lblQtà);
				contentPanel.add(txtQtà);
				
				JLabel lblVolume = new JLabel("Volume");
				final JTextField txtVolume = new JTextField(10);
				contentPanel.add(lblVolume);
				contentPanel.add(txtVolume);
				
				JLabel lblTipo = new JLabel("Tipo");
				final JTextField txtTipo = new JTextField(10);
				contentPanel.add(lblTipo);
				contentPanel.add(txtTipo);
				
				JLabel lblPericolosa = new JLabel("Pericolosa");
				final JTextField txtPericolosa = new JTextField(10);
				contentPanel.add(lblPericolosa);
				contentPanel.add(txtPericolosa);
				
				JLabel lblNecessità = new JLabel("Necessità");
				final JTextField txtNecessità = new JTextField(10);
				contentPanel.add(lblNecessità);
				contentPanel.add(txtNecessità);
				
				JLabel lblPartenza = new JLabel("Partenza");
				final JTextField txtPartenza = new JTextField(10);
				contentPanel.add(lblPartenza);
				contentPanel.add(txtPartenza);
				
				JLabel lblDestinazione = new JLabel("Destinazione");
				final JTextField txtDestinazione = new JTextField(10);
				contentPanel.add(lblDestinazione);
				contentPanel.add(txtDestinazione);
				
				JLabel lblDal = new JLabel("Dal");
				final JTextField txtDal = new JTextField(10);
				contentPanel.add(lblDal);
				contentPanel.add(txtDal);
				
				JLabel lblEntro = new JLabel("Entro");
				final JTextField txtEntro = new JTextField(10);
				contentPanel.add(lblEntro);
				contentPanel.add(txtEntro);
				
				okButton = new JButton("OK");
				okButton.addActionListener(new ActionListener() {
					public void actionPerformed(ActionEvent e) {
						Goods g = new Goods();
						
						if (!txtCodice.getText().isEmpty())
							g.setCodice(txtCodice.getText());
						if (!txtDescrizione.getText().isEmpty())
							g.setDescrizione(txtDescrizione.getText());
						if (!txtDimensioni.getText().isEmpty())
							g.setDimensione(txtDimensioni.getText());
						if (!txtTipo.getText().isEmpty())
							g.setTipo(txtTipo.getText());
						
						if (!txtPartenza.getText().isEmpty())
							g.setLocationStart(txtPartenza.getText());
						
						if (!txtDestinazione.getText().isEmpty())
							g.setLocationEnd(txtDestinazione.getText());
						
						
						try{
							if (!txtDal.getText().isEmpty())
								g.setDateStart(Date.valueOf(txtDal.getText()));
						} catch (IllegalArgumentException e1) {}
						
						try{
							if (!txtQtà.getText().isEmpty())
								g.setQuantità(Integer.valueOf(txtQtà.getText()));
							if (!txtVolume.getText().isEmpty())
								g.setVolume(Double.valueOf(txtVolume.getText()));
							if (!txtEntro.getText().isEmpty())
								g.setDateLimit(Integer.valueOf(txtEntro.getText()));
						} catch (NumberFormatException e1) {}
						
						if (!txtPericolosa.getText().isEmpty())
							g.setPericolosa(Boolean.valueOf(txtPericolosa.getText()));
						
						if (!txtNecessità.getText().isEmpty())
							g.setNecessità(txtNecessità.getText());
						
						goodsModel.addRow(g);
						buyerAgent.addGoods(g);
					}
				});
				
				JButton cancelButton = new JButton("Cancel");
				cancelButton.addActionListener(new ActionListener() {
					public void actionPerformed(ActionEvent e) {
						dispose();
					}
				});
				 
				JPanel buttonPane = new JPanel();
				buttonPane.setLayout(new FlowLayout(FlowLayout.RIGHT));
				getContentPane().add(buttonPane, BorderLayout.SOUTH);
				getRootPane().setDefaultButton(okButton);
				buttonPane.add(okButton);
				buttonPane.add(cancelButton);
			}
			
			pack();
			setVisible(true);
		}
	}
	
	

}

