package cal_train;

<<<<<<< HEAD
import java.util.Scanner;
import java.util.concurrent.Semaphore;

import javafx.application.Application;
import javafx.application.Platform;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

public class CalTrain extends Application {

	/** Global Variables **/
=======
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

public class CalTrain implements Runnable {

    /** Local Variables **/
    private Station station;
    private Lock station_lock = new ReentrantLock();
    private Condition train_arrival = station_lock.newCondition();
    private Condition train_leave =  station_lock.newCondition();
    ArrayList<Station> stations = new ArrayList<Station>();

    private static ArrayList<Thread> trains = new ArrayList<Thread>();


    public void start() throws InterruptedException {
        station_init();
        System.out.println("STARTING THREAD");
>>>>>>> 432b55420d8691fc003d01141ef8b50b9a8def30

	public static Station stations[];
	public static Thread trains[];
	public static Semaphore semaphore;
	public static Semaphore mutex;
	
	public static boolean solType; // true pag locks, false pag semaphores
	
	/** GUI **/

<<<<<<< HEAD
	private BorderPane mainPane;
	private TextField peopleTextField,
			  		  trainTextField;
=======
        for (int i = 0; i < trainlimit; i++){
            Thread train = new Thread(new Train(i+1, 10, stations));
            trains.add(train);
        }
>>>>>>> 432b55420d8691fc003d01141ef8b50b9a8def30

	public static void station_init(){
		
		/* NOTES: 
		 * 1. used to invoke station object
		 * 2. 8 stations lang
		 */
				
		for(int i = 0; i < 8; i++)
			stations[i] = new Station(i+1);
		
		System.out.println("INITIALIZED STATIONS\n");

<<<<<<< HEAD
	}
	
	public static void train_init(){
		
		/* NOTES:
		 * 1. 15 trains all in all
		 */
				
		for(int i = 0; i < 15; i++){
			trains[i] = new Thread(new Train((i+1), 1));
		}		
		
		System.out.println("INITIALIZED TRAINS\n");
	}
	
	public static void main(String[] args){
		
		System.out.println("SOLUTION TYPE: ");
		System.out.println("[1] Locks \n[2] Semaphores\n");
		Scanner sc = new Scanner(System.in);
		int x = sc.nextInt();
		
		switch(x){
			case 1: solType = true;
					break;
			case 2: solType = false;
					break;
		}
		
		/* INITIALIZE GLOBAL VARIABLES */
		
		stations = new Station[8];
		trains = new Thread[15];
		
		semaphore = new Semaphore(8);
		mutex = new Semaphore(1); 	  // mutual exclusion for threads
		
		station_init();
		train_init();
		
        System.out.println("STARTING THREADS");
        
		Passenger pass = null;
		
		if(solType){
			for(int i = 0; i < stations.length; i++){
				for(int j = 0; j < 15; j++){
					pass = new Passenger(stations[i], stations[4]);
					pass.run();
					stations[i].addPassenger(pass);
				}
			}
			
			for(int i = 0; i < stations.length; i++){
				System.out.println("Station " + stations[i].getStation_number() + " has " + stations[i].getPassengers().size());
			}
			
		} else {
			for(int i = 0; i < 8; i++){
				if(i < 7){
					pass = new Passenger(stations[i], stations[i+1]);
				}else{
					pass = new Passenger(stations[i], stations[0]);
				}	
				
				stations[i].addPassenger(pass);
				pass.run();
			}
		}
				
		for(int i = 0; i < 15; i++){
			trains[i].start();	
		}

		sc.close();
		
//		launch(args);
	}

	@Override
	public void start(Stage primaryStage) {
=======
        for(int i = 0; i < stations.size(); i++){
            for(int j = 0; j < 15; j++){
                Passenger pass = new Passenger(stations.get(i), stations.get(4));
                pass.start();
                stations.get(i).addPassenger(pass);
            }
        }

        for(int i = 0; i < stations.size(); i++){
            System.out.println("Station " + stations.get(i).getStation_number() + " has " + stations.get(i).getWaiting_passengers().size());
        }

        for(int i  = 0; i < trains.size(); i++){

            trains.get(i).start();
            //Thread.sleep(1000);
        }
>>>>>>> 432b55420d8691fc003d01141ef8b50b9a8def30

		mainPane = new BorderPane();
		mainPane.setId("mainPane");
		Scene scene = new Scene(mainPane, 1000, 650);
		primaryStage.setTitle("CaltrainII (OPERSYS MP2)");
		primaryStage.getIcons().add(new Image("images/trainSubIcon.png"));
		scene.getStylesheets().add("Style.css");
		primaryStage.setResizable(false);
		primaryStage.setScene(scene);
		primaryStage.show();
		primaryStage.setOnCloseRequest(e -> terminateProgram());

		mainPane.setRight(initRightVBox());
		mainPane.setCenter(initCenterVBox());
		mainPane.setAlignment(mainPane.getRight(), Pos.CENTER);
		mainPane.setAlignment(mainPane.getCenter(), Pos.CENTER);
	}

	public VBox initRightVBox()
	{
		VBox vBox = new VBox();
		vBox.setId("vBoxRight");

<<<<<<< HEAD
		GridPane gridPane = new GridPane();
		gridPane.setId("rightGridPane");
=======
    }

    public void run(){
        System.out.println("RUNNING THREAD");
    }

    public void station_init(){
>>>>>>> 432b55420d8691fc003d01141ef8b50b9a8def30

		//Train Number
		ImageView trainIcon = new ImageView("images/trainSubIcon2.png");
		trainIcon.setFitHeight(25);
		trainIcon.setFitWidth(25);
		trainIcon.setPreserveRatio(true);

		trainTextField = new TextField();
		trainTextField.setDisable(true);
		trainTextField.setPromptText("Train Number");

<<<<<<< HEAD
		GridPane.setConstraints(trainIcon, 0, 0);
		GridPane.setConstraints(trainTextField, 1, 0);
		gridPane.getChildren().addAll(trainIcon, trainTextField);
=======
        //Initialize Stations
        for(int i = 0; i < 8; i++){
            Station station = new Station(i+1, this);
>>>>>>> 432b55420d8691fc003d01141ef8b50b9a8def30

		//People
		ImageView peopleIcon = new ImageView("images/peopleIcon2.png");
		peopleIcon.setFitHeight(25);
		peopleIcon.setFitWidth(25);
		peopleIcon.setPreserveRatio(true);

<<<<<<< HEAD
		peopleTextField = new TextField();
		peopleTextField.setDisable(true);
		peopleTextField.setPromptText("Number of People");
		Button button = new Button("^");

		GridPane.setConstraints(peopleIcon, 0, 1);
		GridPane.setConstraints(peopleTextField, 1, 1);
		gridPane.getChildren().addAll(peopleTextField);
		GridPane.setConstraints(button, 2, 1);
		gridPane.getChildren().addAll(peopleIcon, button);

		vBox.getChildren().addAll(gridPane);
		return vBox;
	}
=======
//			for(int j = 0; j < 5; j++){
//				Passenger pass = new Passenger(station, 5);
//			}
            //created new caltrains
            station.start();
            stations.add(station);
        }
    }

    public void station_load_train(Station station, int count){

		/* NOTES:
		 * 1. count - how many seats are available on the train
		 * 2. either puno na yung train or lahat ng naghihintay na passenger
		 * nakasakay na bago mag end yung funtion
		 * 3. number of seats may vary among trains and should be treated as
		 * input parameter
		 */
        //System.out.println("There are " + station.getCurr_train().getVacantSeats() + " in Train " + station.getCurr_train().getTrain_number());
    }

    public void station_wait_for_train(Station station) throws InterruptedException {
//		station_lock.lock();
//		try{
//			train_arrival.await();
//			System.out.println("Train " + station.getCurr_train().getTrain_number() + " has arrived in station " + station.getStation_number());
//
//			station
//
//		}catch (InterruptedException e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//		} catch(NullPointerException n){
//		}finally {
//			station_lock.unlock();
//		}
    }

    public void station_on_board(Station station){

		/* NOTES:
		 * 1. called pag naka-board na si passenger
		 */

    }
>>>>>>> 432b55420d8691fc003d01141ef8b50b9a8def30

	public VBox initCenterVBox()
    {
        VBox vBox = new VBox();
        vBox.setId("vBoxCenter");

<<<<<<< HEAD
        Pane field = new Pane();
        field.setId("centerPane");

        for(int i = 0; i < 8; i++){
            ImageView trainStation = new ImageView("images/station.png");
            trainStation.setFitHeight(100);
            trainStation.setFitWidth(100);
            trainStation.setPreserveRatio(true);

            trainStation.setOnMouseClicked(e -> {
                System.out.println("WA");
            });

            trainStation.setLayoutX(150 * (i + 1));
            trainStation.setLayoutY(0);

            if(i > 2){
                trainStation.setLayoutX(150 * (i - 3 + 1));
                trainStation.setLayoutY(300);
            }

            if(i >= 6){
                trainStation.setLayoutX(10);
                trainStation.setLayoutY(150);

                if(i == 7)
                    trainStation.setLayoutX(600);
            }

            field.getChildren().add(trainStation);
        }

        for(int i = 0; i < 16; i++){
            ImageView train1 = new ImageView("images/train2.png");
            train1.setFitHeight(50);
            train1.setFitWidth(50);

            if(i == 0){ //First Train
                train1 = new ImageView("images/train1.png");
                train1.setFitHeight(50);
                train1.setFitWidth(50);
                train1.setLayoutX(225);
                train1.setLayoutY(425);
            } else if(i < 4){
                train1.setLayoutX(225 + (75 * i));
                train1.setLayoutY(425);
            } else if(i < 8){
                train1.setLayoutX(225 + (75 * (i - 4)));
                train1.setLayoutY(450);
            } else if(i < 12){
                train1.setLayoutX(225 + (75 * (i - 8)));
                train1.setLayoutY(475);
            } else if(i < 16){
                train1.setLayoutX(225 + (75 * (i - 12)));
                train1.setLayoutY(500);
            }

            train1.setRotate(train1.getRotate() + 90);
            train1.setPreserveRatio(true);

            train1.setOnMouseClicked(e -> {
                System.out.println("WA");
            });
            train1.setId("train"+i);

            field.getChildren().add(train1);
        }

        vBox.getChildren().add(field);
        return vBox;
    }

	public void terminateProgram()
	{
		System.out.println("\nProgram has been terminated.");
		Platform.exit();
		System.exit(0);
	}
}
=======
    public void getCurrTime(){
        Calendar cal = Calendar.getInstance();
        SimpleDateFormat sdf = new SimpleDateFormat("HH:mm:ss");
        System.out.println( sdf.format(cal.getTime()) );
    }

    public ArrayList<Station> getStations() {
        return stations;
    }

    public void setStations(ArrayList<Station> stations) {
        this.stations = stations;
    }

    public Station getStation() {
        return station;
    }

    public void setStation(Station station) {
        this.station = station;
    }
}
>>>>>>> 432b55420d8691fc003d01141ef8b50b9a8def30
