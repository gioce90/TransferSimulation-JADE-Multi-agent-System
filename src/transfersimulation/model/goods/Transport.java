package transfersimulation.model.goods;


public class Transport {
	
	// TODO forse da rinominare in Order
	// da aggiungere la data di presa in consegna e la data di consegna
	//
	
	Goods goods;
	String locationStart, locationEnd;
	
	public Transport(Goods goods, String locationStart, String locationEnd) {
		this.goods = goods;
		this.locationStart = locationStart;
		this.locationEnd = locationEnd;
	}
}
