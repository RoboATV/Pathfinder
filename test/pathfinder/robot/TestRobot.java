package pathfinder.robot;

import lejos.robotics.SampleProvider;

public class TestRobot implements IRobot{
	
	private Orientation orientation;

	public TestRobot(){
		orientation = Orientation.NORTH;
	}

	@Override
	public void rotateTurnArm(int degrees) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public SampleProvider getDistanceProvider() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void rotate(int degrees) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public Orientation getOrientation() {
		return orientation;
	}	
	
	
}
