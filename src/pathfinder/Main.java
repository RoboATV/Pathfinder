package pathfinder;

import java.net.MalformedURLException;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;

import lejos.robotics.subsumption.Arbitrator;
import lejos.robotics.subsumption.Behavior;
import pathfinder.location.Locator;
import pathfinder.orientation.InitialOrientation;
import pathfinder.orientation.TurnNotPossible;
import pathfinder.robot.Robot;

public class Main {

	public static void main(String[] args) {
		
		System.out.println("starting main");
		 
		try {
			Robot robot = new Robot();		
			System.out.println("new robot initialized");
			
			Locator locator = new Locator(robot);
			InitialOrientation initialOrientation = new InitialOrientation(robot);
			
			System.out.println("loading behaviors");
			Behavior drive = new Drive(robot, locator);
			Behavior avoidObstacle = new AvoidObstacle(robot, locator);
			
			Behavior[] behaviors = new Behavior[2];
			behaviors[0] = drive;
			behaviors[1] = avoidObstacle;
			
			
			try {
				System.out.println("align robot");
				robot.setTurnDirection(initialOrientation.alignRobot());
			} catch (TurnNotPossible | RemoteException e) {
				System.out.println(e.toString());
			}
			System.out.println("initializing arbitrator");
			Arbitrator arbitrator = new Arbitrator(behaviors);
			arbitrator.start();
			
//			locator.relocate(new Coordinate(0, 20));
			
			robot.shutdown();
		} catch(RemoteException | MalformedURLException | NotBoundException e){
			e.printStackTrace();
		}
		
	}

}
