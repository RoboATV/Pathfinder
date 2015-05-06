package pathfinder.moves;

import pathfinder.location.Locator;
import pathfinder.map.Coordinate;
import pathfinder.orientation.TurnNotPossible;

public class MoveToCoordinate implements IMove {
	/**
	 * The locator to execute the move.
	 * 
	 * @type	Locator
	 */
	private	Locator		locator;
	
	/**
	 * The coordinate to move to.
	 * 
	 * @type	Coordinate
	 */
	private	Coordinate	coordinate;
	
	/**
	 * Lets the robot move to a specified coordinate.
	 * 
	 * @param	Locator		locator
	 *   the locator to execute the move.
	 * @param	Coordinate	coordinate
	 *   the coordinate to move to.
	 */
	public MoveToCoordinate(Locator locator, Coordinate coordinate) {
		this.locator	= locator;
		this.coordinate	= coordinate;
	}
	
	@Override
	public void execute() {
		System.out.println("Move to coordinate: " + coordinate.toString());
		try {
			locator.relocateAbsolute(coordinate);
		} catch (TurnNotPossible e) {
			System.out.println(e.toString());
		}
	}
}
