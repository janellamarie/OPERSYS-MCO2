package cal_train;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

public class CalTrain implements Runnable {
	
	/** Local Variables **/
	private Station station;
	private Lock station_lock = new ReentrantLock();
	private Condition train_arrival = station_lock.newCondition();
	ArrayList<Station> stations = new ArrayList<Station>();
	public static ArrayList trains = new ArrayList<Thread>();

	public void start() throws InterruptedException {
		station_init();
        System.out.println("STARTING THREAD");

        int trainlimit = 3;

			for (int i = 0 ; i < trainlimit; i++){
				Thread train = new Thread(new Train(i+1, 15, stations));
				train.start();
				trains.add(train);
				Thread.sleep(1000);
			}


		System.out.println("PAM");

		for(int i = 0; i < stations.size(); i++){

			for(int j = 0; j < 5; j++){
				Passengers pass = new Passengers(station, 5);
				stations.get(i).addPassengers(pass);
			}

		}




	}
	
	public void run(){
		System.out.println("RUNNING THREAD");

	}
	
	public void station_init(){

		/* NOTES:
		 * 1. used to invoke station object
		 * 2. 8 stations lang
		 * 3. 15 trains all in all
		 */


		//Initialize Stations
		for(int i = 0; i < 2; i++){
			Station station = new Station(i+1, this);


//			for(int j = 0; j < 5; j++){
//				Passengers pass = new Passengers(station, 5);
//			}
			//created new caltrains
			station.start();
			stations.add(station);
		}
	}

	public void station_load_train(Station station, int count){
		
		/* NOTES:
		 * 1. count - how many seats are available on the train
		 * 2. either puno na yung train or lahat ng naghihintay na passenger
		 * nakasakay na bago mag end yung funtion
		 * 3. number of seats may vary among trains and should be treated as
		 * input parameter
		 */
		System.out.println("There are " + station.getCurr_train().getVacantSeats() + " in Train " + station.getCurr_train().getTrain_number());



	} 
	
	public void station_wait_for_train(Station station) throws InterruptedException {

		System.out.println("passenger is now waiting");
		station.passengers_waiting();

	}
	
	public void station_on_board(Station station){
		
		/* NOTES:
		 * 1. called pag naka-board na si passenger
		 */
	}




	public void getCurrTime(){
		Calendar cal = Calendar.getInstance();
		SimpleDateFormat sdf = new SimpleDateFormat("HH:mm:ss");
		System.out.println( sdf.format(cal.getTime()) );
	}

	public ArrayList<Station> getStations() {
		return stations;
	}

	public void setStations(ArrayList<Station> stations) {
		this.stations = stations;
	}

	public Station getStation() {
		return station;
	}

	public void setStation(Station station) {
		this.station = station;
	}
}
