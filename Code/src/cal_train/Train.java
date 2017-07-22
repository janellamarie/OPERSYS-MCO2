package cal_train;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;

public class Train implements Runnable{

	private int train_number;
	private int occupiedSeats;
	private int vacantSeats;
	private int current_station;
	private int next_station;

	private ArrayList<Station> stations;
	private ArrayList<Passengers> passengers;


	public Train(int train_number, int count, ArrayList<Station> stations){
		vacantSeats = count;
		passengers = new ArrayList<Passengers>();
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

	public ArrayList<Passengers> getPassengers() {
		return passengers;
	}

	public void setPassengers(ArrayList<Passengers> passengers) {
		this.passengers = passengers;
	}

	public void removePassengers(int ctr){
		passengers.remove(ctr);
		vacantSeats++;
	}

	public void addPassengers(Passengers passenger){
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
