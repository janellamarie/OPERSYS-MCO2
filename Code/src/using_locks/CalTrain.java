package using_locks;

public class CalTrain {
	
	public void station_init(){
		/* NOTES: 
		 * 1. used to invoke station object
		 */
	}

	public void station_load_train(Station station, int count){
		/* NOTES: 
		 * 1. count - how many seats are available on the train
		 * 2. either puno na yung train or lahat ng naghihintay na passenger
		 * nakasakay na bago mag end yung funtion
		 * 3. number of seats may vary among trains and should be treated as
		 * input parameter
		 */
	} 
	
	public void station_wait_for_train(Station station){
		/* NOTES:
		 * 1. pag dumating si passenger eto yung tatawagin
		 */
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
	
	public static void main(String[] args){
		
	}

}
