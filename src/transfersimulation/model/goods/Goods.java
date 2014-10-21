package transfersimulation.model.goods;

public class Goods {
	
	private String codice;
	private String nome, descrizione, dimensione;
	private int quantità;
	
	// tipo (solido, liquido, gas.... sfusa o non..)
	
	
	public Goods(String codice, String nome, String descrizione, String dimensione, int quantità) {
		this.codice = codice;
		this.nome = nome;
		this.descrizione = descrizione;
		this.dimensione = dimensione;
		this.quantità = quantità;
	}

	
	
	
	public String getCodice() {
		return codice;
	}

	public void setCodice(String codice) {
		this.codice = codice;
	}

	public String getNome() {
		return nome;
	}

	public void setNome(String nome) {
		this.nome = nome;
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

	public int getQuantità() {
		return quantità;
	}

	public void setQuantità(int quantità) {
		this.quantità = quantità;
	}
	
}
