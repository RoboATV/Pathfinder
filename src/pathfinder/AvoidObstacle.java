package pathfinder;

import java.util.LinkedList;
import java.util.List;

import lejos.robotics.subsumption.Behavior;
import pathfinder.configuration.Configuration;
import pathfinder.location.Locator;
import pathfinder.map.Coordinate;
import pathfinder.robot.Robot;

import com.google.common.collect.Range;

public class AvoidObstacle implements Behavior{

	private Robot robot;
	private boolean suppressed = false;
	private Locator locator;
	
	public AvoidObstacle(Robot robot, Locator locator){
		this.robot = robot;
		this.locator = locator;
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

	
	private int calculateSensorAngle(){
		float g = (Configuration.OBSTACLE_SIZE / 2) + Configuration.OBSTACLE_OFFSET;
		float a = Configuration.WALLDISTANCE;
		
		float divAG = a /g;
		
		int sensorAngle =  (int) Math.round(Math.atan(divAG));
		
		return sensorAngle;
	}
	
	
	private double calculateExpectation(){
		double g = (Configuration.OBSTACLE_SIZE / 2) + Configuration.OBSTACLE_OFFSET;
		double a = Configuration.WALLDISTANCE;
		
		double h = Math.sqrt(Math.pow(a, 2) + Math.pow(g, 2));
				
		return h;
	}
	
	
	private List<Double> measureObstacle(){
		List<Double> distances = new LinkedList<Double>();		
		int sensorAngle = calculateSensorAngle();		
		
		//measure right side
		robot.rotateTurnArm(sensorAngle);				
		distances.add((double) robot.getDistance());	
			
		//measure left side
		robot.rotateTurnArm(-2 * sensorAngle);		
		distances.add((double) robot.getDistance());
		
		return distances;
	}
	
	
	private boolean detectWall(List<Double> distances){
		double expectationValue = calculateExpectation();		
		Range<Double> valueRange = Range.closed(expectationValue-2, expectationValue+2);
		
		if(valueRange.contains(distances.get(0)) && valueRange.contains(distances.get(1))){
			return true;
		}		
		
		return false;
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
