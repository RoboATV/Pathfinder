package pathfinder.robot;

import java.rmi.RemoteException;

import lejos.robotics.navigation.Move;
import pathfinder.components.ITurnArm;
import pathfinder.orientation.Orientation;
import pathfinder.orientation.TurnNotPossible;


public interface IRobot {
	/**
	 * Rotate the turn arm by a specified angle.
	 * 
	 * @see	ITurnArm#rotate(int)
	 */
	public void turnArm_rotate(int degrees) throws RemoteException;
	
	/**
	 * Rotate the turn arm to the center.
	 * 
	 * @throws	RemoteException
	 * 
	 * @see	ITurnArm#rotateToCenter()
	 */
	public void turnArm_rotateToCenter() throws RemoteException;
	
	/**
	 * Rotate the turn arm to the left.
	 * 
	 * @throws	RemoteException
	 * 
	 * @see	ITurnArm#rotateToLeft()
	 */
	public void turnArm_rotateToLeft() throws RemoteException;
	
	/**
	 * Rotate the turn arm to the right.
	 * 
	 * @throws	RemoteException
	 * 
	 * @see	ITurnArm#rotateToRight()
	 */
	public void turnArm_rotateToRight() throws RemoteException;
	
	/**
	 * Returns the turn angle.
	 * 
	 * @return	int the turn angle of the turn arm.
	 * 
	 * @see	ITurnArm#getTurnAngle()
	 */
	public int turnArm_getTurnAngle();
	
	/**
	 * Returns if the turn arm is centered.
	 * 
	 * @throws	RemoteException
	 * 
	 * @see	ITurnArm#isCentered()
	 */
	public boolean turnArm_isCentered();
	
	public void rotate(int degrees) throws TurnNotPossible;
	public void travel(double distance);
	public void travel(double distance, boolean immediateReturn);
	public void stop();
	public void setTravelSpeed(int speed);
	public void setRotateSpeed(double speed);

	
	
	//position
	public Orientation getOrientation();	
	public Direction getTurnDirection();
	public void setTurnDirection(Direction turnDirection);
	public void invertTurnDirection();
	
	//movement
	public Move getMovement();
	
	
	//sensors
	public float getDistance() throws RemoteException; 	
	public float getHeading() throws RemoteException;
	public float getLightIntensity();
	public float getLightColor();
	
	
	//grappler
	public void grapObject();
	
}
