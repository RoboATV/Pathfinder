package pathfinder.behaviors;

import java.rmi.RemoteException;

import lejos.robotics.subsumption.Behavior;
import pathfinder.configuration.Configuration;
import pathfinder.location.Locator;
import pathfinder.robot.IRobot;

public class Drive implements Behavior{
	
	private IRobot robot;
	private boolean suppressed = false;
	private Locator locator;
	
	public Drive(IRobot robot, Locator locator){
		System.out.println("  drive");
		this.robot = robot;
		this.locator = locator;
	}

	@Override
	public boolean takeControl() {
		return true;
	}

	@Override
	public void action() {
		suppressed = false;
		while(!suppressed){
			System.out.println("Drive...");
			try{
				if(!robot.carriage_isMoving()){
					locator.measureEnvironment();						
				//this.robot.printArray(this.robot.mapToArray(this.locator.map, this.locator.currentPos));

					locator.travelAhead(Configuration.TRAVEL_DISTANCE, true);		
		
				}

				
			} catch(RemoteException e){
				e.printStackTrace();
			}
		}
	}

	@Override
	public void suppress() {		
		suppressed = true;
	}
}	
