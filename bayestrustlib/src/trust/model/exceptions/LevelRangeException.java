/**
 * 
 */
package trust.model.exceptions;

import trust.model.math.Stats;

/**
 * @author cfournie
 *
 */
public class LevelRangeException extends IllegalArgumentException {

	/**
	 * serialVersionUID
	 */
	private static final long serialVersionUID = 1L;
	
	public LevelRangeException(int l, Stats stats) {
		super("Invalid level '" + l + " was outside of the 0 to " + stats.getN() + " range");
	}
	
	public LevelRangeException(double l, Stats stats) {
		super("Invalid level '" + l + " was outside of the 0 to " + stats.getN() + " range");
	}
}
