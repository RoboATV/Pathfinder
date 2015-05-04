package pathfinder.map.victim;

import pathfinder.map.MapObject;

// TODO: Check if needed. If not -> delete.
public class Victim extends MapObject{

	private boolean rescued = false;

	@Override
	public int numericalValue() {
		return 4;
	}

	public boolean isRescued(){
		return this.rescued ;
	}
	
	public void rescue(){
		this.rescued = true;
	}
	
}
