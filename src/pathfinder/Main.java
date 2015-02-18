package pathfinder;

import lejos.robotics.subsumption.Arbitrator;
import lejos.robotics.subsumption.Behavior;
import pathfinder.location.Locator;
import pathfinder.robot.Robot;

public class Main {

	public static void main(String[] args) {
		
		Robot robot = new Robot();
		Locator locator = new Locator(robot);
		
		Behavior drive = new Drive(robot, locator);
		Behavior avoidObstacle = new AvoidObstacle(robot);
		
		Behavior[] behaviors = new Behavior[1];
		behaviors[0] = drive;
//		behaviors[1] = avoidObstacle;
		
		Arbitrator arbi = new Arbitrator(behaviors);
		arbi.start();
		
	}

}
