package trust.model;

import trust.TrustDecision;
import trust.model.exceptions.DuplicatePeerException;
import trust.model.exceptions.LevelRangeException;
import trust.model.primitives.Context;
import trust.model.primitives.Peer;

/**
 * B-trust interface.
 * @author cfournie
 */
public interface TrustInterface {
	/**
	 * Begin tracking a peer.
	 * @param py
	 * @return True if the peer was added successfully, false otherwise
	 * @throws DuplicatePeerException if a duplicate peer was added
	 */
	public boolean addPeer(Peer py) throws DuplicatePeerException;
	
	/**
	 * Record an encounter with a peer in a context
	 * @param ck
	 * @param py
	 * @param level
	 * @param td
	 * @throws LevelRangeException
	 */
	public void storeEncounter(Context ck, Peer py, double level, TrustDecision td) throws LevelRangeException;
	
	/**
	 * Receive a recommendation from one peer relating to another.
	 * @param ck
	 * @param pr
	 * @param py
	 * @param level
	 * @throws LevelRangeException
	 */
	public void storeRecommendation(Context ck, Peer pr, Peer py, double level) throws LevelRangeException;
	
	/**
	 * Get a trust pmf
	 * @param ck
	 * @param py
	 * @return Overall trust pmf
	 */
	public double[] getOverallTrust(Context ck, Peer py);
	
	/**
	 * Get confidence in the trust pmf
	 * @param ck
	 * @param py
	 * @return Confidence in trust pmf
	 */
	public double getOverallTrustConfidence(Context ck, Peer py);
	
	/**
	 * Gets the number of levels
	 */
	public int getN();
}
