package model;

import java.util.ArrayList;
import java.util.Observable;
import java.util.Random;

public class World extends Observable implements Runnable {

	private static final int FPS_24 = 42; // 42 ms = 24 fps

	private ArrayList<Tree> trees;
	
	private Random random;
	
	private boolean play;
	
	private String size;
	private String type;
	
	private int relX;
	private int relY;
	private int randomInt;

	public World() {
		play = false;
		random = new Random();
		trees = new ArrayList<Tree>();
	}

	@Override
	public void run() { // method that runs in a separate thread
		while (true) {
			// if play is true the trees move
			if (play) {
				trees.forEach(Tree::move); // move each tree
				this.setChanged(); // marks this Observable as having been changed
				this.notifyObservers(); // notifies all observers of the change (repaint)
				try {
					Thread.sleep(FPS_24);
				} 
				catch (InterruptedException e) {
				}
			} else {
				try {
					Thread.sleep(0); // 0 ms = 0 fps (stop playing)
				} 
				catch (InterruptedException e) {
				}
			}
		}
	}

	public void addRandomTree(String treeType) { // geen enum gebruikt want "random"
		size = "";
		type = treeType;
		relX = 0;
		relY = 0;
		randomInt = random.nextInt(2);
		
		// give tree a random type
		if (treeType.equals("random")) {
			if (randomInt == 0) {
				type = "PINE";
			} else {
				type = "LEAF";
			}
		}
		else if (treeType.equals("PINE")) {
			type = "PINE";
		}
		else if (treeType.equals("LEAF")) {
			type = "LEAF";
		}

		// set the size
		randomInt = random.nextInt(5);
		if (randomInt == 0) {
			size = "S";
		} else if (randomInt == 1) {
			size = "M";
		} else if (randomInt == 2) {
			size = "L";
		} else if (randomInt == 3) {
			size = "XL";
		} else if (randomInt == 4) {
			size = "XXL";
		}

		randomInt = random.nextInt(101); // relX 0-100
		relX = randomInt;

		randomInt = random.nextInt(51);
		// + 50 because the lowest possible rely = 50
		relY = randomInt + 50;

		trees.add(new Tree(size, type, relX, relY));

	}

	public void addTree(String type, String size, int relX, int relY) { // for getPainting
		trees.add(new Tree(size, type, relX, relY));
	}

	// remove all the trees
	public void clearTrees() {
		trees.clear();
	}

	// start or stop the movie
	public void start() {
		if(trees.isEmpty()) { // start app > play > add tree resulted in error, this fixes it
			clearTrees();
		}
		
		if (play == true) {
			play = false;
		} else {
			play = true;
		}
	}

	public ArrayList<Tree> getTrees() {
		return trees;
	}	
	
	public boolean getPlay() {
		return play;
	}
	
}
