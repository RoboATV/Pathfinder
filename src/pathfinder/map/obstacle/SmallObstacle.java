package pathfinder.map.obstacle;

public class SmallObstacle extends Obstacle{

	@Override
	public boolean conquerable() {
		return true;
	}

	@Override
	public int numericalValue() {
		return 1;
	}

	
}
