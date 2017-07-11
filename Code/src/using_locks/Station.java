package using_locks;

import java.util.ArrayList;

public class Station {
	
	private ArrayList<Train> trains; //16 trains lang ba per station OR 16 trains all in all?
	
	public Station(){
		trains = new ArrayList<Train>();
	}

	public void createTrain(int count){
		trains.add(new Train(count));
	}
	
	public ArrayList<Train> getTrains() {
		return trains;
	}

	public void setTrains(ArrayList<Train> trains) {
		this.trains = trains;
	}
	
	
}
