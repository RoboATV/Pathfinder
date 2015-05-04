package pathfinder.components;

/**
 * Interface fo a grappler.
 */
public interface IGrappler {
	/**
	 * Grap an object.
	 */
	public void grap();
	
	/**
	 * Release an object.
	 */
	public void release();
	
	/**
	 * Returns if there is already an object loaded.
	 * 
	 * @return	boolean
	 *   if an object is loaded
	 */
	public boolean isLoaded();
}
