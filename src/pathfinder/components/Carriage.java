package pathfinder.components;

import lejos.robotics.navigation.DifferentialPilot;
import lejos.robotics.navigation.Move;
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
//		this.pilot.rotate(degrees);
		int startHeading	= this.robot.getHeading();
		degrees = degrees % 360;
		int aimHeading		= startHeading + degrees;
		
		if(aimHeading < 0) {
			aimHeading += 360;
		}
		
		System.out.println("Start heading: " + startHeading + "; Aim heading: " + aimHeading + "; Degrees: " + degrees);
		
		if(degrees < 0) {
			this.pilot.rotateRight();
		} else {
			this.pilot.rotateLeft();
		}
		
		while(!this.isHeadingNear(aimHeading, this.robot.getHeading(), 2)) {
//			System.out.println("Heading: " + this.robot.getHeading());
//			Delay.msDelay(100);
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
		Move move = this.pilot.getMovement();
		return new Move(move.getMoveType(), (float) (move.getDistanceTraveled() / this.ratio), move.getAngleTurned(), move.isMoving());
	}
	
	@Override
	public Orientation getOrientation() {
		return this.orientation;
	}
	
	/**
	 * Checks if the heading is near a aim heading to have a tolerance.
	 * 
	 * @param	int	aim
	 *   The aim heading.
	 * @param	int	measured
	 *   The measured heading.
	 * @param	int	tolerance
	 *   The tolerance for the heading.
	 * @return	boolean
	 *   if the heading is near the aim heading.
	 */
	public boolean isHeadingNear(int aim, int measured, int tolerance) {
		for (int i = -tolerance; i < tolerance; i++) {
			int toCheck = (measured + i) % 360;
			
			if(toCheck < 0) {
				toCheck += 360;
			}
			
			if(aim == toCheck) {
				return true;
			}
		}
		
		return false;
	}
}