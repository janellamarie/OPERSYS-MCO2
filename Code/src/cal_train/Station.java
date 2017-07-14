package cal_train;

public class Station {
	
	private int station_number;

	public Station(int station_number){
		this.station_number = station_number;
	}

	public void createTrain(int count, Train trains[]){
		if(trains.length < 15)
			trains[trains.length] = new Train(trains.length, count);
		else
			System.out.print("CANNOT CREATE NEW TRAIN.");
	}

	public int getStation_number() {
		return station_number;
	}

	public void setStation_number(int station_number) {
		this.station_number = station_number;
	}
	
}
