package pathfinder.robot;

public class NoOrientationToAngle extends Exception {

	
	private int angle;
	
	public NoOrientationToAngle(){
		
	}
	
	public NoOrientationToAngle(int angle){
		this.angle = angle;
	}
	
	
	
	public String toString(){
		return "no orientation found for such angle" + angle;
	}
	
	
	
	
	
}
