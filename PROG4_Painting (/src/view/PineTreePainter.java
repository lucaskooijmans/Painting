package view;

import javafx.scene.paint.Color;

import model.Tree;

public class PineTreePainter extends TreePainter {

	private double x;
	private double y;
	private double sizeRatio;
	private double height;
	private double width;
	private double treeWidth;
	private double treeHeight;
	private double screenWidth;
	private double screenHeight;
	
	private Color color;
	
	public PineTreePainter() {
		
	}

	@Override
	public void paintTree(Tree tree, double screenWidth, double screenHeight) {
		this.screenWidth = screenWidth / 100;
		this.screenHeight = screenHeight / 100;
		x = tree.getRelX() * this.screenWidth;
		y = tree.getRelY() * this.screenHeight;

		sizeRatio = (20 + 3.6 * (tree.getRelY() - 50)) / 200;
		
		switch (tree.getSize()) {
		case S:
			height = (50 * sizeRatio);
			width = (5 * sizeRatio);
			color = Color.valueOf("#00FF78");
			break;
		case M:
			height = (80 * sizeRatio);
			width = (8 * sizeRatio);
			color = Color.valueOf("#00EA86");

			break;
		case L:
			height = (100 * sizeRatio);
			width = (10 * sizeRatio);
			color = Color.valueOf("#00C26F");
			break;
		case XL:
			height = (150 * sizeRatio);
			width = (15 * sizeRatio);
			color = Color.valueOf("#009656");

			break;
		case XXL:
			height = (200 * sizeRatio);
			width = (20 * sizeRatio);
			color = Color.valueOf("#006A3D");

			break;
		}

		x = tree.getRelX() * this.screenWidth;
		y = tree.getRelY() * this.screenHeight - height;
		treeWidth = width * 8;
		treeHeight = height * 0.75 * 2;
	}

	public Color getColor() {
		return color;
	}

	public double getX() {
		return x;
	}

	public double getY() {
		return y;
	}

	public double getHeight() {
		return height;
	}

	public double getWidth() {
		return width;
	}

	public double getTreeWidth() {
		return treeWidth;
	}

	public double getTreeHeight() {
		return treeHeight;
	}

}
