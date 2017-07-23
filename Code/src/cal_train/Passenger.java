package cal_train;

public class Passenger implements Runnable {

	private Station destination;
	private Station current;
	
	@Override
	public void run() {
		/* run passenger thread */
		System.out.println("RUNNING PASSENGER THREAD");
		station_wait_for_train(current);
	
	}
		
	public Passenger(Station current, Station destination){
		this.current = current;
		this.destination = destination;
		current.addPassenger(this);
//		System.out.println(toString());
	}
	
	public void station_wait_for_train(Station station){
		/* called when passenger robot arrives in the station */
		System.out.println("PASSENGER: wait_for_train in Station " + current.getStation_number());
		
		System.out.println("TOTAL PASSENGERS WAITING IN STATION: " + current.getPassengers().size());
		
		try{		
			while(!CalTrain.mutex.tryAcquire()){
				
			}
			
			System.out.println("--> Passenger acquired mutex ");

			if(station.getStationSemaphore().availablePermits() == 0){
				/* CRITICAL SECTION -> passenger is boarding */
				station_on_board(station);
			}
			
		}catch(Exception e){
			e.printStackTrace();
		} finally {
			System.out.println("--> Passenger released mutex");
			CalTrain.mutex.release();
		}		
	}
	
	public void station_on_board(Station station){
		/* called when passenger is seated */
		
		Train currTrain = station.getCurrentTrain();
	
		currTrain.addPassenger(this);
		
		System.out.println("PASSENGER HAS BOARDED TRAIN");
	}

	/* SETTERS AND GETTERS */
	
	public Station getCurrent() {
		return current;
	}

	public void setCurrent(Station current) {
		this.current = current;
	}

	public Station getDestination() {
		return destination;
	}

	public void setDestination(Station destination) {
		this.destination = destination;
	}

	@Override
	public String toString() {
		return "Passenger [destination=" + destination.getStation_number() + ", current=" + current.getStation_number() + "]";
	}
}
