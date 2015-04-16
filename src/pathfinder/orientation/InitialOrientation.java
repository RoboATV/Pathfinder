package pathfinder.orientation;

import java.rmi.RemoteException;

import pathfinder.configuration.Configuration;
import pathfinder.robot.Direction;
import pathfinder.robot.IRobot;

public class InitialOrientation {

	private IRobot robot;


	public InitialOrientation(IRobot robot){
		this.robot = robot;
	}
	
	
	public Direction alignRobot() throws TurnNotPossible, RemoteException{
		
		//check right wall
		robot.rotateTurnArm(90);
		float rightDistance = robot.getDistance();
		
		if(rightDistance < Configuration.INITIAL_WALLDISTANCE){
			robot.rotateTurnArm(-90);
			return Direction.LEFT;
		}
		
		//check left wall
		robot.rotateTurnArm(-90);
		float leftDistance = robot.getDistance();
		
		if(leftDistance < Configuration.INITIAL_WALLDISTANCE){
			robot.rotateTurnArm(90);
			return Direction.RIGHT;
		}
		
		robot.rotate(Configuration.INITIAL_DIRECTION.getTurnAngle());
		
		return Direction.getOpposite(Configuration.INITIAL_DIRECTION);
		
	}
	
	
}
