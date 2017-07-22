package cal_train;

/**
 * Created by Jords on 7/18/2017.
 */
public class Passengers extends Thread{
    private Station curr_station;
    private Station destination;

    public Passengers(Station curr_station, Station destination){
        this.curr_station = curr_station;
        this.destination = destination;
    }

    public Station getCurr_station() {
        return curr_station;
    }

    public void setCurr_station(Station curr_station) {
        this.curr_station = curr_station;
    }

    public Station getDestination() {
        return destination;
    }

    public void setDestination(Station destination) {
        this.destination = destination;
    }
}
