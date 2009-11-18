/**
 * 
 */
package trust;

import java.util.LinkedList;
import java.util.List;
import trust.model.sets.*;
import trust.model.exceptions.DuplicatePeerException;
import trust.model.math.Misc;
import trust.model.math.Stats;
import trust.model.primitives.*;

/**
 * @author cfournie
 *
 */
public class BayesTrust {
	protected List<Context> c = new LinkedList<Context>();
	protected List<Peer> p = new LinkedList<Peer>();
	protected DirectTrustSet dts;
	protected DirectExperienceSet des;
	protected RecommendedTrustSet rts;
	protected SentRecommendationSet srs;
	
	/** A default value to initialize the DTS to */
	private static final double DEFAULT_TRUST = 0.25;
	/** Weighting factor sigma */
	private static final double SIGMA = 0.5;
	
	/**
	 * Constructor
	 * @param n
	 */
	public BayesTrust(int nLevels, List<Context> contexts) {
		Stats.setN(nLevels);
		
		this.dts = new DirectTrustSet();
		this.des = new DirectExperienceSet();
		this.rts = new RecommendedTrustSet();
		this.srs = new SentRecommendationSet();
		
		this.c = new LinkedList<Context>(contexts);
	}
	
	/**
	 * Adds and initializes the system for a new peer
	 * @throws DuplicatePeerException 
	 * @return true upon success, else failure
	 */
	public boolean addPeer(Peer py) throws DuplicatePeerException {
		boolean success = false;
		
		if (p.contains(py))
			throw new DuplicatePeerException(py);
		
		for (Context ck : c)
		{
			// Init DTS with no confidence trust values (variance = 0)
			des.store(ck, py, Misc.makeMatrix(DEFAULT_TRUST));
			
			// Init DES with blank experience counts
			des.store(ck, py, Misc.makeMatrix());
			
			// Init RTS
			// TODO: Add RTS init here
			
			// Init SRS
			// TODO: Add SRS init here
		}
		
		success = this.p.add(py);
		
		return success;
	}
	
	/**
	 * Record an encounter
	 * @param ck
	 * @param py
	 * @param lb
	 */
	public void storeEncounter(Context ck, Peer py, int lb) {
		des.storeEncounter(ck, py, lb, getOverallTrust(ck, py));
	}
	
	/**
	 * Record a recommendation
	 * @param ck
	 * @param py
	 * @param lb
	 */
	public void storeRecommendation(Context ck, Peer py, int lb) {
		// TODO: Implement
	}

	/**
	 * Returns overall trust in a peer for a context
	 * @param ck Context
	 * @param py Peer
	 * @param lj Level
	 * @return level from 0 to n-1
	 * @deprecated
	 */
	public int getOverallTrust(Context ck, Peer py, int lj) {
		double dj = dts.retrieve(ck, py)[lj];	// Direct trust
		double rj = rts.retrieve(ck, py)[lj];	// Recommended trust
		return Misc.discretize((SIGMA * dj) + ((1-SIGMA) * rj));
	}
	
	/**
	 * Returns overall trust in a peer for a context
	 * @param ck Context
	 * @param py Peer
	 * @return level from 0 to n
	 */
	public int getOverallTrust(Context ck, Peer py) {
		double[] d = dts.retrieve(ck, py);
		double[] r = rts.retrieve(ck, py);
		double dy = 0;
		double ry = 0;
		
		for (int i = 0; i < Stats.getN(); i++)
		{
			dy += (i+1) * d[i];
			ry += (i+1) * r[i];
		}
		
		return Misc.discretize((SIGMA * dy) + ((1-SIGMA) * ry));
	}
	
	/**
	 * Returns the confidence in the overall trust as a probability
	 * @param ck Context
	 * @param py Peer
	 * @return cts value from 0,1
	 */
	public double getOverallTrustConfidence(Context ck, Peer py) {
		double[] d = dts.retrieve(ck, py);
		double[] r = rts.retrieve(ck, py);
		double[] t = Misc.makeTuple();
		
		for (int i = 0; i < Stats.getN(); i++)
		{
			t[i] = (SIGMA * d[i]) + ((1-SIGMA) * r[i]);
		}
		
		return Stats.confidencePmfMax() / Stats.variancePmf(t);
	}
}
