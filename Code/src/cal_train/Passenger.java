package cal_train;

public class Passenger implements Runnable {

	private Station destination;
	private Station current;
	private boolean onBoard = false;
	
	@Override
	public void run() {
		/* run passenger thread */
		System.out.println("\nRUNNING PASSENGER THREAD");
		
		station_wait_for_train(current);
	
	}
		
	public Passenger(Station current, Station destination){
		this.current = current;
		this.destination = destination;
		current.addPassenger(this);
		System.out.println(toString());
	}
	
	public void station_wait_for_train(Station station){
		/* called when passenger robot arrives in the station */
		System.out.println("PASSENGER: wait_for_train in Station " + current.getStation_number());
		
		System.out.println("TOTAL PASSENGERS WAITING IN STATION: " + current.getPassengers().size());
		try{
			if(station.getCurrentTrain() != null){
				while(!CalTrain.mutex.tryAcquire()){
					System.out.println("--> Passenger acquired mutex ");
					/* CRITICAL SECTION -> passenger is boarding */
					station_on_board(station);
				}
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
		
		if(currTrain.getPassengers().size() <= currTrain.getSeats()){
			onBoard = true;
			currTrain.addPassenger(this);	
		}	
		
		System.out.println("PASSENGER HAS BOARDED TRAIN");
		CalTrain.mutex.release();
		System.out.println("--> Passenger released mutex");
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

	public boolean isOnBoard() {
		return onBoard;
	}

	public void setOnBoard(boolean onBoard) {
		this.onBoard = onBoard;
	}

	@Override
	public String toString() {
		return "Passenger [destination=" + destination.getStation_number() + ", current=" + current.getStation_number() + ", onBoard=" + onBoard + "]";
	}
}