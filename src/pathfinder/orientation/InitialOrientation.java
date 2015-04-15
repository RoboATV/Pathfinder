package pathfinder.orientation;

import pathfinder.configuration.Configuration;
import pathfinder.robot.Direction;
import pathfinder.robot.IRobot;

public class InitialOrientation {

	private IRobot robot;


	public InitialOrientation(IRobot robot){
		this.robot = robot;
	}
	
	
	public Direction alignRobot() throws TurnNotPossible{
		
		//check right wall
		robot.rotateTurnArm(90);
		float rightDistance = robot.getDistance();
		System.out.println("r" + rightDistance);
		if(rightDistance < Configuration.INITIAL_WALLDISTANCE){
			robot.rotateTurnArm(-90);
			return Direction.LEFT;
		}
		
		//check left wall
		robot.rotateTurnArm(-90);
		float leftDistance = robot.getDistance();
		System.out.println("l" + leftDistance);
		if(leftDistance < Configuration.INITIAL_WALLDISTANCE){
			robot.rotateTurnArm(90);
			return Direction.LEFT;
		}
		
		robot.rotate(Configuration.INITIAL_DIRECTION.getTurnAngle());
		
		return Direction.getOpposite(Configuration.INITIAL_DIRECTION);
		
	}
	
	
}
