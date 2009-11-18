/**
 * 
 */
package trust.model.exceptions;

import trust.model.primitives.Peer;

/**
 * @author cfournie
 *
 */
public class DuplicatePeerException extends IllegalArgumentException {

	/**
	 * serialVersionUID
	 */
	private static final long serialVersionUID = 1L;

	/**
	 * Constructor
	 */
	public DuplicatePeerException(Peer py) {
		super("Duplicate Peer '" + py.getName() + "' encountered");
	}
}
