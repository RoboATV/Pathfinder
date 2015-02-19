package pathfinder;

import lejos.robotics.subsumption.Behavior;
import pathfinder.location.Locator;
import pathfinder.map.Coordinate;
import pathfinder.robot.Robot;

public class Drive implements Behavior{
	
	private Robot robot;
	private boolean suppressed = false;
	private Locator locator;
	
	
	public Drive(Robot robot, Locator locator){
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
			locator.measureEnvironment();		
			this.robot.printArray(this.robot.mapToArray(this.locator.map, this.locator.currentPos));
			Coordinate newPos = locator.getNextCoordinate();
			this.relocateRobot(newPos);
			}
	}

	@Override
	public void suppress() {		
		suppressed = true;
	}

	
	private void relocateRobot(Coordinate position){
		if(locator.currentPos.X == position.X){
			int travelY = position.Y - locator.currentPos.Y;
			this.robot.pilot.travel(travelY * 100);
			this.locator.relocate(position);
		}
	}
		
}	
