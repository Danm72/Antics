package com.antics.ant;

import com.antics.data.Board;

public class Soldier extends Ant {

	public Soldier(Board board,Float x, Float y) {
        super(board);
		//position = pos;
	}
	
	@Override
	public boolean action() {
		// TODO Auto-generated method stub
		return false;
	}
	
	// Random
	public void wander(){
		
	}

	@Override
	public int antType() {
		// TODO Auto-generated method stub
		return 3;
	}

	@Override
	public void move(int moveX, int moveY) {
		// TODO Auto-generated method stub
		
	}

}
