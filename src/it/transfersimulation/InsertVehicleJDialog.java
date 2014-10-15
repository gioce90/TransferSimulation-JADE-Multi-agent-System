package it.transfersimulation;

import it.transfersimulation.ShipperAgentGUI.Coordinator;
import it.transfersimulation.model.*;

import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.FlowLayout;

import javax.swing.DefaultListCellRenderer;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JDialog;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;
import javax.swing.table.DefaultTableModel;
import javax.swing.JLabel;
import javax.swing.JTextField;

import java.awt.GridLayout;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;

import javax.swing.DefaultComboBoxModel;

public class InsertVehicleJDialog extends JDialog {
 
	private final JPanel contentPanel = new JPanel();
	
	private JTextField textFieldTarga;
	private JTextField textFieldMarca;
	private JTextField textFieldPesoTrasportabile;
	private JLabel jlbSuggest;
	
	private JButton okButton;
	
	private JComboBox<Stato> stato = new JComboBox<Stato>(Stato.values());
	private JComboBox<Class<?>> vehicleType = new JComboBox<Class<?>>();
	DefaultComboBoxModel<Class<?>> dcbm = new DefaultComboBoxModel<Class<?>>( 
		new Class[] {Truck.class, Trailer.class, RoadTractor.class, SemiTrailer.class, Van.class, Car.class});
	
	ImageIcon icon;
	
	ShipperAgentGUI gui;
	
	
	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		try {
			InsertVehicleJDialog dialog = new InsertVehicleJDialog();
			dialog.setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
			dialog.setVisible(true);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/**
	 * Create the dialog.
	 */
	private InsertVehicleJDialog() {
		this.setTitle("Insert Vehicle");
		setBounds(100, 100, 400, 230);
		getContentPane().setLayout(new BorderLayout());
		getContentPane().add(contentPanel, BorderLayout.NORTH);
		
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
			jlbSuggest = new JLabel();
			
			textFieldTarga.setColumns(10);
			textFieldMarca.setColumns(10);
			textFieldPesoTrasportabile.setColumns(10);
			jlbSuggest.setText(" ");
			
			vehicleType.setModel(dcbm);
			vehicleType.setRenderer(new DefaultListCellRenderer() {
				private static final long serialVersionUID = 1L;
				@Override
			    public Component getListCellRendererComponent(JList<?> list, Object value, int index, boolean isSelected, boolean cellHasFocus) {
			        super.getListCellRendererComponent(list, value, index, isSelected, cellHasFocus);
			        if(value != null) {
			            setText(((Class<?>)value).getSimpleName());
			        }
			        return this;
			    }
			});
			
			contentPanel.add(lblTarga);
			contentPanel.add(textFieldTarga);
			contentPanel.add(lblTipoVeicolo);
			contentPanel.add(vehicleType);
			contentPanel.add(lblMarca);
			contentPanel.add(textFieldMarca);
			contentPanel.add(lblStato);
			contentPanel.add(stato);
			contentPanel.add(lblPesoTrasportabile);
			contentPanel.add(textFieldPesoTrasportabile);
		}
		
		{
			JPanel suggestPanel = new JPanel();
			suggestPanel.setLayout(new FlowLayout(FlowLayout.CENTER));
			suggestPanel.add(jlbSuggest);
			getContentPane().add(suggestPanel, BorderLayout.CENTER);
		}
		
		{
			JPanel buttonPane = new JPanel();
			buttonPane.setLayout(new FlowLayout(FlowLayout.RIGHT));
			getContentPane().add(buttonPane, BorderLayout.SOUTH);
			
			okButton = new JButton("OK");
			buttonPane.add(okButton);
			getRootPane().setDefaultButton(okButton);

			JButton cancelButton = new JButton("Cancel");
			cancelButton.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent e) {
					dispose();
				}
			});
			buttonPane.add(cancelButton);
			
		}
		pack();
		setVisible(true);
	}
	
	
	
	
	public InsertVehicleJDialog(final ShipperAgentGUI gui, final Coordinator coordinator) {
		this();
		
		okButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				
				Class<? extends Vehicle> tipo = (Class<? extends Vehicle>) vehicleType.getSelectedItem();
				
				Vehicle vehicle = null;
				
				String targa = textFieldTarga.getText();
				String ptt = textFieldPesoTrasportabile.getText();
				String mark = textFieldMarca.getText();
				
				try {
					if (targa.isEmpty())
						throw new InsertVehicleException("Inserire targa");
					if( ptt.isEmpty())
						throw new InsertVehicleException("Inserire ptt");
					if( mark.isEmpty())
						throw new InsertVehicleException("Inserire marca");
					
					if (tipo.equals(Truck.class))
						vehicle = new Truck(targa);
					else if (tipo.equals(RoadTractor.class))
						vehicle = new RoadTractor(targa);
					else if (tipo.equals(Trailer.class))
						vehicle = new Trailer(targa);
					else if (tipo.equals(SemiTrailer.class))
						vehicle = new SemiTrailer(targa);
					else if (tipo.equals(Car.class))
						vehicle = new Car(targa);
					else if (tipo.equals(Van.class))
						vehicle = new Van(targa);
					
					vehicle.setMark(mark);
					vehicle.setStato((Stato)stato.getSelectedItem());
					
					try {
						vehicle.setPtt(Float.valueOf(ptt));
					} catch (NumberFormatException e1) {
						throw new InsertVehicleException("Inserire un valore di ptt valido");
					}
					
					jlbSuggest.setText(" ");
					gui.addVehicle(coordinator, vehicle);
					
				} catch (InsertVehicleException ee) {
					jlbSuggest.setText(ee.getMessage());
				}
			}
		});
	}
	
	
	
}
