package pathfinder.location;

import static org.junit.Assert.assertEquals;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.LinkedList;
import java.util.List;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import pathfinder.map.Coordinate;
import pathfinder.orientation.Orientation;
import pathfinder.orientation.TurnNotPossible;
import pathfinder.robot.ITestRobot;
import pathfinder.robot.TestRobot;

public class LocatorTest {


	private Locator locator;
	private ITestRobot robot;


	@Before
	public void startup(){
		this.robot = new TestRobot();
		this.locator = new Locator(robot);
	}
	
	
	@Test
	public void correctMapCalculationsNorth() throws NoSuchMethodException, SecurityException, IllegalAccessException, IllegalArgumentException, InvocationTargetException{
		
		this.robot.setOrientation(Orientation.NORTH);
		
		List<MapCalculationTestValue> testValues = new LinkedList<MapCalculationTestValue>();
		
		testValues.add(new MapCalculationTestValue(10, 10, 45, 14));
		testValues.add(new MapCalculationTestValue(15, 15, 45, 21));
		testValues.add(new MapCalculationTestValue(-10, 10, -45, 14));
		testValues.add(new MapCalculationTestValue(-15, 15, -45, 21));
		
		Class[] cArg = new Class[2];
        cArg[0] = Integer.TYPE;
        cArg[1] = Float.TYPE;
        	
		Method calculateMapPosition = locator.getClass().getDeclaredMethod("calculateMapPosition", cArg);
		calculateMapPosition.setAccessible(true);
		
		for(MapCalculationTestValue testValue : testValues){		
			Coordinate calculatedCor = (Coordinate) calculateMapPosition.invoke(locator, testValue.angle, testValue.distance);
			assertEquals(testValue.expectedCoordinate.X, calculatedCor.X);
			assertEquals(testValue.expectedCoordinate.Y, calculatedCor.Y);
		}	
	}
	
	
	@Test
	public void correctMapCalculationsSouth() throws NoSuchMethodException, SecurityException, IllegalAccessException, IllegalArgumentException, InvocationTargetException{
		
		this.robot.setOrientation(Orientation.SOUTH);
		
		List<MapCalculationTestValue> testValues = new LinkedList<MapCalculationTestValue>();
		
		testValues.add(new MapCalculationTestValue(-10, -10, 45, 14));
		testValues.add(new MapCalculationTestValue(-15, -15, 45, 21));
		testValues.add(new MapCalculationTestValue(10, -10, -45, 14));
		testValues.add(new MapCalculationTestValue(15, -15, -45, 21));
		
		Class[] cArg = new Class[2];
        cArg[0] = Integer.TYPE;
        cArg[1] = Float.TYPE;
        	
		Method calculateMapPosition = locator.getClass().getDeclaredMethod("calculateMapPosition", cArg);
		calculateMapPosition.setAccessible(true);
		
		for(MapCalculationTestValue testValue : testValues){		
			Coordinate calculatedCor = (Coordinate) calculateMapPosition.invoke(locator, testValue.angle, testValue.distance);
			assertEquals(testValue.expectedCoordinate.X, calculatedCor.X);
			assertEquals(testValue.expectedCoordinate.Y, calculatedCor.Y);
		}	
	}
	
	
	@Test
	public void correctMapCalculationsEast() throws NoSuchMethodException, SecurityException, IllegalAccessException, IllegalArgumentException, InvocationTargetException{
		
		this.robot.setOrientation(Orientation.EAST);
		
		List<MapCalculationTestValue> testValues = new LinkedList<MapCalculationTestValue>();
//		
		testValues.add(new MapCalculationTestValue(10, -10, 45, 14));
		testValues.add(new MapCalculationTestValue(15, -15, 45, 21));
		testValues.add(new MapCalculationTestValue(10, 10, -45, 14));
		testValues.add(new MapCalculationTestValue(15, 15, -45, 21));
		
		Class[] cArg = new Class[2];
        cArg[0] = Integer.TYPE;
        cArg[1] = Float.TYPE;
        	
		Method calculateMapPosition = locator.getClass().getDeclaredMethod("calculateMapPosition", cArg);
		calculateMapPosition.setAccessible(true);
		
		for(MapCalculationTestValue testValue : testValues){		
			Coordinate calculatedCor = (Coordinate) calculateMapPosition.invoke(locator, testValue.angle, testValue.distance);
			assertEquals(testValue.expectedCoordinate.X, calculatedCor.X);
			assertEquals(testValue.expectedCoordinate.Y, calculatedCor.Y);
		}	
	}
	
	
	@Test
	public void correctMapCalculationsWest() throws NoSuchMethodException, SecurityException, IllegalAccessException, IllegalArgumentException, InvocationTargetException{
		
		this.robot.setOrientation(Orientation.WEST);
		
		List<MapCalculationTestValue> testValues = new LinkedList<MapCalculationTestValue>();
		
		testValues.add(new MapCalculationTestValue(-10, 10, 45, 14));
		testValues.add(new MapCalculationTestValue(-15, 15, 45, 21));
		testValues.add(new MapCalculationTestValue(-10, -10, -45, 14));
		testValues.add(new MapCalculationTestValue(-15, -15, -45, 21));
		
		Class[] cArg = new Class[2];
        cArg[0] = Integer.TYPE;
        cArg[1] = Float.TYPE;
        	
		Method calculateMapPosition = locator.getClass().getDeclaredMethod("calculateMapPosition", cArg);
		calculateMapPosition.setAccessible(true);
		
		for(MapCalculationTestValue testValue : testValues){		
			Coordinate calculatedCor = (Coordinate) calculateMapPosition.invoke(locator, testValue.angle, testValue.distance);
			assertEquals(testValue.expectedCoordinate.X, calculatedCor.X);
			assertEquals(testValue.expectedCoordinate.Y, calculatedCor.Y);
		}	
	}
	
	
	@Test 
	public void correctRelocationAhead() throws NoSuchFieldException, SecurityException, IllegalArgumentException, IllegalAccessException{
		
		robot.setOrientation(Orientation.NORTH);
		Field currentPos = locator.getClass().getDeclaredField("currentPos");
		currentPos.setAccessible(true);
		currentPos.set(locator, new Coordinate(0, 0));
		
		
		locator.travelAhead(30);
		locator.travelAhead(20);
		
		assertEquals(0, locator.getCurrentPosition().X);
		assertEquals(50, locator.getCurrentPosition().Y);
		
	}
	
	
	@Test
	public void correctRelocationWithRotation() throws NoSuchFieldException, SecurityException, IllegalArgumentException, IllegalAccessException, TurnNotPossible{
		robot.setOrientation(Orientation.NORTH);
		Field currentPos = locator.getClass().getDeclaredField("currentPos");
		currentPos.setAccessible(true);
		currentPos.set(locator, new Coordinate(0, 0));
		
		
		locator.travelAhead(50);
		robot.carriage_rotate(-90);
		locator.travelAhead(20);
		
		
		assertEquals(-20, locator.getCurrentPosition().X);
		assertEquals(50, locator.getCurrentPosition().Y);
		
	}
	
	
	
	
	@After
	public void shutdown(){
		
	}
	
}
