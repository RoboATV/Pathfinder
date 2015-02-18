package pathfinder.robot;

public enum Direction {

	LEFT {
		@Override
		public int getNumerical() {
			return -1;
		}
	},RIGHT {
		@Override
		public int getNumerical() {
			return 1;
		}
	};
	
	public abstract int getNumerical();  
	
}
