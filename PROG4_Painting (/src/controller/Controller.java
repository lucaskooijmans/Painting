package controller;

import javafx.stage.Stage;
import model.World;
import view.PaintingScene;

public class Controller {

    private World world;
    private PaintingScene paintingScene;
    private WorldController worldController;
    
    public Controller(Stage primaryStage) {
    	world = new World();
    	worldController = new WorldController(world);
    	
        paintingScene = new PaintingScene(world, primaryStage, worldController);

        // add observer to observable
        world.addObserver((observable, arg) -> paintingScene.getPaintingPane().repaint());
    }

}
