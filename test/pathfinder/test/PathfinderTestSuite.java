package pathfinder.test;

import org.junit.runner.RunWith;
import org.junit.runners.Suite;

import pathfinder.location.LocatorTest;
import pathfinder.robot.OrientationTest;


@RunWith(Suite.class)
@Suite.SuiteClasses({
	LocatorTest.class,
	OrientationTest.class	
})


public class PathfinderTestSuite {	
}
