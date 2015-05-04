package pathfinder.components;

import java.rmi.RemoteException;

import lejos.remote.ev3.RMIRegulatedMotor;

public class TurnArm implements ITurnArm {
	/**
	 * The transmission ratio between motor and turn table.
	 * 
	 * @type	double
	 */
	private double ratio;
	
	/**
	 * If the motor turn direction should be inverted.
	 * 
	 * @type	boolean
	 */
	private boolean invertDirection;
	
	/**
	 * The motor to turn the arm.
	 */
	private RMIRegulatedMotor motor;
	
	/**
	 * The direction of the turn arm in degrees. 0 is center, negative to left, positive to right.
	 */
	private int direction = 0;
	
	/**
	 * Create a new turn arm.
	 * 
	 * @param	{double}			ratio
	 *   The transmission ratio between motor and turn table.
	 * @param	{boolean}			invertDirection
	 *   The motor turn orientation. Used to make a right turn really a right turn. Either -1 or 1.
	 * @param	{RMIRegulatedMotor}	motor
	 *   Thze motor to turn the arm.
	 */
	public TurnArm(double ratio, boolean invertDirection, RMIRegulatedMotor motor) {
		this.ratio				= ratio;
		this.invertDirection	= invertDirection;
		this.motor				= motor;
	}

	@Override
	public void rotate(int degrees) throws RemoteException {
		int degreesRatio = (int) Math.round(degrees * this.ratio);
		
		if(this.invertDirection == true)
			degreesRatio *= -1;
		
		this.motor.rotate(degreesRatio);
		
		this.direction += degrees;
	}

	@Override
	public void rotateToCenter() throws RemoteException {
		int toRotate = this.direction * -1;
		this.rotate(toRotate);
	}
	
	@Override
	public void rotateToLeft() throws RemoteException {
		int toRotate = -90 - this.direction;
		this.rotate(toRotate);
	}
	
	@Override
	public void rotateToRight() throws RemoteException {
		int toRotate = 90 - this.direction;
		this.rotate(toRotate);
	}
	
	@Override
	public int getTurnAngle() {
		return this.direction;
	}
	
	@Override
	public boolean isCentered() {
		return this.getTurnAngle() == 0;
	}

	@Override
	public void shutdown() throws RemoteException {
		motor.close();
	}
}
