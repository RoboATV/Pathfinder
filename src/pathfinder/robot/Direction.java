package pathfinder.robot;

public enum Direction {

	LEFT {
		@Override
		public int getNumerical() {
			return -1;
		}

		@Override
		public int getTurnAngle() {
			return -90;
		}
	},RIGHT {
		@Override
		public int getNumerical() {
			return 1;
		}

		@Override
		public int getTurnAngle() {
			return 90;
		}
	};
	
	public static Direction getOpposite(Direction direction){
		if(direction == Direction.LEFT){
			return Direction.RIGHT;
		} 
		return Direction.LEFT;
	}
	
	public abstract int getNumerical();  
	public abstract int getTurnAngle();
}
