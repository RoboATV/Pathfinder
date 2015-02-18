package pathfinder.location;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import lejos.robotics.SampleProvider;
import pathfinder.map.Coordinate;
import pathfinder.robot.Direction;
import pathfinder.robot.IRobot;

public class Locator {

	
	public List<Coordinate> robotTrack;
	public Coordinate currentPos;
	
	public Map<Coordinate, Integer> map = new HashMap<Coordinate, Integer>();
	private IRobot robot;
	
	private Coordinate nextCoordinate;
	
	
	public Locator(IRobot robot){
		this.robot = robot;
		robotTrack = new ArrayList<Coordinate>();
	}
	
	
	public void relocate(Coordinate destination){
		this.currentPos = destination;
	}
	
	
	public void enterNewPosition(Coordinate position, Integer value){
		
		position.X += this.currentPos.X;
		position.Y += this.currentPos.Y;
		map.put(position, value);		
		
	}
	
	
	public void measureEnvironment(){
		List<Coordinate> coordinates = new LinkedList<Coordinate>();
		
		coordinates.add(this.measureSide(Direction.RIGHT));
		coordinates.add(this.measureSide(Direction.LEFT));
		
		this.nextCoordinate = this.getFarestCoordinate(coordinates);		
	}
	
	
	
	private Coordinate getFarestCoordinate(List<Coordinate> coordinates){
		
		Coordinate farestCoordinate = coordinates.get(0);
		coordinates.remove(0);
		
		for(Coordinate coordinate : coordinates){
			if(coordinate.Y > farestCoordinate.Y){
				farestCoordinate = coordinate;
			}
		}
		return farestCoordinate;
	}
	
	
	private Coordinate measureSide(Direction direction){
		int i = 90 * direction.getNumerical();
		int step = -5 * direction.getNumerical();
		
		Coordinate farestPos = null;
		
		robot.rotateTurnArm(i);
		
		while(i >= 0){		
			float sample = this.getDistance();
			if(!Float.isInfinite(sample)){
				Coordinate position = this.calculateMapPosition(i, sample);
				System.out.println(position.toString());
				enterNewPosition(position, 1);
				farestPos = new Coordinate(currentPos.X, position.Y);
			}	
			robot.rotateTurnArm(step);
			i += step;
		}
		return farestPos;
	}
	
	
	
	private float getDistance(){
		SampleProvider distance = robot.getDistanceProvider();
		float[] sample = new float[distance.sampleSize()];
		
		distance.fetchSample(sample, 0);
		float oldSample = sample[0];
		
		
		for(int i = 0; i < 5; i++){
			distance.fetchSample(sample, 0);
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
	
	
	public Coordinate getNextCoordinate(){
		return this.nextCoordinate;
	}
	
	
}
