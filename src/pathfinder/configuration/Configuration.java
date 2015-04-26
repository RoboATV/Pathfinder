package pathfinder.configuration;

import pathfinder.robot.Direction;

public class Configuration {
	public final static float WALLDISTANCE = 30;	
	public final static float INITIAL_WALLDISTANCE = 100;
	
	public final static float OBSTACLE_SIZE = 40;		
	public final static float OBSTACLE_OFFSET = 5;
	public final static float TOTAL_OBSTACLE_SIZE = OBSTACLE_OFFSET + OBSTACLE_SIZE;
	
	public final static Direction INITIAL_DIRECTION = Direction.RIGHT;
	
	public final static int GRID_SIZE = 40;
	
	public final static int TRAVEL_DISTANCE = 60;
	
	public final static String IP_EV3_1 = "192.168.178.20";
	public final static String IP_EV3_2 = "192.168.178.21";
}
