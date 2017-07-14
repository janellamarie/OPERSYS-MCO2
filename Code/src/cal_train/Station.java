package cal_train;

public class Station {
	
	private int station_number;

	public Station(int station_number){
		this.station_number = station_number;
	}

	public void createTrain(int count){
		
		for(int i = 0; i < 15; i++)	
			if(Driver.trains[i] == null){
				Train temp = new Train(i+1, count);
				Driver.trains[i] = temp;
				System.out.println("SUCCESSFULLY INITIALIZED TRAIN");
				break;
			} else {
				System.out.println("ERROR INITIALIZING TRAIN");
				 break;
			}
	}

	public int getStation_number() {
		return station_number;
	}

	public void setStation_number(int station_number) {
		this.station_number = station_number;
	}
	
}
