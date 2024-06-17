package model;

public class Tree {

	private TreeSize size;
	private TreeType type;
	
	private double relX;
	private double relY;
	private double speed;

	public Tree(String size, String type, double relX, double relY) {
		this.size = TreeSize.valueOf(size); // using valueOf to set the enums
		this.type = TreeType.valueOf(type);
		this.relX = relX;
		this.relY = relY;
	}
	
	public void move() {
		// using a formula to set the speed
		// the bigger relY is the faster they go
		speed = (20 + 3.6 * (getRelY() - 50)) / 200; // - 50 to slow it down to 24 fps, / 200 to stop it from spacing
		relX = relX + speed * 1.2; // * 1.2 to match Ger's demo
		if (relX > 110) { 
			relX = -10; // come back, set to left
		}
	}

	public TreeSize getSize() {
		return size;
	}

	public void setSize(TreeSize size) {
		this.size = size;
	}

	public TreeType getType() {
		return type;
	}

	public void setType(TreeType type) {
		this.type = type;
	}

	public double getRelX() {
		return relX;
	}

	public int getRelXint() {
		return (int) Math.round(relX); // Math.round to make int from double
	}

	public void setRelX(double relX) {
		this.relX = relX;
	}

	public double getRelY() {
		return relY;
	}

	public int getRelYint() {
		return (int) Math.round(relY);
	}

	public void setRelY(double relY) {
		this.relY = relY;
	}

}
