package pathfinder;

import java.net.MalformedURLException;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;

import lejos.robotics.subsumption.Arbitrator;
import lejos.robotics.subsumption.Behavior;
import pathfinder.behaviors.AvoidObstacle;
import pathfinder.behaviors.Drive;
import pathfinder.behaviors.PickUpVictim;
import pathfinder.behaviors.RescueVictim;
import pathfinder.behaviors.Shutdown;
import pathfinder.location.Locator;
import pathfinder.orientation.InitialOrientation;
import pathfinder.orientation.TurnNotPossible;
import pathfinder.robot.Robot;

public class Main {
	private static Behavior[] behaviors		= new Behavior[2];
	private static Arbitrator arbitrator	= new Arbitrator(behaviors);
	
	public static void shutdown(){
		arbitrator.stop();
		System.exit(0);
	}

	public static void main(String[] args) {
		System.out.println("Start set up...");
		
		try {
			Robot robot = new Robot();
			
//			Locator locator = new Locator(robot);
//			InitialOrientation initialOrientation = new InitialOrientation(robot);
			
			System.out.println("Load behaviors...");
//			Behavior drive			= new Drive(robot, locator);
//			Behavior avoidObstacle	= new AvoidObstacle(robot, locator);
			Behavior pickUpVictim	= new PickUpVictim(robot);
//			Behavior rescueVictim	= new RescueVictim(robot, locator);
			Behavior shutdown		= new Shutdown(robot);
			
//			behaviors[0] = drive;
//			behaviors[1] = avoidObstacle;
			behaviors[0] = pickUpVictim;
//			behaviors[3] = rescueVictim;
			behaviors[1] = shutdown;
			System.out.println("Behaviors loaded...");
			
//			System.out.println("Align robot...");
//			try {
//				robot.setTurnDirection(initialOrientation.alignRobot());
//			} catch (TurnNotPossible | RemoteException e) {
//				System.out.println(e.toString());
//			}
//			System.out.println("Robot aligned...");
			
			System.out.println("Initialize arbitrator...");
			arbitrator.start();
			System.out.println("Arbitrator initialized...");
			
//			locator.relocate(new Coordinate(0, 20));
		} catch(RemoteException | MalformedURLException | NotBoundException e){
			e.printStackTrace();
		}
		
		System.out.println("Robot set up - let the games begin ;)");
	}
}
