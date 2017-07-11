package using_locks;

import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

public class Driver {

    private static Lock station = new ReentrantLock();
    private static Condition arrival = station.newCondition();
		
	public static void main(String[] args){
		
		CalTrain calTrain = new CalTrain();




	}
}
