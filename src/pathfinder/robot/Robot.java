package pathfinder.robot;

import java.util.List;
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
import pathfinder.location.Locator;
import pathfinder.map.Coordinate;



public class Robot {
	
	public RegulatedMotor leftDrive;
	public RegulatedMotor rightDrive;
	public RegulatedMotor turnArm;
	
	public SampleProvider distance;
	
	public DifferentialPilot pilot;
	
	private final double wheelDiameter = 56;
	private final double trackWidth = 135;
	
	private final int leftSide = -90;
	private final int rightSide = 90;
	private final int backSide = 180;
	
	public final double obstacleDistance = 0.25;
	
	
	
	
	public Robot(){
		
//		initialize Motors
		leftDrive = new EV3LargeRegulatedMotor(MotorPort.A);
		rightDrive = new EV3LargeRegulatedMotor(MotorPort.B);
		turnArm = new EV3LargeRegulatedMotor(MotorPort.C);
		
		turnArm.setSpeed(20);
		
		
//		initialize Sensors
		Port port1 = LocalEV3.get().getPort("S1");
		EV3UltrasonicSensor ultraSonic1 = new EV3UltrasonicSensor(port1);
		distance = ultraSonic1.getDistanceMode();
		
		
		
//		initialize pilot
		pilot = new DifferentialPilot(wheelDiameter, trackWidth, leftDrive, rightDrive);
		pilot.setTravelSpeed(120);
		pilot.setRotateSpeed(60);
		
				
	}

	
	
	public boolean checkLeft(){
		turnArm.rotate(leftSide);
		float[] sample = new float[distance.sampleSize()];
		distance.fetchSample(sample, 0);
		
		turnArm.rotate(-leftSide);
		return sample[0] > obstacleDistance;
	}
	
	public boolean checkRight(){
		turnArm.rotate(rightSide);
		float[] sample = new float[distance.sampleSize()];
		distance.fetchSample(sample, 0);
		
		turnArm.rotate(-rightSide);
		return sample[0] > obstacleDistance;
	}
	
	
	public boolean checkBack(){
		turnArm.rotate(backSide);
		float[] sample = new float[distance.sampleSize()];
		distance.fetchSample(sample, 0);
		
		turnArm.rotate(-backSide);
		return sample[0] > obstacleDistance;
	}

	public Integer[][] mapToArray(Map<Coordinate, Integer> map){
		
		Integer largestX = 0;
		Integer smallestX = 0;
		
		Integer largestY = 0;
		Integer smallestY = 0;
		
		Integer robotX = 0;
		Integer robotY = 0;
		
		for(Entry<Coordinate, Integer> value : map.entrySet()){
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
		robotX = Math.abs(smallestX);
		
		Integer sizeY = Math.abs(smallestY) + largestY;
		robotY = Math.abs(smallestY);
		
		Integer[][] newMap = new Integer[sizeX + 1][sizeY + 1];
		
		
		for(Entry<Coordinate, Integer> value : map.entrySet()){
			Coordinate key = value.getKey();
			
			Integer newX = key.X + Math.abs(smallestX);
			Integer newY = key.Y + Math.abs(smallestY);
			
			newMap[newX][newY] = 1;
			
		}	
		
		newMap[robotX][robotY] = 2;
		
		
		return newMap;
	}
	
	
	public void printArray(Integer[][] array){
		for(int i = 0; i < array.length; i++){
			for(int j = 0; j < array[i].length; j++){
				System.out.print("	" + array[i][j]);
			}
			System.out.println();			
		}
	}
	
	
	
}
