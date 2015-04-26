package pathfinder.components;

import java.rmi.RemoteException;

public interface ITurnArm {
	/**
	 * Turns the arm. Negative value: left, positive value: right.
	 * 
	 * @throws	RemoteException
	 * 
	 * @param	{int}	degrees
	 *   The angle to turn the arm.
	 */
	public void rotate(int degrees) throws RemoteException;
	
	/**
	 * Turns the arm to be centered.
	 * 
	 * @throws	RemoteException
	 */
	public void rotateToCenter() throws RemoteException;
	
	/**
	 * Turns the arm to be at 90 degrees to the left.
	 * 
	 * @throws	RemoteException
	 */
	public void rotateToLeft() throws RemoteException;
	
	/**
	 * Turns the arm to be at 90 degrees to the right.
	 * 
	 * @throws	RemoteException
	 */
	public void rotateToRight() throws RemoteException;
	
	/**
	 * Returns the turn angle in degrees. Negative: to left, positive: to right.
	 * 
	 * @return	int	the turn angle.
	 */
	public int getTurnAngle();
	
	/**
	 * returns if the arm is centered.
	 * 
	 * @return	booelan
	 *   if the arm is centered.
	 */
	public boolean isCentered();
	
	/**
	 * Does a shutdown of the turn arm.
	 * 
	 * @throws	RemoteException
	 */
	public void shutdown() throws RemoteException;
}
