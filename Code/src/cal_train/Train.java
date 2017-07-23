package cal_train;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;

public class Train implements Runnable{

<<<<<<< HEAD
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
			stations[i].trainArrived(this);
		}
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
=======
    private int train_number;
    private int occupiedSeats;
    private int vacantSeats;
    private int current_station;
    private int next_station;

    private ArrayList<Station> stations;
    private ArrayList<Passenger> passengers;


    public Train(int train_number, int count, ArrayList<Station> stations){
        vacantSeats = count;
        passengers = new ArrayList<Passenger>();
        this.train_number = train_number;
        this.stations = stations;
    }

    public int getOccupiedSeats() {
        return occupiedSeats;
    }
    public void setOccupiedSeats(int occupiedSeats) {
        this.occupiedSeats = occupiedSeats;
    }
    public int getVacantSeats() {
        return vacantSeats;
    }
    public void setVacantSeats(int vacantSeats) {
        this.vacantSeats = vacantSeats;
    }

    public int getTrain_number() {
        return train_number;
    }

    public void setTrain_number(int train_number) {
        this.train_number = train_number;
    }

    public int getCurrent_station() {
        return current_station;
    }

    public void setCurrent_station(int current_station) {
        this.current_station = current_station;
    }

    public int getNext_station() {
        return next_station;
    }

    public void setNext_station(int next_station) {
        this.next_station = next_station;
    }

    public ArrayList<Passenger> getPassengers() {
        return passengers;
    }

    public void setPassengers(ArrayList<Passenger> passengers) {
        this.passengers = passengers;
    }

    public void removePassengers(int ctr){
        passengers.remove(ctr);
        vacantSeats++;
    }

    public void addPassengers(Passenger passenger){
        passengers.add(passenger);
        vacantSeats--;
    }

    public void getCurrTime(){
        Calendar cal = Calendar.getInstance();
        SimpleDateFormat sdf = new SimpleDateFormat("HH:mm:ss");
        System.out.println( sdf.format(cal.getTime()) );
    }


    @Override
    public void run() {
        //while(true) {
        for (int i = 1; i <= stations.size(); i++) {
            try {
                stations.get(i - 1).train_arrived(this);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
        //}
    }
}
>>>>>>> 432b55420d8691fc003d01141ef8b50b9a8def30
