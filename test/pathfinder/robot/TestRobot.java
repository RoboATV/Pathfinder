package pathfinder.robot;

import java.util.Queue;


public class TestRobot implements ITestRobot{
	
	private Orientation orientation;
	private Queue<Float> distances;
	private Queue<Float> headings;

	public TestRobot(){
		orientation = Orientation.NORTH;
	}

	@Override
	public void rotateTurnArm(int degrees) {
				
	}


	@Override
	public void rotate(int degrees) {
				
	}

	@Override
	public Orientation getOrientation() {
		return orientation;
	}

	@Override
	public void travel(double distance) {
			
	}

	@Override
	public void stop() {
				
	}

	@Override
	public float getDistance() {
		if(distances.isEmpty()){
			return -1;
		}
		float distance = distances.remove();		
		
		return distance;
	}

	@Override
	public void setDistances(Queue<Float> distances) {
		this.distances = distances;
	}

	@Override
	public void setOrientation(Orientation orientation) {
		this.orientation = orientation;
	}

	@Override
	public void setHeadings(Queue<Float> headings) {
		this.headings = headings;
		
	}

	@Override
	public float getHeading() {
		if(headings.isEmpty()){
			return -1;
		}
		float heading = headings.remove();
		
		return heading;
	}	
	
	
	
	
}
