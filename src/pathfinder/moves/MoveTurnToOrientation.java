package pathfinder.moves;

import pathfinder.orientation.IOrientation;
import pathfinder.orientation.TurnNotPossible;
import pathfinder.robot.IRobot;

public class MoveTurnToOrientation implements IMove {
	/**
	 * The orientation the robot should have in the end.
	 * 
	 * @type	IOrientaion
	 */
	private IOrientation	orientation;
	
	/**
	 * The robot to execute the turn move on.
	 * 
	 * @type	IRobot
	 */
	private IRobot	robot;
	
	/**
	 * Turns the robot to a specified orientation.
	 * 
	 * @param	IRobot			robot
	 *   the robot to execute the turn move on.
	 * @param	IOrientation	orientation
	 *   the orientation to turn the robot to.
	 */
	public MoveTurnToOrientation(IRobot robot, IOrientation orientation) {
		this.robot			= robot;
		this.orientation	= orientation;
	}
	
	@Override
	public void execute() {
		int	angleNow 		= this.robot.carriage_getOrientation().getAngle();
		int angleToReach	= this.orientation.getAngle();
		
		try {
			this.robot.carriage_rotate(angleToReach - angleNow);
		} catch (TurnNotPossible e) {
			System.out.println(e.toString());
		}
	}
}
