package view;

import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.BorderPane;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import javafx.scene.text.Font;
import javafx.scene.text.FontPosture;
import javafx.scene.text.FontWeight;
import model.Tree;
import model.World;

import java.io.*;

import controller.WorldController;

public class PaintingScene {

	private BorderPane root;
    private PaintingPane paintingPane;
    
    private Scene scene;
    
    private World world;
    private WorldController worldController;
    
    private Thread worldThread;
    
    private FileChooser fileChooser;
    private BufferedReader bufferedReader;

    private MenuBar menuBar;
    
    private Menu fileMenu;
    private Menu menu;
    private Menu autograph;
    private Menu movie;

    private MenuItem loadPainting;
    private MenuItem savePainting;
    private MenuItem exit;
    private MenuItem addLeafTree;
    private MenuItem addPineTree;
    private MenuItem add100Trees;
    private MenuItem clear;

    private RadioMenuItem arial;
    private RadioMenuItem courier;
    private RadioMenuItem helvetica;
    private RadioMenuItem timesNewRoman;
    
    private File file;
    private File targetFile;
    
    private FileWriter fw;
    private BufferedWriter bw;

    private ToggleGroup fonts;

    private CheckMenuItem play;

    private String errorMessage;
    private String line;
    private String[] part;
    private String type;
    private String size;
    
    private int relX;
    private int relY;
    
    private Alert alert;
    

    public PaintingScene(World world, Stage primaryStage, WorldController worldController) {
        this.world = world;
        this.worldController = worldController; // <MVC gedachte> = Een View object mag niet rechtstreeks wijzigen in een Model object
        this.paintingPane = new PaintingPane(world);
        this.worldThread = new Thread(world);
        paintingPane.repaint();
        
        root = new BorderPane();
        root.setCenter(paintingPane);

        fileChooser = new FileChooser();
        fileChooser.setInitialDirectory(new File("paintings/"));
        fileChooser.getExtensionFilters().add(new FileChooser.ExtensionFilter("Painting files", "*.painting"));
        errorMessage = "Can't load that file";

        createMenu();
        root.setTop(menuBar);

        scene = new Scene(root, 800, 630);
        primaryStage.setScene(scene);
        primaryStage.setTitle("Lucas Kooijmans - Painting");
        primaryStage.show();

        worldThread.start();
    }

    private void createMenu() {
        menuBar = new MenuBar();

        fileMenu = new Menu("File");

        loadPainting = new MenuItem("load painting...");
        savePainting = new MenuItem("save painting as...");
        exit = new MenuItem("exit");

        loadPainting.setOnAction(e -> getPainting());
        savePainting.setOnAction(e -> savePainting());
        exit.setOnAction(e -> System.exit(0)); // terminate JVM

        fileMenu.getItems().addAll(loadPainting, savePainting, exit);

        menu = new Menu("Tree");
        movie = new Menu("Movie");
        autograph = new Menu("Autograph font");

        addLeafTree = new MenuItem("add Leaf Tree");
        addLeafTree.setOnAction(e -> {
            worldController.addRandomTree("LEAF");
            paintingPane.repaint();
        });

        addPineTree = new MenuItem("add Pine Tree");
        addPineTree.setOnAction(e -> {
            worldController.addRandomTree("PINE");
            paintingPane.repaint();
        });

        add100Trees = new MenuItem("add 100 Trees");
        add100Trees.setOnAction(e -> {
        	
            for (int i = 0; i < 100; i++) {
                worldController.addRandomTree("random");
            }
            paintingPane.repaint();
        });

        clear = new MenuItem("clear all Trees");
        clear.setOnAction(e -> {
            worldController.clearTrees();
            paintingPane.repaint();
        });

        play = new CheckMenuItem("play");
        play.setOnAction(e -> {
            worldController.start();
        });

        arial = new RadioMenuItem("Arial");
        arial.setOnAction(e -> {
            paintingPane.setFont(Font.font("Arial", FontWeight.NORMAL, FontPosture.REGULAR, 20));
            paintingPane.repaint();
        });

        courier = new RadioMenuItem("Courier");
        courier.setOnAction(e -> {
            paintingPane.setFont(Font.font("Courier", FontWeight.NORMAL, FontPosture.REGULAR, 20));
            paintingPane.repaint();
        });

        helvetica = new RadioMenuItem("Helvetica");
        helvetica.setOnAction(e -> {
            paintingPane.setFont(Font.font("Helvetica", FontWeight.NORMAL, FontPosture.REGULAR, 20));
            paintingPane.repaint();
        });

        timesNewRoman = new RadioMenuItem("Times New Roman");
        timesNewRoman.setOnAction(e -> {
            paintingPane.setFont(Font.font("Times New Roman", FontWeight.NORMAL, FontPosture.REGULAR, 20));
            paintingPane.repaint();
        });

        fonts = new ToggleGroup();
        arial.setToggleGroup(fonts);
        courier.setToggleGroup(fonts);
        helvetica.setToggleGroup(fonts);
        timesNewRoman.setToggleGroup(fonts);

        autograph.getItems().addAll(arial, courier, helvetica, timesNewRoman);
        arial.setSelected(true);

        movie.getItems().add(play);

        menu.getItems().addAll(addLeafTree, addPineTree, add100Trees, clear);

        menuBar.getMenus().addAll(fileMenu, menu, autograph, movie);
    }
    
//    ZO 9-JUN-'24
//    Q: Moet de FileChooser in de view-layer of mag het ook in de class FileIO in de controller-layer.
//    A: beide implementaties zullen worden geaccepteerd.

    private void getPainting() {
        try {
            file = fileChooser.showOpenDialog(null);
            if (file != null) {
                bufferedReader = new BufferedReader(new FileReader(file));
                
                worldController.clearTrees();
                
                while ((line = bufferedReader.readLine()) != null) {
                    part = line.split(":");
                    if (!part[0].isEmpty() && !part[1].isEmpty() && !part[2].isEmpty() && !part[3].isEmpty()) {
                        type = part[0].toUpperCase();
                        size = part[1].toUpperCase();
                        relX = Integer.parseInt(part[2]);
                        relY = Integer.parseInt(part[3]);
                        if (type.matches("LEAF|PINE") && size.matches("S|M|L|XL|XXL") && relX >= 0 && relX <= 100 && relY >= 50 && relY <= 100) {
                            worldController.addTree(type, size, relX, relY);
                        } else {
                            showError(errorMessage);
                            worldController.clearTrees();
                            paintingPane.repaint();
                            return;
                        }
                    } else {
                        showError(errorMessage);
                        worldController.clearTrees();
                        paintingPane.repaint();
                        return;
                    }
                }
                paintingPane.repaint();
            }
        } catch (Exception e) {
            showError(errorMessage);
        }
    }

    private void savePainting() {
        targetFile = fileChooser.showSaveDialog(null);
        if (targetFile != null) {
            try {
                if (!targetFile.exists()) {
                    targetFile.createNewFile();
                }

                fw = new FileWriter(targetFile);
                bw = new BufferedWriter(fw);

                for (Tree t : world.getTrees()) {
                    bw.write(t.getType().toString().toLowerCase() + ":" + t.getSize() + ":" + t.getRelXint() + ":" + t.getRelYint());
                    bw.newLine();
                }
                bw.close();
            } catch (IOException e) {
                showError("Error saving the painting");
            }
        }
    }

    private void showError(String message) {
        alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle("Error");
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }

    public PaintingPane getPaintingPane() {
        return paintingPane;
    }
}
