package pathfinder.robot;

import java.net.MalformedURLException;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;

import lejos.hardware.ev3.LocalEV3;
import lejos.hardware.motor.EV3LargeRegulatedMotor;
import lejos.hardware.port.MotorPort;
import lejos.hardware.port.Port;
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
	
	private RMISampleProvider distance;	
	private RMISampleProvider compass;
	
	private DifferentialPilot pilot;
	
	private final double wheelDiameter = 56;
	private final double trackWidth = 135;
	private final double travelRatio = 100;
	private final double turnArmRatio = 1;
	
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
		
		turnArm = remote.createRegulatedMotor("D", 'L');
		
		turnArm.setSpeed(20);
		
		
//		initialize Sensors				
		distance = remote.createSampleProvider("S4", "lejos.hardware.sensor.EV3UltrasonicSensor", "Distance");		
		
		compass = remote.createSampleProvider("S3", "lejos.hardware.sensor.MindsensorCompass", "Compass");	
		
		
//		initialize pilot
		pilot = new DifferentialPilot(wheelDiameter, trackWidth, leftDrive, rightDrive);
		pilot.setTravelSpeed(120);
		pilot.setRotateSpeed(60);
		
		
		this.orientation = Orientation.NORTH;
		
				
	}
	
	
	public void shutdown() throws RemoteException{
		this.distance.close();
		this.compass.close();
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
		float[] sample = distance.fetchSample();
		
		return sample[0];
	}


	@Override
	public void stop() {
		pilot.stop();
	}


	@Override
	public float getHeading() throws RemoteException {
		float[] sample = compass.fetchSample();	
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
