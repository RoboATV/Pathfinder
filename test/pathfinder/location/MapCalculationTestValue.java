package pathfinder.location;

import pathfinder.map.Coordinate;

public class MapCalculationTestValue {

	public Coordinate expectedCoordinate;
	public int angle;
	public float distance;
	
	
	public MapCalculationTestValue(int x, int y, int angle, float distance){
		this.expectedCoordinate = new Coordinate(x, y);
		this.angle = angle;
		this.distance = distance;
	}
	
	
}
