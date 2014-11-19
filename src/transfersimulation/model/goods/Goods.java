package transfersimulation.model.goods;

import java.io.Serializable;
import java.util.Date;

public class Goods implements Serializable {
	
	private String codice;
	private String descrizione;
	private String tipo;
	private String dimensione;
	private double quantità;
	private double volume;
	private boolean pericolosa;
	
	private String locationStart, locationEnd;
	private Date dateStart;
	private int dateLimit;
	
	
	public String getCodice() {
		return codice;
	}
	
	public void setCodice(String codice) {
		this.codice = codice;
	}
	
	public String getDescrizione() {
		return descrizione;
	}

	public void setDescrizione(String descrizione) {
		this.descrizione = descrizione;
	}

	public String getDimensione() {
		return dimensione;
	}

	public void setDimensione(String dimensione) {
		this.dimensione = dimensione;
	}

	public double getQuantità() {
		return quantità;
	}

	public void setQuantità(int quantità) {
		this.quantità = quantità;
	}
	
	public double getVolume() {
		return volume;
	}
	
	public void setVolume(double volume) {
		this.volume = volume;
	}

	public boolean isPericolosa() {
		return pericolosa;
	}

	public void setPericolosa(boolean pericolosa) {
		this.pericolosa = pericolosa;
	}

	public String getTipo() {
		return tipo;
	}

	public void setTipo(String tipo) {
		this.tipo = tipo;
	}

	public String getLocationStart() {
		return locationStart;
	}

	public void setLocationStart(String locationStart) {
		this.locationStart = locationStart;
	}

	public String getLocationEnd() {
		return locationEnd;
	}

	public void setLocationEnd(String locationEnd) {
		this.locationEnd = locationEnd;
	}

	public Date getDateStart() {
		return dateStart;
	}

	public void setDateStart(Date dateStart) {
		this.dateStart = dateStart;
	}

	public int getDateLimit() {
		return dateLimit;
	}

	public void setDateLimit(int dateLimit) {
		this.dateLimit = dateLimit;
	}

}