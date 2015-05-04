package pathfinder;

import lejos.robotics.subsumption.Behavior;
import pathfinder.location.Locator;
import pathfinder.robot.Robot;

public class RescueVictim implements Behavior {
	private Robot robot;
	private boolean suppressed = false;
	private Locator locator;
	
	public RescueVictim(Robot robot, Locator locator){
		this.robot = robot;
		this.locator = locator;
	}
	
	@Override
	public void action() {
		
	}

	@Override
	public boolean takeControl() {
		if(!this.robot.carriage_isMoving() && this.robot.turnArm_isCentered() && !this.robot.grappler_isLoaded() && this.robot.victimDetected()) {
			return true;
		}
		
		return false;
	}

	@Override
	public void suppress() {
		this.suppressed = true;
	}
}
