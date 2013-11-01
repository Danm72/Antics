package com.antics.ant;

import com.antics.data.Board;

/*
 * 
 * Hungry + no food -> Follow search pheromone back to nest
 * 
 * Hungry + has food/standing on food -> Eat
 * 
 * No food + No trail -> Builder
 * 
 * No food + On trail -> Follow trail + Lay search pheromone.
 * 
 * Find food -> pick it up + turn around
 * 
 * Carrying food -> follow search trail back to nest + lay Food Pheromone
 * 
 * Home + With food -> Drop food
 * 
 */ 

/*
 * The Gatherer class is responsible for collecting food and
 * bringing it home to the nest.
 * It will dynamically become a Builder if it has no inclination 
 * of where food may be.
 */
public class Gatherer extends Ant {
	
	// Used when converting an Ant to Gatherer
	public Gatherer(Board board, Ant ant){
        super(board);
		this.health = ant.getHealth();
		this.antType = 1;
		this.hasFood = ant.hasFood();
		this.hasDirt = ant.hasDirt();
		this.hunger = ant.hungerLevel();
		this.positionX = ant.getPositionX();
		this.positionY = ant.getPositionY();
		this.lastX = ant.getLastYPosition();
		this.lastY = ant.getLastXPosition();
		board.tiles[this.positionX][this.positionY].antVacateTile(ant.antType());
		board.tiles[this.positionX][this.positionY].newAnt(this.antType);
	}
	
	public Gatherer(Board board,int x0, int y0) {
        super(board);
		positionX = x0;
		positionY = y0;
		lastX = x0;
		lastY = y0;
		// Survive for 250 - 400 steps
		health = ((int) (Math.random() * 150)) + 350;
		// < 20 and Ant gets hungry
		hunger = 140;
		hasFood = false;
		hasDirt = false;
		antType = 1;
		board.tiles[this.positionX][this.positionY].setBackgroundType(1);
		board.tiles[this.positionX][this.positionY].newAnt(antType());
	}

	@Override
	public boolean action() {
		
		if(hunger > 0){
			hunger--;
			health--;
		}
		else
			health -= 2;
		
		if(hungry()){
			if(hasFood || foundFood())
				eat();
			else
				returnToNest();
		}
		else if(atNest() && hasFood){
			dropFood();
			//moveLast();
    	}
    	else if(!hasFood){
    		if(foundFood() && !atNest()){
	    		pickFood();
	    		moveLast();
	    	}
	    	else if(checkForFood())
	    		return false;
	    	else if(smellFoodPheromone()){
	    		int tmpX = positionX;
	    		int tmpY = positionY;
	    		followTrail();
	    		if(tmpX == positionX && tmpY == positionY)
	    			return true;
	    	}
	    	else{
	    		// No food trail, so go dig
	    		return true;
	    	}
    	}
    	else
    		returnToNest();
		
		return false;
	}
	
	/*
     * Determine pheromone trail to follow, according to
     * how strong they are.
     * 
     */
    protected void followTrail(){
    	int moveX = -1;
    	int moveY = -1;
    	
    	// Left
    	if(notBackwards(positionX, positionY -1)){
	    	if(positionY > 0 && board.tiles[positionX][positionY -1] != null
	    			&& board.tiles[positionX][positionY -1].hasFoodPheromone() ){
	    		moveX = positionX;
	    		moveY = positionY -1;
	    	}
    	}		
    	
    	// Right
    	if(notBackwards(positionX, positionY +1)){
	    	if(positionY < board.width && board.tiles[positionX][positionY +1] != null
	    			&& board.tiles[positionX][positionY +1].hasFoodPheromone() ){
	    		if(moveX + moveY > -2){
	    			
	    			// This may have to involve probability *
	    			if(board.tiles[moveX][moveY].foodPheromone()
	    					>= board.tiles[positionX][positionY +1].foodPheromone() ){
	    				moveX = positionX;
	    				moveY = positionY +1;
	    			}
	    		}
	    		else{
		    		moveX = positionX;
		    		moveY = positionY +1;
	    		}
	    	}
    	}
    	
    	if(positionX % 2 == 0){
    		if(positionY > 0){
    			if(notBackwards(positionX -1, positionY -1)){
		    		// Top Left
		    		if(positionX > 0 && board.tiles[positionX -1][positionY -1] != null
		    			&& board.tiles[positionX -1][positionY -1].hasFoodPheromone() ){
		    			if(moveX + moveY > -2){
		    				if(board.tiles[moveX][moveY].foodPheromone()
		    						>= board.tiles[positionX -1][positionY -1].foodPheromone() ) {
		    					moveX = positionX -1;
		    					moveY = positionY -1;
		    				}
		    			}
		    			else{
		    				moveX = positionX -1;
		    				moveY = positionY -1;
		    			}
		    		}
    			}
    			
    			if(notBackwards(positionX +1, positionY -1)){
		    		// Bot Left
		    		if(positionX < board.height && board.tiles[positionX +1][positionY -1] != null
		    			&& board.tiles[positionX +1][positionY -1].hasFoodPheromone() ){
		    			if(moveX + moveY > -2){
		    				if(board.tiles[moveX][moveY].foodPheromone()
		    						>= board.tiles[positionX +1][positionY -1].foodPheromone() ){
		    					moveX = positionX +1;
		    					moveY = positionY -1;
		    				}
		    			}
		    			else{
		    				moveX = positionX +1;
		    				moveY = positionY -1;
		    			}
		    		}
    			}
    		}	
    		
    		// Top Right
    		if(notBackwards(positionX -1, positionY)){
	    		if(positionX > 0 && board.tiles[positionX -1][positionY] != null
	    			&& board.tiles[positionX -1][positionY].hasFoodPheromone() ){
	    			if(moveX + moveY > -2){
	    				if(board.tiles[moveX][moveY].foodPheromone()
	    						>= board.tiles[positionX -1][positionY].foodPheromone() ){
	    					moveX = positionX -1;
	    					moveY = positionY;
	    				}
	    			}
	    			else{
	    				moveX = positionX -1;
	    				moveY = positionY;
	    			}
	    		}
    		}
    		
    		if(notBackwards(positionX +1, positionY)){
	    		// Bot Right
	    		if(positionX < board.height && board.tiles[positionX +1][positionY] != null
	    			&& board.tiles[positionX +1][positionY].hasFoodPheromone() ){
	    			if(moveX + moveY > -2){
	    				if(board.tiles[moveX][moveY].foodPheromone()
	    						>= board.tiles[positionX +1][positionY].foodPheromone() ){
	    					moveX = positionX +1;
	    					moveY = positionY;
	    				}
	    			}
	    			else{
	    				moveX = positionX +1;
	    				moveY = positionY;
	    			}
	    		}
    		}
    	}
    	else{
			if(notBackwards(positionX -1, positionY)){
	    		// Top Left
	    		if(positionX > 0 && board.tiles[positionX -1][positionY] != null
	    			&& board.tiles[positionX -1][positionY].hasFoodPheromone() ){
	    			if(moveX + moveY > -2){
	    				if(board.tiles[moveX][moveY].foodPheromone()
	    						>= board.tiles[positionX -1][positionY].foodPheromone() ){
	    					moveX = positionX -1;
	    					moveY = positionY;
	    				}
	    			}
	    			else{
	    				moveX = positionX -1;
	    				moveY = positionY;
	    			}
	    		}
			}
			
			if(notBackwards(positionX +1, positionY)){
	    		// Bot Left
	    		if(positionX < board.height && board.tiles[positionX +1][positionY] != null
	    			&& board.tiles[positionX +1][positionY].hasFoodPheromone() ){
	    			if(moveX + moveY > -2){
	    				if(board.tiles[moveX][moveY].foodPheromone()
	    						>= board.tiles[positionX +1][positionY].foodPheromone() ){
	    					moveX = positionX +1;
	    					moveY = positionY;
	    				}
	    			}
	    			else{
	    				moveX = positionX +1;
	    				moveY = positionY;
	    			}
	    		}
			}
			
			if(positionY < board.width-1){
	    		if(notBackwards(positionX -1, positionY +1)){
		    		// Top Right
		    		if(positionX > 0 && board.tiles[positionX -1][positionY +1] != null
		    			&& board.tiles[positionX -1][positionY +1].hasFoodPheromone() ){
		    			if(moveX + moveY > -2){
		    				if(board.tiles[moveX][moveY].foodPheromone()
		    						>= board.tiles[positionX -1][positionY +1].foodPheromone() ){
		    					moveX = positionX -1;
		    					moveY = positionY +1;
		    				}
		    			}
		    			else{
		    				moveX = positionX -1;
		    				moveY = positionY +1;
		    			}
		    		}
	    		}
	    		
	    		if(notBackwards(positionX +1, positionY +1)){
		    		// Bot Right
		    		if(positionX < board.height && board.tiles[positionX +1][positionY +1] != null
		    			&& board.tiles[positionX +1][positionY +1].hasFoodPheromone() ){
		    			if(moveX + moveY > -2){
		    				if(board.tiles[moveX][moveY].foodPheromone()
		    						>= board.tiles[positionX +1][positionY +1].foodPheromone() ){
		    					moveX = positionX +1;
		    					moveY = positionY +1;
		    				}
		    			}
		    			else{
		    				moveX = positionX +1;
		    				moveY = positionY +1;
		    			}
		    		}
	    		}
			}
    			
    	}

		if(moveX + moveY != -2)
			move(moveX, moveY);
    }

    // Move randomly
	public void wander() {
		float decision;
		
		int attempts = 60;
		while (attempts > 0) {
			decision = (float) (Math.random() * 1);
			if (decision < 0.1666) {
				if(notBackwards(positionX, positionY -1)){
					if(moveLeft())
						return;
				}
			}
			else if (decision > 0.1666 && decision < 0.3332) {
				if(notBackwards(positionX, positionY +1)){
					if(moveRight())
						return;
				}
			}
			else if (decision > 0.3332f && decision < 0.4998f) {
				if(notBackwards(positionX -1,
						board.tiles[positionX][positionY].topLeftY())){
					if(moveTopLeft())
						return;
				}
			}
			else if (decision > 0.4998f && decision < 0.6664f) {
				if(notBackwards(positionX -1,
						board.tiles[positionX][positionY].topRightY())){
					if(moveTopRight())
						return;
				}
			}
			else if (decision > 0.6664f && decision < 0.8333f) {
				if(notBackwards(positionX +1,
						board.tiles[positionX][positionY].botLeftY())){
					if(moveBotLeft())
						return;
				}
			}
			else {
				if(notBackwards(positionX +1,
						board.tiles[positionX][positionY].botRightY())){
					if(moveBotRight())
						return;
				}
			}
			attempts--;
		}
		moveLast();
	}

	

}
