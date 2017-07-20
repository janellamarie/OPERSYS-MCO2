package cal_train;

import javafx.animation.PathTransition;
import javafx.application.Application;
import javafx.application.Platform;
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

import java.util.concurrent.Semaphore;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

public class Test extends Application {

    /** Global Variables **/

    public static Station stations[];
    public static Train trains[];

    public static Semaphore semaphore;
    public static Semaphore mutex;
    public static Lock station_lock = new ReentrantLock();
    public static Condition trainArrival = station_lock.newCondition();

    /** GUI **/
    private boolean semaphoreMachine = true;        //use this to check if the user wants a semaphore machine or lock(?)

    private BorderPane mainPane;
    private TextField peopleTextField,
            trainTextField,
            stationTextField,
            seatsTextField;
    private ChoiceBox<Integer> destinationChoiceBox;


    public static void main(String[] args){
        stations = new Station[8];
        trains = new Train[15];

        semaphore = new Semaphore(0);
        mutex = new Semaphore(1);

//        CalTrain calTrain = new CalTrain();
//        calTrain.start();

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

        mainPane.setTop(initTopBar());
        mainPane.setRight(initRightVBox());
        mainPane.setCenter(initCenterVBox());
    }

    public MenuBar initTopBar(){

        //Menu
        Menu menu = new Menu("_Menu");
        Menu machine = new Menu("_Machine");

        //Initializing menu items
        MenuItem exit    = new MenuItem("_Exit");
        MenuItem about   = new MenuItem("_About");
        MenuItem semaphore   = new MenuItem("_Semaphore");
        MenuItem lock   = new MenuItem("_Lock");

        machine.getItems().addAll(semaphore, lock);
        menu.getItems().addAll(machine, new SeparatorMenuItem(), about, exit);

        MenuBar menuBar = new MenuBar();
        menuBar.getMenus().addAll(menu);

        exit.setOnAction(e -> terminateProgram());

        semaphore.setOnAction(e -> {
            mainPane.setRight(initRightVBox());
            mainPane.setCenter(initCenterVBox());
        });

        lock.setOnAction(e -> {
            mainPane.setRight(initRightVBox());
            mainPane.setCenter(initCenterVBox());
        });

        return menuBar;
    }

    public VBox initRightVBox()
    {
        VBox vBox = new VBox();
        vBox.setId("vBoxRight");

        GridPane gridPane = new GridPane();
        gridPane.setId("rightGridPane");

        //Station Number
        ImageView stationIcon = new ImageView("images/station.png");
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

        //Seats for the train
        ImageView seatsIcon = new ImageView("images/trainSubIcon2.png");    // Update trainIcon
        seatsIcon.setFitHeight(25);
        seatsIcon.setFitWidth(25);
        seatsIcon.setPreserveRatio(true);

        seatsTextField = new TextField();
        seatsTextField.setDisable(true);
        seatsTextField.setPromptText("Train available seats / seats");

        GridPane.setConstraints(stationIcon, 0, 0);
        GridPane.setConstraints(stationTextField, 1, 0);
        GridPane.setConstraints(trainIcon, 0, 1);
        GridPane.setConstraints(trainTextField, 1, 1);
        GridPane.setConstraints(seatsIcon, 0, 2);
        GridPane.setConstraints(seatsTextField, 1, 2);
        gridPane.getChildren().addAll(stationIcon, stationTextField,
                                      trainIcon, trainTextField,
                                      seatsIcon, seatsTextField);

        //People
        ImageView peopleIcon = new ImageView("images/peopleIcon2.png");
        peopleIcon.setFitHeight(25);
        peopleIcon.setFitWidth(25);
        peopleIcon.setPreserveRatio(true);

        Spinner<Integer> spinner = new Spinner<Integer>(0, Integer.MAX_VALUE, 0, 1);
        peopleTextField = new TextField();
        peopleTextField.setDisable(true);
        peopleTextField.setPromptText("Number of People");

        //Destination
        ImageView destinationIcon = new ImageView("images/peopleIcon2.png"); // Update destinationIcon
        destinationIcon.setFitHeight(25);
        destinationIcon.setFitWidth(25);
        destinationIcon.setPreserveRatio(true);
        destinationChoiceBox = new ChoiceBox<Integer>();
        destinationChoiceBox.getItems().addAll(0, 1, 2, 3, 4, 5, 6, 7);

        GridPane.setConstraints(peopleIcon, 0, 3);
        GridPane.setConstraints(spinner, 1, 3);
        GridPane.setConstraints(destinationIcon, 0, 4);
        GridPane.setConstraints(destinationChoiceBox, 1, 4);
        gridPane.getChildren().addAll(spinner, peopleIcon, destinationIcon, destinationChoiceBox);

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
            ImageView trainStation = new ImageView("images/station.png");
            trainStation.setId("" + i);
            trainStation.setFitHeight(100);
            trainStation.setFitWidth(100);
            trainStation.setPreserveRatio(true);

            trainStation.setOnMouseClicked(e -> {
                stationTextField.setText(((ImageView)e.getSource()).getId());
                //Update Train number
                trainTextField.setText("Train number");
                System.out.println(((ImageView)e.getSource()).getId());   //UPDATE RIGHT VBOX
            });

            trainStation.setX(150 * (i + 1));
            trainStation.setY(0);

            if(i > 2){
                trainStation.setX(150 * (i - 3 + 1));
                trainStation.setY(300);
            }

            if(i >= 6){
                trainStation.setX(10);
                trainStation.setY(150);

                if(i == 7)
                    trainStation.setX(600);
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
                train1.setX(225);
                train1.setY(425);
            } else if(i < 4){
                train1.setX(225 + (75 * i));
                train1.setY(425);
            } else if(i < 8){
                train1.setX(225 + (75 * (i - 4)));
                train1.setY(450);
            } else if(i < 12){
                train1.setX(225 + (75 * (i - 8)));
                train1.setY(475);
            } else if(i < 16){
                train1.setX(225 + (75 * (i - 12)));
                train1.setY(500);
            }

            train1.setPreserveRatio(true);

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

            if(i % 4 == 0)
                field.getChildren().add(polyline);

            train1.setOnMouseClicked(e -> {
                trainTextField.setText(((ImageView)e.getSource()).getId());
                seatsTextField.setText("A/T Seats");
                destinationChoiceBox.setValue(0);
                destinationChoiceBox.setDisable(true);
                System.out.println(((ImageView)e.getSource()).getX() + " " + ((ImageView)e.getSource()).getTranslateX());
                PathTransition transition = new PathTransition();
                transition.setOrientation(PathTransition.OrientationType.ORTHOGONAL_TO_TANGENT);
                transition.setNode((ImageView)e.getSource());
                transition.setDuration(Duration.seconds(10));
                transition.setPath(polyline);
                transition.setCycleCount(1);
                transition.play();
            });

            train1.setId("" + i);

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