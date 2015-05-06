package pathfinder.robot;

import java.util.Queue;

import pathfinder.orientation.Orientation;

public interface ITestRobot extends IRobot{
	public void setDistances(Queue<Float> distances);
	public void setOrientation(Orientation orientation);
}
