package pathfinder.behaviors;

import lejos.hardware.Button;
import lejos.robotics.subsumption.Behavior;
import pathfinder.Main;
import pathfinder.robot.IRobot;

public class Shutdown implements Behavior{

	private boolean suppressed;
	private IRobot robot;

	public Shutdown(IRobot robot){
		System.out.println("  shutdown");
		this.robot = robot;
	}
	
	
	@Override
	public void action() {
		suppressed = false;
		if(!suppressed){
			System.out.println("Shutdown...");
			robot.shutdown();
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
