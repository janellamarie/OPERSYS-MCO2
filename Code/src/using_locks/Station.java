package using_locks;

import java.util.ArrayList;

public class Station {
	
	private ArrayList<Train> trains; //16 trains lang ba per station OR 16 trains all in all?



	private int station_number;

	public Station(int station_number){
		trains = new ArrayList<Train>();
		this.station_number = station_number;
	}

	public void createTrain(int count, int train_number){
		trains.add(new Train(train_number, count));
	}
	
	public ArrayList<Train> getTrains() {
		return trains;
	}

	public void setTrains(ArrayList<Train> trains) {
		this.trains = trains;
	}

	public int getStation_number() {
		return station_number;
	}

	public void setStation_number(int station_number) {
		this.station_number = station_number;
	}
	
}
