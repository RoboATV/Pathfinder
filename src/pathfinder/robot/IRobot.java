package pathfinder.robot;

import lejos.robotics.SampleProvider;

public interface IRobot {

	//movement
	public void rotateTurnArm(int degrees);
	public void rotate(int degrees);
	public void travel(double distance);
	public Orientation getOrientation();
	
	
	//sensors
	public SampleProvider getDistanceProvider();
	
	
	
}
