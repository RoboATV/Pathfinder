package pathfinder;



import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Queue;

import org.junit.Before;
import org.junit.Test;

import pathfinder.configuration.Configuration;
import pathfinder.location.Locator;
import pathfinder.map.Coordinate;
import pathfinder.orientation.Orientation;
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
	    
		Queue<Float> distances = new LinkedList<Float>();
		distances.add(new Float(20));
		robot.setDistances(distances);
	    assertEquals(Configuration.OBSTACLE_SIZE, 40, 0);
	    assertEquals(Configuration.OBSTACLE_OFFSET, 5, 0);
	    
	    Method calculateSensorAngle = avoidObstacle.getClass().getDeclaredMethod("calculateSensorAngle");
	    calculateSensorAngle.setAccessible(true);
	    
	    int angle = (int) calculateSensorAngle.invoke(avoidObstacle);
	   
	 
	    assertEquals(angle, 51);
		
		
	}
	
	
	@Test
	public void correctExpectationCalculation() throws IllegalAccessException, IllegalArgumentException, InvocationTargetException, NoSuchMethodException, SecurityException{
		 Queue<Float> distances = new LinkedList<Float>();
		 distances.add(new Float(20));
		 robot.setDistances(distances);
		 assertEquals(Configuration.OBSTACLE_SIZE, 40, 0);
		 assertEquals(Configuration.OBSTACLE_OFFSET, 5, 0);		 
		 
		 Method calculateExpectation = avoidObstacle.getClass().getDeclaredMethod("calculateExpectation");
		 calculateExpectation.setAccessible(true);
		    
		 double angle = (double) calculateExpectation.invoke(avoidObstacle);
		   
		 
		 
		 assertEquals(32, angle, 5);
		 
	}
	
	
	@Test
	public void correctWallDetection() throws NoSuchMethodException, SecurityException, IllegalAccessException, IllegalArgumentException, InvocationTargetException{
		 Queue<Float> expDistances = new LinkedList<Float>();
		 expDistances.add(new Float(20));
		 robot.setDistances(expDistances);
		 assertEquals(Configuration.OBSTACLE_SIZE, 40, 0);
		 assertEquals(Configuration.OBSTACLE_OFFSET, 5, 0);	
		 		 
		 List<Double> distances = new ArrayList<Double>();
		 distances.add(32.0);
		 distances.add(31.0);
		 
		 Class[] cArg = new Class[1];
	     cArg[0] = List.class;   
	        	
	     Method detectWall = avoidObstacle.getClass().getDeclaredMethod("detectWall", cArg);    	     
	     detectWall.setAccessible(true);
			
	     Boolean wall =  (Boolean) detectWall.invoke(avoidObstacle, distances);		 	     
	     assertTrue(wall);
	     
	     
	     distances.set(0, 37.0);
	   
	     wall =  (Boolean) detectWall.invoke(avoidObstacle, distances);
	     assertFalse(wall);
	}
	
	
	@Test
	public void correctObstacleAvoidanceLeft() throws NoSuchMethodException, SecurityException, IllegalAccessException, IllegalArgumentException, InvocationTargetException, NoSuchFieldException{
		
		robot.setOrientation(Orientation.NORTH);
		
		List<Double> edges = new LinkedList<Double>();
		edges.add(30.0);
		edges.add(Double.POSITIVE_INFINITY);
		
		Queue<Float> expDistances = new LinkedList<Float>();
		//obstacle distance
		expDistances.add(20f);
		//obstacle side
		expDistances.add(10f);
		
		expDistances.add(11f);
		expDistances.add(9f);
		expDistances.add(Float.POSITIVE_INFINITY);
		
		robot.setDistances(expDistances);
		
		
		Class[] cArg = new Class[1];
		cArg[0] = List.class;	
		
		Method avoidObstacle = this.avoidObstacle.getClass().getDeclaredMethod("avoidObstacle", cArg);
		avoidObstacle.setAccessible(true);
		Field currentPos = locator.getClass().getDeclaredField("currentPos");
		currentPos.setAccessible(true);
		currentPos.set(locator, new Coordinate(0, 0));
		
			
		
		avoidObstacle.invoke(this.avoidObstacle, edges);
		
		
		assertEquals(0, locator.getCurrentPosition().X);
		assertEquals(60, locator.getCurrentPosition().Y);
		
		assertEquals(Orientation.NORTH, robot.carriage_getOrientation());
		
		System.out.println("track"+locator.robotTrack);
	}
	
	
	
	
}
