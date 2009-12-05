package trust.model.exceptions;

import trust.model.primitives.Context;
import trust.model.primitives.Peer;

/**
 * Thrown when information on a peer or context is requested but they have not been previously encountered.
 * @author cfournie
 */
public class PeerContextUnfoundException extends IllegalArgumentException {

	/**
	 * serialVersionUID
	 */
	private static final long serialVersionUID = 1L;

	/**
	 * Constructor.
	 */
	public PeerContextUnfoundException(Context ck, Peer py) {
		super("Unknown context '" + ck.getName() + "' or peer '" + py.getName() + "' encountered");
	}
}
