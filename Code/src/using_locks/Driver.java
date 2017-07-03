package using_locks;

public class Driver {
	
		
	public static void main(String[] args){
		
		CalTrain calTrain = new CalTrain();	
		Station x = new Station();
		
		calTrain.station_load_train(x, 10);
	}
}
