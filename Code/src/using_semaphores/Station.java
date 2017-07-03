package using_semaphores;

import java.util.ArrayList;

public class Station {
	
	ArrayList<Train> trains;
	
	public Station(){
		trains = new ArrayList<Train>();
	}

	public ArrayList<Train> getTrains() {
		return trains;
	}

	public void setTrains(ArrayList<Train> trains) {
		this.trains = trains;
	}
	
	
	
}
