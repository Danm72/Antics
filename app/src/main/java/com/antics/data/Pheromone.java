package com.antics.data;

public class Pheromone {
	private int intensity;
	
	public void enforce(int v){
		intensity += v;
	}
	
	public void deteriate(){
		if(intensity > 0){
			intensity -= 10;
			if(intensity < 0)
				intensity = 0;
		}
	}
	
	public int strength(){
		return intensity;
	}
	
	public boolean isEmpty(){
		return intensity == 0;
	}
}
