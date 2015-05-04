package pathfinder;

import java.net.MalformedURLException;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;

import lejos.robotics.subsumption.Arbitrator;
import lejos.robotics.subsumption.Behavior;
import pathfinder.behaviors.AvoidObstacle;
import pathfinder.behaviors.Drive;
import pathfinder.behaviors.PickUpVictim;
import pathfinder.behaviors.Shutdown;
import pathfinder.location.Locator;
import pathfinder.orientation.InitialOrientation;
import pathfinder.orientation.TurnNotPossible;
import pathfinder.robot.Robot;

public class Main {
	private static Behavior[] behaviors		= new Behavior[4];
	private static Arbitrator arbitrator	= new Arbitrator(behaviors);
	
	public static void shutdown(){
		arbitrator.stop();
		System.exit(0);
	}

	public static void main(String[] args) {
		System.out.println("starting main");
		
		try {
			Robot robot = new Robot();		
			System.out.println("new robot initialized");
			
			Locator locator = new Locator(robot);
			InitialOrientation initialOrientation = new InitialOrientation(robot);
			
			System.out.println("loading behaviors");
			Behavior drive			= new Drive(robot, locator);
			Behavior avoidObstacle	= new AvoidObstacle(robot, locator);
			Behavior pickUpVictim	= new PickUpVictim(robot);
			Behavior shutdown		= new Shutdown(robot);
			
			behaviors[0] = drive;
			behaviors[1] = avoidObstacle;
			behaviors[2] = pickUpVictim;
			behaviors[3] = shutdown;
			
			try {
				System.out.println("align robot");
				robot.setTurnDirection(initialOrientation.alignRobot());
			} catch (TurnNotPossible | RemoteException e) {
				System.out.println(e.toString());
			}
			
			System.out.println("initializing arbitrator");
			arbitrator.start();
			
//			locator.relocate(new Coordinate(0, 20));
		} catch(RemoteException | MalformedURLException | NotBoundException e){
			e.printStackTrace();
		}
		
	}

}
