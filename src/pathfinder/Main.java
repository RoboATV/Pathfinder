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
		
		Robot robot = null;
		try {
			robot = new Robot();
		} catch (RemoteException | MalformedURLException | NotBoundException e) {
			e.printStackTrace();
		}
		
		Locator locator = new Locator(robot);
		InitialOrientation initialOrientation = new InitialOrientation(robot);
		
		Behavior drive = new Drive(robot, locator);
		Behavior avoidObstacle = new AvoidObstacle(robot, locator);
		
		Behavior[] behaviors = new Behavior[1];
		behaviors[0] = drive;
		behaviors[1] = avoidObstacle;
		
		
		try {
			robot.setTurnDirection(initialOrientation.alignRobot());
		} catch (TurnNotPossible | RemoteException e) {
			System.out.println(e.toString());
		}
		
		Arbitrator arbitrator = new Arbitrator(behaviors);
		arbitrator.start();
		
	}

}
