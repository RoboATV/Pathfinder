package pathfinder.rescueVictim;

import pathfinder.robot.IRobot;

public class MoveTravel implements IMove {
	/**
	 * The distance to travel.
	 * 
	 * @type	double
	 */
	private double	distance;
	
	/**
	 * The robot to execute the travel move on.
	 * 
	 * @type	IRobot
	 */
	private IRobot	robot;
	
	/**
	 * Lets the robot travel for a specified distance.
	 * 
	 * @param	IRobot	robot
	 *   the robot to execute the travel move on.
	 * @param	double	distance
	 *   the distance to travel.
	 */
	public MoveTravel(IRobot robot, double distance) {
		this.distance	= distance;
		this.robot		= robot;
	}
	
	@Override
	public void execute() {
		this.robot.carriage_travel(this.distance);
	}

}
