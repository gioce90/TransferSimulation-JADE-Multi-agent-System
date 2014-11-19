package transfersimulation.model.goods;

import jade.util.leap.Serializable;

import java.util.Date;


public class Transport implements Serializable{
	
	// TODO forse da rinominare in Order
	// da aggiungere la data di presa in consegna e la data di consegna
	// tipo di mezzo richiesto
	
	Goods goods;
	String locationStart, locationEnd;
	private Date dateStart;
	private int dateLimit;
	
	public Transport(Goods goods, String locationStart, String locationEnd, Date dateStart, int dateLimit) {
		this.goods = goods;
		this.locationStart = locationStart;
		this.locationEnd = locationEnd;
		this.dateStart = dateStart;
		this.dateLimit = dateLimit;
	}
	
	

	public Goods getGoods() {
		return goods;
	}

	public void setGoods(Goods goods) {
		this.goods = goods;
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
