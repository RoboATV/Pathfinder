package pathfinder.behaviors;

import java.util.ArrayList;
import java.util.Iterator;

import lejos.robotics.subsumption.Behavior;
import pathfinder.location.Locator;
import pathfinder.moves.IMove;
import pathfinder.robot.IRobot;

public class RescueVictim implements Behavior {
	private IRobot	robot;
	private boolean	suppressed = false;
	private Locator	locator;
	
	private ArrayList<IMove> movePath = new ArrayList<IMove>();
	
	public RescueVictim(IRobot robot, Locator locator) {
		this.robot = robot;
		this.locator = locator;
	}
	
	@Override
	public void action() {
		if(!this.suppressed) {
			this.travelToStart();
			this.deliverVictim();
			this.travelToLastLocation();
		}
	}

	@Override
	public void suppress() {
		this.suppressed = true;
	}

	@Override
	public boolean takeControl() {
		return this.robot.grappler_isLoaded();
	}
	
	private void travelToStart() {
		movePath = this.locator.returnToStart();
	}
	
	private void deliverVictim() {
		try {
			this.robot.carriage_rotate(180);
		} catch (Exception e) {
			System.out.println(e.getMessage());
		}
		
		this.robot.grappler_release();
	}
	
	private void travelToLastLocation() {
		for (Iterator<IMove> iterator = movePath.iterator(); iterator.hasNext();) {
			IMove move = (IMove) iterator.next();
			move.execute();
		}
	}

}
