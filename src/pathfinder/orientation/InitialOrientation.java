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
		System.out.println("check right side");
		robot.rotateTurnArm(90);
		float rightDistance = robot.getDistance();
		robot.rotateTurnArm(-90);
		if(rightDistance < Configuration.INITIAL_WALLDISTANCE){
			System.out.println("wall on right side");
			return Direction.LEFT;
		}
		
		//check left wall
		System.out.println("check left side");
		robot.rotateTurnArm(-90);
		float leftDistance = robot.getDistance();
		robot.rotateTurnArm(90);
		if(leftDistance < Configuration.INITIAL_WALLDISTANCE){
			System.out.println("wall on left side");
			return Direction.RIGHT;
		}
		
		System.out.println("no wall");
		robot.rotate(Configuration.INITIAL_DIRECTION.getTurnAngle());
		
		return Direction.getOpposite(Configuration.INITIAL_DIRECTION);
		
	}
	
	
}
