package pathfinder.components;

import lejos.robotics.navigation.DifferentialPilot;
import lejos.robotics.navigation.Move;
import pathfinder.orientation.NoOrientationToAngle;
import pathfinder.orientation.Orientation;
import pathfinder.orientation.TurnNotPossible;

public class Carriage implements ICarriage {
	/**
	 * The differntial pilot for easier turns, drives etc.
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
	 *   The differntial pilot for easier turns, drives etc.
	 * @param	double				ratio
	 *   The ratio the distances get multiplied with.
	 */
	public Carriage(DifferentialPilot pilot, double ratio, Orientation initialOrientation) {
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
		
		this.pilot.rotate(degrees);
	}
	
	@Override
	public void rotateUnchecked(int degrees) {
		this.pilot.rotate(degrees);
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
		this.pilot.stop();
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