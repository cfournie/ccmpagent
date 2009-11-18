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
	
	public LevelRangeException(int l) {
		super("Invalid level '" + l + " was outside of the 0 to " + Stats.getN() + " range");
	}
	
	public LevelRangeException(double l) {
		super("Invalid level '" + l + " was outside of the 0 to " + Stats.getN() + " range");
	}
}
