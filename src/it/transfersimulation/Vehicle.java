package it.transfersimulation;

public class Vehicle {
	 
	// PER QUESTA VERSIONE DO PER SCONTATO CHE SIANO TUTTI VEICOLI
	// NON SCOMPONIBILI (niente autotreni o autoarticolati)
	
	enum Categoria {
		// privato?
		COMMERCIALE,
		INDUSTRIALE
	};
	
	enum TipoVeicolo {
		AUTO, FURGONE,
		AUTOCARRO,
		AUTOARTICOLATO // da rimuovere o no?
		//, AUTOTRENO
		//, autosnodato?
	};
	
	enum Stato {
		DISPONIBILE,
		NON_DISPONIBILE,
		IN_VIAGGIO
		// ... ?
	}

	private String targa;
	private float ptt;			// massa a pieno carico, misurata in tonnellate
	private String marca;
	private Stato stato;
	private TipoVeicolo tipoVeicolo;
	
	private Categoria categoria;
	private float portata;		// misurata in tonnellate
	private float volume; 		// misurata in metri cubi
	
	private float larghezza;	// misurata in metri
	private float lunghezza;	// misurata in metri
	private float altezza;		// misurata in metri
	
	private short europallet;	// numero di Pedane Standard da Trasporto trasportabili
	
	//Date datImmatricolazione;
	//String polizza;
	
	
	public Vehicle(String targa, TipoVeicolo tipoVeicolo, String marca,	Stato stato, float ptt) {
		this.targa=targa;
		this.tipoVeicolo=tipoVeicolo;
		this.marca=marca;
		this.stato=stato;
		this.ptt=ptt;
	}
	
	
	//////////////////////////////////////////////
	// GET and SET methods
	
	/**
	 * @return the targa
	 */
	public String getTarga() {
		return targa;
	}

	/**
	 * @param targa the targa to set
	 */
	public void setTarga(String targa) {
		this.targa = targa;
	}

	/**
	 * @return the ptt
	 */
	public float getPtt() {
		return ptt;
	}

	/**
	 * @param ptt the ptt to set
	 */
	public void setPtt(float ptt) {
		this.ptt = ptt;
	}

	/**
	 * @return the portata
	 */
	public float getPortata() {
		return portata;
	}

	/**
	 * @param portata the portata to set
	 */
	public void setPortata(float portata) {
		this.portata = portata;
	}

	/**
	 * @return the volume
	 */
	public float getVolume() {
		return volume;
	}

	/**
	 * @param volume the volume to set
	 */
	public void setVolume(float volume) {
		this.volume = volume;
	}

	/**
	 * @return the larghezza
	 */
	public float getLarghezza() {
		return larghezza;
	}

	/**
	 * @param larghezza the larghezza to set
	 */
	public void setLarghezza(float larghezza) {
		this.larghezza = larghezza;
	}

	/**
	 * @return the lunghezza
	 */
	public float getLunghezza() {
		return lunghezza;
	}

	/**
	 * @param lunghezza the lunghezza to set
	 */
	public void setLunghezza(float lunghezza) {
		this.lunghezza = lunghezza;
	}

	/**
	 * @return the altezza
	 */
	public float getAltezza() {
		return altezza;
	}

	/**
	 * @param altezza the altezza to set
	 */
	public void setAltezza(float altezza) {
		this.altezza = altezza;
	}

	/**
	 * @return the europallet
	 */
	public short getEuropallet() {
		return europallet;
	}

	/**
	 * @param europallet the europallet to set
	 */
	public void setEuropallet(short europallet) {
		this.europallet = europallet;
	}

	/**
	 * @return the marca
	 */
	public String getMarca() {
		return marca;
	}

	/**
	 * @param marca the marca to set
	 */
	public void setMarca(String marca) {
		this.marca = marca;
	}

	/**
	 * @return the categoria
	 */
	public Categoria getCategoria() {
		return categoria;
	}

	/**
	 * @param categoria the categoria to set
	 */
	public void setCategoria(Categoria categoria) {
		this.categoria = categoria;
	}

	/**
	 * @return the tipoVeicolo
	 */
	public TipoVeicolo getTipoVeicolo() {
		return tipoVeicolo;
	}

	/**
	 * @param tipoVeicolo the tipoVeicolo to set
	 */
	public void setTipoVeicolo(TipoVeicolo tipoVeicolo) {
		this.tipoVeicolo = tipoVeicolo;
	}

	/**
	 * @return the stato
	 */
	public Stato getStato() {
		return stato;
	}

	/**
	 * @param stato the stato to set
	 */
	public void setStato(Stato stato) {
		this.stato = stato;
	}

	
}
