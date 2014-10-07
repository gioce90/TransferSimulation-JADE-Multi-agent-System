package it.transfersimulation;

import it.transfersimulation.ShipperAgentGUI.Coordinator;
import it.transfersimulation.Vehicle.Stato;
import it.transfersimulation.Vehicle.TipoVeicolo;

import java.awt.BorderLayout;
import java.awt.FlowLayout;

import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JDialog;
import javax.swing.JPanel;
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
	private JComboBox<TipoVeicolo> tipoVeicolo = new JComboBox<TipoVeicolo>(TipoVeicolo.values());
	private JTextField textFieldMarca;
	private JComboBox<Stato> stato = new JComboBox<Stato>(Stato.values());
	private JTextField textFieldPesoTrasportabile;
	private JButton okButton;

	
	ShipperAgentGUI gui;
	DefaultTableModel tm;
	
	
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
			textFieldMarca = new JTextField();
			textFieldPesoTrasportabile = new JTextField();
			
			textFieldTarga.setColumns(10);
			textFieldMarca.setColumns(10);
			textFieldPesoTrasportabile.setColumns(10);
			
			contentPanel.add(lblTarga);
			contentPanel.add(textFieldTarga);
			contentPanel.add(lblTipoVeicolo);
			contentPanel.add(tipoVeicolo);
			contentPanel.add(lblMarca);
			contentPanel.add(textFieldMarca);
			contentPanel.add(lblStato);
			contentPanel.add(stato);
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
	
	
	public VehicleInsertJDialog(final ShipperAgentGUI gui, final Coordinator coordinator) {
		this();
		
		okButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				TipoVeicolo tipo = (TipoVeicolo)tipoVeicolo.getSelectedItem();
				
				try{
					if (textFieldTarga.getText().equals(""))
						throw new InsertVehicleException("Inserire targa");
					
					try{
						Vehicle v = new Vehicle(
								textFieldTarga.getText(),
								tipo,
								textFieldMarca.getText(), (Stato)stato.getSelectedItem(),
								Float.valueOf(textFieldPesoTrasportabile.getText())
						);
						
						coordinator.notifyAndAddRow(v);
						//VehicleTableModel.findImageByColumnCarType(tipo),
						
					} catch (NumberFormatException e1){
						throw new InsertVehicleException("Inserire peso in formato numerico");
					}
				} catch (InsertVehicleException e1) {
					System.out.println(e1.getMessage());
				}
			}
		});
	}
}
