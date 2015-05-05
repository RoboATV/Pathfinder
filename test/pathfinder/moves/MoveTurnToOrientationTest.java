package pathfinder.moves;

import static org.junit.Assert.*;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import pathfinder.orientation.Orientation;
import pathfinder.robot.ITestRobot;
import pathfinder.robot.TestRobot;

public class MoveTurnToOrientationTest {
	private ITestRobot robot;
	
	@Before
	public void startup(){
		this.robot = new TestRobot();
	}
	
	@Test
	public void correctTurnToNorth() {
		for (Orientation orientation : Orientation.values()) {
			this.robot.setOrientation(orientation);
			new MoveTurnToOrientation(robot, Orientation.NORTH).execute();
			assertEquals(Orientation.NORTH, this.robot.carriage_getOrientation());
		}
	}
	
	@Test
	public void correctTurnToEast() {
		for (Orientation orientation : Orientation.values()) {
			this.robot.setOrientation(orientation);
			new MoveTurnToOrientation(robot, Orientation.EAST).execute();
			assertEquals(Orientation.EAST, this.robot.carriage_getOrientation());
		}
	}
	
	@Test
	public void correctTurnToSouth() {
		for (Orientation orientation : Orientation.values()) {
			this.robot.setOrientation(orientation);
			new MoveTurnToOrientation(robot, Orientation.SOUTH).execute();
			assertEquals(Orientation.SOUTH, this.robot.carriage_getOrientation());
		}
	}
	
	@Test
	public void correctTurnToWest() {
		for (Orientation orientation : Orientation.values()) {
			this.robot.setOrientation(orientation);
			new MoveTurnToOrientation(robot, Orientation.WEST).execute();
			assertEquals(Orientation.WEST, this.robot.carriage_getOrientation());
		}
	}
	
	@After
	public void shutdown(){
		
	}
}
