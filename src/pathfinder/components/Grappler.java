package pathfinder.components;

import lejos.robotics.RegulatedMotor;

public class Grappler implements IGrappler {
	/**
	 * The motor to move the grappler up and down.
	 * 
	 * @type	RegulatedMotor
	 */
	private RegulatedMotor move;
	
	/**
	 * The motor to open and close the grappler.
	 * 
	 * @type	RegulatedMotor
	 */
	private RegulatedMotor grap;
	
	/**
	 * The rotation of the move motor in degrees.
	 * 
	 * @type	int
	 */
	private int rotateMoveDegrees;
	
	/**
	 * The rotation of the grap motor in degrees.
	 * 
	 * @type	int
	 */
	private int rotateGrapDegrees;
	
	/**
	 * Stores if the grappler is loaded.
	 * 
	 * @type	boolean
	 */
	private boolean isLoaded;
	
	/**
	 * Create a new grappler.
	 * 
	 * @param	RegulatedMotor	move
	 *   The motor to move the grappler up and down.
	 * @param	RegulatedMotor	grap
	 *   The motor to open and close the grappler.
	 * @param	int				rotateMoveDegrees
	 *   The roation of the move motor in degrees.
	 * @param	int				rotateGrapDegrees
	 *   The rotation of the grapler in degrees to grap something.
	 * @param	double			grapRatio
	 *   The ratio of the grap motor to the grappler.
	 */
	public Grappler(RegulatedMotor move, RegulatedMotor grap, int rotateMoveDegrees, int rotateGrapDegrees, double grapRatio) {
		this.move				= move;
		this.grap				= grap;
		this.rotateMoveDegrees	= rotateMoveDegrees;
		this.rotateGrapDegrees	= (int) (rotateGrapDegrees * grapRatio);
		this.isLoaded			= false;
	}
	
	@Override
	public void grap() {
		if(this.isLoaded()) {
			return;
		}
		
		this.move.rotate(this.rotateMoveDegrees);
		this.grap.rotate(this.rotateGrapDegrees);
		this.move.rotate(-this.rotateMoveDegrees);
		this.isLoaded = true;
	}

	@Override
	public void release() {
		if(!this.isLoaded()) {
			return;
		}
		
		this.grap.rotate(-this.rotateGrapDegrees);
		this.isLoaded = false;
	}
	
	@Override
	public boolean isLoaded() {
		return this.isLoaded;
	}
}
