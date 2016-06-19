package com.antics.ant;

import com.antics.data.Board;

/*
 * 
 * Hungry + no food -> Follow search pheromone back to nest
 * 
 * Hungry + has food/standing on food -> Eat
 * 
 * Food nearby + no dirt -> Gatherer
 * 
 * No Dirt + On dig trail -> Follow trail + Lay search pheromone.
 * 
 * Find undug tile -> Dig at it + pick up dirt
 * 
 * Carrying dirt -> follow search trail back to nest + lay dig pheromone
 * 
 * Home + With dirt -> Drop dirt
 * 
 */ 

/*
 * The Builder class is responsible for expanding the 
 * colony's tunnel system.
 * It will dynamically become a Gatherer if it finds food
 * along its path.
 */
public class Builder extends Ant {

    private static final int BUILDER = 1;

    // Used when converting an Ant into a Builder
	public Builder(Board board, Ant ant){
        super(board);
        this.health = ant.getHealth();
		this.antType = 0;
		this.hasFood = ant.hasFood();
		this.hasDirt = ant.hasDirt();
		this.hunger = ant.hungerLevel();
		this.positionX = ant.getPositionX();
		this.positionY = ant.getPositionY();
		this.lastX = ant.getLastYPosition();
		this.lastY = ant.getLastXPosition();
		board.tiles[this.positionX][this.positionY].antVacateTile(ant.antType());
		board.tiles[this.positionX][this.positionY].newAnt(antType);
	}

    // Used when initializing a new Builder
	public Builder(Board board, int x0, int y0) {
        super(board);
		positionX = x0;
		positionY = y0;
		lastX = x0;
		lastY = y0;
		antType = 0;
		board.tiles[this.positionX][this.positionY].setBackgroundType(BUILDER);
		board.tiles[this.positionX][this.positionY].newAnt(antType());
		hasDirt = false;
		hasFood = false;
		health = ((int) (Math.random() * 150)) + 350;
		hunger = 140;
	}

	@Override
	public boolean action() {
		
		if(hunger > 0){
			hunger--;
			health--;
		}
		else
			health -= 2;
		
		// If found food, and not already returning dirt
		// become a Gatherer
		if(!hasDirt){
			if(checkForFood())
				return true;
		}
		
		if(hungry()){
			if(hasFood || foundFood())
				eat();
			else
				returnToNest();
		}
		else if(hasDirt){
			if(atNest()){
				dropDirt();
			}
			else{
				returnToNest();
			}
		}
		else if(smellDigPheromone()){
			followTrail();
		}
		else{
			wander();
		}
		return false;
	}
	
	// Drop dirt they are holding
	private void dropDirt(){
		hasDirt = false;
	}
	
	// Make random decision
	public void wander() {
		// wtf am I doing.
    	float decision;
    	
	    while(true){
	    	decision = (float) (Math.random() * 1);
		    // Left
		    if(decision < 0.1666f){ 
		        if(board.inBounds(positionX, positionY -1)){
		            if(!board.tiles[positionX][positionY -1].dug())
		                dig(positionX, positionY -1);
		            else
		                moveLeft();
		            return;
		        }
		        else
		        	continue;
		    }
		    
		    // Right
		    if(decision > 0.1666f && decision < 0.3332f){
		        if(positionY + 1 < board.width && board.tiles[positionX][positionY +1] != null){
		            if(!board.tiles[positionX][positionY +1].dug()){
		                dig(positionX, positionY +1);
		            }
		            else
		                move(positionX, positionY +1);
		            return;
		        }
		        else
		        	continue;
		    }
		    
		    // Topleft
		    if(decision > 0.3332f && decision < 0.4998f){
		        // Even row
		        if(positionX % 2 == 0){
		            if(positionX > 0 && positionY > 0 && board.tiles[positionX -1][positionY -1] != null){
		                if(!board.tiles[positionX -1][positionY -1].dug())
		                	dig(positionX -1, positionY -1);
		                else
		                    move(positionX -1, positionY -1);
		                return;
		            }
		            else
		            	continue;
		        }
		        else{   // Odd row
		            if(positionX > 0 && board.tiles[positionX -1][positionY] != null){
		                if(!board.tiles[positionX -1][positionY].dug())
		                    dig(positionX -1, positionY);
		                else
		                    move(positionX -1, positionY);
		                return;
		            }
		            else
		            	continue;
		        }
		    }
		    
		    // Topright
		    if(decision > 0.4998 && decision < 0.6664){
		        
		        // Even row
		        if(positionX % 2 == 0){
		            if(positionX > 0 && board.tiles[positionX -1][positionY] != null){
		                if(!board.tiles[positionX -1][positionY].dug())
		                    dig(positionX -1, positionY);
		                else
		                    move(positionX -1, positionY);
		                return;
		            }
		            else
		            	continue;
		        }
		        else{   // Odd row
		            if(positionX > 0 && positionY < board.width-1 && board.tiles[positionX -1][positionY +1] != null){
		                if(!board.tiles[positionX -1][positionY +1].dug())
		                    dig(positionX -1, positionY +1);
		                else
		                	move(positionX -1, positionY +1);
		                return;
		            }
		            else
		            	continue;
		        }
		    }
		    
		    // Botleft
		    if(decision > 0.6664f && decision < 0.8333f){
		        if(positionX % 2 == 0){
		            if((positionX + 1 < board.height && positionY - 1 >= 0) && board.tiles[positionX +1][positionY -1] != null){
		                if(!board.tiles[positionX +1][positionY -1].dug())
		                    dig(positionX +1, positionY -1);
		                else
		                    move(positionX +1, positionY -1);
		                return;
		            }
		            else
		            	continue;
		        }
		        else{
		            if(positionX + 1 < board.height && board.tiles[positionX +1][positionY] != null){
		                if(!board.tiles[positionX +1][positionY].dug())
		                    dig(positionX +1, positionY);
		                else
		                	move(positionX +1, positionY);
		                return;
		            }
		            else
		            	continue;
		        }
		    }
		    
		    if(decision > 0.8333f){
			    // Botright
			    if(positionX % 2 == 0){
			        if(positionX < board.height-1 && board.tiles[positionX +1][positionY] != null){
			            if(!board.tiles[positionX +1][positionY].dug())
			                dig(positionX +1, positionY);
			            else
			            	move(positionX +1, positionY);
			            return;
			        }
			    }
			    else{
			        if(positionX < board.height - 1 && positionY < board.width-1 && board.tiles[positionX +1][positionY +1] != null){
			            if(!board.tiles[positionX +1][positionY +1].dug())
			                dig(positionX +1, positionY +1);
			            else
			                move(positionX +1, positionY +1);
			            return;
			        }
			    }
	    	}
	    }
	}
	
	// Follow Dig Pheromone trail
	protected void followTrail(){
		int moveX = -1;
    	int moveY = -1;
    	
    	
    	float decision = (float) (Math.random() * 1);
		if(decision < .15f){
			wander();
			return;
		}
    	
    	// Left
    	if(notBackwards(positionX, positionY -1)){
	    	if(positionY > 0 && board.tiles[positionX][positionY -1] != null
	    			&& board.tiles[positionX][positionY -1].hasDigPheromone() ){
	    		moveX = positionX;
	    		moveY = positionY -1;
	    	}
    	}		
    	
    	// Right
    	if(notBackwards(positionX, positionY +1)){
	    	if(positionY < board.width && board.tiles[positionX][positionY +1] != null
	    			&& board.tiles[positionX][positionY +1].hasDigPheromone() ){
	    		if(moveX + moveY > -2){
	    			
	    			if(board.tiles[moveX][moveY].digPheromone()
	    					>= board.tiles[positionX][positionY +1].digPheromone() ){
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
		    			&& board.tiles[positionX -1][positionY -1].hasDigPheromone() ){
		    			if(moveX + moveY > -2){
		    				if(board.tiles[moveX][moveY].digPheromone()
		    						>= board.tiles[positionX -1][positionY -1].digPheromone() ) {
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
		    			&& board.tiles[positionX +1][positionY -1].hasDigPheromone() ){
		    			if(moveX + moveY > -2){
		    				if(board.tiles[moveX][moveY].digPheromone()
		    						>= board.tiles[positionX +1][positionY -1].digPheromone() ){
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
	    			&& board.tiles[positionX -1][positionY].hasDigPheromone() ){
	    			if(moveX + moveY > -2){
	    				if(board.tiles[moveX][moveY].digPheromone()
	    						>= board.tiles[positionX -1][positionY].digPheromone() ){
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
	    			&& board.tiles[positionX +1][positionY].hasDigPheromone() ){
	    			if(moveX + moveY > -2){
	    				if(board.tiles[moveX][moveY].digPheromone()
	    						>= board.tiles[positionX +1][positionY].digPheromone() ){
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
	    			&& board.tiles[positionX -1][positionY].hasDigPheromone() ){
	    			if(moveX + moveY > -2){
	    				if(board.tiles[moveX][moveY].digPheromone()
	    						>= board.tiles[positionX -1][positionY].digPheromone() ){
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
	    			&& board.tiles[positionX +1][positionY].hasDigPheromone() ){
	    			if(moveX + moveY > -2){
	    				if(board.tiles[moveX][moveY].digPheromone()
	    						>= board.tiles[positionX +1][positionY].digPheromone() ){
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
		    			&& board.tiles[positionX -1][positionY +1].hasDigPheromone() ){
		    			if(moveX + moveY > -2){
		    				if(board.tiles[moveX][moveY].digPheromone()
		    						>= board.tiles[positionX -1][positionY +1].digPheromone() ){
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
		    			&& board.tiles[positionX +1][positionY +1].hasDigPheromone() ){
		    			if(moveX + moveY > -2){
		    				if(board.tiles[moveX][moveY].digPheromone()
		    						>= board.tiles[positionX +1][positionY +1].digPheromone() ){
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

		if(moveX + moveY != -2){
			if(board.tiles[moveX][moveY].dug())
				move(moveX,moveY);
			else
				dig(moveX, moveY);
		}
		else{	// Will move or dig randomly
			wander();
		}
    }
	
	// Dig method
	private void dig(int x0, int y0){
		if(!hasDirt && legalDig(x0,y0)){
			board.tiles[x0][y0].dig();
			hasDirt = true;
		}
	}
	
	// Check if dig would be structurally sound
	private boolean legalDig(int x0, int y0){
		int count = 0;
		
		// Check left side
		if(board.tiles[x0][y0].leftDug()){
			count++;
			if(board.tiles[x0][y0].topLeftDug()
					|| board.tiles[x0][y0].botLeftDug())
				count++;
			
			if(count == 2)
				return false;
			
			count = 0;
		}
		
		// Check top left
		if(board.tiles[x0][y0].topLeftDug()){
			count++;
			if(board.tiles[x0][y0].leftDug()
					|| board.tiles[x0][y0].topRightDug())
				count++;
			
			if(count == 2)
				return false;
			
			count = 0;
				
		}
		
		// Check top right
		if(board.tiles[x0][y0].topRightDug()){
			count++;
			if(board.tiles[x0][y0].topLeftDug()
					|| board.tiles[x0][y0].rightDug())
				count++;
			
			if(count == 2)
				return false;
			
			count = 0;
		}
			
		// Check right side
		if(board.tiles[x0][y0].rightDug()){
			count++;
			if(board.tiles[x0][y0].topRightDug()
					|| board.tiles[x0][y0].botRightDug())
				count++;
			
			if(count == 2)
				return false;
			
			count = 0;
		}
		
		// Check bot right
		if(board.tiles[x0][y0].botRightDug()){
			count++;
			if(board.tiles[x0][y0].rightDug()
					|| board.tiles[x0][y0].botLeftDug())
				count++;
			
			if(count == 2)
				return false;
			
			count = 0;
		}
		
		// Check bot left
		if(board.tiles[x0][y0].botLeftDug()){
			count++;
			if(board.tiles[x0][y0].botRightDug()
					|| board.tiles[x0][y0].leftDug())
				count++;
			
			if(count == 2)
				return false;
		}
			
		return board.inBounds(x0,y0);
		
	}
	
	// Check surrounding tiles for trail
    protected boolean smellDigPheromone(){
    	// Left
    	if(positionY > 0 && board.tiles[positionX][positionY -1] != null
    			&& board.tiles[positionX][positionY -1].hasDigPheromone() )
    		return true;
    	
    	// Right
    	if(positionY < board.width-1 && board.tiles[positionX][positionY +1] != null
    			&& board.tiles[positionX][positionY +1].hasDigPheromone() )
    		return true;
    	
    	if(positionX % 2 == 0){
    		if(positionY > 0){
	    		// Top Left
	    		if(positionX > 0 && board.tiles[positionX -1][positionY -1] != null
	    			&& board.tiles[positionX -1][positionY -1].hasDigPheromone() )
	    			return true;
	    		
	    		// Bot Left
	    		if(positionX < board.height-1 && board.tiles[positionX +1][positionY -1] != null
	    			&& board.tiles[positionX +1][positionY -1].hasDigPheromone() )
	    			return true;
    		}	
    		
    		// Top Right
    		if(positionX > 0 && board.tiles[positionX -1][positionY] != null
    			&& board.tiles[positionX -1][positionY].hasDigPheromone() )
    			return true;
    		
    		// Bot Right
    		if(positionX < board.height-1 && board.tiles[positionX +1][positionY] != null
    			&& board.tiles[positionX +1][positionY].hasDigPheromone() )
    			return true;
    	}
    	else{
    		// Top Left
    		if(positionX > 0 && board.tiles[positionX -1][positionY] != null
    			&& board.tiles[positionX -1][positionY].hasDigPheromone() )
    			return true;
    		
    		// Bot Left
    		if(positionX < board.height && board.tiles[positionX +1][positionY] != null
    			&& board.tiles[positionX +1][positionY].hasDigPheromone() )
    			return true;
    		
    		if(positionY < board.width-1){
	    		// Top Right
	    		if(positionX > 0 && board.tiles[positionX -1][positionY +1] != null
	    			&& board.tiles[positionX -1][positionY +1].hasDigPheromone() )
	    			return true;
	    		
	    		// Bot Right
	    		if(positionX < board.height-1 && board.tiles[positionX +1][positionY +1] != null
	    			&& board.tiles[positionX +1][positionY +1].hasDigPheromone() )
	    			return true;
    		}
    	}
    	return false;
    }
}
