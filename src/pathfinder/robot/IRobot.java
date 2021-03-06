package pathfinder.robot;

import java.rmi.RemoteException;

import lejos.robotics.geometry.Rectangle2D;
import lejos.robotics.navigation.Move;
import pathfinder.components.ICarriage;
import pathfinder.components.ITurnArm;
import pathfinder.orientation.Orientation;
import pathfinder.orientation.TurnNotPossible;

/**
 * Interface for a robot.
 */
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
	
	/**
	 * Rotate the robot a specified amount of degrees.
	 * 
	 * @param	int	degrees
	 *   the degrees to rotate.
	 * 
	 * @throws	TurnNotPossible
	 * 
	 * @see	ICarriage#rotate()
	 */
	public void carriage_rotate(int degrees) throws TurnNotPossible;
	
	/**
	 * Rotate the robot a specified amount of degrees. Do not check if the orientation would be correct afterwards!
	 * 
	 * @param	int	degrees
	 *   the degrees to rotate.
	 * 
	 * @see	ICarriage#rotateUnchecked()
	 */
	public void carriage_rotateUnchecked(int degrees);
	
	/**
	 * Turn 90 degrees to the left.
	 * 
	 * @throws	TurnNotPossible
	 * 
	 * @see	ICarriage#turnLeft()
	 */
	public void carriage_turnLeft() throws TurnNotPossible;
	
	/**
	 * Turn 90 degrees to the right.
	 * 
	 * @throws TurnNotPossible
	 * 
	 * @see	ICarriage#turnRight()
	 */
	public void carriage_turnRight() throws TurnNotPossible;
	
	/**
	 * Travel a given distance.
	 * 
	 * @param	double	distance
	 *   The distance to travel.
	 * 
	 * @see	ICarriage#travel(double, boolean)
	 */
	public void carriage_travel(double distance);
	
	/**
	 * Travel a given distance.
	 * 
	 * @param	double	distance
	 *   The distance to travel.
	 * @param	boolean	immediateReturn
	 *   If the method should return immediately so other methods can invoke now.
	 * 
	 * @see	ICarriage#travel(double, boolean)
	 */
	public void carriage_travel(double distance, boolean immediateReturn);
	
	/**
	 * Stop the carriage movement.
	 * 
	 * @see	ICarriage#stop()
	 */
	public void carriage_stop();
	
	/**
	 * Check if the carriage is moving.
	 * 
	 * @return	boolean
	 *   if the carriage is moving.
	 * 
	 * @see	ICarriage#stop()
	 */
	public boolean carriage_isMoving();
	
	/**
	 * Get the actual movement while the robot moves.
	 * 
	 * @return	Move
	 *   the actual movement.
	 * 
	 * @see	ICarriage#getMovement()
	 */
	public Move carriage_getMovement();
	
	/**
	 * Get the orientation of the carriage in the coordinate system.
	 * 
	 * @return	Orientation
	 *   the orientation
	 * 
	 * @see	ICarriage#getOrientation()
	 */
	public Orientation carriage_getOrientation();
	
	/**
	 * Let the grappler grap an object.
	 * 
	 * @see	IGrappler#grap()
	 */
	public void grappler_grap();
	
	/**
	 * Let the grappler release an object.
	 * 
	 * @see	IGrappler#release()
	 */
	public void grappler_release();
	
	/**
	 * Returns if the grappler already has an object loaded.
	 * 
	 * @return	boolean
	 *   if the grappler is loaded
	 * 
	 * @see	IGrappler#isLoaded()
	 */
	public boolean grappler_isLoaded();
	
	/**
	 * Get the now used turn direction.
	 * 
	 * @return	Direction
	 *   the turn direction.
	 */
	public Direction getTurnDirection();
	
	/**
	 * Set the turn direction.
	 * 
	 * @param	Direction	turnDirection
	 *   The new turn direction.
	 */
	public void setTurnDirection(Direction turnDirection);
	
	/**
	 * Invert the turn direction.
	 */
	public void invertTurnDirection();
	
	/**
	 * Returns the distance measured by the ultrasonic sensor.
	 * 
	 * @return	float
	 *   the measured distance.
	 * @throws RemoteException
	 */
	public float getDistance() throws RemoteException;
	
	/**
	 * Checks if there is any victim detected by the color sensor.
	 * 
	 * @return	boolean
	 *   if there is any victim detected by the color sensor.
	 */
	public boolean victim_detectedColorSensor();
	
	/**
	 * Checks if there is any victim detected by the camera sensor.
	 * 
	 * @return	boolean
	 *   if there is any victim detected by the camera sensor.
	 */
	public boolean victim_detectedCamera();
	
	/**
	 * Returns the location of a rectangle containing the victim in the camera picture.
	 * 
	 * @return	Rectangle2D
	 *   the location of the victim in the camera picture. If there is no victim detected, return null.
	 */
	public Rectangle2D victim_getLocation();
	
	/**
	 * Returns the heading of the compass sensor.
	 * 
	 * @return	int
	 *   the heading of the compass.
	 */
	public int getHeading();
	
	/**
	 * Do a shutdown of the robot. Free all used ports etc.
	 */
	public void shutdown();
}
