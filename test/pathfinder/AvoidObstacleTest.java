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

import pathfinder.behaviors.AvoidObstacle;
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
	    assertEquals(Configuration.OBSTACLE_OFFSET, 10, 0);
	    
	    Method calculateSensorAngle = avoidObstacle.getClass().getDeclaredMethod("calculateSensorAngle");
	    calculateSensorAngle.setAccessible(true);
	    
	    int angle = (int) calculateSensorAngle.invoke(avoidObstacle);
	    
	    assertEquals(angle, 56);
	}
	
	@Test
	public void correctExpectationCalculation() throws IllegalAccessException, IllegalArgumentException, InvocationTargetException, NoSuchMethodException, SecurityException{
		 Queue<Float> distances = new LinkedList<Float>();
		 distances.add(new Float(20));
		 robot.setDistances(distances);
		 assertEquals(Configuration.OBSTACLE_SIZE, 40, 0);
		 assertEquals(Configuration.OBSTACLE_OFFSET, 10, 0);		 
		 
		 Method calculateExpectation = avoidObstacle.getClass().getDeclaredMethod("calculateExpectation");
		 calculateExpectation.setAccessible(true);
		    
		 float angle =  (float) calculateExpectation.invoke(avoidObstacle);
		 
		 assertEquals(32, angle, 5);
	}
	
	@SuppressWarnings("rawtypes")
	@Test
	public void correctWallDetection() throws NoSuchMethodException, SecurityException, IllegalAccessException, IllegalArgumentException, InvocationTargetException{
		 Queue<Float> expDistances = new LinkedList<Float>();
		 expDistances.add(new Float(20));
		 robot.setDistances(expDistances);
		 assertEquals(Configuration.OBSTACLE_SIZE, 40, 0);
		 assertEquals(Configuration.OBSTACLE_OFFSET, 10, 0);	
		 		 
		 List<Float> distances = new ArrayList<Float>();
		 distances.add(35f);
		 distances.add(36f);
		 
		 Class[] cArg = new Class[1];
	     cArg[0] = List.class;   
	        	
	     Method detectWall = avoidObstacle.getClass().getDeclaredMethod("detectWall", cArg);    	     
	     detectWall.setAccessible(true);
			
	     Boolean wall =  (Boolean) detectWall.invoke(avoidObstacle, distances);		 	     
	     assertTrue(wall);
	     
	     distances.set(0, 70f);
	   
	     wall =  (Boolean) detectWall.invoke(avoidObstacle, distances);
	     assertFalse(wall);
	}
	
	@SuppressWarnings("rawtypes")
	@Test
	public void correctObstacleAvoidanceLeft() throws NoSuchMethodException, SecurityException, IllegalAccessException, IllegalArgumentException, InvocationTargetException, NoSuchFieldException{
		robot.setOrientation(Orientation.NORTH);
		
		List<Float> edges = new LinkedList<Float>();
		edges.add(30f);
		edges.add(Float.POSITIVE_INFINITY);
		
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
		
		System.out.println(locator.robotTrack);
		System.out.println(locator.map);
		
		assertEquals(new Coordinate(0, 6), locator.getCurrentPosition());		
		
		assertEquals(Orientation.NORTH, robot.carriage_getOrientation());
		
		assertEquals(7, locator.robotTrack.size());
		
		assertEquals(new Coordinate(0, 0), locator.robotTrack.get(0));
		assertEquals(new Coordinate(-5, 0), locator.robotTrack.get(1));
		assertEquals(new Coordinate(-5, 3), locator.robotTrack.get(2));
		assertEquals(new Coordinate(-5, 4), locator.robotTrack.get(3));
		assertEquals(new Coordinate(-5, 5), locator.robotTrack.get(4));
		assertEquals(new Coordinate(-5, 6), locator.robotTrack.get(5));
		assertEquals(new Coordinate(0, 6), locator.robotTrack.get(6));
	}
}