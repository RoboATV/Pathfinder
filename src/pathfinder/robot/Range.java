package pathfinder.robot;

public class Range {

	private float min;
	private float max;

	public Range(float min, float max){
		this.min = min;
		this.max = max;
	}
	
	
	public boolean contains(float test){
		if(test >= min && test <= max){
			return true;
		}
		return false;
	}
	
	
}
