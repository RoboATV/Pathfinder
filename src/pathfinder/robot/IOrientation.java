package pathfinder.robot;

public interface IOrientation {

	public int getAngle();
	public Orientation getOrientation(int angle) throws NoOrientationToAngle;
	
}
