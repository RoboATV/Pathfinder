package pathfinder.location;

import java.rmi.RemoteException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import lejos.robotics.navigation.Move;
import pathfinder.map.Coordinate;
import pathfinder.map.MapObject;
import pathfinder.map.obstacle.LargeObstacle;
import pathfinder.moves.IMove;
import pathfinder.moves.MoveToCoordinate;
import pathfinder.moves.MoveTurnToOrientation;
import pathfinder.orientation.Orientation;
import pathfinder.orientation.TurnNotPossible;
import pathfinder.robot.Direction;
import pathfinder.robot.IRobot;

public class Locator {
	public List<Coordinate> robotTrack;
	private Coordinate currentPos; 
	
	public Map<Coordinate, MapObject> map = new HashMap<Coordinate, MapObject>();
	private IRobot robot;
	
	public Locator(IRobot robot){
		System.out.println("Initialize locator...");
		
		this.robot = robot;		
		this.robotTrack = new ArrayList<Coordinate>();
		setCurrentPosition(new Coordinate(0, 0));
		
		System.out.println("Locator initilized...");
	}
	
	
	private void setCurrentPosition(Coordinate position){
		System.out.println("set current pos" + position);
		this.currentPos = new Coordinate(position);
		System.out.println("current pos" + this.currentPos);
		this.robotTrack.add(new Coordinate(position.X, position.Y));
	}
	
	public void enterCoordinateFromMove(Move move){
		Coordinate relPos = new Coordinate(0, (int) move.getDistanceTraveled());
		Coordinate absPos = calcNewPos(relPos);
		
		System.out.println("last position relative " + relPos);
		System.out.println("last position absolute " + absPos);
		
		setCurrentPosition(absPos);
	}
	
	
	public Coordinate getCurrentPosition(){
		return this.currentPos;
	}
	
	
	private void relocateRobotAbsolute(Coordinate destination) throws TurnNotPossible{
		int distanceX = destination.X - currentPos.X;
		int distanceY = destination.Y - currentPos.Y;
		
		if(this.robot.carriage_getOrientation() == Orientation.NORTH){
			if(distanceY != 0){
				travelAhead(distanceY);
			} 
			if(distanceX > 0){
				robot.carriage_rotate(90);
				travelAhead(distanceX);
				robot.carriage_rotate(-90);
			} else if(distanceX < 0){
				robot.carriage_rotate(-90);
				travelAhead(distanceX);
				robot.carriage_rotate(90);
			}			
		} else if(this.robot.carriage_getOrientation() == Orientation.EAST){
			if(distanceX != 0){
				travelAhead(distanceX);
			}
			if(distanceY > 0){
				robot.carriage_rotate(Direction.LEFT.getTurnAngle());
				travelAhead(distanceY);
				robot.carriage_rotate(Direction.RIGHT.getTurnAngle());
			} else if(distanceY < 0){
				robot.carriage_rotate(Direction.RIGHT.getTurnAngle());
				travelAhead(Math.abs(distanceY));
				robot.carriage_rotate(Direction.LEFT.getTurnAngle());
			}			
		} else if(this.robot.carriage_getOrientation() == Orientation.SOUTH){
			if(distanceY != 0){
				travelAhead(-distanceY);
			} 
			if(distanceX > 0){
				robot.carriage_rotate(Direction.LEFT.getTurnAngle());
				travelAhead(distanceX);
				robot.carriage_rotate(Direction.RIGHT.getTurnAngle());
			} else if(distanceX < 0){
				robot.carriage_rotate(Direction.RIGHT.getTurnAngle());
				travelAhead(Math.abs(distanceX));
				robot.carriage_rotate(Direction.LEFT.getTurnAngle());
			}	
		} else if(this.robot.carriage_getOrientation() == Orientation.WEST){
			if(distanceX != 0){
				travelAhead(Math.abs(distanceX));
			}
			if(distanceY > 0){
				robot.carriage_rotate(Direction.RIGHT.getTurnAngle());
				travelAhead(distanceY);
				robot.carriage_rotate(Direction.LEFT.getTurnAngle());
			} else if(distanceY < 0){
				robot.carriage_rotate(Direction.LEFT.getTurnAngle());
				travelAhead(Math.abs(distanceY));
				robot.carriage_rotate(Direction.RIGHT.getTurnAngle());
			}		
		}
	}
	
	public Coordinate calcNewPos(Coordinate destination){
		Coordinate newPos = new Coordinate(currentPos);
		
		System.out.println("current position " + newPos);
		
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
	
	
	
	public void relocateAbsolute(Coordinate destination) throws TurnNotPossible{
		System.out.println("relocate to " + destination);
		
		relocateRobotAbsolute(destination);
		setCurrentPosition(destination);
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
//				System.out.println(position.toString());
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
			
			rndX = rndX / 10;
			rndY = rndY / 10;
			
			Coordinate coordinate = new Coordinate(rndX, rndY);
			 		
			
			return invokePosition(coordinate); 
		}
		
		int y = (int) (distance / 10);
		Coordinate coordinate = new Coordinate(0, y);
		
		return invokePosition(coordinate);
	}
	
	
	public void travelAhead(int distance){		
		Coordinate newPosition = calcNewPos(new Coordinate(0, distance));
		
		setCurrentPosition(newPosition);
		
		robot.carriage_travel(distance);
	}
	
	
	public void travelAhead(int distance, boolean immediateReturn){		
		Coordinate newPosition = calcNewPos(new Coordinate(0, distance));
		
		setCurrentPosition(newPosition);
		
		robot.carriage_travel(distance, immediateReturn);
	}	
	
	
	
	public ArrayList<IMove> returnToStart(){
		ArrayList<IMove>	movePath = new ArrayList<IMove>();
		
		movePath.add(new MoveTurnToOrientation(this.robot, this.robot.carriage_getOrientation()));
		movePath.add(0, new MoveToCoordinate(this, this.currentPos));
		
		Coordinate startPoint	= new Coordinate(0, 0);
		
		MoveToCoordinate move;
		
		// If the robot travels from east to west to check the area, then we have to move first in y direction to avoid unknown areas.
		if(this.robot.carriage_getOrientation() == Orientation.EAST || this.robot.carriage_getOrientation() == Orientation.WEST) {
			move = new MoveToCoordinate(this, this.findFreePathY(this.currentPos));
			move.execute();
			movePath.add(0, move);
		}
		
		while(!startPoint.equals(this.currentPos)) {
			move = new MoveToCoordinate(this, this.findFreePathX(this.currentPos));
			move.execute();
			movePath.add(0, move);
			
			move = new MoveToCoordinate(this, this.findFreePathY(this.currentPos));
			move.execute();
			movePath.add(0, move);
		}
		
		return movePath;
	}
	
	private Coordinate findFreePathX(Coordinate start) {
		if(start.X == 0) {
			return start;
		}
		
		int factor;
		
		if(start.X > 0) {
			factor = 1;
		} else {
			factor = -1;
		}
		
		Coordinate	toCheck = new Coordinate(start.X, start.Y);
		MapObject	value;
		
		for(int i = start.X * factor; i >= 0; i--) {
			toCheck.X = i * factor;
			value = this.map.get(toCheck);
			
			if(value != null && value.numericalValue() == LargeObstacle.NUMERICAL_VALUE) {
				toCheck.X = i + 2 * factor;
				break;
			}
		}
		
		return toCheck;
	}
	
	private Coordinate findFreePathY(Coordinate start) {
		if(start.Y == 0) {
			return start;
		}
		
		int factor;
		
		if(start.Y > 0) {
			factor = 1;
		} else {
			factor = -1;
		}
		
		Coordinate	toCheck = new Coordinate(start.X, start.Y);
		MapObject	value;
		
		for(int i = start.Y * factor; i >= 0; i--) {
			toCheck.Y = i * factor;
			value = this.map.get(toCheck);
			
			if(value != null && value.numericalValue() == LargeObstacle.NUMERICAL_VALUE) {
				toCheck.Y = i + 2 * factor;
				break;
			}
		}
		
		return toCheck;
	}
	
	
	public void enterObstacle(Coordinate edge, double distance, Direction obstacleSide){
		
		if(robot.carriage_getOrientation() == Orientation.NORTH){
			int xStart = (int) (currentPos.X + ((distance * obstacleSide.getNumerical())/10));
			for(int i = xStart; i != edge.X; i += obstacleSide.getNumerical()){
				System.out.println("i" +i);
				enterNewPosition(new Coordinate(i, currentPos.Y), new LargeObstacle());
			}
		} else if(robot.carriage_getOrientation() == Orientation.EAST){
			int startY = (int) (currentPos.Y - ((distance * obstacleSide.getNumerical())/10));
			for(int i = startY; i != edge.Y; i += -obstacleSide.getNumerical()){
				enterNewPosition(new Coordinate(currentPos.X, i), new LargeObstacle());
			}
		} else if(robot.carriage_getOrientation() == Orientation.SOUTH){
			int xStart = (int) (currentPos.X - ((distance * obstacleSide.getNumerical())/10));
			for(int i = xStart; i != edge.X; i += -obstacleSide.getNumerical()){
				enterNewPosition(new Coordinate(i, currentPos.Y), new LargeObstacle());
			}
		} else if(robot.carriage_getOrientation() == Orientation.WEST){
			int startY = (int) (currentPos.Y + ((distance * obstacleSide.getNumerical())/10));
			for(int i = startY; i != edge.Y; i += obstacleSide.getNumerical()){
				enterNewPosition(new Coordinate(currentPos.X, i), new LargeObstacle());
			}
		}
		
	}
}
