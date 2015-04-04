package pathfinder.map.obstacle;

public class LargeObstacle extends Obstacle {

	@Override
	public boolean conquerable() {
		return false;
	}

	@Override
	public int numericalValue() {
		return 3;
	}

}
