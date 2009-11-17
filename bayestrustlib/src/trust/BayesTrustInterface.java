/**
 * 
 */
package trust;

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
	 */
	public boolean addPeer(Peer p);
	
	/**
	 * Returns overall trust in a peer for a context
	 * @param p Peer
	 * @param c Context
	 * @return double from 0 to nLevels
	 */
	public double getOverallTrust(Peer p, Context c);

	/**
	 * Returns recommended trust in a peer for a context to be sent to another agent
	 * @param p Peer
	 * @param c Context
	 * @return double from 0 to nLevels
	 */
	public double getRecommendedTrust(Peer p, Context c);
}
