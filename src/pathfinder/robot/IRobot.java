package pathfinder.robot;

import lejos.robotics.SampleProvider;

public interface IRobot {

	public void rotateTurnArm(int degrees);
	public SampleProvider getDistanceProvider();
	
	public void rotate(int degrees);
	
	public Orientation getOrientation();
	
	
}
