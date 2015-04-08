package pathfinder.map;

public class Coordinate {

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
	
}
