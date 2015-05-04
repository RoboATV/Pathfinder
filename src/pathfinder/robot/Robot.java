package pathfinder.robot;

import java.net.MalformedURLException;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;

import lejos.hardware.device.NXTCam;
import lejos.hardware.motor.EV3LargeRegulatedMotor;
import lejos.hardware.port.Port;
import lejos.hardware.sensor.EV3ColorSensor;
import lejos.hardware.sensor.EV3UltrasonicSensor;
import lejos.remote.ev3.RMIRegulatedMotor;
import lejos.remote.ev3.RemoteEV3;
import lejos.robotics.RegulatedMotor;
import lejos.robotics.SampleProvider;
import lejos.robotics.geometry.Rectangle2D;
import lejos.robotics.navigation.DifferentialPilot;
import lejos.robotics.navigation.Move;
import pathfinder.components.Carriage;
import pathfinder.components.Grappler;
import pathfinder.components.ICarriage;
import pathfinder.components.IGrappler;
import pathfinder.components.ITurnArm;
import pathfinder.components.TurnArm;
import pathfinder.configuration.Configuration;
import pathfinder.configuration.RobotConfiguration;
import pathfinder.orientation.Orientation;
import pathfinder.orientation.TurnNotPossible;

public class Robot implements IRobot{
	/**
	 * The turn arm.
	 * 
	 * @type	ITurnArm
	 */
	private	ITurnArm	turnArm;
	
	/**
	 * The carriage.
	 * 
	 * @type	ICarriage
	 */
	private ICarriage carriage;
	
	/**
	 * The grappler.
	 * 
	 * @type	IGrappler
	 */
	private IGrappler grappler;
	
	/**
	 * The ultrasonic distance sensor.
	 * 
	 * @type	EV3UltrasonicSensor
	 */
	private EV3UltrasonicSensor	distanceSensor;
	
	/**
	 * The distance sample provider.
	 * 
	 * @type	SampleProvider
	 */
	private SampleProvider		distance;
	
	/**
	 * The camera sensor.
	 * 
	 * @type	NXTCam
	 */
	private NXTCam				cameraSensor;
	
//	private MindsensorsCompass	compassSensor;
//	private SampleProvider		compass;
	
	/**
	 * The color sensor.
	 * 
	 * @type	EV3ColorSensor
	 */
	private EV3ColorSensor	colorSensor;
	
	/**
	 * The color id sample provider.
	 * 
	 * @type	SampleProvider
	 */
	private SampleProvider	color;
	
	/**
	 * The turn direction for the robot. If the robot should turn, this direction is used to do the turn.
	 * 
	 * @type	Direction
	 */
	private Direction turnDirection;
	
	/**
	 * Create a new robot. Configuration is set in {@link RobotConfiguration}.
	 * 
	 * @throws	RemoteException
	 * @throws	MalformedURLException
	 * @throws	NotBoundException
	 */
	public Robot() throws RemoteException, MalformedURLException, NotBoundException{
		System.out.println("loading remote EV3 brick");
		RemoteEV3 remote = new RemoteEV3(Configuration.IP_EV3_2);
		
		System.out.println("initializing carriage");
		RegulatedMotor leftDrive	= new EV3LargeRegulatedMotor(RobotConfiguration.PORT_LEFT_DRIVE);		
		RegulatedMotor rightDrive	= new EV3LargeRegulatedMotor(RobotConfiguration.PORT_RIGHT_DRIVE);
		DifferentialPilot pilot		= new DifferentialPilot(RobotConfiguration.WHEEL_DIAMETER, RobotConfiguration.TRACK_WIDTH, leftDrive, rightDrive, RobotConfiguration.REVERSE_DRIVE_PILOT);
		pilot.setTravelSpeed(RobotConfiguration.SPEED_CARRIAGE_TRAVEL);
		pilot.setRotateSpeed(RobotConfiguration.SPEED_CARRIAGE_ROTATE);
		this.carriage = new Carriage(pilot, RobotConfiguration.RATIO_CARRIAGE, RobotConfiguration.INITIAL_ORIENTATION);
		
		System.out.println("initializing turn arm");
		RMIRegulatedMotor turnArmMotor = remote.createRegulatedMotor(RobotConfiguration.PORT_TURN_ARM, 'M');
		turnArmMotor.setSpeed(RobotConfiguration.SPEED_TURN_ARM_TURN);
		this.turnArm = new TurnArm(RobotConfiguration.RATIO_TURN_ARM, RobotConfiguration.REVERSE_TURN_ARM, turnArmMotor);
		
		System.out.println("initializing grappler");
		RegulatedMotor	grapplerMove	= new EV3LargeRegulatedMotor(RobotConfiguration.PORT_GRAPPLER_MOVE);
		RegulatedMotor	grapplerGrap	= new EV3LargeRegulatedMotor(RobotConfiguration.PORT_GRAPPLER_GRAP);
		
		grapplerMove.setSpeed(RobotConfiguration.SPEED_GRAPPLER_MOVE);
		grapplerGrap.setSpeed(RobotConfiguration.SPEED_GRAPPLER_GRAP);
		
		this.grappler = new Grappler(grapplerMove, grapplerGrap, RobotConfiguration.ROTATE_GRAPPLER_MOVE, RobotConfiguration.ROTATE_GRAPPLER_GRAP, RobotConfiguration.RATIO_GRAPPLER);
		
		System.out.println("initializing sensors");
		Port distancePort	= remote.getPort(RobotConfiguration.PORT_DISTANCE);
		this.distanceSensor	= new EV3UltrasonicSensor(distancePort);
		this.distance		= this.distanceSensor.getDistanceMode();
		
		Port cameraPort		= remote.getPort(RobotConfiguration.PORT_CAMERA);
		this.cameraSensor	= new NXTCam(cameraPort);
		this.cameraSensor.sortBy(NXTCam.COLOR);
		this.cameraSensor.setTrackingMode(NXTCam.OBJECT_TRACKING);
		this.cameraSensor.enableTracking(true);
		
//		Port compassPort	= remote.getPort(RobotConfiguration.PORT_COMPASS);
//		this.compassSensor	= new MindsensorsCompass(compassPort);
//		this.compass		= this.compassSensor.getCompassMode();
		
		this.colorSensor	= new EV3ColorSensor(RobotConfiguration.PORT_COLOR);
		this.color			= this.colorSensor.getColorIDMode();
	}
	
	/**
	 * Do a shutdown of the robot. Free all used ports etc.
	 * 
	 * @throws	RemoteException
	 */
	public void shutdown() throws RemoteException{
		this.distanceSensor.close();
		this.cameraSensor.close();
//		this.compassSensor.close();
		this.colorSensor.close();
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
	public void carriage_rotate(int degrees) throws TurnNotPossible {
		this.carriage.rotate(degrees);
	}
	
	@Override
	public void carriage_rotateUnchecked(int degrees) {
		this.carriage.rotateUnchecked(degrees);
	}
	
	@Override
	public void carriage_turnLeft() throws TurnNotPossible {
		this.carriage.turnLeft();
	}
	
	@Override
	public void carriage_turnRight() throws TurnNotPossible {
		this.carriage.turnRight();
	}
	
	@Override
	public void carriage_travel(double distance) {
		this.carriage.travel(distance, false);
	}
	
	@Override
	public void carriage_travel(double distance, boolean immediateReturn) {
		this.carriage.travel(distance, immediateReturn);
	}
	
	@Override
	public void carriage_stop() {
		this.carriage.stop();
	}
	
	@Override
	public boolean carriage_isMoving() {
		return this.carriage.isMoving();
	}
	
	@Override
	public Move carriage_getMovement() {
		return this.carriage.getMovement();
	}
	
	@Override
	public Orientation carriage_getOrientation() {
		return this.carriage.getOrientation();
	}
	
	@Override
	public void grappler_grap() {
		this.grappler.grap();
	}
	
	@Override
	public void grappler_release() {
		this.grappler.release();
	}
	
	@Override
	public boolean grappler_isLoaded() {
		return this.grappler.isLoaded();
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
	public void invertTurnDirection() {
		Direction oldDirection = this.turnDirection;
		this.turnDirection = Direction.getOpposite(oldDirection);
	}
	
	@Override
	public float getDistance() throws RemoteException {
		float[] sample = new float[this.distance.sampleSize()];
		this.distance.fetchSample(sample, 0);
		
		return sample[0] * 100;
	}
	
	@Override
	public boolean victim_detectedCamera() {
		return this.cameraSensor.getNumberOfObjects() > 0;
	}
	
	@Override
	public Rectangle2D victim_getLocation() {
		if(this.victim_detectedCamera()) {
			return this.cameraSensor.getRectangle(0);
		}
		
		return null;
	}
	
	@Override
	public boolean victim_detectedColorSensor() {
		float[] sample = new float[this.color.sampleSize()];
		this.color.fetchSample(sample, 0);
		int detected = (int) sample[0];
		
		for (int i = 0; i < RobotConfiguration.VICTIM_COLORS.length; i++) {
			if(detected == RobotConfiguration.VICTIM_COLORS[i]) {
				return true;
			}
		}
		
		return false;
	}

//	@Override
//	public float getHeading() {
//		float[] sample = new float[this.compass.sampleSize()];
//		this.compass.fetchSample(sample, 0);
//		
//		return sample[0];
//	}
}
