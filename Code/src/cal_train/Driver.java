package cal_train;

import java.util.ArrayList;
import java.util.concurrent.Semaphore;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

public class Driver {

	/** Global Variables **/

	
	public static Semaphore semaphore;
	public static Semaphore mutex;
	public static Lock station_lock = new ReentrantLock();
	public static Condition trainArrival = station_lock.newCondition();

	public static ArrayList stations = new ArrayList<Thread>();
	public static ArrayList trains = new ArrayList<Thread>();
	
	public static void main(String[] args) throws InterruptedException {

		CalTrain calTrain = new CalTrain();

		calTrain.start();
	}

}
