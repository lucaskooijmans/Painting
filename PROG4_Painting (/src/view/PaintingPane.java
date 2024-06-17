package view;

import java.util.Observable;
import java.util.Observer;

import javafx.application.Platform;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Arc;
import javafx.scene.shape.ArcType;
import javafx.scene.shape.Rectangle;
import javafx.scene.shape.Circle;
import javafx.scene.text.Font;
import javafx.scene.text.FontPosture;
import javafx.scene.text.FontWeight;
import javafx.scene.text.Text;
import model.Tree;
import model.World;

public class PaintingPane extends Pane implements Observer {
	private World world;
	
	private LeafTreePainter leafTreePainter;
	private PineTreePainter pineTreePainter;
	
	private Rectangle skyRect;
	private Rectangle groundRect;
	private Rectangle trunk;
	
	private Circle leaves;
	
	private Arc pineLeavesBorder;
	private Arc pineLeaves;
	
	private String name;
	
	private Font font;
	
	private Text text;
	
	private int fontSize;
	
	private double width;
	private double height;
	private double textWidth;
	private double xPosition;
	private double yPosition;
	
	private Color sky;
	private Color ground;

	public PaintingPane(World world) {
		this.setPrefSize(800, 600);
		leafTreePainter = new LeafTreePainter();
		pineTreePainter = new PineTreePainter();
		this.world = world;
		name = "Lucas Kooijmans";
		fontSize = 20;
		font = Font.font("Arial", FontWeight.NORMAL, FontPosture.REGULAR, fontSize);
		sky = Color.rgb(208, 255, 244);
		ground = Color.rgb(207, 143, 7);

		// resizing
		this.widthProperty().addListener(evt -> repaint());
		this.heightProperty().addListener(evt -> repaint());	
	}

	public void repaint() {
		if (!Platform.isFxApplicationThread()) {
	        Platform.runLater(this::repaint);
	        return;
	    }
		
		this.getChildren().clear();

		// get width and height of this pane
		width = this.getWidth();
		height = this.getHeight();

		// Painting the sky
		skyRect = new Rectangle(0, 0, width, height / 2);
		skyRect.setFill(sky);
		this.getChildren().add(skyRect);

		// Painting the ground
		groundRect = new Rectangle(0, height / 2, width, height / 2);
		groundRect.setFill(ground);
		this.getChildren().add(groundRect);

		sortArrayList();

		for (Tree t : world.getTrees()) {
			if (t.getType().name().equals("LEAF")) {
				leafTreePainter.paintTree(t, width, height);

				// Painting the trunk of the tree
				trunk = new Rectangle(leafTreePainter.getX(),
						leafTreePainter.getY() + leafTreePainter.getHeight() / 2, leafTreePainter.getWidth(),
						leafTreePainter.getHeight() / 2);
				trunk.setFill(Color.rgb(127, 106, 31));
				trunk.setStroke(Color.BLACK);
				this.getChildren().add(trunk);

				// Painting the leaves
				leaves = new Circle(leafTreePainter.getX() + leafTreePainter.getWidth() / 2,
						leafTreePainter.getY() + leafTreePainter.getHeight() / 4, leafTreePainter.getTreeWidth() / 2);
				leaves.setFill(leafTreePainter.getColor());
				leaves.setStroke(Color.BLACK);
				this.getChildren().add(leaves);
			}

			if (t.getType().name().equals("PINE")) {
				pineTreePainter.paintTree(t, width, height);

				// Painting the trunk of the tree
				trunk = new Rectangle(pineTreePainter.getX(),
						pineTreePainter.getY() + pineTreePainter.getHeight() / 2, pineTreePainter.getWidth(),
						pineTreePainter.getHeight() / 2);
				trunk.setFill(Color.rgb(127, 106, 31));
				trunk.setStroke(Color.BLACK);
				this.getChildren().add(trunk);

				// Painting the pine leaves
				pineLeavesBorder = new Arc(pineTreePainter.getX() + pineTreePainter.getWidth() / 2,
						pineTreePainter.getY(), pineTreePainter.getTreeWidth() / 2, pineTreePainter.getTreeHeight() / 2,
						-120, 60);
				pineLeavesBorder.setFill(Color.TRANSPARENT);
				pineLeavesBorder.setStroke(Color.BLACK);
				pineLeavesBorder.setType(ArcType.ROUND);
				this.getChildren().add(pineLeavesBorder);

				pineLeaves = new Arc(pineTreePainter.getX() + pineTreePainter.getWidth() / 2,
						pineTreePainter.getY(), pineTreePainter.getTreeWidth() / 2,
						pineTreePainter.getTreeHeight() / 2, -120, 60);
				pineLeaves.setFill(pineTreePainter.getColor());
				pineLeaves.setType(ArcType.ROUND);
				this.getChildren().add(pineLeaves);
			}
		}

		// Drawing the name
		text = new Text(name);
		text.setFont(font);
		text.setFill(Color.BLACK);
		textWidth = text.getLayoutBounds().getWidth();
		xPosition = width - 10 - textWidth;
		yPosition = height - 10;
		text.setX(xPosition);
		text.setY(yPosition);
		this.getChildren().add(text);
	}

	public void sortArrayList() {
		// Sort the trees by relY in ascending order using ArrayList.sort() with a lambda expression for the comparator
		world.getTrees().sort((tree1, tree2) -> Double.compare(tree1.getRelY(), tree2.getRelY()));
	}

	public void setFont(Font font) {
		this.font = font;
	}

	@Override
	public void update(Observable arg0, Object arg1) {
		repaint();
	}
}
