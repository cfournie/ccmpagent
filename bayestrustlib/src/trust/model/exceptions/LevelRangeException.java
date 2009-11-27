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
		super("Invalid discrete level " + l + " is outside of [0, " +
			(stats.getN() - 1) + "]");
	}
	
	public LevelRangeException(double l, Stats stats) {
		super("Invalid continuous level " + l + " is outside of [0, 1]");
	}
}
