package using_locks;

import java.util.ArrayList;

public class Station {

	private int station_num;
	private ArrayList<Train> trains;
	private boolean station_avail;
	
	public Station(int station_num){
		trains = new ArrayList<Train>();
		this.station_num = station_num;
		station_avail = true;
	}

	public void createTrain(int count){
		trains.add(new Train(count));
	}
	
	public ArrayList<Train> getTrains() {
		return trains;
	}

	public void setTrains(ArrayList<Train> trains) {
		this.trains = trains;
	}
	
	public void setStationAvail(){
		this.station_avail = true;
	}

	public void setStationNotAvail(){
		this.station_avail = false;
	}

	public boolean getStationAvail(){
		return station_avail;
	}

}
