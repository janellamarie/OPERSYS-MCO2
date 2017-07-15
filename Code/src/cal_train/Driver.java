package cal_train;

import java.util.concurrent.Semaphore;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

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

public class Driver extends Application {

	/** Global Variables **/
	
	public static Station stations[];
	public static Train trains[];
	
	public static Semaphore semaphore;
	public static Semaphore mutex;
	public static Lock station_lock = new ReentrantLock();
	public static Condition trainArrival = station_lock.newCondition();
	
	/** GUI **/

	private BorderPane mainPane;
	private TextField peopleTextField,
			  trainTextField;
	
	public static void main(String[] args){
		
		stations = new Station[8]; 
		trains = new Train[15];
		
		semaphore = new Semaphore(0);
		mutex = new Semaphore(1);
		
		CalTrain calTrain = new CalTrain();
		calTrain.start();	
		
		launch(args);
	}
	
	@Override
	public void start(Stage primaryStage) {
		
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

		GridPane gridPane = new GridPane();
		gridPane.setId("rightGridPane");

		//Train Number
		ImageView trainIcon = new ImageView("images/trainSubIcon.png");
		trainIcon.setFitHeight(50);
		trainIcon.setFitWidth(50);
		trainIcon.setPreserveRatio(true);

		trainTextField = new TextField();
		trainTextField.setDisable(true);
		trainTextField.setPromptText("Train Number");

		GridPane.setConstraints(trainIcon, 0, 0);
		GridPane.setConstraints(trainTextField, 1, 0);
		gridPane.getChildren().addAll(trainIcon, trainTextField);

		//People
		ImageView peopleIcon = new ImageView("images/peopleIcon.png");
		peopleIcon.setFitHeight(50);
		peopleIcon.setFitWidth(50);
		peopleIcon.setPreserveRatio(true);

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

	public VBox initCenterVBox()
	{
		VBox vBox = new VBox();
		vBox.setId("vBoxCenter");

		Pane field = new Pane();
		field.setId("centerPane");

		for(int i = 0; i < 8; i++){
			ImageView peopleIcon = new ImageView("images/station.png");
			peopleIcon.setFitHeight(100);
			peopleIcon.setFitWidth(100);
			peopleIcon.setPreserveRatio(true);

			peopleIcon.setOnMouseClicked(e -> {
				System.out.println("WA");
		    	});

			peopleIcon.setLayoutX(150 * (i + 1));
			peopleIcon.setLayoutY(0);

			if(i > 2){
				peopleIcon.setLayoutX(150 * (i - 3 + 1));
				peopleIcon.setLayoutY(300);
			}

			if(i >= 6){
				peopleIcon.setLayoutX(10);
				peopleIcon.setLayoutY(150);

				if(i == 7)
					peopleIcon.setLayoutX(150*4);
			}

			field.getChildren().add(peopleIcon);
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
