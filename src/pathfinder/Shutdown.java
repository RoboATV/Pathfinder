package pathfinder;

import java.rmi.RemoteException;

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
			try {
				robot.shutdown();
				Main.shutdown();
			} catch (RemoteException e) {
				e.printStackTrace();
			}
			
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
