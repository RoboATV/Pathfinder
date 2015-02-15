package pathfinder;

import lejos.robotics.subsumption.Behavior;
import pathfinder.map.Coordinate;

public class Drive implements Behavior{
	
	private Robot robot;
	private boolean suppressed = false;
	
	
	public Drive(Robot robot){
		this.robot = robot;
	}

	@Override
	public boolean takeControl() {
		return true;
	}

	@Override
	public void action() {
		while(!suppressed){
			this.measureEnvironment();		
			this.robot.pilot.travel(500);
			this.robot.printArray(this.robot.mapToArray(this.robot.map));
		}
	}

	@Override
	public void suppress() {		
		suppressed = true;
	}

	
	private void measureEnvironment(){
		int i = 0;
				
		while(i <= 90){		
			float sample = this.getDistance();
			if(!Float.isInfinite(sample)){
				Coordinate position = this.calculateMapPosition(i, sample);
				System.out.println(position.toString());
				robot.map.put(position, 1);
			}	
			robot.turnArm.rotate(5);
			i += 5;
		}
		
		
		robot.turnArm.rotate(-90);
		
	}
	
	
	private float getDistance(){
		
		float[] sample = new float[this.robot.distance.sampleSize()];
		
		this.robot.distance.fetchSample(sample, 0);
		float oldSample = sample[0];
		
		
		for(int i = 0; i < 5; i++){
			this.robot.distance.fetchSample(sample, 0);
			if(sample[0] < oldSample){
				oldSample = sample[0];
			}
		}
		
		oldSample = oldSample * 100;
		
		return oldSample;
	}
	
	
	
	private Coordinate calculateMapPosition(int angleA, float distance){
		
		int angleB = 90 - angleA;
		
		System.out.println("distance: " + distance);
		System.out.println("angle " + angleB);
		
		if(angleB != 90){
			double gradient = Math.tan(Math.toRadians(angleB));
				
			System.out.println("gradient " + gradient);
			
			double divisor = Math.sqrt(Math.pow(gradient + 1, 2));		
			
			int coorX = (int) (distance / divisor);
			
			int coorY = (int) (coorX * gradient);
			
			return new Coordinate(coorX, coorY);
			
		}
		
		return new Coordinate(0, (int) distance);
		
}


	
	
	
	
}	
