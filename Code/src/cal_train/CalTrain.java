package cal_train;

import javafx.animation.FadeTransition;
import javafx.animation.PathTransition;
import javafx.application.Application;
import javafx.application.Platform;
import javafx.beans.binding.Bindings;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import javafx.scene.shape.Polyline;
import javafx.stage.Stage;
import javafx.util.Duration;

import java.util.ArrayList;
import java.util.concurrent.Semaphore;

public class CalTrain extends Application{

	/** Global Variables **/

	public static ArrayList<Station> stations;
	public static ArrayList<Train> trains;
	public static Semaphore semaphore;
	public static Semaphore mutex;
	

	public static boolean solType = true;        //use this to check if the user wants a semaphore machine or lock(?)
	/** GUI **/

	private BorderPane mainPane;
	private TextField  trainTextField,
			stationTextField,
			seatsTextField,
			nPeopleTextField;

	private ChoiceBox<Integer> destinationChoiceBox;
	private Spinner<Integer> nPeopleSpinner;
	private MenuItem semaphoreMenuItem,
			lockMenuItem;
	private static Scene scene;



//	public void run() {
//		System.out.println("SOLUTION TYPE: ");
//		System.out.println("[1] Locks \n[2] Semaphores\n");
//		Scanner sc = new Scanner(System.in);
//		int x = sc.nextInt();
//
//		switch(x){
//			case 1: solType = true;
//				break;
//			case 2: solType = false;
//				break;
//		}
//
//		/* INITIALIZE GLOBAL VARIABLES */
//
//
//		semaphore = new Semaphore(8);
//		mutex = new Semaphore(1); 	  // mutual exclusion for threads
//
//		System.out.println("STARTING THREADS");
//
//		Passenger pass = null;
//
//		if(solType){
//			for(int i = 0; i < stations.size(); i++){
//				for(int j = 0; j < 15; j++){
//
//					pass = new Passenger(stations[i], stations[4]);
//					pass.start();
//					stations[i].addPassenger(pass);
//				}
//			}
//
//			for(int i = 0; i < stations.size(); i++){
//				System.out.println("Station " + stations[i].getStation_number() + " has " + stations[i].getPassengers().size());
//			}
//
//		} else {
//			for(int i = 0; i < 8; i++){
//				if(i < 7){
//					pass = new Passenger(stations[i], stations[i+1]);
//				}else{
//					pass = new Passenger(stations[i], stations[0]);
//				}
//
//				stations[i].addPassenger(pass);
//				pass.run();
//			}
//		}
//
//		for(int i = 0; i < 15; i++){
//			trains[i].start();
//		}
//
//		sc.close();
//	}

	@Override
	public void start(Stage primaryStage) {

		mainPane = new BorderPane();
		mainPane.setId("mainPane");

		scene = new Scene(mainPane, 1000, 650);
		primaryStage.setTitle("CaltrainII (OPERSYS MP2)");
		primaryStage.getIcons().add(new Image("images/trainSubIcon.png"));
		scene.getStylesheets().add("Style.css");
		primaryStage.setResizable(false);
		primaryStage.setScene(scene);
		primaryStage.show();
		primaryStage.setOnCloseRequest(e -> terminateProgram());

		Label headerLabel = new Label("Train Simulation");
		headerLabel.setId("headerLabel");

//        initScreen2(true);
		FadeTransition fadeOut = new FadeTransition(javafx.util.Duration.seconds(1), mainPane);
		fadeOut.setToValue(0);
		fadeOut.setCycleCount(1);
		fadeOut.play();
		fadeOut.setOnFinished(e -> {
			fadeOut.setToValue(1);
			fadeOut.setCycleCount(1);
			fadeOut.play();
			mainPane.setCenter(headerLabel);

			fadeOut.setOnFinished(e2 -> {
				fadeOut.setToValue(0);
				fadeOut.setCycleCount(1);
				fadeOut.play();

				fadeOut.setOnFinished(e3 -> {
					fadeOut.setToValue(1);
					fadeOut.setCycleCount(1);
					fadeOut.play();

					// Update initialization of trains first before going there
					initScreen1();
					fadeOut.setOnFinished(e4 -> fadeOut.stop());

				});
			});
		});
	}

	public void initScreen1(){
		mainPane.getChildren().removeAll();
		mainPane.setRight(null);
		mainPane.setTop(initTopBar());
		semaphoreMenuItem.setDisable(true);
		lockMenuItem.setDisable(true);
		mainPane.setStyle("-fx-background-color : null;");

		VBox centerVBox = new VBox(90);
		centerVBox.getStyleClass().add("vBoxCenter");
		centerVBox.setId("trainInitialization");

		Label headerLabel = new Label("Train Simulation");
		headerLabel.setId("headerLabel");

		// Create a label with a bind property placed on the bottom of the mainPane
		Label guideLabel = new Label();
		guideLabel.setId("guideLabel");
		guideLabel.textProperty().bind(Bindings.concat("Create ", 15 - trains.size(), " more trains"));


		// Gridpane for the initialization of trains
		GridPane trainInitGrid = new GridPane();
		trainInitGrid.getStyleClass().add("grid-pane");
		trainInitGrid.setId("trainInitGrid");

		Label nTrainsLabel = new Label("Number of Trains ");

		ChoiceBox<Integer> nTrainsChoiceBox = new ChoiceBox<>();
		for(int i = 1; i < 16; i++) {
			if(i + trains.size() < 16)
				nTrainsChoiceBox.getItems().addAll(i);
			if(i == 1)
				nTrainsChoiceBox.setValue(i);
		}

		Label nSeats = new Label("Number of Seats ");
		Spinner<Integer> spinner = new Spinner<>(1, Integer.MAX_VALUE, 0, 5);

		Button createTrain = new Button("Create");

		createTrain.setOnAction(e -> {

			for(int i = 0; i < nTrainsChoiceBox.getValue(); i++)
				trains.add(new Train(i + 1, spinner.getValue()));

			if(trains.size() == 15) {
				semaphoreMenuItem.setDisable(false);
				lockMenuItem.setDisable(false);

				VBox centerVBox2 = new VBox(100);
				centerVBox2.getStyleClass().add("vBoxCenter");
				centerVBox2.setId("trainInitialization");
				guideLabel.textProperty().unbind();
				guideLabel.setText("Select the type of solution");
				Button semaphores = new Button("Semaphores");
				Button locks = new Button("Locks");

				semaphores.setOnAction(e2 -> initScreen2(true));
				locks.setOnAction(e2 -> initScreen2(false));

				VBox subVBox = new VBox();
				subVBox.getChildren().addAll(semaphores, locks);
				subVBox.setId("subVBox");
				subVBox.setAlignment(Pos.CENTER);

				centerVBox2.getChildren().addAll(headerLabel, subVBox, guideLabel);
				mainPane.setCenter(centerVBox2);
			} else{
				initScreen1();
			}
		});


		GridPane.setConstraints(nTrainsLabel, 0, 1);
		GridPane.setConstraints(nTrainsChoiceBox, 1, 1);

		GridPane.setConstraints(nSeats, 0, 2);
		GridPane.setConstraints(spinner, 1, 2);
		GridPane.setConstraints(createTrain, 1, 3);


		trainInitGrid.getChildren().addAll(nTrainsLabel, nTrainsChoiceBox,
				nSeats, createTrain, spinner);
		//Add button for adding of passengers
		trainInitGrid.setAlignment(Pos.CENTER);

		centerVBox.getChildren().addAll(headerLabel, trainInitGrid, guideLabel);
		mainPane.setCenter(centerVBox);
	}

	public void initScreen2(Boolean semaphoreMachine){
		this.solType = semaphoreMachine;

		mainPane.setStyle("-fx-background-image: url(\"images/train_station_bg_2.jpg\");\n" +
				"-fx-background-size: cover;");
		mainPane.getChildren().remove(mainPane.getCenter());
		mainPane.setRight(initRightVBox());
		mainPane.setCenter(initCenterVBox());
	}

	public MenuBar initTopBar(){

		//Menu
		Menu menu = new Menu("_Menu");
		Menu machine = new Menu("Ma_chine");

		//Initializing menu items
		MenuItem exit      = new MenuItem("_Exit");
		MenuItem about     = new MenuItem("_About");
		MenuItem restart   = new MenuItem("_Restart");
		semaphoreMenuItem  = new MenuItem("_Semaphore");
		lockMenuItem       = new MenuItem("_Lock");

		machine.getItems().addAll(semaphoreMenuItem, lockMenuItem, restart);
		menu.getItems().addAll(machine, new SeparatorMenuItem(), about, exit);

		MenuBar menuBar = new MenuBar();
		menuBar.getMenus().addAll(menu);

		exit.setOnAction(e -> terminateProgram());


		semaphoreMenuItem.setOnAction(e -> {
			solType = true;
			initScreen2(true);
		});

		lockMenuItem.setOnAction(e -> {
			solType = false;
			initScreen2(false);
		});

		restart.setOnAction(e -> {
			trains.clear();
			initScreen1();
		});

		return menuBar;
	}


	public VBox initRightVBox()
	{
		VBox vBox = new VBox();
		vBox.setId("vBoxRight");

		GridPane gridPane = new GridPane();
		gridPane.setId("rightGridPane");
		gridPane.getStyleClass().add("grid-pane");

		//Station Number
		ImageView stationIcon = new ImageView("images/trainStation.png");
		stationIcon.setFitHeight(25);
		stationIcon.setFitWidth(25);
		stationIcon.setPreserveRatio(true);

		stationTextField = new TextField();
		stationTextField.setDisable(true);
		stationTextField.setPromptText("Station Number");

		//Train Number
		ImageView trainIcon = new ImageView("images/trainSubIcon2.png");
		trainIcon.setFitHeight(25);
		trainIcon.setFitWidth(25);
		trainIcon.setPreserveRatio(true);

		trainTextField = new TextField();
		trainTextField.setDisable(true);
		trainTextField.setPromptText("Train Number");

		//People
		ImageView peopleIcon = new ImageView("images/peopleIcon2.png");
		peopleIcon.setFitHeight(25);
		peopleIcon.setFitWidth(25);
		peopleIcon.setPreserveRatio(true);


		nPeopleTextField = new TextField();
		nPeopleTextField.setDisable(true);
		nPeopleTextField.setPromptText("Number of People");

		//Seats for the train
		ImageView seatsIcon = new ImageView("images/seatsIcon.png");
		seatsIcon.setFitHeight(25);
		seatsIcon.setFitWidth(25);
		seatsIcon.setPreserveRatio(true);

		seatsTextField = new TextField();
		seatsTextField.setDisable(true);
		seatsTextField.setPromptText("A/T Seats");

		//Add People
		ImageView addPeopleIcon = new ImageView("images/addPeopleIcon.png");
		addPeopleIcon.setFitHeight(25);
		addPeopleIcon.setFitWidth(25);
		addPeopleIcon.setPreserveRatio(true);

		nPeopleSpinner = new Spinner<>(1, Integer.MAX_VALUE, 1, 1);

		//Destination
		ImageView destinationIcon = new ImageView("images/trainDestination.png"); // Update destinationIcon
		destinationIcon.setFitHeight(25);
		destinationIcon.setFitWidth(25);
		destinationIcon.setPreserveRatio(true);
		destinationChoiceBox = new ChoiceBox<>();
		destinationChoiceBox.getItems().addAll(1, 2, 3, 4, 5, 6, 7, 8);


		Button createPassenger = new Button("Create");
		createPassenger.setOnAction(e -> {
			if(destinationChoiceBox.getValue() != null) {
				for (int i = 0; i < nPeopleSpinner.getValue(); i++) {
					Passenger passenger = new Passenger(stations.get(Integer.parseInt(stationTextField.getText()) - 1),
							stations.get(destinationChoiceBox.getValue() - 1));
					passenger.run();
				}

				Station station = stations.get(Integer.parseInt(stationTextField.getText()) - 1);

				nPeopleTextField.textProperty().bind(Bindings.concat((station.getPassengers().size() == 0 ?
						"Nobody is at the station" :
						(String.valueOf(station.getPassengers().size()) +
								(station.getPassengers().size() == 1 ?
										" Person " : " People " + "at the station")))));
			}

			destinationChoiceBox.setValue(null);
			nPeopleSpinner.getValueFactory().setValue(1);

		});

		nPeopleSpinner.setDisable(true);
		destinationChoiceBox.setValue(null);
		destinationChoiceBox.setDisable(true);
		nPeopleSpinner.getValueFactory().setValue(1);

		GridPane.setConstraints(stationIcon         , 0, 0);
		GridPane.setConstraints(stationTextField    , 1, 0);
		GridPane.setConstraints(trainIcon           , 0, 1);
		GridPane.setConstraints(trainTextField      , 1, 1);
		GridPane.setConstraints(peopleIcon          , 0, 2);
		GridPane.setConstraints(nPeopleTextField    , 1, 2);
		GridPane.setConstraints(seatsIcon           , 0, 3);
		GridPane.setConstraints(seatsTextField      , 1, 3);

		GridPane.setConstraints(addPeopleIcon       , 0, 5);
		GridPane.setConstraints(nPeopleSpinner      , 1, 5);
		GridPane.setConstraints(destinationIcon     , 0, 4);
		GridPane.setConstraints(destinationChoiceBox, 1, 4);
		GridPane.setConstraints(createPassenger     , 1, 6);

		gridPane.getChildren().addAll(stationIcon   , stationTextField, trainIcon,
				trainTextField, peopleIcon      , nPeopleTextField,
				seatsIcon     , seatsTextField  , nPeopleSpinner,
				addPeopleIcon , destinationIcon , destinationChoiceBox,
				createPassenger);

		vBox.getChildren().addAll(gridPane);
		return vBox;
	}

	public VBox initCenterVBox() {
		VBox vBox = new VBox();
		vBox.getStyleClass().add("vBoxCenter");

		Pane field = new Pane();
		field.setId("centerPane");

		for (int i = 0; i < 8; i++) {
			Label trainStation = new Label("Station " + (i + 1));
			Station newStation = new Station(i + 1);
			newStation.run();
			stations.add(newStation);

			ImageView stationImageView = new ImageView("images/station.png");
			stationImageView.setFitHeight(100);
			stationImageView.setFitWidth(100);
			stationImageView.setPreserveRatio(true);
			trainStation.setGraphic(stationImageView);
			trainStation.setContentDisplay(ContentDisplay.TOP);
			trainStation.setGraphicTextGap(-55);
//            trainStation.setPadding(new Insets(0, 0, 20, 0));
			trainStation.setId(String.valueOf((i + 1)));

			trainStation.setOnMouseClicked(e -> {
				String id = ((Label) e.getSource()).getId();
				int stationId = Integer.parseInt(String.valueOf(id.charAt(id.length() - 1))) - 1;
				Station station = stations.get(stationId);

				System.out.println(stationId + " " + station.getPassengers());

				seatsTextField.textProperty().unbind();
				trainTextField.textProperty().unbind();
				nPeopleTextField.textProperty().unbind();
				stationTextField.textProperty().unbind();

				stationTextField.setText(String.valueOf(station.getStation_number()));
				nPeopleTextField.textProperty().bind(Bindings.concat((station.getPassengers().size() == 0 ?
						"Nobody is at the station" :
						(String.valueOf(station.getPassengers().size()) +
								(station.getPassengers().size() == 1 ?
										" Person " : " People " + "at the station")))));

				trainTextField.textProperty().bind(Bindings.concat(station.getCurrentTrain() == null ?
						"No train" : "Train " +
						station.getCurrentTrain().getTrain_number()));

				seatsTextField.textProperty().bind(Bindings.concat(station.getCurrentTrain() == null ?
						"There is no train" :
						station.getCurrentTrain().getPassengers() + "/" +
								station.getCurrentTrain().getSeats() + " Seats"));

				if (false) {
					nPeopleSpinner.setDisable(true);
					nPeopleSpinner.getValueFactory().setValue(1);
					destinationChoiceBox.setDisable(true);
					destinationChoiceBox.setValue(null);
				} else {
					nPeopleSpinner.setDisable(false);
					nPeopleSpinner.getValueFactory().setValue(1);
					destinationChoiceBox.setDisable(false);
					destinationChoiceBox.setValue(null);
				}
			});

			if (i == 0 || i == 4) {
				trainStation.setLayoutY(150);
				trainStation.setLayoutX(10);
				if (i == 0)
					trainStation.setLayoutX(600);
			} else if (i < 4) {
				trainStation.setLayoutX(750 - 150 * (i + 1));
				trainStation.setLayoutY(0);
			} else if (i < 8) {
				trainStation.setLayoutX(150 * (i - 5 + 1));
				trainStation.setLayoutY(300);
			}

			field.getChildren().add(trainStation);
		}

		for (int i = 0; i < 15; i++) {
			ImageView train1 = new ImageView("images/train2.png");
			train1.setFitHeight(50);
			train1.setFitWidth(50);

			if (i < 4) {
				if (i == 0)
					train1.setImage(new Image("images/train1.png"));

				train1.setX(450 - (75 * i));
				train1.setY(425);
			} else if (i < 8) {
				train1.setX(450 - (75 * (i - 4)));
				train1.setY(450);
			} else if (i < 12) {
				train1.setX(450 - (75 * (i - 8)));
				train1.setY(475);
			} else if (i < 16) {
				train1.setX(375 - (75 * (i - 12)));
				train1.setY(500);
			}


			train1.setPreserveRatio(true);

			if(i % 4 == 0 ) {
				Polyline polyline = new Polyline();
				polyline.setLayoutX(25);
				polyline.setLayoutY(25);
				polyline.getPoints().addAll(
						train1.getX(), train1.getY(),
						600.0 - 40, train1.getY(),
						600.0 - 40, 160.0,              //Station Right
						475.0, 70.0,                    //Station middle 1 (top)
						474.0, 70.0,                    //Station middle 1b (top)     // STOP HERE
						325.0, 70.0,                    //Station middle 2 (top)
						175.0, 70.0,                    //Station middle 3 (top)
						100.0, 160.0,                   //Station left
						100.0, 161.0,                   //Station left b              // STOP HERE
						175.0, 270.0,                    //Station middle 3 (bottom)
						176.0, 270.0,                    //Station middle 3b (bottom) // STOP HERE
						325.0, 270.0,                    //Station middle 2 (bottom)
						475.0, 270.0,                    //Station middle 1 (bottom)
                    /*Another loop*/
						600.0 - 40, 160.0,              //Station Right
						600.0 - 40, 159.0,              //Station Right b             // STOP HERE
						475.0, 70.0,                    //Station middle 1 (top)
						474.0, 70.0,                    //Station middle 1b (top)     // STOP HERE
						325.0, 70.0,                    //Station middle 2 (top)
						175.0, 70.0,                    //Station middle 3 (top)
						100.0, 160.0,                   //Station left
						100.0, 161.0,                   //Station left b              // STOP HERE
						100.0, train1.getY(),
						train1.getX(), train1.getY());

				field.getChildren().add(polyline);
			}


			train1.setOnMouseClicked(e -> {
				String id = ((ImageView) e.getSource()).getId();
				int trainId = Integer.parseInt(String.valueOf(id.charAt(id.length())));
				Train train = trains.get(trainId - 1);

				seatsTextField.textProperty().unbind();
				trainTextField.textProperty().unbind();
				nPeopleTextField.textProperty().unbind();
				stationTextField.textProperty().unbind();

				nPeopleSpinner.setDisable(true);
				nPeopleSpinner.getValueFactory().setValue(1);
				destinationChoiceBox.setDisable(true);
				destinationChoiceBox.setValue(null);

				trainTextField.setText(String.valueOf(trainId));
				seatsTextField.textProperty().bind(Bindings.concat(train.getPassengers().size(),
						"/", train.getSeats(), " Seats"));
				stationTextField.textProperty().bind(Bindings.concat(train.getCurrentStation() == null ?
						"Not in a station" : "Station " +
						train.getCurrentStation().getStation_number()));
				nPeopleTextField.textProperty().bind(Bindings.concat(train.getPassengers().size() == 0 ?
								"Nobody is in the train" :
								train.getPassengers().size(),
						train.getPassengers().size() == 1 ?
								" Person " : " People ", "in the train"));

				System.out.println(((ImageView) e.getSource()).getX() + " " + ((ImageView) e.getSource()).getTranslateX());
//				PathTransition transition = new PathTransition();
//				transition.setOrientation(PathTransition.OrientationType.ORTHOGONAL_TO_TANGENT);
//				transition.setNode((ImageView) e.getSource());
//				transition.setDuration(Duration.seconds(10));
//				transition.setPath(polyline);
//				transition.setCycleCount(1);
//				transition.play();
			});

			train1.setId("Train_" + (i + 1));
			System.out.println(train1.getId());
			field.getChildren().add(train1);

			trains.get(i).run();

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

	public static void main(String[] args){

		stations = new ArrayList<>();
		trains = new ArrayList<>();

		semaphore = new Semaphore(8);
		mutex = new Semaphore(1); 	  // mutual exclusion for threads

//		new CalTrain().run();

		launch(args);
	}


	public static void moveTrain(int train_number, int station_number) {

		ImageView train = (ImageView)scene.lookup("#Train_" + train_number);

		System.out.println(train);
		System.out.println("#Train_" + train_number);
		System.out.println(" || " + train.getY());


		String coordinates = (int)train.getX() + " " + (int)train.getY();

		Polyline polyline = new Polyline();
		polyline.setLayoutX(25);
		polyline.setLayoutY(25);

		switch (coordinates){
			case "560 161": // Station 1
							polyline.getPoints().addAll(475.0, 160.0,
														474.0,  70.0);
			case "474  70": // Station 2
							polyline.getPoints().addAll(325.0,  70.0);

			case "325  70": // Station 3
							polyline.getPoints().addAll(175.0,  70.0);

			case "175  70": // Station 4
							polyline.getPoints().addAll(100.0, 160.0,
														100.0, 161.0);
			case "100 161": // Station 5
							polyline.getPoints().addAll(175.0, 270.0,
														176.0, 270.0);
			case "176 270": // Station 7
							polyline.getPoints().addAll(325.0, 270.0);

			case "325 270": // Station 8
							polyline.getPoints().addAll(560.0, 160.0,
														560.0, 159.0);

			default		  :	// Initial
							polyline.getPoints().addAll(train.getX(), train.getY(),
														560.0       , train.getY(),
														560.0		, 161.0);
		}

//		polyline.getPoints().addAll(
//				train.getX(), train.getY(),
//				600.0 - 40, train.getY(),
//				600.0 - 40, 160.0,              //Station Right
//				475.0, 70.0,                    //Station middle 1 (top)
//				474.0, 70.0,                    //Station middle 1b (top)     // STOP HERE
//				325.0, 70.0,                    //Station middle 2 (top)
//				175.0, 70.0,                    //Station middle 3 (top)
//				100.0, 160.0,                   //Station left
//				100.0, 161.0,                   //Station left b              // STOP HERE
//				175.0, 270.0,                    //Station middle 3 (bottom)
//				176.0, 270.0,                    //Station middle 3b (bottom) // STOP HERE
//				325.0, 270.0,                    //Station middle 2 (bottom)
//				475.0, 270.0,                    //Station middle 1 (bottom)
//                    /*Another loop*/
//				600.0 - 40, 160.0,              //Station Right
//				600.0 - 40, 159.0,              //Station Right b             // STOP HERE
//				475.0, 70.0,                    //Station middle 1 (top)
//				474.0, 70.0,                    //Station middle 1b (top)     // STOP HERE
//				325.0, 70.0,                    //Station middle 2 (top)
//				175.0, 70.0,                    //Station middle 3 (top)
//				100.0, 160.0,                   //Station left
//				100.0, 161.0,                   //Station left b              // STOP HERE
//				100.0, train.getY(),
//				train.getX(), train.getY());

		PathTransition transition = new PathTransition();
		transition.setOrientation(PathTransition.OrientationType.ORTHOGONAL_TO_TANGENT);
		transition.setNode(train);
		transition.setDuration(Duration.seconds(5));
		transition.setPath(polyline);
		transition.setCycleCount(1);
		transition.play();

		transition.setOnFinished(e -> stations.get(station_number).setGuiMoveTrain(new Semaphore(1)));
	}
}
