package trust.model.exceptions;

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
	public MalformedTupleException(int tupleLength, int nLevels) {
		super("Received " + tupleLength + "expected " + nLevels);
	}
}
