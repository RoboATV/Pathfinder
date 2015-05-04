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
import pathfinder.orientation.TurnNotPossible;
import pathfinder.robot.Direction;
import pathfinder.robot.IRobot;

public class Locator {

	
	public List<Coordinate> robotTrack;
	public Coordinate currentPos;
	
	public Map<Coordinate, MapObject> map = new HashMap<Coordinate, MapObject>();
	private IRobot robot;
	
	
	
	
	public Locator(IRobot robot){
		System.out.println("initializing locator");
		this.robot = robot;
		this.currentPos = new Coordinate(0, 0);
		robotTrack = new ArrayList<Coordinate>();
	}
	
	
	
	private void relocateRobotAbsolute(Coordinate destination) throws TurnNotPossible{
		int distanceX = destination.X - currentPos.X;
		int distanceY = destination.Y - currentPos.Y;
		
		if(this.robot.carriage_getOrientation() == Orientation.NORTH){
			if(distanceY != 0){
				robot.carriage_travel(distanceY);
			} 
			if(distanceX > 0){
				robot.carriage_rotate(90);
				robot.carriage_travel(distanceX);
				robot.carriage_rotate(-90);
			} else if(distanceX < 0){
				robot.carriage_rotate(-90);
				robot.carriage_travel(distanceX);
				robot.carriage_rotate(90);
			}			
		} else if(this.robot.carriage_getOrientation() == Orientation.EAST){
			
		} else if(this.robot.carriage_getOrientation() == Orientation.SOUTH){
			
		} else if(this.robot.carriage_getOrientation() == Orientation.WEST){
			
		}
		
	}	
	
	
	private void relocateRobotRelative(Coordinate destination) throws TurnNotPossible{
		if(destination.X < 0){
			robot.carriage_rotate(Direction.LEFT.getTurnAngle());
			robot.carriage_travel(Math.abs(destination.X));
			robot.carriage_rotate(Direction.getOpposite(Direction.LEFT).getTurnAngle());
		} else if(destination.X > 0) {
			robot.carriage_rotate(Direction.RIGHT.getTurnAngle());
			robot.carriage_travel(Math.abs(destination.X));
			robot.carriage_rotate(Direction.getOpposite(Direction.RIGHT).getTurnAngle());
		}
				
		robot.carriage_travel(destination.Y);
	}
	
	public Coordinate calcNewPos(Coordinate destination){
		Coordinate newPos = currentPos;
		
		if(robot.carriage_getOrientation() == Orientation.NORTH){
			newPos.X += destination.X;
			newPos.Y += destination.Y;
			return newPos;
		} else if(robot.carriage_getOrientation() == Orientation.EAST){
			newPos.X += destination.Y;
			newPos.Y -= destination.X;
			return newPos;
		} else if(robot.carriage_getOrientation() == Orientation.SOUTH){
			newPos.X -= destination.X;
			newPos.Y -= destination.Y;
			return newPos;
		} 
		newPos.X -= destination.Y;
		newPos.Y -= destination.X;
		return newPos;
		
	}
	
	
	public void relocateRelative(Coordinate destination) throws TurnNotPossible{
		System.out.println("relocate to " + destination);
		robotTrack.add(currentPos);
		relocateRobotRelative(destination);
		currentPos = calcNewPos(destination);
	}
	
	public void relocateAbsolute(Coordinate destination) throws TurnNotPossible{
		System.out.println("relocate to " + destination);
		robotTrack.add(this.currentPos);
		relocateRobotAbsolute(destination);
		this.currentPos = destination;
	}
	
	
	public void enterNewPosition(Coordinate position, MapObject object){		
		this.map.put(position, object);		
	}
	
	
	public void measureEnvironment() throws RemoteException{				
		this.measureSide(Direction.RIGHT);
		this.measureSide(Direction.LEFT);	
	}	
	
	
	private void measureSide(Direction direction) throws RemoteException{
		System.out.println("measuring side " + direction.toString());
		int step = -5 * direction.getNumerical();		
		
		if(direction == Direction.LEFT)
			this.robot.turnArm_rotateToLeft();
		else
			this.robot.turnArm_rotateToRight();
		
		while(this.robot.turnArm_isCentered() != true){		
			float sample = this.getDistance();
			
			if(!Float.isInfinite(sample)){
				Coordinate position = this.calculateMapPosition(this.robot.turnArm_getTurnAngle(), sample);
				System.out.println(position.toString());
				enterNewPosition(position, new LargeObstacle());
			}
			
			this.robot.turnArm_rotate(step);
		}
		
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
		
		
		return oldSample;
	}
	
	
	private Coordinate invokePosition(Coordinate coordinate){
		
		if(this.robot.carriage_getOrientation() == Orientation.NORTH){
			coordinate.X += this.currentPos.X;
			coordinate.Y += this.currentPos.Y;
			return coordinate;
		}
		if(this.robot.carriage_getOrientation() == Orientation.SOUTH){
			coordinate.Y = currentPos.Y - coordinate.Y;
			coordinate.X = this.currentPos.X - coordinate.X;
			return coordinate;
		}
		if(this.robot.carriage_getOrientation() == Orientation.EAST){		
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
	
	
	
	
}
