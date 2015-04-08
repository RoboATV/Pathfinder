package pathfinder;

import lejos.robotics.subsumption.Behavior;
import pathfinder.configuration.Configuration;
import pathfinder.robot.Robot;

public class AvoidObstacle implements Behavior{

	private Robot robot;
	private boolean suppressed = false;
	
	public AvoidObstacle(Robot robot){
		this.robot = robot;
	}
	
	
	@Override
	public boolean takeControl() {		
		return robot.getDistance() < Configuration.WALLDISTANCE;
	}

	@Override
	public void action() {
		if(!suppressed){
			this.robot.stop();		
		}	
	}

	@Override
	public void suppress() {
		this.suppressed = true;		
	}

}
