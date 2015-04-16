package pathfinder;

import java.rmi.RemoteException;
import java.util.LinkedList;
import java.util.List;

import lejos.robotics.subsumption.Behavior;
import pathfinder.configuration.Configuration;
import pathfinder.location.Locator;
import pathfinder.map.Coordinate;
import pathfinder.orientation.TurnNotPossible;
import pathfinder.robot.IRobot;

import com.google.common.collect.Range;

public class AvoidObstacle implements Behavior{

	private IRobot robot;
	private boolean suppressed = false;
	private Locator locator;
	
	public AvoidObstacle(IRobot robot, Locator locator){
		this.robot = robot;
		this.locator = locator;
	}
	
	
	@Override
	public boolean takeControl() {		
		try {
			return robot.getDistance() < Configuration.WALLDISTANCE;
		} catch (RemoteException e) {			
			e.printStackTrace();
			return false;
		}
	}

	@Override
	public void action() {
		if(!suppressed){
			this.robot.stop();	
			try{
				if(this.detectWall(this.measureObstacle())){
					try {
						this.turnRobot();
					} catch (TurnNotPossible e) {
						System.out.println(e.toString());
					}
				} else {
					this.avoidObstacle();
				}
			} catch (RemoteException e){
				e.printStackTrace();
			}
		}	
	}

	@Override
	public void suppress() {
		this.suppressed = true;		
	}

	
	private int calculateSensorAngle(){
		float g = (Configuration.OBSTACLE_SIZE / 2) + Configuration.OBSTACLE_OFFSET;
		float a = Configuration.WALLDISTANCE;
		
		float divAG = g / a ;		
		
		int sensorAngle = (int) Math.toDegrees(Math.atan(divAG));
		
		
		return sensorAngle;
	}
	
	
	private double calculateExpectation(){
		double g = (Configuration.OBSTACLE_SIZE / 2) + Configuration.OBSTACLE_OFFSET;
		double a = Configuration.WALLDISTANCE;
		
		double h = Math.sqrt(Math.pow(a, 2) + Math.pow(g, 2));
				
		return h;
	}
	
	
	private List<Double> measureObstacle() throws RemoteException{
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
	
	
	private void turnRobot() throws TurnNotPossible{
		robot.rotate(robot.getTurnDirection().getTurnAngle());
		robot.travel(Configuration.GRID_SIZE);
		robot.rotate(robot.getTurnDirection().getTurnAngle());
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
