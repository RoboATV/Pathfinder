package pathfinder.robot;

import java.net.MalformedURLException;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.util.Map;
import java.util.Map.Entry;

import lejos.hardware.ev3.LocalEV3;
import lejos.hardware.motor.EV3LargeRegulatedMotor;
import lejos.hardware.port.MotorPort;
import lejos.hardware.port.Port;
import lejos.hardware.sensor.EV3UltrasonicSensor;
import lejos.hardware.sensor.MindsensorsCompass;
import lejos.remote.ev3.RemoteEV3;
import lejos.robotics.RegulatedMotor;
import lejos.robotics.SampleProvider;
import lejos.robotics.navigation.DifferentialPilot;
import pathfinder.configuration.Configuration;
import pathfinder.map.Coordinate;
import pathfinder.map.MapObject;
import pathfinder.orientation.NoOrientationToAngle;
import pathfinder.orientation.Orientation;
import pathfinder.orientation.TurnNotPossible;

public class Robot implements IRobot{
	
	private RegulatedMotor leftDrive;
	private RegulatedMotor rightDrive;
	private RegulatedMotor turnArm;
	
	private EV3UltrasonicSensor ultraSonic1;
	private SampleProvider distance;
	
	private MindsensorsCompass compassSensor;
	private SampleProvider compass;
	
	private DifferentialPilot pilot;
	
	private final double wheelDiameter = 56;
	private final double trackWidth = 135;
	private final double travelRatio = 100;
	
	private Orientation orientation;
	private Direction turnDirection;	
	
	
	
	public Robot() throws RemoteException, MalformedURLException, NotBoundException{
		
//		initialize remote ev3
		RemoteEV3 remote = new RemoteEV3(Configuration.IP_EV3_2);
		
		
//		initialize Motors
		leftDrive = new EV3LargeRegulatedMotor(MotorPort.A);		
		rightDrive = new EV3LargeRegulatedMotor(MotorPort.B);
		
		turnArm = new EV3LargeRegulatedMotor(MotorPort.C);
		
		turnArm.setSpeed(20);
		
		
//		initialize Sensors
		Port port1 = LocalEV3.get().getPort("S1");
		this.ultraSonic1 = new EV3UltrasonicSensor(port1);
		distance = ultraSonic1.getDistanceMode();
		
		Port port2 = LocalEV3.get().getPort("S2");
		this.compassSensor = new MindsensorsCompass(port2);
		compass = compassSensor.getCompassMode();		
		
		
//		initialize pilot
		pilot = new DifferentialPilot(wheelDiameter, trackWidth, leftDrive, rightDrive);
		pilot.setTravelSpeed(120);
		pilot.setRotateSpeed(60);
		
		
		this.orientation = Orientation.NORTH;
		
				
	}
	
	
	public void shutdown(){
		this.ultraSonic1.close();
	}

	

	@Override
	public void rotateTurnArm(int degrees) {
		this.turnArm.rotate(degrees);
		
	}


	@Override
	public void rotate(int degrees) throws TurnNotPossible{
		int newAngle = orientation.getAngle() + degrees;
		try {
			this.orientation = Orientation.getOrientation(newAngle);
		} catch (NoOrientationToAngle e) {
			throw new TurnNotPossible(degrees);
		}		
		
		
		pilot.rotate(degrees);
	}


	@Override
	public Orientation getOrientation() {
		return this.orientation;
	}


	@Override
	public void travel(double distance) {
		this.pilot.travel(distance * this.travelRatio);
		
	}


	@Override
	public float getDistance() {
		float[] sample = new float[distance.sampleSize()];
		distance.fetchSample(sample, 0);
		
		return sample[0];
	}


	@Override
	public void stop() {
		pilot.stop();
	}


	@Override
	public float getHeading() {
		float[] sample = new float[compass.sampleSize()];
		compass.fetchSample(sample, 0);
	
		return sample[0];
	}


	@Override
	public Direction getTurnDirection() {
		return this.turnDirection;
	}
	
	
	@Override
	public void setTurnDirection(Direction turnDirection){
		this.turnDirection = turnDirection;
	}
	
}
