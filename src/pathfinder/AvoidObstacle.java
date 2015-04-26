package pathfinder;

import java.rmi.RemoteException;
import java.util.LinkedList;
import java.util.List;

import lejos.robotics.navigation.Move;
import lejos.robotics.subsumption.Behavior;
import pathfinder.configuration.Configuration;
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
			enterLastCoordinate(robot.getMovement());
			this.robot.stop();	
			try{
				List<Double> obstacleEdges = this.measureObstacle();
				if(this.detectWall(obstacleEdges)){
					try {
						this.turnRobot();
					} catch (TurnNotPossible e) {
						System.out.println(e.toString());
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

	
	private void enterLastCoordinate(Move move){
		Coordinate relPos = new Coordinate(0, (int) move.getDistanceTraveled());
		Coordinate absPos = locator.calcNewPos(relPos);
		
		System.out.println("last position relative " + relPos);
		System.out.println("last position absolute " + absPos);
		
		locator.robotTrack.add(absPos);
		locator.currentPos = absPos;
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
		robot.rotateTurnArm(sensorAngle);				
		distances.add((double) robot.getDistance());	
			
		//measure left side
		robot.rotateTurnArm(-2 * sensorAngle);		
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
	
	
	private void turnRobot() throws TurnNotPossible{
		robot.rotate(robot.getTurnDirection().getTurnAngle());
		Coordinate newPos = new Coordinate(0, Configuration.GRID_SIZE);
		locator.relocateRelative(newPos);
		robot.rotate(robot.getTurnDirection().getTurnAngle());
		robot.invertTurnDirection();
	}
	
	
	private void avoidObstacle(List<Double> obstacleEdges) throws TurnNotPossible, RemoteException{
		
		Direction turnDirection = turnDirection(obstacleEdges);			
		robot.rotate(turnDirection.getTurnAngle());
		robot.travel(Configuration.TOTAL_OBSTACLE_SIZE);		
		robot.rotate(Direction.getOpposite(turnDirection).getTurnAngle());
		robot.travel(Configuration.WALLDISTANCE + Configuration.OBSTACLE_OFFSET);
		
		travelVertical(Direction.getOpposite(turnDirection));	
		robot.rotate(Direction.getOpposite(turnDirection).getTurnAngle());
		
		robot.travel(Configuration.TOTAL_OBSTACLE_SIZE);
		
		robot.rotate(turnDirection.getTurnAngle());
		
	}
	
	private Direction turnDirection(List<Double> distances){
		if(distances.get(0) > distances.get(1)){
			return Direction.LEFT;
		}
				
		return Direction.RIGHT;
	}
	
	
	private void travelVertical(Direction obstacleSide) throws RemoteException{
		robot.rotateTurnArm(obstacleSide.getTurnAngle());
		
		float distance = robot.getDistance();
		
		Range distanceRange = new Range(distance-5, distance+5);
		
		while(distanceRange.contains(robot.getDistance())){
			robot.travel(10);
		}
		
		
	}
}
