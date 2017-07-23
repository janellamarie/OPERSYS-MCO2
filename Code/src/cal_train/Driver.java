package cal_train;

import java.util.concurrent.Semaphore;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

public class Driver {

	/** Global Variables **/

	public static Station stations[];
	public static Train trains[];

	public static Semaphore semaphore;
	public static Semaphore mutex;
	public static Lock station_lock = new ReentrantLock();
	public static Condition trainArrival = station_lock.newCondition();

	public static void main(String[] args) throws InterruptedException {

		stations = new Station[8];
		trains = new Train[15];

		semaphore = new Semaphore(0);
		mutex = new Semaphore(1);

		CalTrain calTrain = new CalTrain();

		//calTrain.start();
	}
}