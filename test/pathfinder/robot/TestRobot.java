package pathfinder.robot;

import java.rmi.RemoteException;
import java.util.Queue;

import lejos.robotics.navigation.Move;
import pathfinder.orientation.Orientation;


public class TestRobot implements ITestRobot{
	
	private Orientation orientation;
	private Queue<Float> distances;
	private Queue<Float> headings;
	private Queue<Float> colors;
	private Queue<Float> light;
	private Direction turnDirection;
	private int travelSpeed;
	private double rotateSpeed;

	public TestRobot(){
		orientation = Orientation.NORTH;
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

	@Override
	public float getLightIntensity() {
		if(light.isEmpty()){
			return -1;
		}
		float lightSample = light.remove();
		return lightSample;
	}

	@Override
	public float getLightColor() {
		if(colors.isEmpty()){
			return -1;
		}
		float color = colors.remove();
		return color;
	}

	@Override
	public void setColors(Queue<Float> colors) {
		this.colors = colors;
		
	}

	@Override
	public void setLight(Queue<Float> light) {
		this.light = light;
		
	}

	@Override
	public void invertTurnDirection() {
		Direction oldDirection = this.turnDirection;
		turnDirection = Direction.getOpposite(oldDirection);
		
	}

	@Override
	public void travel(double distance, boolean immediateReturn) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public Move getMovement() {
		// TODO Auto-generated method stub
		return null;
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
	
	
	
}
