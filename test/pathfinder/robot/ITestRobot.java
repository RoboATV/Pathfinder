package pathfinder.robot;

import java.util.Queue;

public interface ITestRobot extends IRobot{

	public void setDistances(Queue<Float> distances);
	public void setHeadings(Queue<Float> headings);
	public void setOrientation(Orientation orientation);
	
	
}
