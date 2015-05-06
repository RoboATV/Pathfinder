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
	private Direction turnDirection;

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

	}

	@Override
	public void turnArm_rotateToCenter() throws RemoteException {
		
	}

	@Override
	public void turnArm_rotateToLeft() throws RemoteException {
		
	}

	@Override
	public void turnArm_rotateToRight() throws RemoteException {
		
	}

	@Override
	public int turnArm_getTurnAngle() {
		return 0;
	}

	@Override
	public boolean turnArm_isCentered() {
		return false;
	}

	@Override
	public void carriage_rotate(int degrees) throws TurnNotPossible {
		int newAngle = this.orientation.getAngle() + degrees;
		
		try {
			this.orientation = Orientation.getOrientation(newAngle);
		} catch (NoOrientationToAngle e) {
			throw new TurnNotPossible(degrees);
		}
	}

	@Override
	public void carriage_turnLeft() throws TurnNotPossible {
		
	}



	@Override
	public void carriage_turnRight() throws TurnNotPossible {
		
	}



	@Override
	public void carriage_travel(double distance) {
		
	}



	@Override
	public void carriage_travel(double distance, boolean immediateReturn) {
		
	}



	@Override
	public void carriage_stop() {
		
	}



	@Override
	public Move carriage_getMovement() {
		return null;
	}



	@Override
	public Orientation carriage_getOrientation() {
		return this.orientation;
	}



	@Override
	public void grappler_grap() {
		
	}



	@Override
	public void grappler_release() {
		
	}


	@Override
	public void carriage_rotateUnchecked(int degrees) {
		
	}


	@Override
	public boolean carriage_isMoving() {
		return false;
	}


	@Override
	public boolean grappler_isLoaded() {
		return false;
	}


	@Override
	public boolean victim_detectedColorSensor() {
		return false;
	}


	@Override
	public boolean victim_detectedCamera() {
		return false;
	}


	@Override
	public Rectangle2D victim_getLocation() {
		return null;
	}


	@Override
	public int getHeading() {
		return 0;
	}


	@Override
	public void shutdown() {
		
	}
}
