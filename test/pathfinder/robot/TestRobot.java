package pathfinder.robot;

import java.util.Queue;

import pathfinder.orientation.Orientation;


public class TestRobot implements ITestRobot{
	
	private Orientation orientation;
	private Queue<Float> distances;
	private Queue<Float> headings;
	private Direction turnDirection;
	private int travelSpeed;
	private double rotateSpeed;

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

	@Override
	public Direction getTurnDirection() {
		return this.turnDirection;
	}	
	
	@Override
	public void setTurnDirection(Direction turnDirection){
		this.turnDirection = turnDirection;
	}

	@Override
	public void grapObject() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void setTravelSpeed(int speed) {
		this.travelSpeed = speed;
		
	}

	@Override
	public void setRotateSpeed(double speed) {
		this.rotateSpeed = speed;
		
	}
	
	
	
}
