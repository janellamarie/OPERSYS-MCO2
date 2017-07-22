package cal_train;

import java.util.ArrayList;

public class Train implements Runnable{

	private int train_number;	
	private int seats;
	private ArrayList<Passenger> passengers;
	
	private Station nextStation;
	private Station currentStation;
	
	private Station stations[];
	
	public Train(int train_number, int count){
		seats = count;
		this.train_number = train_number;
		passengers = new ArrayList<Passenger>();
		
		stations = new Station[8];
		stations = CalTrain.stations.clone();
	}
	
	public Train(int train_number){
		this.train_number = train_number;
	}
	
	@Override
	public void run() {		
		for (int i = 0; i < 8; i++) 
			stations[i].trainArrived(this);
	}	 
	
	public void addPassenger(Passenger p){
		if(passengers != null)
			passengers.add(p);
	}
	
	public void removePassengers(){
		for(int i = 0; i < passengers.size(); i++){
			if(currentStation.getStation_number() == passengers.get(i).getDestination().getStation_number()){
				passengers.remove(i);
			}
		}
		System.out.println("AFTER REMOVING (IN METHOD): " + passengers.size());
	}
	
	public void moveTrains(){
		
		System.out.println("MOVING TRAIN # " + train_number);
		while(!CalTrain.mutex.tryAcquire()){
			
		}
		
		System.out.println("--> Train acquired mutex");
		
		if(currentStation.getStation_number() < 8){
			int temp = currentStation.getStation_number();
			System.out.println("NEW CURRENT STATION: " + temp);
			currentStation = stations[temp];
			
			System.out.println("NEW NEXT STATION: " + (temp+1));
			nextStation = stations[temp+1];
		}
		
		CalTrain.mutex.release();
		System.out.println("--> Train released mutex\n");
	}
	
	/* SETTERS AND GETTERS */
	
	public int getSeats() {
		return seats; 
	}

	public void setSeats(int seats) {
		this.seats = seats;
	}

	public ArrayList<Passenger> getPassengers() {
		return passengers;
	}

	public void setPassengers(ArrayList<Passenger> passengers) {
		this.passengers = passengers;
	}

	public int getTrain_number() {
		return train_number;
	}

	public void setTrain_number(int train_number) {
		this.train_number = train_number;
	}

	public Station getNextStation() {
		return nextStation;
	}

	public void setNextStation(Station nextStation) {
		this.nextStation = nextStation;
	}

	public Station getCurrentStation() {
		return currentStation;
	}

	public void setCurrentStation(Station currentStation) {
		this.currentStation = currentStation;
	}

	public Station[] getStations() {
		return stations;
	}

	public void setStations(Station[] stations) {
		this.stations = stations;
	}
	
	
}
