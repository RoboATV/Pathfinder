package pathfinder;

import lejos.robotics.subsumption.Behavior;
import pathfinder.map.Coordinate;

public class Drive implements Behavior{
	
	private Robot robot;
	
	public Drive(Robot robot){
		this.robot = robot;
	}

	@Override
	public boolean takeControl() {
		return true;
	}

	@Override
	public void action() {
		this.robot.pilot.forward();
		
	}

	@Override
	public void suppress() {		
		
	}

	
	private void measureEnvironment(){
		int i = 0;
		float[] sample = new float[this.robot.distance.sampleSize()];
		
		while(i <= 90){
			robot.turnArm.rotate(5);
			this.robot.distance.fetchSample(sample, 0);
			Coordinate position = this.calculateMapPosition(i, sample[0]);
			robot.map.put(position, 1);
		}
		
		
	}
	
	
	private Coordinate calculateMapPosition(int angleA, float distance){
		
		int angleB = 90 - angleA;
		
		if(angleB != 90){
			double gradient = Math.tan(angleB);
			double divisor = Math.sqrt(Math.pow(gradient + 1, 2));		
			
			int coorX = (int) (distance / divisor);
			
			int coorY = (int) (coorX * gradient);
			
			return new Coordinate(coorX, coorY);
			
		}
		
		return new Coordinate(0, (int) distance);
		
}


	
	
	
	
}	
