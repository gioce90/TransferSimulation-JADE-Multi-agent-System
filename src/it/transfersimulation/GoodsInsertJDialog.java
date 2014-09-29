package it.transfersimulation;

import java.awt.BorderLayout;
import java.awt.FlowLayout;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.border.EmptyBorder;
import javax.swing.table.DefaultTableModel;

public class GoodsInsertJDialog extends JDialog {

	private final JPanel contentPanel = new JPanel();
	private JTextField textFieldCod;
	private JTextField textFieldTipoMerce;
	private JTextField textFieldNomeMerce;
	private JButton okButton;
	
	/**
	 * Create the dialog.
	 */
	public GoodsInsertJDialog() {
		setBounds(100, 100, 400, 230);
		getContentPane().setLayout(new BorderLayout());
		getContentPane().add(contentPanel, BorderLayout.CENTER);
		
		contentPanel.setBorder(new EmptyBorder(5, 5, 5, 5));
		contentPanel.setLayout(new GridLayout(0, 2, 0, 0));
		
		{
			JLabel lblCod = new JLabel("Cod");
			JLabel lblTipoMerce = new JLabel("Tipo merce");
			JLabel lblNomeMerce = new JLabel("Nome merce");
			
			textFieldCod = new JTextField();
			textFieldTipoMerce = new JTextField();
			textFieldNomeMerce = new JTextField();
			
			textFieldCod.setColumns(10);
			textFieldTipoMerce.setColumns(10);
			textFieldNomeMerce.setColumns(10);
			
			contentPanel.add(lblCod);
			contentPanel.add(textFieldCod);
			contentPanel.add(lblTipoMerce);
			contentPanel.add(textFieldTipoMerce);
			contentPanel.add(lblNomeMerce);
			contentPanel.add(textFieldNomeMerce);
		}
		
		{
			JPanel buttonPane = new JPanel();
			buttonPane.setLayout(new FlowLayout(FlowLayout.RIGHT));
			getContentPane().add(buttonPane, BorderLayout.SOUTH);
			
			okButton = new JButton("OK");
			okButton.setActionCommand("OK");
			buttonPane.add(okButton);
			getRootPane().setDefaultButton(okButton);

			JButton cancelButton = new JButton("Cancel");
			cancelButton.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent e) {
					dispose();
				}
			});
			cancelButton.setActionCommand("Cancel");
			buttonPane.add(cancelButton);
			
		}
		pack();
		setVisible(true);
	}
	
	DefaultTableModel tm;
	public GoodsInsertJDialog(JTable table) {
		this();
		
		tm = (DefaultTableModel) table.getModel();
		
		okButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				tm.addRow( new Object[] {
						textFieldCod.getText(),
						textFieldTipoMerce.getText(),
						textFieldNomeMerce.getText()
				});
			}
		});
	}
	
	
	
	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		try {
			GoodsInsertJDialog dialog = new GoodsInsertJDialog();
			dialog.setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
			dialog.setVisible(true);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

}