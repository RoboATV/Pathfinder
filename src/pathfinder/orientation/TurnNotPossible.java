package pathfinder.orientation;

public class TurnNotPossible extends Exception{
	
	private int angle;

	public TurnNotPossible(){
		
	}
	
	public TurnNotPossible(int angle){
		this.angle = angle;
	}

	public String toString(){
		return "turn not possible for angle " + angle;
	}
	
}
