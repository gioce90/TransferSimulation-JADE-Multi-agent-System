package it.transfersimulation;

import java.awt.BorderLayout;
import java.awt.FlowLayout;

import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JPanel;
import javax.swing.JTable;
import javax.swing.border.EmptyBorder;
import javax.swing.table.DefaultTableModel;
import javax.swing.JLabel;
import javax.swing.JTextField;

import java.awt.GridLayout;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;

public class VehicleInsertJDialog extends JDialog {

	private final JPanel contentPanel = new JPanel();
	private JTextField textFieldTarga;
	private JTextField textFieldTipoVeicolo;
	private JTextField textFieldMarca;
	private JTextField textFieldStato;
	private JTextField textFieldPesoTrasportabile;
	private JButton okButton;

	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		try {
			VehicleInsertJDialog dialog = new VehicleInsertJDialog();
			dialog.setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
			dialog.setVisible(true);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/**
	 * Create the dialog.
	 */
	private VehicleInsertJDialog() {
		setBounds(100, 100, 400, 230);
		getContentPane().setLayout(new BorderLayout());
		getContentPane().add(contentPanel, BorderLayout.CENTER);
		
		contentPanel.setBorder(new EmptyBorder(5, 5, 5, 5));
		contentPanel.setLayout(new GridLayout(0, 2, 0, 0));
		
		{
			JLabel lblTarga = new JLabel("Targa");
			JLabel lblTipoVeicolo = new JLabel("Tipo veicolo");
			JLabel lblMarca = new JLabel("Marca");
			JLabel lblStato = new JLabel("Stato");
			JLabel lblPesoTrasportabile = new JLabel("Peso trasportabile");
			
			textFieldTarga = new JTextField();
			textFieldTipoVeicolo = new JTextField();
			textFieldMarca = new JTextField();
			textFieldStato = new JTextField();
			textFieldPesoTrasportabile = new JTextField();
			
			textFieldTarga.setColumns(10);
			textFieldTipoVeicolo.setColumns(10);
			textFieldMarca.setColumns(10);
			textFieldStato.setColumns(10);
			textFieldPesoTrasportabile.setColumns(10);
			
			contentPanel.add(lblTarga);
			contentPanel.add(textFieldTarga);
			contentPanel.add(lblTipoVeicolo);
			contentPanel.add(textFieldTipoVeicolo);
			contentPanel.add(lblMarca);
			contentPanel.add(textFieldMarca);
			contentPanel.add(lblStato);
			contentPanel.add(textFieldStato);
			contentPanel.add(lblPesoTrasportabile);
			contentPanel.add(textFieldPesoTrasportabile);
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
	
	
	ShipperAgentGUI gui;
	DefaultTableModel tm;
	
	public VehicleInsertJDialog(final ShipperAgentGUI gui, final DefaultTableModel model) {
		this();
		
		okButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				 
				VehicleInsertJDialog.this.gui = gui;
				gui.addVehicle(
						model,
						textFieldTarga.getText(),
						textFieldTipoVeicolo.getText(),
						textFieldMarca.getText(),
						textFieldStato.getText(),
						textFieldPesoTrasportabile.getText()
				);
			}
		});
	}
}
