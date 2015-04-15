package pathfinder;



import static org.junit.Assert.assertEquals;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

import org.junit.Before;
import org.junit.Test;

import pathfinder.configuration.Configuration;
import pathfinder.location.Locator;
import pathfinder.robot.TestRobot;

public class AvoidObstacleTest {

	private Locator locator;
	private TestRobot robot;
	private AvoidObstacle avoidObstacle;

	@Before
	public void startup(){
		this.robot = new TestRobot();
		this.locator = new Locator(robot);
		this.avoidObstacle = new AvoidObstacle(robot, locator);
	}
	
	
	
	
	@Test
	public void correctAngleCalculation() throws NoSuchMethodException, SecurityException, IllegalAccessException, IllegalArgumentException, InvocationTargetException {		    
	    
	    assertEquals(Configuration.WALLDISTANCE, 20, 0);
	    assertEquals(Configuration.OBSTACLE_SIZE, 40, 0);
	    assertEquals(Configuration.OBSTACLE_OFFSET, 5, 0);
	    
	    Method calculateSensorAngle = avoidObstacle.getClass().getDeclaredMethod("calculateSensorAngle");
	    calculateSensorAngle.setAccessible(true);
	    
	    int angle = (int) calculateSensorAngle.invoke(avoidObstacle);
	   
	 
	    assertEquals(angle, 51);
		
		
	}
	
	
	@Test
	public void correctExpectationCalculation() throws IllegalAccessException, IllegalArgumentException, InvocationTargetException, NoSuchMethodException, SecurityException{
		 assertEquals(Configuration.WALLDISTANCE, 20, 0);
		 assertEquals(Configuration.OBSTACLE_SIZE, 40, 0);
		 assertEquals(Configuration.OBSTACLE_OFFSET, 5, 0);		 
		 
		 Method calculateExpectation = avoidObstacle.getClass().getDeclaredMethod("calculateExpectation");
		 calculateExpectation.setAccessible(true);
		    
		 double angle = (double) calculateExpectation.invoke(avoidObstacle);
		   
		 
		 assertEquals(angle, 32, 0.05);
		 
		 
	}
	
	
	
}
