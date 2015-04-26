package pathfinder.robot;

public class Range {

	private double min;
	private double max;

	public Range(double min, double max){
		this.min = min;
		this.max = max;
	}
	
	
	public boolean contains(double test){
		if(test >= min && test <= max){
			return true;
		}
		return false;
	}
	
	
}
