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
	public void setTravelSpeed(int speed);
	public void setRotateSpeed(double speed);
	
	
	//position
	public Orientation getOrientation();	
	public Direction getTurnDirection();
	public void setTurnDirection(Direction turnDirection);
	public void invertTurnDirection();
	
	
	//sensors
	public float getDistance() throws RemoteException; 	
	public float getHeading() throws RemoteException;
	public float getLightIntensity();
	public float getLightColor();
	
	
	//grappler
	public void grapObject();
	
}
