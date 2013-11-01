package com.antics.ant;

import com.antics.data.Board;

public class Termite extends Ant{
	
	public Termite(Board board, int x0, int y0) {
        super(board);
		positionX = x0;
		positionY = y0;
		lastX = x0;
		lastY = y0;
		board.tiles[this.positionX][this.positionY].setBackgroundType(1);
		board.tiles[this.positionX][this.positionY].newAnt(antType());
		health = ((int) (Math.random() * 150)) + 350;
	}

	@Override
	public boolean action() {
		return hasDirt;
		// TODO Auto-generated method stub
		
	}

	@Override
	public void wander() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void move(int x0, int y0) {
		// TODO Auto-generated method stub
		
	}
	
	public int antType(){
		return 5;
	}

}
