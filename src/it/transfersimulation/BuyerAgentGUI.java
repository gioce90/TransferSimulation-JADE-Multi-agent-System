package it.transfersimulation;

import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.table.DefaultTableModel;
import javax.swing.JButton;
import javax.swing.BoxLayout;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.Toolkit;
import java.awt.ComponentOrientation;
import java.awt.Panel;
import java.awt.Button;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;


public class BuyerAgentGUI extends JFrame {
	
	JPanel panel = new JPanel();
	private Panel panel_1;
	private Panel merciPanel;
	private Panel panel_2;
	private Button btnPM_plus;
	private Button btnPM_meno;
	private JTable merciTable;
	
	private String[] headerTable = {
			"Cod", "Tipo merce", "Nome merce", };
	
	private Object[][] primeMerci = {
			{"111", "Sfusa", "Grano"},
			{"222", "Sfusa", "Terra"},
			{"333", "solida", "Lavatrici"},
			{"444", "liquida", "petrolio"},
			{"555", "liquida", "acqua"},
			{"666", "gas", "gas"}
	};
	
	DefaultTableModel dtModelParcoMezzi = new DefaultTableModel(
			primeMerci,	headerTable
	);
	
	DefaultTableModel dtModelMezziDisponibili = new DefaultTableModel(
			new Object[][] { primeMerci[0] }
			, headerTable
	);
	
	
	
	public BuyerAgentGUI() {
		setTitle("Buyer Agent: ");
		getContentPane().add(panel, BorderLayout.CENTER);
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		panel.setComponentOrientation(ComponentOrientation.LEFT_TO_RIGHT);
		panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
		
		merciPanel = new Panel();
		panel.add(merciPanel);
		
		{
			panel_1 = new Panel();
			panel_1.setLayout(new BoxLayout(panel_1, BoxLayout.Y_AXIS));
			merciPanel.add(panel_1);
			
			JLabel label = new JLabel("Merci:");
			panel_1.add(label);
			
			merciTable = new JTable();
			merciTable.setModel(dtModelParcoMezzi);
			merciTable.setPreferredScrollableViewportSize(new Dimension(500, 70));
			merciTable.setFillsViewportHeight(true);
			panel_1.add(new JScrollPane(merciTable));
			
			
			panel_2 = new Panel();
			merciPanel.add(panel_2);
			panel_2.setLayout(new BoxLayout(panel_2, BoxLayout.Y_AXIS));
		}
		
		btnPM_plus = new Button("+");
		btnPM_plus.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				new GoodsInsertJDialog(merciTable);
			}
		});
		panel_2.add(btnPM_plus);
		
		btnPM_meno = new Button("-");
		btnPM_meno.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				int selectedRow = merciTable.getSelectedRow();
				if (selectedRow != -1)
					dtModelParcoMezzi.removeRow(selectedRow);
			}
		});
		panel_2.add(btnPM_meno);
		
		JButton btnSearch = new JButton("Search");
		panel.add(btnSearch);
		
		showGui();
	}
	
    
	public void showGui() {
		pack();
		Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
		int centerX = (int)screenSize.getWidth() / 2;
		int centerY = (int)screenSize.getHeight() / 2;
		setLocation(centerX - getWidth() / 2, centerY - getHeight() / 2);
		super.setVisible(true);
	}
	
	public static void main(String[] args) {
		new BuyerAgentGUI();
	}
}