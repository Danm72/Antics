package com.antics.data;

import com.antics.geometry.Point3D;
import com.antics.util.ConfigFile;

import java.util.LinkedList;

public class Tile {
	// Graphical location3D of the Tile
	public Point3D location3D = null;
	private int x;
	private int y;
	// Number of ants in the Tile
	private int antsInTile = 0;
	// Used for establishing what graphics to use
    private LinkedList<Integer> antTypes = new LinkedList<Integer>(); 
	private int currentForeGroundType = -1;
	private int currentBackGroundType = 0;
	private boolean hive = false;
	
	// Pheromones
	private int searchPheromone;
	private int foodPheromone;
	private int digPheromone;

	private int health; // For digging
	// public Pheromone[] pheromones;

	private int food;

	public Tile(Point3D location0, int x0, int y0) {
		this.location3D = location0;
		this.currentBackGroundType = 0;
		x = x0;
		y = y0;
		health = ((int) (Math.random() * 2)) + 1;
		if (x == ConfigFile.NEST_X && y == ConfigFile.NEST_Y)
			food = 50;
	}

	public Tile(Point3D location0, int x0, int y0, int type0) {
		this.location3D = location0;
		this.currentBackGroundType = type0;
		x = x0;
		y = y0;
		health = ((int) (Math.random() * 2)) + 2;
	}
	
	// Dig this Tile, for Hive
	public void setHive(){
		health = 0;
		setBackgroundType(1);	// dug
		hive = true;
	}
	
	public boolean isHive(){
		return hive;
	}
	
	// Attempt to dig Tile
	public boolean dig() {

		if(!dug()){
			addDigPheromone();

			if(health > 0)
				health -= health;

			if(health == 0){
				setBackgroundType(1);	// dug

				float foodCheck = (float) (Math.random() * 1);

				// 17 percent chance of finding food
				if (foodCheck < (float) 0.18) {
					//Log.d("food", "" + "This happen?");
					food = ((int) (Math.random() * 4)) + 8;
					currentForeGroundType = 1;
				}
			}
			return true;
		}


		return false;
	}

	// Check if tile has been cleared
	public boolean dug() {
		return currentBackGroundType != 0;
	}

	public void reset(){
		digPheromone = 0;
		searchPheromone = 0;
		foodPheromone = 0;
	}

	// Return the graphical location3D of the Tile
	public Point3D getLocation3D() {
		deteriate();	// cheeky 
		return this.location3D;
	}

 	// X for Tile top left of self
	public int topLeftX(){
		return x-1;
	}
	
	// Y for Tile top left of self
	public int topLeftY(){
		if(x % 2 == 0)
			return y-1;
		return y;
	}

	// X for Tile top right of self
	public int topRightX(){
		return x-1;
	}
	
	// Y for Tile top right of self
	public int topRightY(){
		if(x % 2 == 0)
			return y;
		return y+1;
	}
	
	// X for Tile bot left of self
 	public int botLeftX(){
 		return x+1;
 	}
 	
 	// Y for Tile bot left of self
 	public int botLeftY(){
 		if(x % 2 == 0)
 			return y-1;
 		return y;
 	}

	// X for Tile bot right of self
 	public int botRightX(){
 		return x+1;
 	}

	// Y for Tile bot right of self
 	public int botRightY(){
 		if(x % 2 == 0)
 			return y;
 		return y+1;
 	}

	// Check if top left Tile is dug
	public boolean topLeftDug(){
		if(ConfigFile.inBounds(topLeftX(), topLeftY()))
			return ConfigFile.board.tiles[topLeftX()][topLeftY()].dug();
		return false;
	}
	
	// Check if left Tile is dug
	public boolean leftDug(){
		if(ConfigFile.inBounds(x, y-1))
			return ConfigFile.board.tiles[x][y-1].dug();
		return false;
	}
	
	// Check if bot left Tile is dug
	public boolean botLeftDug(){
		if(ConfigFile.inBounds(botLeftX(), botLeftY()))
			return ConfigFile.board.tiles[botLeftX()][botLeftY()].dug();
		return false;
	}
	
	// Check if bot right Tile is dug
	public boolean topRightDug(){
		if(ConfigFile.inBounds(topRightX(), topRightY()))
			return ConfigFile.board.tiles[topRightX()][topRightY()].dug();
		return false;
	}
	
	// Check if right Tile is dug
	public boolean rightDug(){
		if(ConfigFile.inBounds(x, y+1))
			return ConfigFile.board.tiles[x][y+1].dug();
		return false;
	}
	
	// Check if bot right Tile is dug
	public boolean botRightDug(){
		if(ConfigFile.inBounds(botRightX(), botRightY()))
			return ConfigFile.board.tiles[botRightX()][botRightY()].dug();
		return false;
	}
    
    // Most recent Ant
    public int recentAnt(){
        return antTypes.getFirst();
    }
   
	// Add ant to tile
	public void newAnt(int type){
		antsInTile++;
		currentForeGroundType = 0;
		antTypes.addFirst(type);
	}
	
	// Ant left the tile
	public void antVacateTile(int type){
		if(antsInTile > 0){
	        antsInTile -= 1;
	        remove(type);
	        if(antsInTile > 0)
	            currentForeGroundType = 0;
	        else if(food > 0)
	            setFood();
	        else
	            setEmpty();
		}
	}
	
	// Remove specific Ant antType from list
	private void remove(int type){
		for(int i = 0; i < antTypes.size(); i++)
			if(antTypes.get(i) == type){
				antTypes.remove(i);
				i = antTypes.size();
			}
	}

	// Set tile foreground to show food
	public void setFood() {
		currentForeGroundType = 1;
	}
	
	// Nothing in the tile
	public void setEmpty(){
		currentForeGroundType = -1;
	}
	
	// Temporary food placing method
	public void putFood(){
		food += 1;
	}

	// Take a portion of food from the tile
	public void takeFood() {
		if(food > 0)
			food -= 1;
	}
	
	// Amount of food
	public int food(){
		return food;
	}
	
	// Check whether there is food in the tile
	public boolean hasFood(){
		return food > 0;
	}
	
	// Increase dig pheromone
	public void addDigPheromone(){
		digPheromone += 73;
	}
	
	// Increase search pheromone
	public void addSearchPheromone(){
		searchPheromone += 73;
	}
	
	// Increase food pheromone
	public void addFoodPheromone(){
		foodPheromone += 73;
	}
	
	// Pheromones deteriate every cycle
	public void deteriate(){
		if(searchPheromone > 0){
			searchPheromone -= 1;
		}
		if(foodPheromone > 0){
			foodPheromone -= 1;
		}
		if(digPheromone > 0){
			digPheromone -= 1;
		}
	}
	
	// Return strength of dig pheromone
	public int digPheromone(){
		return digPheromone;
	}
	
	// Return strength of search pheromone
	public int searchPheromone(){
		return searchPheromone;
	}
	
	// Return strength of food pheromone 
	public int foodPheromone(){
		return foodPheromone;
	}
	
	// Is there dig pheromone?
	public boolean hasDigPheromone(){
		return digPheromone > 0;
	}
	
	// Is there search pheromone?
	public boolean hasSearchPheromone(){
		return searchPheromone > 0;
	}
	
	// Is there pheromones in this tile?
	public boolean hasFoodPheromone(){
		return foodPheromone > 0;
	}
	
	// Set value for background antType
	public void setBackgroundType(int bt){
		currentBackGroundType = bt;
	}
	
	// Return current background value
	public int currentBackground(){
		return currentBackGroundType;
	}
	
	// Return current foreground value
	public int currentForeground(){
		return currentForeGroundType;
	}}
