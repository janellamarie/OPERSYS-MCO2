package cal_train;

import java.util.ArrayList;
import java.util.concurrent.Semaphore;

public class Station{
	
	private int station_number;
	private Train currentTrain; 
	private ArrayList<Passenger> passengers; // waiting passengers
	private Semaphore stationSemaphore;
	
	public void runPassengerThreads(){
		for(int i = 0; i < passengers.size(); i++){
			passengers.get(i).run();
		}
	}
	
	public Station(int station_number){
		this.station_number = station_number;
		stationSemaphore = new Semaphore(1);
		passengers = new ArrayList<Passenger>();
		
//		System.out.println("RUNNING THREAD  " + station_number);
	}
	
	public void addPassenger(Passenger p){
		this.passengers.add(p);
		p.setCurrent(this);
	}
	
	public void trainArrived(Train train){
		
		while(!CalTrain.mutex.tryAcquire()){
			/* while may nasa cs pa */
		}
		
		try{
			
			while(!CalTrain.semaphore.tryAcquire()){
				
			}
			
			while(!stationSemaphore.tryAcquire()){
				/* while may nasa station pa */
			}
		
			System.out.println("--> STATION SEMAPHORE ACQUIRED (" + station_number + ") ");
			
			currentTrain = train;
			
			System.out.println("\nTrain " + train.getTrain_number() + " has arrived in Station # " + station_number);

			if(station_number <= 7){
				setCurrentTrain(train);
				currentTrain.setCurrentStation(this);
				currentTrain.setNextStation(CalTrain.stations[station_number]);
			} else {
				setCurrentTrain(train);
				currentTrain.setCurrentStation(this);
				currentTrain.setNextStation(CalTrain.stations[0]);
			}

			System.out.println("IN TRAIN : " + currentTrain.getPassengers().size());
			currentTrain.removePassengers();

			for(int i = 0; i < passengers.size(); i++){
				currentTrain.addPassenger(passengers.get(i));
				this.passengers.remove(i);
			}

			System.out.println("AFTER ADDING (TRAIN): " + currentTrain.getPassengers().size());

			System.out.println("WAITING: " + passengers.size());
			
			if(currentTrain.getSeats() - currentTrain.getPassengers().size() == 0)
				currentTrain.moveTrains();
			else if (passengers.size() == 0)
				currentTrain.moveTrains();
			
		} catch (Exception e){
			e.printStackTrace();
		}
		
		currentTrain = null;
		CalTrain.mutex.release();
		CalTrain.semaphore.release();
		stationSemaphore.release();
	}
	
	/* SETTERS AND GETTERS */
	
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

	public ArrayList<Passenger> getPassengers() {
		return passengers;
	}

	public void setPassengers(ArrayList<Passenger> passengers) {
		this.passengers = passengers;
	}

	public Semaphore getStationSemaphore() {
		return stationSemaphore;
	}

	public void setStationSemaphore(Semaphore stationSemaphore) {
		this.stationSemaphore = stationSemaphore;
	}
}
