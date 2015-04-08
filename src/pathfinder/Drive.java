package pathfinder;

import lejos.robotics.subsumption.Behavior;
import pathfinder.location.Locator;
import pathfinder.map.Coordinate;
import pathfinder.robot.Orientation;
import pathfinder.robot.Robot;
import pathfinder.robot.TurnNotPossible;

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
			//this.robot.printArray(this.robot.mapToArray(this.locator.map, this.locator.currentPos));
			Coordinate newPos = locator.getNextCoordinate();
			try {
				this.relocateRobot(newPos);
			} catch (TurnNotPossible e) {				
				e.printStackTrace();
			}
			}
	}

	@Override
	public void suppress() {		
		suppressed = true;
	}

	
	
	
	private void relocateRobot(Coordinate position) throws TurnNotPossible{
		int distanceX = position.X - locator.currentPos.X;
		int distanceY = position.Y - locator.currentPos.Y;
		
		if(this.robot.getOrientation() == Orientation.NORTH){
			if(distanceY != 0){
				robot.travel(distanceY);
			} 
			if(distanceX > 0){
				robot.rotate(90);
				robot.travel(distanceX);
				robot.rotate(-90);
			} else if(distanceX < 0){
				robot.rotate(-90);
				robot.travel(distanceX);
				robot.rotate(90);
			}			
		} else if(this.robot.getOrientation() == Orientation.EAST){
			
		} else if(this.robot.getOrientation() == Orientation.SOUTH){
			
		} else if(this.robot.getOrientation() == Orientation.WEST){
			
		}
		
	}	
		
}	
