package pathfinder.orientation;

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

	public static Orientation getOrientation(int angle) throws NoOrientationToAngle{
		if(angle < 0){
			angle = 360 + angle;
		}
		
		if(angle == 360){
			angle = 0;
		}
		
		for(Orientation orientation : Orientation.values()){
			if(orientation.getAngle() == angle){
				return orientation;
			}
		}
		throw new NoOrientationToAngle(angle);
	}
	
}
