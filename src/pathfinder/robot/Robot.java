package pathfinder.robot;

import java.net.MalformedURLException;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;

import lejos.hardware.motor.EV3LargeRegulatedMotor;
import lejos.hardware.port.MotorPort;
import lejos.hardware.port.Port;
import lejos.hardware.port.SensorPort;
import lejos.hardware.sensor.EV3ColorSensor;
import lejos.hardware.sensor.EV3UltrasonicSensor;
import lejos.hardware.sensor.MindsensorsCompass;
import lejos.remote.ev3.RMIRegulatedMotor;
import lejos.remote.ev3.RemoteEV3;
import lejos.robotics.RegulatedMotor;
import lejos.robotics.SampleProvider;
import lejos.robotics.navigation.DifferentialPilot;
import lejos.robotics.navigation.Move;
import pathfinder.components.ITurnArm;
import pathfinder.components.TurnArm;
import pathfinder.configuration.Configuration;
import pathfinder.orientation.NoOrientationToAngle;
import pathfinder.orientation.Orientation;
import pathfinder.orientation.TurnNotPossible;

public class Robot implements IRobot{
	
	private RegulatedMotor leftDrive;
	private RegulatedMotor rightDrive;
	private RegulatedMotor grapplerMove;
	private RegulatedMotor grapplerGrap;
	
	private	ITurnArm	turnArm;
	
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
	
	private final double wheelDiameter = 41.5f;
	private final double trackWidth = 120;
	private final double travelRatio = 10;
	
	private final int grapplerSpeed = 20;
	private final int grapSpeed = 20;
	
	private Orientation orientation;
	private Direction turnDirection;	
	
	
	
	public Robot() throws RemoteException, MalformedURLException, NotBoundException{
		
//		initialize remote ev3
		System.out.println("loading remote");
		RemoteEV3 remote = new RemoteEV3(Configuration.IP_EV3_2);
		
		
//		initialize Motors
		System.out.println("initializing motors");
		leftDrive = new EV3LargeRegulatedMotor(MotorPort.C);		
		rightDrive = new EV3LargeRegulatedMotor(MotorPort.B);
		
		grapplerMove = new EV3LargeRegulatedMotor(MotorPort.D);
		grapplerGrap = new EV3LargeRegulatedMotor(MotorPort.A);
		
		grapplerMove.setSpeed(grapplerSpeed);
		grapplerGrap.setSpeed(grapSpeed);
		
		// Create the turn arm.
		RMIRegulatedMotor	turnArmMotor = remote.createRegulatedMotor("D", 'M');
		turnArmMotor.setSpeed(50);
		this.turnArm = new TurnArm(39/9, true, turnArmMotor);
		
		
//		initialize Sensors				
//		distance = remote.createSampleProvider("S4", "lejos.hardware.sensor.EV3UltrasonicSensor", "Distance");		
		
		//compass = remote.createSampleProvider("S3", "lejos.hardware.sensor.MindsensorCompass", "Compass");	
		
		System.out.println("initializing sensors");
		Port distancePort = remote.getPort("S4");
		Port compassPort = remote.getPort("S3");
		
		distanceSensor = new EV3UltrasonicSensor(distancePort);
		compassSensor = new MindsensorsCompass(compassPort);
		
		distance = distanceSensor.getDistanceMode();
		compass = compassSensor.getCompassMode();
		
		colorSensor = new EV3ColorSensor(SensorPort.S2);
		color = colorSensor.getColorIDMode();
		light = colorSensor.getAmbientMode();
		
//		initialize pilot
		System.out.println("initializing pilot");
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
		this.turnArm.shutdown();
	}

	

	@Override
	public void turnArm_rotate(int degrees) throws RemoteException {
		this.turnArm.rotate(degrees);
	}
	
	@Override
	public void turnArm_rotateToCenter() throws RemoteException {
		this.turnArm.rotateToCenter();
	}
	
	@Override
	public void turnArm_rotateToLeft() throws RemoteException {
		this.turnArm.rotateToLeft();
	}
	
	@Override
	public void turnArm_rotateToRight() throws RemoteException {
		this.turnArm.rotateToRight();
	}
	
	@Override
	public int turnArm_getTurnAngle() {
		return this.turnArm.getTurnAngle();
	}
	
	@Override
	public boolean turnArm_isCentered() {
		return this.turnArm.isCentered();
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
		
		return sample[0] * 100;
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


	@Override
	public void invertTurnDirection() {
		Direction oldDirection = this.turnDirection;
		turnDirection = Direction.getOpposite(oldDirection);
		
	}


	@Override
	public void travel(double distance, boolean immediateReturn) {
		this.pilot.travel(distance * this.travelRatio, immediateReturn);
	}


	@Override
	public Move getMovement() {
		return this.pilot.getMovement();
	}
	
	

	
}
