package cal_train;

public class CalTrain implements Runnable {
	
	private Station station;
	
	public void start(){
		System.out.println("STARTING THREAD\n");
		station_init();
	}
	
	public void run(){
		System.out.println("RUNNING THREAD " + station.getStation_number() + "\n");
		station_wait_for_train(this.station);
	}
	
	public void station_init(){
		
		/* NOTES: 
		 * 1. used to invoke station object
		 * 2. 8 stations lang
		 * 3. 15 trains all in all
		 */
		
		for(int i = 0; i < 8; i++)	
			if(Driver.stations[i] == null){
				station = new Station(i+1);
				Driver.stations[i] = station;
				System.out.println("SUCCESSFULLY INITIALIZED STATION " + station.getStation_number() + "\n");
				break;
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

//		station.setCurrentTrain(station.createTrain(count));
		
		System.out.println("STATION LOAD TRAIN");
		station.setCurrentTrain(Driver.trains[0]);
//		System.out.println(station.getCurrentTrain().getTrain_number());
	} 
	
	public void station_wait_for_train(Station station){
		
		/* NOTES:
		 * 1. pag dumating si passenger eto yung tatawagin
		 */
		
		System.out.println("WAITING FOR TRAIN");
		this.station_load_train(this.station, 1);
		
		try {
			
			while(!Driver.mutex.tryAcquire()){
				/* DO NOTHING UNTIL MERON PANG NASA CRITICAL SECTION
				 * , WAIT UNTIL OTHER THREAD EXITS CRITICAL SECTION */
				
//				System.out.println("THREAD IS WAITING");
			}
			
			if(!Driver.semaphore.tryAcquire()) // try to acquire semaphore
				System.out.println("\nALL STATIONS ARE BUSY\n" );
			
			System.out.println("THREAD " + this.station.getStation_number() + " ACQUIRED MUTEX \n");
			
			this.station_on_board(this.station);
			
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	public void station_on_board(Station station){
		
		/* NOTES: 
		 * 1. called pag naka-board na si passenger
		 */		
		
		/** PASSENGER BOARDING **/
		
		int trainCounter = station.getCurrentTrain().getTrain_number()-1;
		
		if(Driver.trains[trainCounter+1] != null){
			decVacantSeats(trainCounter);
			incOccupiedSeats(trainCounter);
			
		}
	
		/** MOVE TRAINS **/
	
		if(Driver.trains[trainCounter].getVacantSeats() == 0){
			if(trainCounter >= 0 && trainCounter < 15 && Driver.trains[trainCounter] != null){
				station.setCurrentTrain(Driver.trains[trainCounter]);
				Driver.stations[station.getStation_number()].setCurrentTrain(Driver.trains[trainCounter]);
				Driver.stations[station.getStation_number()-1].setCurrentTrain(Driver.trains[trainCounter+1]);
				System.out.println("TRAIN " + (trainCounter) + " LEFT STATION");
			}
		}
		
		System.out.println("CURRENT STATION'S TRAIN : " + Driver.stations[0].getCurrentTrain().getTrain_number());
		System.out.println("NEXT STATION'S TRAIN : " + Driver.stations[1].getCurrentTrain().getTrain_number());
		System.out.println("THREAD " + this.station.getStation_number() + " RELEASED MUTEX \n");
		
		Driver.mutex.release();
	
	}
	
	public void decVacantSeats(int trainCounter){
		int temp = Driver.trains[trainCounter].getVacantSeats();
		temp--;
		System.out.println("DECREMENTED VACANT SEATS: " + temp);
		Driver.trains[trainCounter].setVacantSeats(temp);
	}
	
	public void incOccupiedSeats(int trainCounter){
		int temp = Driver.trains[trainCounter].getOccupiedSeats();
		temp++;
		System.out.println("INCREMENTED OCCUPIED SEATS: " + temp + "\n");
		Driver.trains[trainCounter].setOccupiedSeats(temp);
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
	
	public Station getStation() {
		return station;
	}

	public void setStation(Station station) {
		this.station = station;
	}
}
