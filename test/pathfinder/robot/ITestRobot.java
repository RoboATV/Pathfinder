package pathfinder.robot;

import java.util.Queue;

import pathfinder.orientation.Orientation;

public interface ITestRobot extends IRobot{

	public void setDistances(Queue<Float> distances);
	public void setHeadings(Queue<Float> headings);
	public void setOrientation(Orientation orientation);
	public void setColors(Queue<Float> colors);
	public void setLight(Queue<Float> light);
	
	
}
