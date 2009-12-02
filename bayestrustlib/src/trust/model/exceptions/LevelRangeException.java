package trust.model.exceptions;

import trust.model.math.Misc;
import trust.model.math.Stats;

/**
 * Thrown when a level is given as a parameter to a method in the framework of an incorrect range.
 * Levels are either continuous or discrete.
 * @author cfournie
 */
public class LevelRangeException extends IllegalArgumentException {

	/**
	 * serialVersionUID
	 */
	private static final long serialVersionUID = 1L;
	
	/**
	 * Discrete level from [0,n-1]
	 * @param l discrete level
	 * @param stats
	 */
	public LevelRangeException(int l, Stats stats) {
		super("Invalid discrete level " + l + " is outside of [0, " + new Misc(stats).getMaxDiscreteLevel() + "]");
	}

	/**
	 * Continuous level from [0,1]
	 * @param l cts level
	 */
	public LevelRangeException(double l) {
		super("Invalid continuous level " + l + " is outside of [0, 1]");
	}
}
