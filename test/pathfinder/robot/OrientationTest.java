package pathfinder.robot;

import static org.junit.Assert.*;
import static org.hamcrest.CoreMatchers.*;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

public class OrientationTest {

	@Before
	public void startup(){
		
	}
	
	
	@Test
	public void correctAngles(){		
		
		assertEquals(Orientation.NORTH.getAngle(), 0);
		assertEquals(Orientation.EAST.getAngle(), 90);
		assertEquals(Orientation.SOUTH.getAngle(), 180);
		assertEquals(Orientation.WEST.getAngle(), 270);		
		
	}
	
	
	@Test()
	public void correctAngleConversion(){
		try{
			assertEquals(Orientation.getOrientation(0), Orientation.NORTH);
			assertEquals(Orientation.getOrientation(90), Orientation.EAST);
			assertEquals(Orientation.getOrientation(180), Orientation.SOUTH);
			assertEquals(Orientation.getOrientation(270), Orientation.WEST);
			
		} catch (NoOrientationToAngle e){
			fail(e.getMessage());
		}
		
	}
	
	
	@Test public void wrongAngleConversion(){
		
		try{
			assertEquals(Orientation.getOrientation(10), Orientation.WEST);
			fail("Expected an NoOrientationToAngle exception to be thrown");
		} catch(NoOrientationToAngle e){
			assertThat(e.toString(), is("no orientation found for such angle 10"));
		}
		
	}
	
	
	
	@After
	public void shutdown(){
		
	}
	
	
}
