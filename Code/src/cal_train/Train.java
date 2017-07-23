package cal_train;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;

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
		stations = CalTrain.stations;
	}
	
	public Train(int train_number){
		this.train_number = train_number;
	}
	
	@Override
	public void run() {		
		for (int i = 0; i < 8; i++){
			try {
				setCurrentStation(stations[i]);
				stations[i].trainArrived(this);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
	}	 
	
	public void addPassenger(Passenger p){
		if(passengers != null)
			passengers.add(p);
	}
	
	public void removePassengers(){
		for(int i = 0; i < passengers.size(); i++){
			//System.out.println(train_number);
			if(currentStation.getStation_number() == passengers.get(i).getDestination().getStation_number()){
				passengers.remove(i);
			}
		}
	}
	
	public void moveTrains_semaphores(){
		
		System.out.println("MOVING TRAIN # " + train_number);
		System.out.println("--> Train acquired mutex");
		
		if(currentStation.getStation_number() < 7){
			int temp = currentStation.getStation_number();
			System.out.println("NEW CURRENT STATION: " + stations[temp].getStation_number());
			currentStation = stations[temp];
			
			System.out.println("NEW NEXT STATION: " + stations[temp+1].getStation_number());
			nextStation = stations[temp+1];
		} else if(currentStation.getStation_number() == 7){
			int temp = currentStation.getStation_number();
			System.out.println("NEW CURRENT STATION: " + stations[temp].getStation_number());
			currentStation = stations[temp];
		}
		
		CalTrain.mutex.release();
		System.out.println("--> Train released mutex");
	}
	
	/* METHODS FOR LOCKS SOLUTIONS */
	
	public void getCurrTime(){
		Calendar cal = Calendar.getInstance();
		SimpleDateFormat sdf = new SimpleDateFormat("HH:mm:ss");
		System.out.println( sdf.format(cal.getTime()) );
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

	@Override
	public String toString() {
		return "Train [train_number=" + train_number + ", seats=" + seats + ", passengers=" + passengers
				+ ", nextStation=" + nextStation.getStation_number() + ", currentStation=" + currentStation.getStation_number();
	}
		
}
