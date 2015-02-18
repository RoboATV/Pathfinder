package pathfinder;

import pathfinder.robot.Robot;
import lejos.robotics.subsumption.Behavior;

public class AvoidObstacle implements Behavior{

	private Robot robot;
	private boolean suppressed = false;
	
	public AvoidObstacle(Robot robot){
		this.robot = robot;
	}
	
	
	@Override
	public boolean takeControl() {
		
		float[] sample = new float[this.robot.distance.sampleSize()];
		this.robot.distance.fetchSample(sample, 0);
		
		return sample[0] < this.robot.obstacleDistance;
	}

	@Override
	public void action() {
		this.robot.pilot.stop();
		if(!suppressed){			
			if(this.robot.checkLeft()){
				this.robot.pilot.rotate(90);
			} else if(this.robot.checkRight()){
				this.robot.pilot.rotate(-90);
			}			
		}		
		
	}

	@Override
	public void suppress() {
		this.suppressed = true;		
	}

}
