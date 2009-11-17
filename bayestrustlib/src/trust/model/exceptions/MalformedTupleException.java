package trust.model.exceptions;

public class MalformedTupleException extends IllegalArgumentException {
	/**
	 * serialVersionUID
	 */
	private static final long serialVersionUID = 1L;

	/**
	 * Constructor
	 * @param tupleLength
	 * @param nLevel
	 */
	public MalformedTupleException(int tupleLength, int nLevel) {
		super("Recieved " + tupleLength + "expected " + nLevel);
	}
}
