package cal_train;

import javafx.animation.PathTransition;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;

public class Train extends Thread implements Runnable{

	private int train_number;	
	private int seats;
	private ArrayList<Passenger> passengers;
	
	private Station nextStation;
	private Station currentStation;
	private ArrayList<Station> stations;
	private boolean guiDone = false;

	private transient ObservableList<Passenger> observablePassengers;

	public Train(int train_number, int count){
		seats = count;
		this.train_number = train_number;
		passengers = new ArrayList<Passenger>();
		
		stations = CalTrain.stations;
		observablePassengers = FXCollections.observableList(passengers);
	}

	public ObservableList<Passenger> getObservablePassengers() {
		return observablePassengers;
	}

	public Train(int train_number){
		this.train_number = train_number;
	}
	
	@Override
	public void run() {
		PathTransition transition = CalTrain.moveTrain(this, 0 + 1);
		transition.setOnFinished(e -> {

			CalTrain.stations.get(0).trainArrived(this);
			currentStation = CalTrain.stations.get(0);
		});
	}

	public void addXPassenger(Passenger p){
		observablePassengers.add(p);
		observablePassengers.remove(p);
	}

	public void addPassenger(Passenger p){
		observablePassengers.add(p);
	}
	
	public void removePassengers(){
		for(int i = 0; i < passengers.size(); i++){
			//System.out.println(train_number);
			if(currentStation.getStation_number() == passengers.get(i).getDestination().getStation_number()){
				observablePassengers.remove(i);
			}
		}
	}
	
	public void moveTrains(){
		
		System.out.println("MOVING TRAIN # " + train_number);
//		System.out.println("--> Train acquired mutex");
		int currentStationNumber = currentStation.getStation_number();
		currentStation.setCurrentTrain(null);
		currentStation.addXPassenger(new Passenger());
		currentStation = null;

		addXPassenger(new Passenger());


		PathTransition transition = CalTrain.moveTrain(this, currentStationNumber < 8 ? currentStationNumber + 1 : 1);
		transition.setOnFinished(e -> {

			if (currentStationNumber < 8) {
				System.out.println("NEW CURRENT STATION: " + stations.get(currentStationNumber).getStation_number());
				CalTrain.stations.get(currentStationNumber).trainArrived(this);

				int nextTemp = currentStationNumber + 1;
				if(currentStationNumber == 7)
					nextTemp = 0;

				System.out.println("NEW NEXT STATION: " + stations.get(nextTemp).getStation_number());
				currentStation = CalTrain.stations.get(currentStationNumber);
				nextStation = stations.get(nextTemp);
			} else if (currentStationNumber == 8) {
				System.out.println("NEW CURRENT STATION: " + stations.get(0).getStation_number());
				CalTrain.stations.get(0).trainArrived(this);
				currentStation = stations.get(0);

				System.out.println("NEW NEXT STATION: " + stations.get(1).getStation_number());
				nextStation = stations.get(1);
			}
		});
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

	public ArrayList<Station> getStations() {
		return stations;
	}

	public void setStations(ArrayList<Station> stations) {
		this.stations = stations;
	}

	@Override
	public String toString() {
		return "Train [train_number=" + train_number + ", seats=" + seats + ", passengers=" + passengers
				+ ", nextStation=" + nextStation.getStation_number() + ", currentStation=" + currentStation.getStation_number();
	}

	public boolean getGuiDone() {
		return guiDone;
	}

	public synchronized void triggerGuiDone() {
		this.guiDone = true;
		this.notify();
	}
}
