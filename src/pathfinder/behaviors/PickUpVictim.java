package pathfinder.behaviors;

import java.util.ArrayList;
import java.util.Iterator;

import lejos.robotics.geometry.Rectangle2D;
import lejos.robotics.subsumption.Behavior;
import lejos.utility.Delay;
import pathfinder.moves.IMove;
import pathfinder.moves.MoveTravel;
import pathfinder.moves.MoveTurnUnchecked;
import pathfinder.robot.IRobot;

public class PickUpVictim implements Behavior {
	private IRobot	robot;
	private boolean	suppressed = false;
	
	private ArrayList<IMove> movePath = new ArrayList<IMove>();
	
	public PickUpVictim(IRobot robot){
		System.out.println("  pick up victim");
		this.robot = robot;
	}
	
	@Override
	public void action() {
		suppressed = false;
		if(!this.suppressed) {
			System.out.println("Pick up victim...");
			this.moveToVictim();
			this.grapVictim();
			this.backToDetectionPoint();
		}
	}

	@Override
	public boolean takeControl() {
		if(!this.robot.carriage_isMoving() && this.robot.turnArm_isCentered() && !this.robot.grappler_isLoaded() && this.robot.victim_detectedCamera()) {
			return true;
		}
		
		return false;
	}

	@Override
	public void suppress() {
		this.suppressed = true;
	}
	
	private void moveToVictim() {
		// Turn the robot until the victim is centered.
		this.movePath.add(new MoveTurnUnchecked(robot, -this.centerVictimCamera()));
		
		// Move to the minimum distance recognized by camera.
		this.movePath.add(0, new MoveTravel(robot, this.moveMaximumCamera()));
		
		// Correct the centering of the victim.
		this.movePath.add(0, new MoveTurnUnchecked(robot, -this.centerVictimCamera()));
		
		// Travel to the victim.
		this.movePath.add(0, new MoveTravel(robot, 1.3));
		this.robot.carriage_travel(1.3);
		
		// Correct the centering of the victim.
		this.movePath.add(0, new MoveTurnUnchecked(robot, -this.centerVictimLight()));
	}
	
	private void grapVictim() {
		this.movePath.add(new MoveTurnUnchecked(robot, 180));
		this.robot.carriage_rotateUnchecked(180);
		this.robot.grappler_grap();
	}
	
	private void backToDetectionPoint() {
		for (Iterator<IMove> iterator = movePath.iterator(); iterator.hasNext();) {
			IMove move = (IMove) iterator.next();
			move.execute();
		}
	}
	
	private int centerVictimCamera() {
		Rectangle2D victimLocation = null;
		
		while(victimLocation == null) {
			victimLocation = this.robot.victim_getLocation();
		}
		
		int turnFactor		= victimLocation.getCenterX() < 90 ? -1 : 1;
		int turned			= 0;
		int checkStepSize	= 5;
		
		while(null != victimLocation && (turnFactor < 0 && victimLocation.getCenterX() < 90) || (turnFactor > 0 && victimLocation.getCenterX() > 90)) {
			this.robot.carriage_rotateUnchecked(turnFactor * checkStepSize);
			turned += turnFactor * checkStepSize;
			victimLocation = this.robot.victim_getLocation();
		}
		
		return turned;
	}
	
	private int centerVictimLight() {
		int turned				= 0;
		int maximumAngleToCheck	= 30;
		int checkStepSize		= 10;
		
		while(turned < maximumAngleToCheck && !this.robot.victim_detectedColorSensor()) {
			this.robot.carriage_rotateUnchecked(checkStepSize);
			turned += checkStepSize;
		}
		
		while(turned > -maximumAngleToCheck && !this.robot.victim_detectedColorSensor()) {
			this.robot.carriage_rotateUnchecked(-checkStepSize);
			turned += -checkStepSize;
		}
		
		if(!this.robot.victim_detectedColorSensor()) {
			this.robot.carriage_rotateUnchecked(turned * -1);
			turned = 0;
		}
		
		return turned;
	}
	
	private double moveMaximumCamera() {
		Rectangle2D victimLocation = this.robot.victim_getLocation();
		double distanceTraveled = 0;
		
		this.robot.carriage_travel(200, true);
		
		while(victimLocation != null && victimLocation.getY() < 100) {
			Delay.msDelay(10);
			victimLocation = this.robot.victim_getLocation();
		}
		
		distanceTraveled = (double) this.robot.carriage_getMovement().getDistanceTraveled();
		this.robot.carriage_stop();
		
		return distanceTraveled;
	}
}
