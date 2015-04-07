package transfersimulation.model.goods;

import java.io.Serializable;
import java.util.Date;

public class Goods implements Serializable {
	private static final long serialVersionUID = 1L;
	
	private String codice;
	private String descrizione;
	private String tipo;
	private String dimensione;
	private float quantità;
	private float volume;
	private boolean pericolosa;
	
	private String locationStart, locationEnd;
	private Date dateStart;
	private int dateLimit;
	
	private String necessità;
	
	private String buyer;
	
	@Override
	public String toString() {
		return "Cod: "+codice+", "
				+"Descrizione: "+descrizione+", "
				+"Dimensione: "+dimensione+", "
				+"Tipo: "+tipo+", "
				+"Q.tà: "+quantità+", "
				+"Volume: "+volume+", "
				+"Pericolosa: "+pericolosa+", "
				+"Partenza: "+locationStart+", "
				+"Destinazione: "+locationEnd+", "
				+"Da: "+dateStart+", "
				+"Limiti di consegna: "+dateLimit+" gg, "
				+"Allestimenti necessari: "+necessità;
		
	}
	
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

	public void setQuantità(float quantità) {
		this.quantità = quantità;
	}
	
	public double getVolume() {
		return volume;
	}
	
	public void setVolume(float volume) {
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

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((codice == null) ? 0 : codice.hashCode());
		result = prime * result + dateLimit;
		result = prime * result + ((dateStart == null) ? 0 : dateStart.hashCode());
		result = prime * result + ((descrizione == null) ? 0 : descrizione.hashCode());
		result = prime * result + ((dimensione == null) ? 0 : dimensione.hashCode());
		result = prime * result + ((locationEnd == null) ? 0 : locationEnd.hashCode());
		result = prime * result + ((locationStart == null) ? 0 : locationStart.hashCode());
		result = prime * result + (pericolosa ? 1231 : 1237);
		long temp;
		temp = Double.doubleToLongBits(quantità);
		result = prime * result + (int) (temp ^ (temp >>> 32));
		result = prime * result + ((tipo == null) ? 0 : tipo.hashCode());
		temp = Double.doubleToLongBits(volume);
		result = prime * result + (int) (temp ^ (temp >>> 32));
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Goods other = (Goods) obj;
		if (codice == null) {
			if (other.codice != null)
				return false;
		} else if (!codice.equals(other.codice))
			return false;
		if (dateLimit != other.dateLimit)
			return false;
		if (dateStart == null) {
			if (other.dateStart != null)
				return false;
		} else if (!dateStart.equals(other.dateStart))
			return false;
		if (descrizione == null) {
			if (other.descrizione != null)
				return false;
		} else if (!descrizione.equals(other.descrizione))
			return false;
		if (dimensione == null) {
			if (other.dimensione != null)
				return false;
		} else if (!dimensione.equals(other.dimensione))
			return false;
		if (locationEnd == null) {
			if (other.locationEnd != null)
				return false;
		} else if (!locationEnd.equals(other.locationEnd))
			return false;
		if (locationStart == null) {
			if (other.locationStart != null)
				return false;
		} else if (!locationStart.equals(other.locationStart))
			return false;
		if (pericolosa != other.pericolosa)
			return false;
		if (Double.doubleToLongBits(quantità) != Double.doubleToLongBits(other.quantità))
			return false;
		if (tipo == null) {
			if (other.tipo != null)
				return false;
		} else if (!tipo.equals(other.tipo))
			return false;
		if (Double.doubleToLongBits(volume) != Double.doubleToLongBits(other.volume))
			return false;
		return true;
	}

	public String getBuyer() {
		return buyer;
	}

	public void setBuyer(String buyer) {
		this.buyer = buyer;
	}

	public String getNecessità() {
		return necessità;
	}

	public void setNecessità(String necessità) {
		this.necessità = necessità;
	}
	
}