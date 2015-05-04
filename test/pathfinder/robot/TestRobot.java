package pathfinder.robot;

import java.rmi.RemoteException;
import java.util.Queue;

import lejos.robotics.geometry.Rectangle2D;
import lejos.robotics.navigation.Move;
import pathfinder.orientation.NoOrientationToAngle;
import pathfinder.orientation.Orientation;
import pathfinder.orientation.TurnNotPossible;


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

//	@Override
//	public float getHeading() {
//		if(headings.isEmpty()){
//			return -1;
//		}
//		float heading = headings.remove();
//		
//		return heading;
//	}

	@Override
	public Direction getTurnDirection() {
		return this.turnDirection;
	}	
	
	@Override
	public void setTurnDirection(Direction turnDirection){
		this.turnDirection = turnDirection;
	}

	@Override
	public void invertTurnDirection() {
		Direction oldDirection = this.turnDirection;
		turnDirection = Direction.getOpposite(oldDirection);
		
	}

	@Override
	public void turnArm_rotate(int degrees) throws RemoteException {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void turnArm_rotateToCenter() throws RemoteException {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void turnArm_rotateToLeft() throws RemoteException {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void turnArm_rotateToRight() throws RemoteException {
		// TODO Auto-generated method stub
		
	}

	@Override
	public int turnArm_getTurnAngle() {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public boolean turnArm_isCentered() {
		// TODO Auto-generated method stub
		return false;
	}



	@Override
	public void carriage_rotate(int degrees) throws TurnNotPossible {
		int newAngle = this.orientation.getAngle() + degrees;
		System.out.println(newAngle);
		try {
			this.orientation = Orientation.getOrientation(newAngle);
		} catch (NoOrientationToAngle e) {
			throw new TurnNotPossible(degrees);
		}
		
		
	}



	@Override
	public void carriage_turnLeft() throws TurnNotPossible {
		// TODO Auto-generated method stub
		
	}



	@Override
	public void carriage_turnRight() throws TurnNotPossible {
		// TODO Auto-generated method stub
		
	}



	@Override
	public void carriage_travel(double distance) {
		// TODO Auto-generated method stub
		
	}



	@Override
	public void carriage_travel(double distance, boolean immediateReturn) {
		// TODO Auto-generated method stub
		
	}



	@Override
	public void carriage_stop() {
		// TODO Auto-generated method stub
		
	}



	@Override
	public Move carriage_getMovement() {
		// TODO Auto-generated method stub
		return null;
	}



	@Override
	public Orientation carriage_getOrientation() {
			
		return this.orientation;
	}



	@Override
	public void grappler_grap() {
		// TODO Auto-generated method stub
		
	}



	@Override
	public void grappler_release() {
		// TODO Auto-generated method stub
		
	}


	@Override
	public void carriage_rotateUnchecked(int degrees) {
		// TODO Auto-generated method stub
		
	}


	@Override
	public boolean carriage_isMoving() {
		// TODO Auto-generated method stub
		return false;
	}


	@Override
	public boolean grappler_isLoaded() {
		// TODO Auto-generated method stub
		return false;
	}


	@Override
	public boolean victim_detectedColorSensor() {
		// TODO Auto-generated method stub
		return false;
	}


	@Override
	public boolean victim_detectedCamera() {
		// TODO Auto-generated method stub
		return false;
	}


	@Override
	public Rectangle2D victim_getLocation() {
		// TODO Auto-generated method stub
		return null;
	}
	
	
	
}
