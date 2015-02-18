package pathfinder.obstacle;

public enum Obstacle {

	SMALL {
		@Override
		public boolean conquerable() {
			return true;
		}

		@Override
		public int numericalValue() {
			return 1;
		}
	}, MEDIUM {
		@Override
		public boolean conquerable() {
			return false;
		}

		@Override
		public int numericalValue() {
			return 2;
		}
	}, HIGH {
		@Override
		public boolean conquerable() {
			return false;
		}

		@Override
		public int numericalValue() {
			return 3;
		}
	};
	
	
	public abstract boolean conquerable();
	public abstract int numericalValue();
	
}
