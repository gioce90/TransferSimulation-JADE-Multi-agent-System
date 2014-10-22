package transfersimulation.model.goods;

public class Goods {
	
	private String codice;
	private String descrizione, dimensione;
	private int quantit�;
	private double volume;
	
	// tipo (solido, liquido, gas.... sfusa o non..)
	
	
	public Goods(String codice, String descrizione, String dimensione, int quantit�, double volume) {
		this.codice = codice;
		this.descrizione = descrizione;
		this.dimensione = dimensione;
		this.quantit� = quantit�;
		this.volume = volume;
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

	public int getQuantit�() {
		return quantit�;
	}

	public void setQuantit�(int quantit�) {
		this.quantit� = quantit�;
	}
	
	public double getVolume() {
		return volume;
	}
	
	public void setVolume(int volume) {
		this.volume = volume;
	}
}
