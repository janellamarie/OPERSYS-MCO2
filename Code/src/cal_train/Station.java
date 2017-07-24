package cal_train;

import javafx.animation.PauseTransition;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.util.Duration;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.concurrent.Semaphore;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

public class Station extends Thread{

	private int station_number;
	private Train currentTrain;
	private ArrayList<Passenger> passengers; // waiting passengers

	private Semaphore stationSemaphore;
	private  Semaphore trainInTheStationSemaphore;

	private Lock lock = new ReentrantLock();
	private Lock station_lock = new ReentrantLock();
	private Condition passenger_arrival = station_lock.newCondition();
	private Condition train_leave = station_lock.newCondition();

	private transient ObservableList<Passenger> observablePassengers;

	public ObservableList<Passenger> getObservablePassengers() {
		return observablePassengers;
	}

	public Station(int station_number){
		this.station_number = station_number;
		stationSemaphore = new Semaphore(1);
		trainInTheStationSemaphore = new Semaphore(1);
		passengers = new ArrayList<>();

		observablePassengers = FXCollections.observableList(passengers);
	}

	public void addPassenger(Passenger p){
		observablePassengers.add(p);
	}



	public void trainArrived(Train train) {
//		System.out.println("????????????????????????????????");

//		CalTrain.mutex.tryAcquire();

//
//		while(!CalTrain.mutex.tryAcquire()){
//			/* while may nasa cs pa */
//			System.out.println("WAITING");
//		}
		doneTransition(train);

	}

	public void doneTransition(Train train){
//		train.setCurrentStation(this);



			if (CalTrain.solType) {
				try {
					trainArrived_locks(train);
				} catch (InterruptedException e2) {
					e2.printStackTrace();
				}
			} else {
				trainArrived_semaphores(train);
			}

			trainInTheStationSemaphore.release();

	}

	/* METHODS FOR SEMAPHORE SOLUTION */
	
	public void trainArrived_semaphores(Train train){

//			System.out.println("WWWWWWWWW " + ((PathTransition)event.getSource()).getNode().getLayoutX() + " || " + ((PathTransition)event.getSource()).getNode().getLayoutY());

			while(!CalTrain.mutex.tryAcquire()){
				/* while may nasa cs pa */
			}
			try{
				while(!stationSemaphore.tryAcquire()){
					/* while may nasa station pa */
				}
				System.out.println("--> STATION SEMAPHORE ACQUIRED (" + station_number + ") ");

				currentTrain = train;

				System.out.println("\nTrain " + train.getTrain_number() + " has arrived in Station # " + station_number);

				if(station_number <= 7){
					setCurrentTrain(train);
					currentTrain.setCurrentStation(this);
					currentTrain.setNextStation(CalTrain.stations.get(station_number));
				} else {
					setCurrentTrain(train);
					currentTrain.setCurrentStation(this);
					currentTrain.setNextStation(CalTrain.stations.get(0));
				}

				System.out.println("IN TRAIN : " + currentTrain.getPassengers().size());
				currentTrain.removePassengers();


				for(int i = 0; i < passengers.size(); i++) {
					Passenger p = passengers.get(i);
					if (currentTrain.getSeats() - currentTrain.getPassengers().size() != 0) {
						currentTrain.addPassenger(p);
						observablePassengers.remove(p);
					}
				}

			} catch (Exception e){
				e.printStackTrace();
			}

				train.addXPassenger(new Passenger());
				addXPassenger(new Passenger());

					train.addXPassenger(new Passenger());
					addXPassenger(new Passenger());

					System.out.println("AFTER ADDING (TRAIN): " + currentTrain.getPassengers().size());
					System.out.println("WAITING: " + passengers.size());

					if (currentTrain.getSeats() - currentTrain.getPassengers().size() == 0)
						currentTrain.moveTrains();
					else if (passengers.size() == 0)
						currentTrain.moveTrains();

					setCurrentTrain(null);
					releaseStationSemaphore();
					CalTrain.mutex.release();
					CalTrain.semaphore.release();


	}
	
	/* METHODS FOR LOCKS SOLUTION */

	public void trainArrived_locks(Train train) throws InterruptedException {
		lock.lock();
		station_lock.lock();
		//Thread.sleep(1000);
		System.out.println("Lock: locked -  Train: " + train.getTrain_number() + " (ENTRY) Station " + getStation_number());
		try{
			System.out.println("Train " + train.getTrain_number() + " has arrived in Station " + station_number +  " - "  + getCurrTime());

			if(station_number <= 7){
				setCurrentTrain(train);
				currentTrain.setCurrentStation(this);
				currentTrain.setNextStation(CalTrain.stations.get(station_number));
			} else {
				setCurrentTrain(train);
				currentTrain.setCurrentStation(this);
				currentTrain.setNextStation(CalTrain.stations.get(0));
			}


			getOffPassengers();
			if(train.getSeats() - train.getPassengers().size() > 0){
				passenger_arrival.signalAll();

				System.out.println("# of passenger waiting Station " + getStation_number() + ": " + passengers.size());
				if(passengers.size() > 0 && train.getSeats() - train.getPassengers().size() > 0){
					System.out.println("waiting");
					train_leave.await();
				}
			}

		} catch (InterruptedException e) {
			e.printStackTrace();
		} finally {

			train.addXPassenger(new Passenger());
			addXPassenger(new Passenger());
			PauseTransition delay = new PauseTransition(Duration.seconds(1));

			delay.play();
			delay.setOnFinished(e -> {
				train.moveTrains();
				System.out.println("Lock: Unlocked -  Train: " + train.getTrain_number() + " (EXIT) Station" + getStation_number());
				station_lock.unlock();
				lock.unlock();
			});
		}
	}

	public void getOffPassengers() throws InterruptedException {

		System.out.println("There were " + currentTrain.getPassengers().size() + " Passengers in Train " + currentTrain.getTrain_number());

		for (int i = currentTrain.getPassengers().size()-1; i >= 0; i--) {
			if (getStation_number() == currentTrain.getPassengers().get(i).getDestination().getStation_number()){
				System.out.println("PASSENGERS OFF in station " + getStation_number() + " headed to " + currentTrain.getPassengers().get(i).getDestination().getStation_number());
				currentTrain.removePassengers();
			}
			//System.out.println(i);
		}
		
		System.out.println("There are now " + currentTrain.getPassengers().size() + " Passengers in Train " + currentTrain.getTrain_number());
	}

	
	public void getInPassengers(Passenger pass){

		if (currentTrain.getSeats() - currentTrain.getPassengers().size() > 0){
			currentTrain.addPassenger(pass);
			observablePassengers.remove(pass);
		}else{
			passengers_waiting(pass);
		}

	}
	public void addXPassenger(Passenger p){
		observablePassengers.add(p);
		observablePassengers.remove(p);
	}

	public void passengers_waiting(Passenger pass){
		station_lock.lock();
		try{
			passenger_arrival.await();
			getInPassengers(pass);
			if (passengers.size() == 0 || currentTrain.getSeats() - currentTrain.getPassengers().size() == 0 ){
				train_leave.signalAll();
			}
		} catch (InterruptedException e) {
			e.printStackTrace();
		}finally{
			station_lock.unlock();
		}
	}
	
	public String getCurrTime(){
		Calendar cal = Calendar.getInstance();
		SimpleDateFormat sdf = new SimpleDateFormat("HH:mm:ss");
		return sdf.format(cal.getTime());
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

	
	public Lock getLock() {
		return lock;
	}
	

	public void setLock(Lock lock) {
		this.lock = lock;
	}
	

	public Lock getStation_lock() {
		return station_lock;
	}
	

	public void setStation_lock(Lock station_lock) {
		this.station_lock = station_lock;
	}
	

	public Condition getPassenger_arrival() {
		return passenger_arrival;
	}
	

	public void setPassenger_arrival(Condition passenger_arrival) {
		this.passenger_arrival = passenger_arrival;
	}
	

	public Condition getTrain_leave() {
		return train_leave;
	}
	

	public void setTrain_leave(Condition train_leave) {
		this.train_leave = train_leave;
	}
	


	public void releaseStationSemaphore() {
		stationSemaphore.release();
	}

	public Semaphore getTrainInTheStationSemaphore() {
		return trainInTheStationSemaphore;
	}

	public void setTrainInTheStationSemaphore(Semaphore trainInTheStationSemaphore) {
		this.trainInTheStationSemaphore = trainInTheStationSemaphore;
	}

}
