package pathfinder.location;

import java.rmi.RemoteException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import pathfinder.map.Coordinate;
import pathfinder.map.MapObject;
import pathfinder.map.obstacle.LargeObstacle;
import pathfinder.orientation.Orientation;
import pathfinder.robot.Direction;
import pathfinder.robot.IRobot;

public class Locator {

	
	public List<Coordinate> robotTrack;
	public Coordinate currentPos;
	
	public Map<Coordinate, MapObject> map = new HashMap<Coordinate, MapObject>();
	private IRobot robot;
	
	private Coordinate nextCoordinate;
	
	
	public Locator(IRobot robot){
		this.robot = robot;
		this.currentPos = new Coordinate(0, 0);
		robotTrack = new ArrayList<Coordinate>();
	}
	
	
	public void relocate(Coordinate destination){
		System.out.println("relocate to " + destination);
		robotTrack.add(this.currentPos);
		this.currentPos = destination;
	}
	
	
	public void enterNewPosition(Coordinate position, MapObject object){
		
		this.map.put(position, object);		
		
	}
	
	
	public void measureEnvironment() throws RemoteException{
		List<Coordinate> coordinates = new LinkedList<Coordinate>();
		
		coordinates.add(this.measureSide(Direction.RIGHT));
		coordinates.add(this.measureSide(Direction.LEFT));
		
		this.nextCoordinate = this.getFarestCoordinate(coordinates);		
	}
	
	
	
	private Coordinate getFarestCoordinate(List<Coordinate> coordinates){
		
		Coordinate farestCoordinate = coordinates.get(0);
		coordinates.remove(0);
		
		if(!coordinates.isEmpty()){
			for(Coordinate coordinate : coordinates){
				if(coordinate.Y > farestCoordinate.Y){
					farestCoordinate = coordinate;
				}
			}
		}	
		return farestCoordinate;
	}
	
	
	private Coordinate measureSide(Direction direction) throws RemoteException{
		
		int i = 90 * direction.getNumerical();
		int step = -5 * direction.getNumerical();
		
		Coordinate farestPos = new Coordinate(0, 0);
		
		robot.rotateTurnArm(i);
		
		while(i != 0){		
			float sample = this.getDistance();
			if(!Float.isInfinite(sample)){
				Coordinate position = this.calculateMapPosition(i, sample);
				System.out.println(position.toString());
				enterNewPosition(position, new LargeObstacle());
				farestPos = new Coordinate(currentPos.X, position.Y);
			}	
			robot.rotateTurnArm(step);
			i += step;
		}
		return farestPos;
	}
	
	
	
	private float getDistance() throws RemoteException{
		
		float sample = robot.getDistance();
		
		float oldSample = sample;		
		
		for(int i = 0; i < 5; i++){
			sample = robot.getDistance();
			if(sample < oldSample){
				oldSample = sample;
			}
		}
		
		oldSample = oldSample * 10;
		
		return oldSample;
	}
	
	
	private Coordinate invokePosition(Coordinate coordinate){
		
		if(this.robot.getOrientation() == Orientation.NORTH){
			coordinate.X += this.currentPos.X;
			coordinate.Y += this.currentPos.Y;
			return coordinate;
		}
		if(this.robot.getOrientation() == Orientation.SOUTH){
			coordinate.Y = currentPos.Y - coordinate.Y;
			coordinate.X = this.currentPos.X - coordinate.X;
			return coordinate;
		}
		if(this.robot.getOrientation() == Orientation.EAST){		
			Coordinate newCoordinate = new Coordinate();			
			newCoordinate.X = currentPos.X + coordinate.Y;
			newCoordinate.Y = currentPos.Y - coordinate.X;			
			
			return newCoordinate;
		}
		
		Coordinate newCoordinate = new Coordinate();		
		newCoordinate.X = currentPos.X - coordinate.Y;
		newCoordinate.Y = currentPos.Y + coordinate.X;
		
		
		return newCoordinate;
	}
	
	
	
	public Coordinate calculateMapPosition(int angleA, float distance){
		
		int angleB = 90 - angleA;
				
		if(angleB != 90){
			double gradient = Math.tan(Math.toRadians(angleB));			
			
			double divisor = Math.sqrt(Math.pow(gradient, 2) + 1);		
			
			double x = distance / divisor;
			
			if(angleA < 0){
				x = -x;
			}
			
			int rndX = (int) Math.round(x);
			
			double y = rndX * gradient;			
			int rndY = (int) Math.round(y);
			
			Coordinate coordinate = new Coordinate(rndX, rndY);
			 		
			return invokePosition(coordinate); 
		}
		
		Coordinate coordinate = new Coordinate(0, (int) distance);
		
		return invokePosition(coordinate);
	}
	
	
	public Coordinate getNextCoordinate(){
		return this.nextCoordinate;
	}
	
	
}
