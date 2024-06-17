package controller;

import model.World;

public class WorldController {
	private World world;
	
	public WorldController(World world) {
		this.world = world;
	}
	
	public void clearTrees() {
		world.clearTrees();
	}
	
	public void addRandomTree(String treeType) {
		world.addRandomTree(treeType);
	}
	
	public void start() {
		world.start();
	}
	
	public void addTree(String type, String size, int relX, int relY) {
		world.addTree(type, size, relX, relY);
	}
	
}
