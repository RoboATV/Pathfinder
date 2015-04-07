package pathfinder.robot;

import java.util.Map;
import java.util.Map.Entry;

import lejos.hardware.ev3.LocalEV3;
import lejos.hardware.motor.EV3LargeRegulatedMotor;
import lejos.hardware.port.MotorPort;
import lejos.hardware.port.Port;
import lejos.hardware.sensor.EV3UltrasonicSensor;
import lejos.robotics.RegulatedMotor;
import lejos.robotics.SampleProvider;
import lejos.robotics.navigation.DifferentialPilot;
import pathfinder.map.Coordinate;
import pathfinder.map.MapObject;



public class Robot implements IRobot{
	
	private RegulatedMotor leftDrive;
	private RegulatedMotor rightDrive;
	private RegulatedMotor turnArm;
	
	private EV3UltrasonicSensor ultraSonic1;
	private SampleProvider distance;
	
	private DifferentialPilot pilot;
	
	private final double wheelDiameter = 56;
	private final double trackWidth = 135;
	private final double travelRatio = 100;
	
	private Orientation orientation;	
	
	
	
	public Robot(){
		
//		initialize Motors
		leftDrive = new EV3LargeRegulatedMotor(MotorPort.A);
		
		rightDrive = new EV3LargeRegulatedMotor(MotorPort.B);
		turnArm = new EV3LargeRegulatedMotor(MotorPort.C);
		
		turnArm.setSpeed(20);
		
		
//		initialize Sensors
		Port port1 = LocalEV3.get().getPort("S1");
		this.ultraSonic1 = new EV3UltrasonicSensor(port1);
		distance = ultraSonic1.getDistanceMode();
		
		
		
//		initialize pilot
		pilot = new DifferentialPilot(wheelDiameter, trackWidth, leftDrive, rightDrive);
		pilot.setTravelSpeed(120);
		pilot.setRotateSpeed(60);
		
		
		this.orientation = Orientation.NORTH;
		
				
	}
	
	
	public void shutdown(){
		this.ultraSonic1.close();
	}

	public Integer[][] mapToArray(Map<Coordinate, MapObject> map, Coordinate currentPosition){
		
		Integer largestX = 0;
		Integer smallestX = 0;
		
		Integer largestY = 0;
		Integer smallestY = 0;
		
		Integer robotX = currentPosition.X;
		Integer robotY = currentPosition.Y;
		
		for(Entry<Coordinate, MapObject> value : map.entrySet()){
			Coordinate key = value.getKey();
			if(key.X > largestX){
				largestX = key.X;
			}
			if(key.X < smallestX){
				smallestX = key.X;
			}
			if(key.Y > largestY){
				largestY = key.Y;
			}
			if(key.Y < smallestY){
				smallestY = key.Y;
			}
		}
		
		Integer sizeX = Math.abs(smallestX) + largestX;
		robotX += Math.abs(smallestX);
		
		Integer sizeY = Math.abs(smallestY) + largestY;
//		robotY += Math.abs(smallestY);
		
		Integer[][] newMap = new Integer[sizeY + 1][sizeX + 1];
		
		
		for(Entry<Coordinate, MapObject> value : map.entrySet()){
			Coordinate key = value.getKey();
			MapObject object = value.getValue();
			
			Integer newX = key.X + Math.abs(smallestX);
			Integer newY = key.Y + Math.abs(smallestY);
			
			newMap[newY][newX] = object.numericalValue();
			
		}	
		
		newMap[robotY][robotX] = 2;
		
		
		return newMap;
	}
	
	
	public void printArray(Integer[][] array){
		for(int y = 0; y < array.length; y++){
			for(int x = 0; x < array[y].length; x++){
				if(array[y][x] == null) System.out.print(" 0 ");
				else System.out.print(" " + array[y][x] + " ");
			}
		System.out.println();
		}
	}


	@Override
	public void rotateTurnArm(int degrees) {
		this.turnArm.rotate(degrees);
		
	}


	@Override
	public SampleProvider getDistanceProvider() {
		return this.distance;
	}


	@Override
	public void rotate(int degrees) {
		// TODO Auto-generated method stub
		
	}


	@Override
	public Orientation getOrientation() {
		return this.orientation;
	}


	@Override
	public void travel(double distance) {
		this.pilot.travel(distance * this.travelRatio);
		
	}
	
	
	
}
