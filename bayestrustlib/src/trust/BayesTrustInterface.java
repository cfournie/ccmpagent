/**
 * 
 */
package trust;

import trust.model.exceptions.DuplicatePeerException;
import trust.model.primitives.Peer;
import trust.model.primitives.Context;

/**
 * @author cfournie
 *
 */
public interface BayesTrustInterface {
	
	/**
	 * Adds a peer to be considered
	 * @param p Peer
	 * @return true upon success, else failure
	 * @throws DuplicatePeerException 
	 */
	public boolean addPeer(Peer p) throws DuplicatePeerException;
	
	/**
	 * Returns overall trust in a peer for a context
	 * @param ck Context
	 * @param py Peer
	 * @return double from 0 to nLevels
	 */
	public int getOverallTrust(Context ck, Peer py);

	/**
	 * Returns recommended trust in a peer for a context to be sent to another agent
	 * @param ck Context
	 * @param py Peer
	 * @return double from 0 to nLevels
	 */
	public int getRecommendedTrust(Context ck, Peer py);
}
