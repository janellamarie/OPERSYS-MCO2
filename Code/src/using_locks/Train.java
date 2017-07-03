package using_locks;

public class Train {

	private int occupiedSeats;
	private int vacantSeats;
	
	public Train(int count){
		occupiedSeats = count;
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
}
