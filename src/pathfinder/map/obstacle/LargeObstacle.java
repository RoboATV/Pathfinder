package pathfinder.map.obstacle;

public class LargeObstacle extends Obstacle {
	public final static int NUMERICAL_VALUE = 3;

	@Override
	public boolean conquerable() {
		return false;
	}

	@Override
	public int numericalValue() {
		return LargeObstacle.NUMERICAL_VALUE;
	}

}
