package transfersimulation.model.vehicle;

public class SemiTrailerTruck extends Vehicle {
	
	RoadTractor roadTruck;
	SemiTrailer semiTrailer;
	
	
	public SemiTrailerTruck(RoadTractor roadTruck, SemiTrailer semiTrailer) {
		this.roadTruck=roadTruck;
		this.semiTrailer=semiTrailer;
		
	}
	
	@Override
	public String getPlate() {
		return roadTruck.getPlate()+" - "+semiTrailer.getPlate();
	}
	
	@Override
	public String getAllestimento() {
		String s="";
		if (roadTruck.getAllestimento()!=""){
			s+=roadTruck.getAllestimento();
			if (semiTrailer.getAllestimento()!="")
				s+=", "+semiTrailer.getAllestimento();
		} else if (semiTrailer.getAllestimento()!="")
			s+=semiTrailer.getAllestimento();
		return s;
	}
	
	@Override
	public float getCarryingCapacity() {
		return ((Vehicle) roadTruck).getCarryingCapacity()+semiTrailer.getCarryingCapacity();
	}
	
	@Override
	public float getPtt() {
		return ((Vehicle) roadTruck).getPtt()+semiTrailer.getPtt();
	}
	
	@Override
	public float getVolume() {
		return ((Vehicle) roadTruck).getVolume()+semiTrailer.getVolume();
	}
	
	@Override
	public String getMark() {
		return ((Vehicle) roadTruck).getMark()+" - "+semiTrailer.getMark();
	}
	
	@Override
	public Stato getState() {
		if ( roadTruck.getState().equals(Stato.DISPONIBILE)
		&& semiTrailer.getState().equals(Stato.DISPONIBILE))
			return Stato.DISPONIBILE;
		else return Stato.NON_DISPONIBILE;
	}
	
	
	@Override
	public String toString() {
	  return "SEMITRAILERTRUCK"; // assumes nombre is a string
	}
	
	// TODO tutti i settaggi automatici dovuti alla unione di due veicoli.
}
