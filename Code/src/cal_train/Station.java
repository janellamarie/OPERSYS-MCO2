package cal_train;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Random;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

public class Station extends Thread{
    
    private int station_number;
    private Train curr_train;

    Random r = new Random();
    int time;
    CalTrain calTrain;


    private Lock lock = new ReentrantLock();
    private Lock station_lock = new ReentrantLock();
    private Condition passenger_arrival = station_lock.newCondition();
    private Condition train_leave = station_lock.newCondition();

    private ArrayList<Passenger> waiting_passengers;

    private int passengers_waiting;

    public Station(int station_number, CalTrain calTrain){
        this.station_number = station_number;
        time = r.nextInt(999);
        //this.calTrain = calTrain;
        this.calTrain = new CalTrain();
        passengers_waiting = 0;
        waiting_passengers = new ArrayList<>();
    }

//	public void createTrain(int count){
//
//		for(int i = 0; i < 15; i++)
//			if(Driver.trains.get(i) == null){
//				Train temp = new Train(i+1, count);
////				Driver.trains.set(i, temp);
//				System.out.println("SUCCESSFULLY INITIALIZED TRAIN");
//				break;
//			} else {
//				System.out.println("ERROR INITIALIZING TRAIN");
//				 break;
//			}
//	}

    public void addPassenger(Passenger pass){
        waiting_passengers.add(pass);
        passengers_waiting++;
    }

    public int getStation_number () {
        return station_number;
    }

    public void setStation_number(int station_number) {
        this.station_number = station_number;
    }

    public Train getCurr_train() {
        return curr_train;
    }

    public void setCurr_train(Train curr_train) {
        this.curr_train = curr_train;
    }

    public int getPassengers_waiting() {
        return passengers_waiting;
    }

    public void setPassengers_waiting(int passengers_waiting) {
        this.passengers_waiting = passengers_waiting;
    }

    public Condition getPassenger_arrival() {
        return passenger_arrival;
    }

    public void setPassenger_arrival(Condition passenger_arrival) {
        this.passenger_arrival = passenger_arrival;
    }

    public Lock getStation_lock() {
        return station_lock;
    }

    public void setStation_lock(Lock station_lock) {
        this.station_lock = station_lock;
    }

    public ArrayList<Passenger> getWaiting_passengers() {
        return waiting_passengers;
    }

    public void setWaiting_passengers(ArrayList<Passenger> waiting_passengers) {
        this.waiting_passengers = waiting_passengers;
    }

    public void getOffPassengers() throws InterruptedException {
        System.out.println("There were " + curr_train.getPassengers().size() + " Passenger in Train " + curr_train.getTrain_number());

        for (int i = curr_train.getPassengers().size()-1; i >= 0; i--) {
            if (getStation_number() == curr_train.getPassengers().get(i).getDestination().getStation_number()){
                System.out.println("PASSENGERS OFF in station " + getStation_number() + " headed to " + curr_train.getPassengers().get(i).getDestination().getStation_number());
                curr_train.removePassengers(i);
            }
            //System.out.println(i);
        }
        System.out.println("There are now " + curr_train.getPassengers().size() + " Passenger in Train " + curr_train.getTrain_number());
    }



    public void getInPassengers(Passenger pass){
        //System.out.println("Hello");
        if(curr_train.getVacantSeats() > 0){
            curr_train.addPassengers(pass);
            passengers_waiting--;
        }else{
            passengers_waiting(pass);
        }

    }


    public String getCurrTime(){
        Calendar cal = Calendar.getInstance();
        SimpleDateFormat sdf = new SimpleDateFormat("HH:mm:ss");
        return sdf.format(cal.getTime());
    }


    //This is the robot (I THINK HAHAHA)
    public void train_arrived(Train train) throws InterruptedException {
        lock.lock();
        station_lock.lock();
        try {
            System.out.println("Train " + train.getTrain_number() + " has arrived in Station " + station_number + " - " +getCurrTime());
            setCurr_train(train);
            getOffPassengers();
            if(train.getVacantSeats() > 0) {
                passenger_arrival.signalAll();

                this.curr_train = train;
                calTrain.station_load_train(this, curr_train.getVacantSeats());

                System.out.println("# of passengers ing Station " + getStation_number() + ": " + passengers_waiting);
                //	Thread.sleep(1000);
                if (passengers_waiting > 0 && train.getVacantSeats() > 0) {
                    System.out.println("waiting");
                    train_leave.await();
                }
                System.out.println("# of passengers ing Station " + getStation_number() + ": " + passengers_waiting);
            }

        } finally {
            this.curr_train = null;
            station_lock.unlock();
            lock.unlock();
        }

    }

    public void passengers_waiting(Passenger pass){
        station_lock.lock();
        try {
            passenger_arrival.await();
            //System.out.println("Train arrived " + getCurr_train().getTrain_number());
            getInPassengers(pass);
            //Thread.sleep(1000);
            train_leave.signalAll();
        } catch (InterruptedException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } catch(NullPointerException n){
        }finally {
            station_lock.unlock();
        }
    }





    public void run() {
        //passengers_waiting();

//			while(true) {
//				for (int i = 0; i < waiting_passengers.size(); i++) {
//					waiting_passengers.get(i).run();
//				}
//			}

    }
}