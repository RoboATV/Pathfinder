package pathfinder.location;

@SuppressWarnings("serial")
public class EndOfRoom extends Exception {
	@Override
	public String toString(){
		return "Reached end of room.";
	}
}
