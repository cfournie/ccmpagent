package trust.model;

import java.util.HashSet;
import java.util.List;

import trust.model.exceptions.DuplicatePeerException;
import trust.model.exceptions.LevelRangeException;
import trust.model.math.Misc;
import trust.model.math.Stats;
import trust.model.primitives.Context;
import trust.model.primitives.Peer;

/**
 * B-trust implementation with no logic other than random values returned
 * @author cfournie
 */
public class RandomTrust implements TrustInterface{
	/** Statistics helper */
	protected Stats stats;
	/** Miscellaneous methods */
	protected Misc misc;
	/** List of peers */
	protected HashSet<Peer> p = new HashSet<Peer>();
	
	/**
	 * Constructor
	 * @param nLevels
	 * @param contexts
	 */
	public RandomTrust(int nLevels, List<Context> contexts) {
		this.stats = new Stats(nLevels);
		this.misc = new Misc(stats);
	}
	
	/**
	 * @see trust.model.TrustInterface#addPeer(Peer)
	 */
	public boolean addPeer(Peer py) throws DuplicatePeerException {
		if (this.p.contains(py))
			throw new DuplicatePeerException(py);
		
		return this.p.add(py);
	}

	/**
	 * @see trust.model.TrustInterface#getOverallTrust(Context, Peer)
	 */
	public double[] getOverallTrust(Context ck, Peer py) {
		return misc.makeTuple(Math.random());
	}

	/**
	 * @see trust.model.TrustInterface#getOverallTrustConfidence(Context, Peer)
	 */
	public double getOverallTrustConfidence(Context ck, Peer py) {
		return Math.random();
	}

	/**
	 * @see trust.model.TrustInterface#storeEncounter(Context, Peer, double, TrustDecision)
	 */
	public void storeEncounter(Context ck, Peer py, double level) throws LevelRangeException {
		misc.checkLevel(level);
	}

	/**
	 * @see trust.model.TrustInterface#storeRecommendation(Context, Peer, Peer, double)
	 */
	public void storeRecommendation(Context ck, Peer pr, Peer py, double level) throws LevelRangeException {
		misc.checkLevel(level);
	}
	
	/**
	 * Gets the number of levels
	 */
	public int getN() {
		return stats.getN();
	}
}
