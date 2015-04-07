package pathfinder.robot;

public enum Orientation implements IOrientation{

	NORTH{
		@Override
		public int getAngle(){
			return 0;
		}
	}, 
	EAST {

		@Override
		public int getAngle() {
			return 90;
		}
		
	}, 
	SOUTH{

		@Override
		public int getAngle() {
			return 180;
		}
		
	}, 
	WEST{

		@Override
		public int getAngle() {
			return 270;
		}
		
	}

	
}
