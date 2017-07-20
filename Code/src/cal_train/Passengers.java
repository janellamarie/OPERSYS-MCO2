package cal_train;

/**
 * Created by Jords on 7/18/2017.
 */
public class Passengers extends Thread{
    private Station curr_station;
    private int destination;

    public Passengers(Station curr_station, int destination){
        this.curr_station = curr_station;
        this.destination = destination;
    }

    public Station getCurr_station() {
        return curr_station;
    }

    public void setCurr_station(Station curr_station) {
        this.curr_station = curr_station;
    }

    public int getDestination() {
        return destination;
    }

    public void setDestination(int destination) {
        this.destination = destination;
    }

    public void run(){
        //I removes this because d naman ATA kailangan
        curr_station.addPassengers(this);
       // curr_station.passengers_waiting(this);
        //curr_station.calTrain.station_wait_for_train(curr_station);
        curr_station.passengers_waiting(this);
    }
}
