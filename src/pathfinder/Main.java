package pathfinder;

import lejos.robotics.subsumption.Arbitrator;
import lejos.robotics.subsumption.Behavior;

public class Main {

	public static void main(String[] args) {
		
		Robot robot = new Robot();
		
		Behavior drive = new Drive(robot);
		Behavior avoidObstacle = new AvoidObstacle(robot);
		
		Behavior[] behaviors = new Behavior[1];
		behaviors[0] = drive;
//		behaviors[1] = avoidObstacle;
		
		Arbitrator arbi = new Arbitrator(behaviors);
		arbi.start();
		
	}

}
