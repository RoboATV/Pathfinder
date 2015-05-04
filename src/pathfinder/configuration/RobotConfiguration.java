package pathfinder.configuration;

import pathfinder.orientation.Orientation;
import lejos.hardware.port.MotorPort;
import lejos.hardware.port.Port;
import lejos.hardware.port.SensorPort;

public class RobotConfiguration {
	public final static Port	PORT_LEFT_DRIVE		= MotorPort.C;
	public final static Port	PORT_RIGHT_DRIVE	= MotorPort.B;
	public final static Port	PORT_GRAPPLER_MOVE	= MotorPort.D;
	public final static Port	PORT_GRAPPLER_GRAP	= MotorPort.A;
	public final static String	PORT_TURN_ARM		= "D";
	public final static String	PORT_DISTANCE		= "S4";
	public final static String	PORT_COMPASS		= "S3";
	public final static Port	PORT_COLOR			= SensorPort.S2;
	
	public final static double	WHEEL_DIAMETER	= 41.5f;
	public final static double	TRACK_WIDTH		= 260.0f;
	
	public final static double	SPEED_CARRIAGE_TRAVEL	= 120.0;
	public final static double	SPEED_CARRIAGE_ROTATE	= 60.0;
	public final static int		SPEED_GRAPPLER_MOVE		= 20;
	public final static int		SPEED_GRAPPLER_GRAP		= 20;
	public final static int		SPEED_TURN_ARM_TURN		= 50;
	
	public final static double	RATIO_CARRIAGE	= 10.0;
	public final static double	RATIO_TURN_ARM	= 39/9;
	public final static double	RATIO_GRAPPLER	= 3.0;
	
	public final static boolean	REVERSE_DRIVE_PILOT	= true;
	public final static boolean	REVERSE_TURN_ARM	= true;
	
	public final static Orientation	INITIAL_ORIENTATION	= Orientation.NORTH;
	
	public final static int	ROTATE_GRAPPLER_GRAP	= 180;
	public final static int	ROTATE_GRAPPLER_MOVE	= 270;
}
