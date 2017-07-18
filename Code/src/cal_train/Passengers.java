package cal_train;

/**
 * Created by Jords on 7/18/2017.
 */
public class Passengers {
    private Station destination;

    public Passengers(Station destination){
        this.destination = destination;
    }

    public Station getDestination() {
        return destination;
    }

    public void setDestination(Station destination) {
        this.destination = destination;
    }
}
