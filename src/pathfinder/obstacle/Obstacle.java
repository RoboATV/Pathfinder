package pathfinder.obstacle;

public enum Obstacle {

	SMALL {
		@Override
		public boolean conquerable() {
			return true;
		}
	}, MEDIUM {
		@Override
		public boolean conquerable() {
			return false;
		}
	}, HIGH {
		@Override
		public boolean conquerable() {
			return false;
		}
	};
	
	
	public abstract boolean conquerable();

	
}
