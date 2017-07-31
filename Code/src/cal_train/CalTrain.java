package cal_train;

import javafx.animation.FadeTransition;
import javafx.animation.PathTransition;
import javafx.animation.PauseTransition;
import javafx.application.Application;
import javafx.application.Platform;
import javafx.beans.binding.Bindings;
import javafx.collections.ListChangeListener;
import javafx.collections.ObservableList;
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
	

	public static boolean solType = false;        //use this to check if the user wants a semaphore machine or lock(?)
	/** GUI **/

	private static BorderPane mainPane;
	private ObservableList<Passenger> observablePassengers;
	private static ListChangeListener<Passenger> passengerListChangeListener;

	private TextField  trainTextField, stationTextField,
					  seatsTextField, nPeopleTextField;

	private TextArea passengerTextField;

	private ChoiceBox<Integer> destinationChoiceBox;
	private ChoiceBox<String> chosenChoiceBox;
	private Spinner<Integer> nPeopleSpinner;
	private MenuItem semaphoreMenuItem,
				  	 lockMenuItem;
	private static Scene scene;

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
		Spinner<Integer> spinner = new Spinner<>(1, Integer.MAX_VALUE, 0, 1);

		Button createTrain = new Button("Create");

		createTrain.setOnAction(e -> {

			int temp = trains.size();
			for(int i = 0; i < nTrainsChoiceBox.getValue(); i++) {
				System.out.println(temp + " " + i);
					trains.add(new Train(temp + i + 1, spinner.getValue()));
			}
			if(trains.size() == 15) {
				semaphoreMenuItem.setDisable(false);
				lockMenuItem.setDisable(false);

				VBox centerVBox2 = new VBox(100);
				centerVBox2.getStyleClass().add("vBoxCenter");
				centerVBox2.setId("trainInitialization");

				VBox subVBox = new VBox();
				subVBox.setId("subVBox");

				guideLabel.textProperty().unbind();
				guideLabel.setText("Select the type of solution");
				Button semaphores = new Button("Semaphores");
				Button locks = new Button("Locks");

				semaphores.setOnAction(e2 -> initScreen2(false));
				locks.setOnAction(e2 -> initScreen2(true));

				subVBox.getChildren().addAll(semaphores, locks);
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

		PauseTransition delay = new PauseTransition(Duration.seconds(.1));

		delay.play();
		delay.setOnFinished(e -> {
			delay.setDelay(Duration.seconds(2.5));
			System.out.println("Start trains");

			for(int i = 0; i < 15; i++)
				trains.get(i).setTransition(delay);

			trains.get(0).run();
			delay.play();
			delay.setOnFinished(e2 -> {
				trains.get(1).run();

				delay.play();
				delay.setOnFinished(e3 -> {
					trains.get(2).run();

					delay.play();
					delay.setOnFinished(e4 -> {
						trains.get(3).run();

						delay.play();
						delay.setOnFinished(e5 -> {
							trains.get(4).run();

							delay.play();
							delay.setOnFinished(e6 -> {
								trains.get(5).run();

								delay.play();
								delay.setOnFinished(e7 -> {
									trains.get(6).run();

									delay.play();
									delay.setOnFinished(e8 -> {
										trains.get(7).run();

										delay.play();
										delay.setOnFinished(e9 -> {
											trains.get(8).run();

											delay.play();
											delay.setOnFinished(e10 -> {
												trains.get(9).run();

												delay.play();
												delay.setOnFinished(e11 -> {
													trains.get(10).run();

													delay.play();
													delay.setOnFinished(e12 -> {
														trains.get(11).run();

														delay.play();
														delay.setOnFinished(e13 -> {
															trains.get(12).run();

															delay.play();
															delay.setOnFinished(e14 -> {
																trains.get(13).run();

																delay.play();
																delay.setOnFinished(e15 -> {
																	trains.get(14).run();

																});
															});
														});
													});
												});
											});
										});
									});
								});
							});
						});
					});
				});
			});

		});
//		trains.get(0).run();

	}

	public MenuBar initTopBar(){

		//Menu
		Menu menu = new Menu("_Menu");
		Menu machine = new Menu("Ma_chine");

		//Initializing menu items
		MenuItem exit      = new MenuItem("_Exit");
		MenuItem about     = new MenuItem("_About");
		MenuItem restart   = new MenuItem("_Restart");
		MenuItem pause   = new MenuItem("_Pause");
		MenuItem start   = new MenuItem("Re_sume");
		semaphoreMenuItem  = new MenuItem("_Semaphore");
		lockMenuItem       = new MenuItem("_Lock");

		machine.getItems().addAll(/*semaphoreMenuItem, lockMenuItem, */restart);
		menu.getItems().addAll(machine, new SeparatorMenuItem(), pause, start, new SeparatorMenuItem(), about, exit);

		MenuBar menuBar = new MenuBar();
		menuBar.getMenus().addAll(menu);

		exit.setOnAction(e -> terminateProgram());

//		semaphoreMenuItem.setOnAction(e -> {
//			solType = false;
//			stopAllThreads();
//			initScreen2(solType);
//		});
//
//		lockMenuItem.setOnAction(e -> {
//			solType = true;
//			stopAllThreads();
//			initScreen2(solType);
//		});

		restart.setOnAction(e -> {
			stopAllThreads();
			trains.clear();
			initScreen1();
		});

		pause.setOnAction(e -> {
			stopAllThreads();
		});

		start.setOnAction(e -> {
			runAllThreads();
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
		ImageView trainIcon = new ImageView("images/trainNumberIcon.png");
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

		//Chosen Train Number
		ImageView chosenTrain = new ImageView("images/trainSubIcon2.png");
		chosenTrain.setFitHeight(25);
		chosenTrain.setFitWidth(25);
		chosenTrain.setPreserveRatio(true);

		chosenChoiceBox = new ChoiceBox<>();
		chosenChoiceBox.getItems().addAll("", "1", "2", "3", "4", "5", "6", "7", "8",
										  "9", "10", "11", "12", "13", "14", "15");
		chosenChoiceBox.setValue("");

		//Add People
		ImageView addPeopleIcon = new ImageView("images/addPeopleIcon.png");
		addPeopleIcon.setFitHeight(25);
		addPeopleIcon.setFitWidth(25);
		addPeopleIcon.setPreserveRatio(true);

		nPeopleSpinner = new Spinner<>(1, Integer.MAX_VALUE, 1, 1);

		//Destination
		ImageView destinationIcon = new ImageView("images/trainDestination.png");
		destinationIcon.setFitHeight(25);
		destinationIcon.setFitWidth(25);
		destinationIcon.setPreserveRatio(true);
		destinationChoiceBox = new ChoiceBox<>();
		destinationChoiceBox.getItems().addAll(1, 2, 3, 4, 5, 6, 7, 8);

		final Pane  emptyPane = new Pane();
		emptyPane.setMinHeight(30);


		passengerTextField = new TextArea();
		passengerTextField.setEditable(false);
		passengerTextField.setId("passengerTextField");
//		ScenicView a = new ScenicView();
//		a.show(passengerTextField);


		Button createPassenger = new Button("Create");
		createPassenger.setOnAction(e -> {
			if(destinationChoiceBox.getValue() != null) {
				for (int i = 0; i < nPeopleSpinner.getValue(); i++) {
					Passenger passenger = new Passenger(stations.get(Integer.parseInt(stationTextField.getText()) - 1),
														stations.get(destinationChoiceBox.getValue() - 1));
					passenger.setChosenTrainNumber(chosenChoiceBox.getValue().length() != 0 ?
												   Integer.parseInt(chosenChoiceBox.getValue()): -1);

					if(solType)
						passenger.start();
					else
						passenger.run();

				}
				System.out.println(stations.get(Integer.parseInt(stationTextField.getText()) - 1).getPassengers() + " PPPPPPPPPPPP");
			}

			destinationChoiceBox.setValue(null);
			nPeopleSpinner.getValueFactory().setValue(1);
			chosenChoiceBox.setValue("");
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

		GridPane.setConstraints(chosenTrain         , 0, 4);
		GridPane.setConstraints(chosenChoiceBox, 1, 4);
		GridPane.setConstraints(addPeopleIcon       , 0, 6);
		GridPane.setConstraints(nPeopleSpinner      , 1, 6);
		GridPane.setConstraints(destinationIcon     , 0, 5);
		GridPane.setConstraints(destinationChoiceBox, 1, 5);
		GridPane.setConstraints(createPassenger     , 1, 7);
		GridPane.setConstraints(emptyPane    		, 1, 8);
		GridPane.setConstraints(passengerTextField  , 1, 9);

		gridPane.getChildren().addAll(stationIcon   , stationTextField, trainIcon,
									  trainTextField, peopleIcon      , nPeopleTextField,
									  seatsIcon     , seatsTextField  , chosenTrain,
									  chosenChoiceBox, nPeopleSpinner,
									  addPeopleIcon , destinationIcon , destinationChoiceBox,
									  createPassenger, passengerTextField, emptyPane);

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

				if(observablePassengers != null)
					observablePassengers.removeListener(passengerListChangeListener);

				observablePassengers = stations.get(stationId).getObservablePassengers();
				passengerListChangeListener = (ListChangeListener<Passenger>) c -> {
					nPeopleTextField.textProperty().bind(Bindings.concat((stations.get(stationId).getPassengers().size() == 0 ?
							"Nobody is at the station" :
							(String.valueOf(stations.get(stationId).getPassengers().size()) +
									(stations.get(stationId).getPassengers().size() == 1 ?
											" Person " : " People " + "at the station")))));

					trainTextField.textProperty().bind(Bindings.concat(station.getCurrentTrain() == null ?
							"No train" : "Train " +
							station.getCurrentTrain().getTrain_number()));

					seatsTextField.textProperty().bind(Bindings.concat(station.getCurrentTrain() == null ?
							"There is no train" : (station.getCurrentTrain().getPassengers() == null ?  "0" :
							station.getCurrentTrain().getPassengers().size()) + "/" +
							station.getCurrentTrain().getSeats() + " Seats"));

					passengerTextField.setText(station.getPassengers().toString().replaceAll("\\[",
							"").replaceAll("]", "").replaceAll(", ", "\n"));
				};

				observablePassengers.addListener(passengerListChangeListener);

				nPeopleTextField.textProperty().bind(Bindings.concat((stations.get(stationId).getPassengers().size() == 0 ?
						"Nobody is at the station" :
						(String.valueOf(stations.get(stationId).getPassengers().size()) +
								(stations.get(stationId).getPassengers().size() == 1 ?
										" Person " : " People " + "at the station")))));

				trainTextField.textProperty().bind(Bindings.concat(station.getCurrentTrain() == null ?
						"No train" : "Train " +
						station.getCurrentTrain().getTrain_number()));

				seatsTextField.textProperty().bind(Bindings.concat(station.getCurrentTrain() == null ?
						"There is no train" : station.getCurrentTrain().getPassengers() == null ? "0" :
						station.getCurrentTrain().getPassengers().size() + "/" +
								station.getCurrentTrain().getSeats() + " Seats"));

				passengerTextField.setText(station.getPassengers().toString().replaceAll("\\[",
						"").replaceAll("]", "").replaceAll(", ", "\n"));

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
			train1.setFitHeight(38);
			train1.setFitWidth(29);

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

//			train1.setPreserveRatio(true);

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
				int trainId = Integer.parseInt(id.length() == 7 ? String.valueOf(id.charAt(id.length() - 1)) :
						id.substring(id.length() - 2, id.length()));
				Train train = trains.get(trainId - 1);

				seatsTextField.textProperty().unbind();
				trainTextField.textProperty().unbind();
				nPeopleTextField.textProperty().unbind();
				stationTextField.textProperty().unbind();

				nPeopleSpinner.setDisable(true);
				nPeopleSpinner.getValueFactory().setValue(1);
				destinationChoiceBox.setDisable(true);
				destinationChoiceBox.setValue(null);

				if(observablePassengers != null)
					observablePassengers.removeListener(passengerListChangeListener);
				observablePassengers = train.getObservablePassengers();

				seatsTextField.textProperty().bind(Bindings.concat(train.getPassengers() == null ?
								"0" : train.getPassengers().size(),
						"/", train.getSeats(), " Seats"));

				nPeopleTextField.textProperty().bind(Bindings.concat(train.getPassengers().size() == 0 ?
						"Nobody is in the train" :
						train.getPassengers().size() +
								train.getPassengers().size() == 1 ?
								" Person " : " People " + "in the train"));

				stationTextField.textProperty().bind(Bindings.concat(train.getCurrentStation() == null ?
						"Not in a station" : "Station " +
						train.getCurrentStation().getStation_number()));

				trainTextField.textProperty().bind(Bindings.concat(train.getTrain_number()));

				passengerTextField.setText(train.getPassengers().toString().replaceAll("\\[",
						"").replaceAll("]", "").replaceAll(", ", "\n"));

				passengerListChangeListener = (ListChangeListener<Passenger>) c -> {
					seatsTextField.textProperty().bind(Bindings.concat(train.getPassengers() == null ?
									"0" : train.getPassengers().size(),
							"/", train.getSeats(), " Seats"));

					nPeopleTextField.textProperty().bind(Bindings.concat(train.getPassengers().size() == 0 ?
							"Nobody is in the train" :
							train.getPassengers().size() +
									train.getPassengers().size() == 1 ?
									" Person " : " People " + "in the train"));

					stationTextField.textProperty().bind(Bindings.concat(train.getCurrentStation() == null ?
							"Not in a station" : "Station " +
							train.getCurrentStation().getStation_number()));

					passengerTextField.setText(train.getPassengers().toString().replaceAll("\\[",
							"").replaceAll("]", "").replaceAll(", ", "\n"));
				};

				observablePassengers.addListener(passengerListChangeListener);
			});

			train1.setId("Train_" + (i + 1));
			System.out.println(train1.getId());
			field.getChildren().add(train1);

		}
		vBox.getChildren().add(field);
		return vBox;
	}

	public void stopAllThreads()
	{
		for(int i = 0; i < trains.size(); i++)
			trains.get(i).stopTransition(true);
//		for (int i = 0; i < stations.size(); i++)
//			stations.get(i).setDaemon(true);

	}

	public void runAllThreads()
	{
		for(int i = 0; i < trains.size(); i++)
			trains.get(i).stopTransition(false);
//		for (int i = 0; i < stations.size(); i++)
//			stations.get(i).setDaemon(false);
	}
	public void terminateProgram()
	{
		stopAllThreads();
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

	public static void notifyPassengerListChangeListener(){
		if(passengerListChangeListener != null)
			synchronized(passengerListChangeListener){
				passengerListChangeListener.notify();
			}
	}
	public static PathTransition moveTrain(Train train1, int station_number) {

		ImageView train = (ImageView)((VBox)mainPane.getCenter()).getChildren().get(0).lookup("#Train_" +
																							  train1.getTrain_number());

		train1.setCurrentStation(null);
		Polyline polyline = new Polyline();
		polyline.setLayoutX(25);
		polyline.setLayoutY(25);

		if(train1.getNextStation() == null)
			polyline.getPoints().addAll(train.getX(), train.getY(),
					560.0       , train.getY(),
					560.0		, 161.0);
		else
			switch (train1.getNextStation().getStation_number()){
				case 2/*"560 161"*/: // Station 1
								polyline.getPoints().addAll(560.0, 161.0,
															559.0, 160.0,
															475.0, 70.0,
															474.0, 70.0);
								break;

				case 3/*"474  70"*/: // Station 2
								polyline.getPoints().addAll(474.0,  70.0,
															325.0,  70.0);
								break;

				case 4/*"325  70"*/: // Station 3
								polyline.getPoints().addAll(325.0,  70.0,
															175.0,  70.0);
								break;

				case 5/*"175  70"*/: // Station 4
								polyline.getPoints().addAll(175.0,  70.0,
															174.0,  71.0,
															100.0, 160.0,
															100.0, 161.0);
								break;

				case 6/*"100 161"*/: // Station 5
								polyline.getPoints().addAll(100.0, 161.0,
															175.0, 270.0,
															176.0, 270.0);
								break;

				case 7/*"176 270"*/: // Station 6
								polyline.getPoints().addAll(176.0, 270.0,
															325.0, 270.0);
								break;

				case 8/*"325 270"*/: // Station 7
								polyline.getPoints().addAll(325.0, 270.0,
															474.0, 270.0);
								break;
				case 1/*"??? ???"*/: // Station 8
								polyline.getPoints().addAll(474.0, 270.0,
															475.0, 269.0,
															560.0, 160.0,
															560.0, 159.0);
								break;

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
		transition.setDuration(Duration.seconds(4));
		transition.setPath(polyline);
		transition.setCycleCount(1);
		train1.setTransition(transition);
		transition.play();

		return transition;
	}
}
