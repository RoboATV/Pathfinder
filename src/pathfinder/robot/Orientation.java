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
		
	};

	public Orientation getOrientation(int angle) throws NoOrientationToAngle{
		for(Orientation orientation : Orientation.values()){
			if(orientation.getAngle() == angle){
				return orientation;
			}
		}
		throw new NoOrientationToAngle();
	}
	
}
