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
		robot.turnArm_rotateToRight();
		float rightDistance = robot.getDistance();
		robot.turnArm_rotateToCenter();
		if(rightDistance < Configuration.INITIAL_WALLDISTANCE){
			System.out.println("wall on right side");
			return Direction.LEFT;
		}
		
		//check left wall
		System.out.println("check left side");
		robot.turnArm_rotateToLeft();
		float leftDistance = robot.getDistance();
		robot.turnArm_rotateToCenter();
		if(leftDistance < Configuration.INITIAL_WALLDISTANCE){
			System.out.println("wall on left side");
			return Direction.RIGHT;
		}
		
		System.out.println("no wall");
		robot.rotate(Configuration.INITIAL_DIRECTION.getTurnAngle());
		
		return Direction.getOpposite(Configuration.INITIAL_DIRECTION);
		
	}
	
	
}
