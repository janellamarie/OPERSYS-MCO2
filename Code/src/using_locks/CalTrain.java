package using_locks;

import java.util.Scanner;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

public class CalTrain extends Thread{
	
	public Station stations[];
	public Train trains[];
	public Scanner scan;

	private Lock station_lock = new ReentrantLock();
	private Condition trainArrival = station_lock.newCondition();
	
	public CalTrain() {
		station_init();
	}
	
	public void station_init(){
		
		/* NOTES: 
		 * 1. used to invoke station object
		 * 2. 8 stations lang
		 * 3. 16 trains all in all
		 */
		
		stations = new Station[8];
		trains = new Train[16];
		
		for(int i = 0; i < 8; i++){
			stations[i] = new Station(i+1);
		}
		
		if(stations != null)
			System.out.println("Successfully created eight trains!");
	
	}

	public void station_load_train(Station station, int count){
		
		/* NOTES: 
		 * 1. count - how many seats are available on the train
		 * 2. either puno na yung train or lahat ng naghihintay na passenger
		 * nakasakay na bago mag end yung funtion
		 * 3. number of seats may vary among trains and should be treated as
		 * input parameter 
		 */


		//station.createTrain(,count);
		System.out.println("Successfully created Train with " + count + " available seats.");
		
	} 
	
	public void station_wait_for_train(Station station){
		/* NOTES:
		 * 1. pag dumating si passenger eto yung tatawagin
		 */
			System.out.println("Waiting for Train");
			station_lock.lock();


	}
	
	public void station_on_board(Station station){
		/* NOTES: 
		 * 1. called pag naka-board na si passenger
		 */

	}
	
	/* USING LOCKS/MONITORS:
	 *  1. lock_init (struct lock* lock)
	 *  2. lock_release(struct lock *lock) 
	 *  3. cond_init(struct condition *cond) 
	 *  4. cond_wait(struct condition *cond, struct lock *lock) 
	 *  5. cond_signal(struct condition *cond, struct lock *lock) 
	 *  6. cond_broadcast(struct condition *cond, struct lock *lock)
	 *  
	 *  SOME ASSUMPTIONS:
	 *  1. there is never more that one train in the station
	 *  2. any passenger can get-on and get-off in any station
	 *  3. allow multiple passengers to board simultaneously
	 *  4. must not result in busy waiting
	 */
}
