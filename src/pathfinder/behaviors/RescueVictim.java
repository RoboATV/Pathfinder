package pathfinder.behaviors;

import pathfinder.location.Locator;
import pathfinder.orientation.TurnNotPossible;
import pathfinder.robot.IRobot;
import lejos.robotics.subsumption.Behavior;

public class RescueVictim implements Behavior {
	private IRobot	robot;
	private boolean	suppressed = false;
	private Locator	locator;
	
	public RescueVictim(IRobot robot, Locator locator) {
		this.robot = robot;
		this.locator = locator;
	}
	
	@Override
	public void action() {
		if(!this.suppressed) {
			this.travelToStart();
			this.deliverVictim();
			this.travelToLastLocation();
		}
	}

	@Override
	public void suppress() {
		this.suppressed = true;
	}

	@Override
	public boolean takeControl() {
		return this.robot.grappler_isLoaded();
	}
	
	private void travelToStart() {
		// TODO: Travel to start point.
	}
	
	private void deliverVictim() {
		try {
			this.robot.carriage_rotate(180);
		} catch (Exception e) {
			System.out.println(e.getMessage());
		}
		
		this.robot.grappler_release();
	}
	
	private void travelToLastLocation() {
		// TODO: Travel back to last location where the victim was found.
	}

}
