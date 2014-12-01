package transfersimulation;

import java.util.List;

import transfersimulation.model.goods.Goods;

public interface BuyerInterface {
	void addGoods(Goods g);
	void removeGoods(Goods g);
	List<Goods> getGoods();
	
	/*void newTruck(Vehicle vehicle);
	void removeTruck(Vehicle vehicle);
	void activateTruck(Vehicle vehicle);
	void deactivateTruck(Vehicle vehicle);*/
	
	
}
