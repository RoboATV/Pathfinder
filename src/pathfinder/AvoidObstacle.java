package pathfinder;

import lejos.robotics.subsumption.Behavior;
import pathfinder.configuration.Configuration;
import pathfinder.map.Coordinate;
import pathfinder.robot.Robot;

public class AvoidObstacle implements Behavior{

	private Robot robot;
	private boolean suppressed = false;
	
	public AvoidObstacle(Robot robot){
		this.robot = robot;
	}
	
	
	@Override
	public boolean takeControl() {		
		return robot.getDistance() < Configuration.WALLDISTANCE;
	}

	@Override
	public void action() {
		if(!suppressed){
			this.robot.stop();		
		}	
	}

	@Override
	public void suppress() {
		this.suppressed = true;		
	}

	
	
	private boolean detectWall(){
		//TODO 
		return true;
	}
	
	
	private void turnRobot(){
		//TODO
	}
	
	
	private void avoidObstacle(){
		//TODO
	}
	
	private int turnDirection(Coordinate left, Coordinate right){
		//TODO
		int turnAngle = 90;
		
		return turnAngle;
	}
	
	private int horizontalDistance(Coordinate corner){
		//TODO
		return 0;
	}
	
	private int verticalDistance(int obstacleDistance){
		//TODO
		return 0;
	}
}
