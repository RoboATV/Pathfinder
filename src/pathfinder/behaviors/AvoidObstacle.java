package pathfinder.behaviors;

import java.rmi.RemoteException;
import java.util.LinkedList;
import java.util.List;

import lejos.robotics.subsumption.Behavior;
import pathfinder.Main;
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
		System.out.println("  avoid obstacle");
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
		suppressed = false;
		if(!suppressed){
			System.out.println("Avoid obstacle...");
			locator.enterCoordinateFromMove(robot.carriage_getMovement());
			this.robot.carriage_stop();	
			try{
				List<Float> obstacleEdges = this.measureObstacle();
				if(this.detectWall(obstacleEdges)){
					try {
						this.turnRobot();
					} catch (TurnNotPossible e) {
						System.out.println(e.toString());
					} catch (EndOfRoom e){
						locator.returnToStart();
						
						robot.shutdown();
						Main.shutdown();
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
	
	
	private float calculateExpectation() throws RemoteException{
		double g = (Configuration.OBSTACLE_SIZE / 2) + Configuration.OBSTACLE_OFFSET;
		double a = robot.getDistance();
		
		double h = Math.sqrt(Math.pow(a, 2) + Math.pow(g, 2));
				
		return (float) h;
	}
	
	
	private List<Float> measureObstacle() throws RemoteException{
		List<Float> distances = new LinkedList<Float>();		
		int sensorAngle = calculateSensorAngle();	
		
		sensorAngle += 10;
		
		//measure right side
		robot.turnArm_rotate(sensorAngle);				
		distances.add(robot.getDistance());	
		
		robot.turnArm_rotateToCenter();
		
		//measure left side
		robot.turnArm_rotate(-sensorAngle);		
		distances.add(robot.getDistance());
		
		robot.turnArm_rotateToCenter();
		
		return distances;
	}
	
	
	private boolean detectWall(List<Float> distances) throws RemoteException{
		float expectationValue = calculateExpectation();
		float tolerance = 20f;
		
		System.out.println("distances" + distances);
		System.out.println("range upper" + expectationValue+tolerance);
		
		Range valueRange = new Range(0, expectationValue+tolerance);
		
		if(valueRange.contains(distances.get(0)) && valueRange.contains(distances.get(1))){
			System.out.println("Wall detected");
			return true;
		}		
		
		return false;
	}
		
	
	private void turnRobot() throws TurnNotPossible, RemoteException, EndOfRoom{
		//check if wall in turn direction
		System.out.println("Turn robot");
		robot.turnArm_rotate(robot.getTurnDirection().getTurnAngle());
		float distance = robot.getDistance();
		robot.turnArm_rotateToCenter();
		if(distance <= Configuration.GRID_SIZE){
			throw new EndOfRoom();
		}
				
		robot.carriage_rotate(robot.getTurnDirection().getTurnAngle());
		
		locator.travelAhead(Configuration.GRID_SIZE/10);
		
		robot.carriage_rotate(robot.getTurnDirection().getTurnAngle());
		robot.invertTurnDirection();
	}
	
	
	private void avoidObstacle(List<Float> obstacleEdges) throws TurnNotPossible, RemoteException{
		System.out.println("Avoid obstacle");
		Float obstacleDistance = robot.getDistance();
		Direction turnDirection = getTurnDirection(obstacleEdges);	
		
		Coordinate edge;
		if(turnDirection == Direction.RIGHT){
			edge = locator.calculateMapPosition(calculateSensorAngle(), obstacleEdges.get(1));
		} else {
			edge = locator.calculateMapPosition(calculateSensorAngle(), obstacleEdges.get(0));
		}
		
		robot.carriage_rotate(turnDirection.getTurnAngle());
		locator.travelAhead(Configuration.TOTAL_OBSTACLE_SIZE/10);
			
		robot.carriage_rotate(Direction.getOpposite(turnDirection).getTurnAngle());
	
		travelVertical(Direction.getOpposite(turnDirection), obstacleDistance, edge);			
		
		robot.carriage_rotate(Direction.getOpposite(turnDirection).getTurnAngle());		
		locator.travelAhead(Configuration.TOTAL_OBSTACLE_SIZE/10);		
		robot.carriage_rotate(turnDirection.getTurnAngle());
		
	}
	
	private Direction getTurnDirection(List<Float> distances){
		if(distances.get(0) > distances.get(1)){
			return Direction.RIGHT;
		}
				
		return Direction.LEFT;
	}
	
	
	private void travelVertical(Direction obstacleSide, float obstacleDistance, Coordinate edge) throws RemoteException{
		locator.travelAhead((int) (Configuration.DISTANCE_OFFSET + obstacleDistance)/10);
		
		robot.turnArm_rotate(obstacleSide.getTurnAngle());
		
		float distance = robot.getDistance();
		
		Range distanceRange = new Range(distance-5, distance+5);
		
		while(distanceRange.contains(distance)){
			locator.enterObstacle(edge, distance, obstacleSide);
			locator.travelAhead(1);
			distance = robot.getDistance();
		}
		robot.turnArm_rotateToCenter();
		
		locator.travelAhead(1);
	}
	
	
	
}
