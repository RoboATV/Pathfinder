package pathfinder.orientation;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;

import java.util.LinkedList;
import java.util.Queue;

import org.junit.Before;
import org.junit.Test;

import pathfinder.configuration.Configuration;
import pathfinder.robot.Direction;
import pathfinder.robot.ITestRobot;
import pathfinder.robot.TestRobot;

public class InitialOrientationTest {

	
	private ITestRobot robot;
	private InitialOrientation initialOrientation;

	@Before
	public void startup(){
		this.robot = new TestRobot();
		this.initialOrientation = new InitialOrientation(this.robot);
	}
	
	
	@Test
	public void correctInitialOrientation() {
		Queue<Float> distances = new LinkedList<Float>();
		distances.add(30.0f);
		
		distances.add(Float.POSITIVE_INFINITY);
		distances.add(30.0f);
		
		distances.add(Float.POSITIVE_INFINITY);
		distances.add(Float.POSITIVE_INFINITY);
		
		robot.setDistances(distances);
		
		
		//wall on right side
		try {
			assertEquals(initialOrientation.alignRobot(), Direction.LEFT);
		} catch (TurnNotPossible e) {
			fail(e.toString());
		}
		
		
		//wall on left side
		try {
			assertEquals(initialOrientation.alignRobot(), Direction.RIGHT);
		} catch (TurnNotPossible e) {
			fail(e.toString());
		}
		
		
		//no wall
		try {
			assertEquals(initialOrientation.alignRobot(), Direction.getOpposite(Configuration.INITIAL_DIRECTION));
		} catch (TurnNotPossible e) {
			fail(e.toString());
		}
		
	}
	
	
}
