package pathfinder.components;

import lejos.robotics.navigation.Move;
import pathfinder.orientation.Orientation;
import pathfinder.orientation.TurnNotPossible;

/**
 * Interface for a carriage.
 */
public interface ICarriage {
	/**
	 * Travel a given distance.
	 * 
	 * @param	double	distance
	 *   The distance to travel.
	 * @param	boolean	immediateReturn
	 *   If the method should return immediately so other methods can invoke now.
	 */
	public void travel(double distance, boolean immediateReturn);
	
	/**
	 * Rotate the robot a specified amount of degrees,
	 * 
	 * @param	int	degrees
	 *   the degrees to rotate.
	 * 
	 * @throws	TurnNotPossible
	 */
	public void rotate(int degrees) throws TurnNotPossible;
	
	/**
	 * Turn 90 degrees to the left.
	 * 
	 * @throws	TurnNotPossible
	 */
	public void turnLeft() throws TurnNotPossible;
	
	/**
	 * Turn 90 degrees to the right.
	 * 
	 * @throws TurnNotPossible
	 */
	public void turnRight() throws TurnNotPossible;
	
	/**
	 * Stop the movement.
	 */
	public void stop();
	
	/**
	 * Check if the robot is moving.
	 * 
	 * @return	boolean
	 *   if the robot is moving.
	 */
	public boolean isMoving();
	
	/**
	 * Get the actual movement while the robot moves.
	 * 
	 * @return	Move
	 *   the actual movement.
	 */
	public Move getMovement();
	
	/**
	 * Get the orientation of the carriage in the coordinate system.
	 * 
	 * @return	Orientation
	 *   the orientation
	 */
	public Orientation getOrientation();
}
