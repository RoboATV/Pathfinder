package pathfinder.robot;

import java.rmi.RemoteException;

import pathfinder.orientation.Orientation;
import pathfinder.orientation.TurnNotPossible;


public interface IRobot {

	//movement
	public void rotateTurnArm(int degrees) throws RemoteException;
	public void rotate(int degrees) throws TurnNotPossible;
	public void travel(double distance);
	public void stop();
	
	
	//position
	public Orientation getOrientation();
	
	public Direction getTurnDirection();
	public void setTurnDirection(Direction turnDirection);
	
	
	//sensors
	public float getDistance() throws RemoteException; 
	
	public float getHeading() throws RemoteException;
	
	
}
