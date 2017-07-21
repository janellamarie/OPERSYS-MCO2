package cal_train;

public class Station {
	
	private int station_number;
	private Train currentTrain;
	private int nPassengers;
	
	public Station(int station_number){
		this.station_number = station_number;
	}

	public Train createTrain(int count){
		
		for(int i = 0; i < 15; i++)	
			if(Driver.trains[i] == null){
				Train temp = new Train(i+1, count);
				Driver.trains[i] = temp;
				System.out.println("** Successfully created Train with " + count + " available seats.");
				return temp;
			} 
		
		return null;
	}

	public int getStation_number() {
		return station_number;
	}

	public void setStation_number(int station_number) {
		this.station_number = station_number;
	}

	public Train getCurrentTrain() {
		return currentTrain;
	}

	public void setCurrentTrain(Train currentTrain) {
		this.currentTrain = currentTrain;
	}

	public int getnPassengers() {
		return nPassengers;
	}

	public void setnPassengers(int nPassengers) {
		this.nPassengers = nPassengers;
	}
	
	
	
}
