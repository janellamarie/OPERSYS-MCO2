package cal_train;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Random;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

public class Station extends Thread{
	
	private int station_number;
	private Train curr_train;

	Random r = new Random();
	int time;
	private CalTrain calTrain;

	private Lock station_lock = new ReentrantLock();
	private Condition passenger_arrival = station_lock.newCondition();

	private int passengers_waiting;

	public Station(int station_number, CalTrain calTrain){
		this.station_number = station_number;
		time = r.nextInt(999);
		this.calTrain = calTrain;
		passengers_waiting = 5;
	}

//	public void createTrain(int count){
//
//		for(int i = 0; i < 15; i++)
//			if(Driver.trains.get(i) == null){
//				Train temp = new Train(i+1, count);
////				Driver.trains.set(i, temp);
//				System.out.println("SUCCESSFULLY INITIALIZED TRAIN");
//				break;
//			} else {
//				System.out.println("ERROR INITIALIZING TRAIN");
//				 break;
//			}
//	}

	public int getStation_number () {
		return station_number;
	}

	public void setStation_number(int station_number) {
		this.station_number = station_number;
	}

	public Train getCurr_train() {
		return curr_train;
	}

	public void setCurr_train(Train curr_train) {
		this.curr_train = curr_train;
	}

	public int getPassengers_waiting() {
		return passengers_waiting;
	}

	public void setPassengers_waiting(int passengers_waiting) {
		this.passengers_waiting = passengers_waiting;
	}

	public Condition getPassenger_arrival() {
		return passenger_arrival;
	}

	public void setPassenger_arrival(Condition passenger_arrival) {
		this.passenger_arrival = passenger_arrival;
	}

	public Lock getStation_lock() {
		return station_lock;
	}

	public void setStation_lock(Lock station_lock) {
		this.station_lock = station_lock;
	}

	public String getCurrTime(){
		Calendar cal = Calendar.getInstance();
		SimpleDateFormat sdf = new SimpleDateFormat("HH:mm:ss");
		return sdf.format(cal.getTime());
	}

	public void train_arrived(Train train) throws InterruptedException {

		station_lock.lock();
		try {
			System.out.println("Train " + train.getTrain_number() + " has arrived in Station " + station_number + " - " +getCurrTime());
			passenger_arrival.signalAll();
			this.curr_train = train;
			calTrain.station_load_train(this, curr_train.getVacantSeats());
			Thread.sleep(2000);
		} finally {
			this.curr_train = null;
			station_lock.unlock();
		}

	}

	public void passengers_waiting(){
		station_lock.lock();
		try {
			passenger_arrival.await();
			System.out.println("Train arrived " + getStation_number());
			//System.out.println("Train " + getCurr_train().getTrain_number() + " is Here" + " in Station " + getStation_number());
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch(NullPointerException n){
		}finally {
			station_lock.unlock();
		}
	}





	public void run() {

			//passengers_waiting();

//				try {
//					calTrain.station_wait_for_train(this);
//				} catch (InterruptedException e) {
//					e.printStackTrace();
//				}


	}
}
