package pathfinder.test;

import org.junit.runner.RunWith;
import org.junit.runners.Suite;

import pathfinder.AvoidObstacleTest;
import pathfinder.location.LocatorTest;
import pathfinder.moves.MoveTurnToOrientationTest;
import pathfinder.orientation.InitialOrientationTest;
import pathfinder.orientation.OrientationTest;


@RunWith(Suite.class)
@Suite.SuiteClasses({
	LocatorTest.class,
	OrientationTest.class,
	InitialOrientationTest.class,
	AvoidObstacleTest.class,
	MoveTurnToOrientationTest.class
})


public class PathfinderTestSuite {	
}
