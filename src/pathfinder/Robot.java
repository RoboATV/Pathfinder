package pathfinder;

import java.util.HashMap;
import java.util.Map;

import lejos.hardware.ev3.LocalEV3;
import lejos.hardware.motor.EV3LargeRegulatedMotor;
import lejos.hardware.port.MotorPort;
import lejos.hardware.port.Port;
import lejos.hardware.sensor.EV3UltrasonicSensor;
import lejos.robotics.RegulatedMotor;
import lejos.robotics.SampleProvider;
import lejos.robotics.navigation.DifferentialPilot;
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
	
	public Map<Coordinate, Integer> map = new HashMap<Coordinate, Integer>();
	
	public Robot(){
		
//		initialize Motors
		leftDrive = new EV3LargeRegulatedMotor(MotorPort.A);
		rightDrive = new EV3LargeRegulatedMotor(MotorPort.B);
		turnArm = new EV3LargeRegulatedMotor(MotorPort.C);
		
		turnArm.setSpeed(60);
		
		
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

}
