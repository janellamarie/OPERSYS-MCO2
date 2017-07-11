package using_locks;

public class Train extends Thread{

	private int occupiedSeats;
	private int vacantSeats;

	private int train_number;
	
	public Train(int train_number, int count){
		occupiedSeats = count;
		this.train_number = train_number;
	}
	
	public int getOccupiedSeats() {
		return occupiedSeats;
	}
	public void setOccupiedSeats(int occupiedSeats) {
		this.occupiedSeats = occupiedSeats;
	}
	public int getVacantSeats() {
		return vacantSeats;
	}
	public void setVacantSeats(int vacantSeats) {
		this.vacantSeats = vacantSeats;
	}

	public int getTrain_number() {
		return train_number;
	}

	public void setTrain_number(int train_number) {
		this.train_number = train_number;
	}
}
