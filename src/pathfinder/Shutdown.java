package pathfinder;

import lejos.hardware.Button;
import lejos.robotics.subsumption.Behavior;
import pathfinder.robot.Robot;

public class Shutdown implements Behavior{

	private boolean suppressed;
	private Robot robot;

	public Shutdown(Robot robot){
		this.robot = robot;
	}
	
	
	@Override
	public void action() {
		
		if(!suppressed){
			Main.shutdown();
		}
		
	}

	@Override
	public void suppress() {
		this.suppressed = true;
	}

	@Override
	public boolean takeControl() {
		return Button.ENTER.isDown();
	}

}
