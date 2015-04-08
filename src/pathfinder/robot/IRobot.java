package pathfinder.robot;


public interface IRobot {

	//movement
	public void rotateTurnArm(int degrees);
	public void rotate(int degrees) throws TurnNotPossible;
	public void travel(double distance);
	public void stop();
	
	
	//position
	public Orientation getOrientation();
	
	
	//sensors
	public float getDistance(); 
	
	
	
}
