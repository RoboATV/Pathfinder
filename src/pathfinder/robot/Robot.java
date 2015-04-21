package pathfinder.robot;

import java.net.MalformedURLException;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;

import lejos.hardware.ev3.LocalEV3;
import lejos.hardware.motor.EV3LargeRegulatedMotor;
import lejos.hardware.port.MotorPort;
import lejos.hardware.port.Port;
import lejos.hardware.port.SensorPort;
import lejos.hardware.sensor.EV3ColorSensor;
import lejos.hardware.sensor.EV3UltrasonicSensor;
import lejos.hardware.sensor.MindsensorsCompass;
import lejos.remote.ev3.RMIRegulatedMotor;
import lejos.remote.ev3.RMISampleProvider;
import lejos.remote.ev3.RemoteEV3;
import lejos.robotics.RegulatedMotor;
import lejos.robotics.SampleProvider;
import lejos.robotics.navigation.DifferentialPilot;
import pathfinder.configuration.Configuration;
import pathfinder.orientation.NoOrientationToAngle;
import pathfinder.orientation.Orientation;
import pathfinder.orientation.TurnNotPossible;

public class Robot implements IRobot{
	
	private RegulatedMotor leftDrive;
	private RegulatedMotor rightDrive;
	private RegulatedMotor grapplerMove;
	private RegulatedMotor grapplerGrap;
	
	private RMIRegulatedMotor turnArm;
	
//	private RMISampleProvider distance;	
//	private RMISampleProvider compass;
	
	private SampleProvider distance;	
	private SampleProvider compass;
	private EV3UltrasonicSensor distanceSensor;
	private MindsensorsCompass compassSensor;
	
	
	private EV3ColorSensor colorSensor;
	private SampleProvider color;
	private SampleProvider light;
	
	private DifferentialPilot pilot;
	
	private final double wheelDiameter = 56;
	private final double trackWidth = 135;
	private final double travelRatio = 100;
	private final double turnArmRatio = 35/9;
	
	private final int grapplerSpeed = 20;
	private final int grapSpeed = 20;
	
	private Orientation orientation;
	private Direction turnDirection;	
	
	
	
	public Robot() throws RemoteException, MalformedURLException, NotBoundException{
		
//		initialize remote ev3
		RemoteEV3 remote = new RemoteEV3(Configuration.IP_EV3_2);
		
		
//		initialize Motors
		leftDrive = new EV3LargeRegulatedMotor(MotorPort.C);		
		rightDrive = new EV3LargeRegulatedMotor(MotorPort.B);
		
		grapplerMove = new EV3LargeRegulatedMotor(MotorPort.D);
		grapplerGrap = new EV3LargeRegulatedMotor(MotorPort.A);
		
		grapplerMove.setSpeed(grapplerSpeed);
		grapplerGrap.setSpeed(grapSpeed);
		
		turnArm = remote.createRegulatedMotor("D", 'M');
		
		turnArm.setSpeed(20);
		
		
//		initialize Sensors				
//		distance = remote.createSampleProvider("S4", "lejos.hardware.sensor.EV3UltrasonicSensor", "Distance");		
		
		//compass = remote.createSampleProvider("S3", "lejos.hardware.sensor.MindsensorCompass", "Compass");	
		
		Port distancePort = remote.getPort("S4");
		Port compassPort = remote.getPort("S3");
		
		EV3UltrasonicSensor distanceSensor = new EV3UltrasonicSensor(distancePort);
		MindsensorsCompass compassSensor = new MindsensorsCompass(compassPort);
		
		distance = distanceSensor.getDistanceMode();
		compass = compassSensor.getCompassMode();
		
		colorSensor = new EV3ColorSensor(SensorPort.S2);
		color = colorSensor.getColorIDMode();
		light = colorSensor.getAmbientMode();
		
//		initialize pilot
		pilot = new DifferentialPilot(wheelDiameter, trackWidth, leftDrive, rightDrive, true);
		pilot.setTravelSpeed(120);
		pilot.setRotateSpeed(60);
		
		
		this.orientation = Orientation.NORTH;
		
				
	}
	
	
	public void shutdown() throws RemoteException{
//		this.distance.close();
//		this.compass.close();
		this.compassSensor.close();
		this.distanceSensor.close();
	}

	

	@Override
	public void rotateTurnArm(int degrees) throws RemoteException {
		int degreesRatio = (int) Math.round(degrees * turnArmRatio);
		turnArm.rotate(degreesRatio);
		
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
	public float getDistance() throws RemoteException {
//		float[] sample = distance.fetchSample();
		float[] sample = new float[distance.sampleSize()];
		distance.fetchSample(sample, 0);
		
		return sample[0];
	}


	@Override
	public void stop() {
		pilot.stop();
	}


	@Override
	public float getHeading() throws RemoteException {
//		float[] sample = compass.fetchSample();	
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


	@Override
	public void grapObject() {
		// TODO Auto-generated method stub
		
	}


	@Override
	public void setTravelSpeed(int speed) {
		pilot.setTravelSpeed(speed);		
	}
	
	
	@Override
	public void setRotateSpeed(double speed){
		pilot.setRotateSpeed(speed);
	}


	@Override
	public float getLightIntensity() {
		float[] sample = new float[light.sampleSize()];
		light.fetchSample(sample, 0);
		return sample[0];
	}


	@Override
	public float getLightColor() {
		float[] sample = new float[color.sampleSize()];
		color.fetchSample(sample, 0);
		return sample[0];
	}
	
}
