package trust.model.exceptions;

import trust.model.math.Stats;

public class MalformedTupleException extends IllegalArgumentException {
	/**
	 * serialVersionUID
	 */
	private static final long serialVersionUID = 1L;

	/**
	 * Constructor
	 * @param tupleLength
	 * @param nLevels
	 */
	public MalformedTupleException(int tupleLength) {
		super("Received " + tupleLength + "expected " + Stats.getN());
	}
}
