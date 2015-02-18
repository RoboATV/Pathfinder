package pathfinder.location;

import static org.junit.Assert.assertEquals;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import pathfinder.map.Coordinate;
import pathfinder.robot.IRobot;
import pathfinder.robot.TestRobot;

public class LocatorTest {


	private Locator locator;
	private IRobot robot;


	@Before
	public void startup(){
		this.robot = new TestRobot();
		this.locator = new Locator(robot);
	}
	
	
	@Test
	public void correctMapCalculations() throws NoSuchMethodException, SecurityException, IllegalAccessException, IllegalArgumentException, InvocationTargetException{
		
		Class[] cArg = new Class[2];
        cArg[0] = Integer.class;
        cArg[1] = Float.class;
        	
		Method calculateMapPosition = locator.getClass().getDeclaredMethod("calculateMapPosition", cArg);
		calculateMapPosition.setAccessible(true);
		
		
		Coordinate expectedCor = new Coordinate(10, 10);
		assertEquals(expectedCor, calculateMapPosition.invoke(locator, 45, 14));
	}
	
	
	@After
	public void shutdown(){
		
	}
	
}
