package pathfinder;

import java.rmi.RemoteException;

import lejos.robotics.subsumption.Behavior;
import pathfinder.configuration.Configuration;
import pathfinder.location.Locator;
import pathfinder.map.Coordinate;
import pathfinder.orientation.Orientation;
import pathfinder.orientation.TurnNotPossible;
import pathfinder.robot.Robot;

public class Drive implements Behavior{
	
	private Robot robot;
	private boolean suppressed = false;
	private Locator locator;
	
	
	public Drive(Robot robot, Locator locator){
		System.out.println("loading drive");
		this.robot = robot;
		this.locator = locator;
	}

	@Override
	public boolean takeControl() {
		return true;
	}

	@Override
	public void action() {
		while(!suppressed){
			try{
				locator.measureEnvironment();		
				//this.robot.printArray(this.robot.mapToArray(this.locator.map, this.locator.currentPos));
				Coordinate newPos = new Coordinate(0, Configuration.TRAVEL_DISTANCE);
				robot.travel(Configuration.TRAVEL_DISTANCE);		
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
