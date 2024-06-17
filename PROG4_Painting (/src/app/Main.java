package app;


import controller.Controller;
import javafx.application.Application;
import javafx.stage.Stage;

public class Main extends Application {
	
	Controller controller;
	
    public static void main(String[] args) {
        launch(args);
    }

	@Override
	public void start(Stage primaryStage) throws Exception {
		controller = new Controller(primaryStage);
	}
}
