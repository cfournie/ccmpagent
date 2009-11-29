package trust.model.exceptions;

import trust.model.math.Stats;

/**
 * Thrown when a tuple is encountered of an incorrect size.
 * @author cfournie
 */
public class MalformedTupleException extends IllegalArgumentException {
	/**
	 * serialVersionUID
	 */
	private static final long serialVersionUID = 1L;

	/**
	 * Constructor
	 * @param tupleLength Received length
	 * @param stats Statistics instance
	 */
	public MalformedTupleException(int tupleLength, Stats stats) {
		super("Received " + tupleLength + "expected " + stats.getExpectedTupleLength());
	}
}
