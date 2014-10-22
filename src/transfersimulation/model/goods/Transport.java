package transfersimulation.model.goods;

import java.util.Date;


public class Transport {
	
	// TODO forse da rinominare in Order
	// da aggiungere la data di presa in consegna e la data di consegna
	// tipo di mezzo richiesto
	
	Goods goods;
	String locationStart, locationEnd;
	private Date dateStart;
	
	public Transport(Goods goods, String locationStart, String locationEnd, Date dateStart) {
		this.goods = goods;
		this.locationStart = locationStart;
		this.locationEnd = locationEnd;
		this.dateStart = dateStart;
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
}
