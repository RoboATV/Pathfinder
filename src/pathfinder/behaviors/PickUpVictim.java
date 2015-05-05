package pathfinder.behaviors;

import java.util.ArrayList;
import java.util.Iterator;

import lejos.robotics.geometry.Rectangle2D;
import lejos.robotics.subsumption.Behavior;
import pathfinder.moves.IMove;
import pathfinder.moves.MoveTravel;
import pathfinder.moves.MoveTurnUnchecked;
import pathfinder.robot.Robot;

public class PickUpVictim implements Behavior {
	private Robot	robot;
	private boolean	suppressed = false;
	
	private ArrayList<IMove> movePath = new ArrayList<IMove>();
	
	public PickUpVictim(Robot robot){
		this.robot = robot;
	}
	
	@Override
	public void action() {
		if(!this.suppressed) {
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
		this.movePath.add(0, new MoveTravel(robot, 15));
		this.robot.carriage_travel(15);
		
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
		Rectangle2D victimLocation = this.robot.victim_getLocation();
		int turnFactor		= victimLocation.getCenterX() < 90 ? -1 : 1;
		int turned			= 0;
		int checkStepSize	= 3;
		
		while((turnFactor < 0 && victimLocation.getCenterX() < 90) || (turnFactor > 0 && victimLocation.getCenterX() > 90)) {
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
	
	private int moveMaximumCamera() {
		Rectangle2D victimLocation = this.robot.victim_getLocation();
		int distanceTraveled = 0;
		
		while(victimLocation != null && victimLocation.getY() < 100) {
			this.robot.carriage_travel(3);
			distanceTraveled += 3;
			victimLocation = this.robot.victim_getLocation();
		}
		
		return distanceTraveled;
	}
}
