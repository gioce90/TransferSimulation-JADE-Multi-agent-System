package transfersimulation.model.vehicle;

import java.io.Serializable;



public abstract class Vehicle implements Serializable {
	
	public enum Stato {
		DISPONIBILE,
		NON_DISPONIBILE
	}
	
	private String plate;	// targa
	
	private String mark;	// Marca, Casa Produttrice
	private String model;	// Modello
	private String trim;	// Trim
	
	private float weight;			// peso, misurato in tonnellate ???
	private float carryingCapacity;	// capacità di carico (portata massima) misurato in tonnellate
	private float ptt = weight+carryingCapacity; // massa a pieno carico, misurata in tonnellate
	
	private float volume; 		// volume, misurato in metri cubi
	private float width;		// larghezza, misurata in metri
	private float length;		// lunghezza, misurata in metri
	private float height;		// altezza, misurata in metri
	
	private Stato stato;		// Stato del veicolo
	
	private String allestimento;
	
	private String locazioneAttuale = "";
	
	
	//private short europallet;	// numero di Pedane Standard da Trasporto trasportabili
	
	// forse in futuro
	//private Class tipo = this.getClass();
	
	/*
	enum Categoria {
		// privato?
		COMMERCIALE,
		INDUSTRIALE
	}; 
	*/
	
	public Vehicle(){}
	
	public Vehicle(String plate) {
		this.plate=plate;
	}
	
	
	
	///////////////////////
	// Get and Set methods 
	
	public String getPlate() {
		return plate;
	}
	public void setPlate(String plate) {
		this.plate = plate;
	}
	
	public String getMark() {
		return mark;
	}
	public void setMark(String mark) {
		this.mark = mark;
	}
	
	public String getModel() {
		return model;
	}
	public void setModel(String model) {
		this.model = model;
	}
	
	public String getTrim() {
		return trim;
	}
	public void setTrim(String trim) {
		this.trim = trim;
	}
	
	public float getWeight() {
		return weight;
	}
	public void setWeight(float weight) {
		this.weight = weight;
	}
	
	public float getCarryingCapacity() {
		return carryingCapacity;
	}
	public void setCarryingCapacity(float carryingCapacity) {
		this.carryingCapacity = carryingCapacity;
	}
	
	public float getPtt() {
		return ptt;
	}
	public void setPtt(float ptt) {
		this.ptt = ptt;
	}
	
	public float getVolume() {
		return volume;
	}
	public void setVolume(float volume) {
		this.volume = volume;
	}
	
	public float getWidth() {
		return width;
	}
	public void setWidth(float width) {
		this.width = width;
	}
	
	public float getLength() {
		return length;
	}
	public void setLength(float length) {
		this.length = length;
	}
	
	public float getHeight() {
		return height;
	}
	public void setHeight(float height) {
		this.height = height;
	}
	
	public Stato getState() {
		return stato;
	}
	public void setStato(Stato value) {
		this.stato=value;
	}
	
	public  Class<? extends Vehicle> getType() {
		return this.getClass();
	}

	public String getLocazioneAttuale() {
		return locazioneAttuale;
	}

	public void setLocazioneAttuale(String locazioneAttuale) {
		this.locazioneAttuale = locazioneAttuale;
	}

	public String getAllestimento() {
		return allestimento;
	}

	public void setAllestimento(String allestimento) {
		this.allestimento = allestimento;
	}
	
}
