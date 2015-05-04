package pathfinder.map;

public class Coordinate{

	public int X;
	public int Y;
	
	public Coordinate(int x, int y){
		this.X = x;
		this.Y = y;
	}
	
	public Coordinate(){
		
	}

	public String toString(){
		return "x: " + X + " y: " + Y;
	}

	@Override
	public boolean equals(Object other){
		Coordinate otherCoordinate = (Coordinate) other;
		
		if(this.X == otherCoordinate.X && this.Y == otherCoordinate.Y){
			return true;
		}
		return false;
	}
	
}
