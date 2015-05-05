package pathfinder.moves;

import pathfinder.robot.IRobot;

public class MoveTurnUnchecked implements IMove {
	/**
	 * The angle to turn.
	 * 
	 * @type	int
	 */
	private int		degrees;
	
	/**
	 * The robot to execute the turn move on.
	 * 
	 * @type	IRobot
	 */
	private IRobot	robot;
	
	/**
	 * Lets the robot turn for a specified angle.
	 * 
	 * @param	IRobot	robot
	 *   the robot to execute the turn move on.
	 * @param	int		degrees
	 *   the angle to turn.
	 */
	public MoveTurnUnchecked(IRobot robot, int degrees) {
		this.degrees	= degrees;
		this.robot		= robot;
	}
	
	@Override
	public void execute() {
		this.robot.carriage_rotateUnchecked(this.degrees);
	}
}
