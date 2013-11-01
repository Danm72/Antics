package com.antics.ant;

import com.antics.data.Board;
import static com.antics.util.ConfigFile.ants;

public class Queen extends Ant {
	private int timer;
	
	public Queen(Board board){
        super(board);
		timer = (int) (Math.random() % 20) + 36;
		positionX = board.NEST_X +1;
		positionY = board.NEST_Y;
		antType = antType();
		board.tiles[this.positionX][this.positionY].setBackgroundType(1);
		board.tiles[this.positionX][this.positionY].newAnt(antType());
		// co-ordinates for graphics (super.)
	}
	
	public void spawnGatherer(){
		ants.add(new Gatherer(board, board.NEST_X, board.NEST_Y));
		board.ants++;
	}
	
	public void spawnBuilder(){
		ants.add(new Builder(board,board.NEST_X, board.NEST_Y));
		board.ants++;
	}

	@Override
	public boolean action() {
		// TODO Auto-generated method stub
		float decision = (float) (Math.random() * 1);
		
		if(board.tiles[board.NEST_X][board.NEST_Y].food()
				> ants.size()){
			if(board.tiles[board.NEST_X][board.NEST_Y].food()
					> ((int) ants.size() * 1.5) ){
				if(decision < 0.35f){
					decision = (float) (Math.random() * 1);
					if(decision > .50f)
						spawnGatherer();
					else
						spawnBuilder();
				}
			}
			else if(decision < 0.20f){
				decision = (float) (Math.random() * 1);
				if(decision > .50f)
					spawnGatherer();
				else
					spawnBuilder();
			}
		}
		return false;
	}


	@Override
	public int antType() {
		// TODO Auto-generated method stub
		return 2;
	}
	
	// Random
	public void wander(){
		
	}
	
	@Override
	public void move(int moveX, int moveY) {
		// TODO Auto-generated method stub
		
	}
	
}
