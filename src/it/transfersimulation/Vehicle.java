package it.transfersimulation;

import java.util.Vector;

public class Vehicle {
	
	// PER QUESTA VERSIONE DO PER SCONTATO CHE SIANO TUTTI VEICOLI
	// NON SCOMPONIBILI (niente autotreni o autoarticolati)
	
	enum Categoria {
		// privato?
		COMMERCIALE,
		INDUSTRIALE
	};
	
	enum TipoVeicolo {
		AUTO, MINIVAN, FURGONE,
		AUTOCARRO
		//, AUTOTRENO, AUTOARTICOLATO
		//, autosnodato?
	};
	
	enum Stato {
		DISPONIBILE,
		NON_DISPONIBILE,
		IN_VIAGGIO
	}

	String targa;		
	
	float ptt; 			// massa a pieno carico, misurata in tonnellate
	float portata;		// misurata in tonnellate
	float volume; 		// misurata in meri cubi
	
	float larghezza;	// misurata in metri
	float lunghezza;	// misurata in metri
	float altezza;		// misurata in metri
	
	short europallet;	// numero di Pedane Standard da Trasporto trasportabili
	
	String marca;
	
	Categoria categoria;
	Stato stato;
	TipoVeicolo tipoVeicolo;
	
	
	//Date datImmatricolazione;
	//String polizza;
	
	private static Vector<String> header;
	private static Vector<Class> headerType;
	
	public Vehicle(String targa, TipoVeicolo tipoVeicolo, String marca,	Stato stato, float ptt) {
		this.targa=targa;
		this.tipoVeicolo=tipoVeicolo;
		this.marca=marca;
		this.stato=stato;
		this.ptt=ptt;
	}
	
	/*
	public static Vector<String> getHeader(){
		header = new Vector<String>();
		header.add("TARGA");
		header.add("TIPO VEICOLO");
		header.add("MARCA");
		header.add("STATO");
		header.add("PTT");
		return header;
	}
	
	
	public static Vector<Class> getHeaderType(){
		headerType = new Vector<Class>();
		headerType.add(String.class);
		headerType.add(TipoVeicolo.class);
		headerType.add(String.class);
		headerType.add(Stato.class);
		headerType.add(Float.class);
		return headerType;
	}
	*/
	
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
