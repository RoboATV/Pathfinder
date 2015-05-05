package pathfinder.behaviors;

import java.rmi.RemoteException;
import java.util.LinkedList;
import java.util.List;

import lejos.robotics.navigation.Move;
import lejos.robotics.subsumption.Behavior;
import pathfinder.configuration.Configuration;
import pathfinder.location.EndOfRoom;
import pathfinder.location.Locator;
import pathfinder.map.Coordinate;
import pathfinder.orientation.TurnNotPossible;
import pathfinder.robot.Direction;
import pathfinder.robot.IRobot;
import pathfinder.robot.Range;



public class AvoidObstacle implements Behavior{

	private IRobot robot;
	private boolean suppressed = false;
	private Locator locator;
	
	public AvoidObstacle(IRobot robot, Locator locator){
		System.out.println("loading avoidobstacle");
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
			locator.enterCoordinateFromMove(robot.carriage_getMovement());
			this.robot.carriage_stop();	
			try{
				List<Double> obstacleEdges = this.measureObstacle();
				if(this.detectWall(obstacleEdges)){
					try {
						this.turnRobot();
					} catch (TurnNotPossible e) {
						System.out.println(e.toString());
					} catch (EndOfRoom e){
						locator.returnToStart();
					}
				} else {
					this.avoidObstacle(obstacleEdges);
				}
			} catch (RemoteException | TurnNotPossible e){
				e.printStackTrace();
			}
		}	
	}

	@Override
	public void suppress() {
		this.suppressed = true;		
	}

	

		
	
	private int calculateSensorAngle() throws RemoteException{
		float g = (Configuration.OBSTACLE_SIZE / 2) + Configuration.OBSTACLE_OFFSET;
		float a = robot.getDistance();
		
		float divAG = g / a ;		
		
		int sensorAngle = (int) Math.toDegrees(Math.atan(divAG));
		
			
		return sensorAngle;
	}
	
	
	private double calculateExpectation() throws RemoteException{
		double g = (Configuration.OBSTACLE_SIZE / 2) + Configuration.OBSTACLE_OFFSET;
		double a = robot.getDistance();
		
		double h = Math.sqrt(Math.pow(a, 2) + Math.pow(g, 2));
				
		return h;
	}
	
	
	private List<Double> measureObstacle() throws RemoteException{
		List<Double> distances = new LinkedList<Double>();		
		int sensorAngle = calculateSensorAngle();		
		
		//measure right side
		robot.turnArm_rotate(sensorAngle);				
		distances.add((double) robot.getDistance());	
			
		//measure left side
		robot.turnArm_rotate(-2 * sensorAngle);		
		distances.add((double) robot.getDistance());
		
		return distances;
	}
	
	
	private boolean detectWall(List<Double> distances) throws RemoteException{
		double expectationValue = calculateExpectation();		
			
		Range valueRange = new Range(expectationValue-2, expectationValue+2);
		
		if(valueRange.contains(distances.get(0)) && valueRange.contains(distances.get(1))){
			return true;
		}		
		
		return false;
	}
	
	
	private void turnRobot() throws TurnNotPossible, RemoteException, EndOfRoom{
		//check if wall in turn direction
		robot.turnArm_rotate(robot.getTurnDirection().getTurnAngle());
		float distance = robot.getDistance();
		if(distance <= Configuration.GRID_SIZE){
			throw new EndOfRoom();
		}
				
		robot.carriage_rotate(robot.getTurnDirection().getTurnAngle());
		
		locator.travelAhead(Configuration.GRID_SIZE);
		
		robot.carriage_rotate(robot.getTurnDirection().getTurnAngle());
		robot.invertTurnDirection();
	}
	
	
	private void avoidObstacle(List<Double> obstacleEdges) throws TurnNotPossible, RemoteException{
		
		Float obstacleDistance = robot.getDistance();
		Direction turnDirection = getTurnDirection(obstacleEdges);			
		
		robot.carriage_rotate(turnDirection.getTurnAngle());
		locator.travelAhead(Configuration.TOTAL_OBSTACLE_SIZE);
			
		robot.carriage_rotate(Direction.getOpposite(turnDirection).getTurnAngle());
		travelVertical(Direction.getOpposite(turnDirection), obstacleDistance);	
		
		
		robot.carriage_rotate(Direction.getOpposite(turnDirection).getTurnAngle());		
		locator.travelAhead(Configuration.TOTAL_OBSTACLE_SIZE);		
		robot.carriage_rotate(turnDirection.getTurnAngle());
		
	}
	
	private Direction getTurnDirection(List<Double> distances){
		if(distances.get(0) > distances.get(1)){
			return Direction.RIGHT;
		}
				
		return Direction.LEFT;
	}
	
	
	private void travelVertical(Direction obstacleSide, float obstacleDistance) throws RemoteException{
		locator.travelAhead((int) (Configuration.DISTANCE_OFFSET + obstacleDistance));
		
		robot.turnArm_rotate(obstacleSide.getTurnAngle());
		
		float distance = robot.getDistance();
		
		Range distanceRange = new Range(distance-5, distance+5);
		
		while(distanceRange.contains(robot.getDistance())){
			locator.travelAhead(10);
		}
		
		locator.travelAhead(10);
	}
}
