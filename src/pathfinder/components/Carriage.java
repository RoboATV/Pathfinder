package pathfinder.components;

import lejos.robotics.navigation.DifferentialPilot;
import lejos.robotics.navigation.Move;
import lejos.utility.Delay;
import pathfinder.orientation.NoOrientationToAngle;
import pathfinder.orientation.Orientation;
import pathfinder.orientation.TurnNotPossible;
import pathfinder.robot.IRobot;

public class Carriage implements ICarriage {
	/**
	 * The robot.
	 * 
	 * @type	IRobot
	 */
	private	IRobot robot;
	
	/**
	 * The differential pilot for easier turns, drives etc.
	 * 
	 * @type	DifferentialPilot
	 */
	private	DifferentialPilot pilot;
	
	/**
	 * The ratio the distances get multiplied with.
	 * 
	 * @type	double
	 */
	private	double ratio;
	
	/**
	 * The orientation of the robot in its coordinate system.
	 * 
	 * @type	Orientation
	 */
	private Orientation orientation;
	
	/**
	 * Create a new carriage.
	 * 
	 * @param	DifferentialPilot	pilot
	 *   The differential pilot for easier turns, drives etc.
	 * @param	double				ratio
	 *   The ratio the distances get multiplied with.
	 * @param	Orientation			initialOrientation
	 *   The initial orientation of the robot.
	 */
	public Carriage(IRobot robot, DifferentialPilot pilot, double ratio, Orientation initialOrientation) {
		this.robot			= robot;
		this.pilot			= pilot;
		this.ratio			= ratio;
		this.orientation	= initialOrientation;
	}

	@Override
	public void rotate(int degrees) throws TurnNotPossible {
		int newAngle = this.orientation.getAngle() + degrees;
		
		try {
			this.orientation = Orientation.getOrientation(newAngle);
		} catch (NoOrientationToAngle e) {
			throw new TurnNotPossible(degrees);
		}
		
		this.rotateUnchecked(degrees);
	}
	
	@Override
	public void rotateUnchecked(int degrees) {
		int startHeading	= this.robot.getHeading();
		degrees = degrees % 360;
		int aimHeading		= startHeading + degrees;
		
		if(aimHeading < 0) {
			aimHeading += 360;
		}
		
		if(degrees < 0) {
			this.pilot.rotateLeft();
		} else {
			this.pilot.rotateRight();
		}
		
		
		while(this.robot.getHeading() != aimHeading) {
			Delay.msDelay(100);
		}
		
		this.stop();
	}
	
	@Override
	public void turnLeft() throws TurnNotPossible {
		this.rotate(-90);
	}
	
	@Override
	public void turnRight() throws TurnNotPossible {
		this.rotate(90);
	}
	
	@Override
	public void travel(double distance, boolean immediateReturn) {
		this.pilot.travel(distance * this.ratio, immediateReturn);
	}
	
	@Override
	public void stop() {
		this.pilot.quickStop();
	}
	
	@Override
	public boolean isMoving() {
		return this.pilot.isMoving();
	}
	
	@Override
	public Move getMovement() {
		return this.pilot.getMovement();
	}
	
	@Override
	public Orientation getOrientation() {
		return this.orientation;
	}
}