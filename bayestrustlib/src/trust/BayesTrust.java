/**
 * 
 */
package trust;

import java.util.LinkedList;
import java.util.List;
import trust.model.sets.*;
import trust.model.exceptions.DuplicatePeerException;
import trust.model.exceptions.LevelRangeException;
import trust.model.math.Misc;
import trust.model.math.Stats;
import trust.model.primitives.*;

/**
 * @author cfournie
 *
 */
public class BayesTrust implements TrustInterface {
	protected List<Context> c = new LinkedList<Context>();
	protected List<Peer> p = new LinkedList<Peer>();
	protected DirectTrustSet dts;
	protected DirectExperienceSet des;
	protected RecommendedTrustSet rts;
	protected SentRecommendationSet srs;
	protected Stats stats;
	protected Misc misc;
	
	/** Weighting factor sigma, defines how heavily direct trust is considered
	 *  versus recommended trust */
	private static final double SIGMA = 0.5;
	
	/**
	 * Constructor
	 * @param n
	 */
	public BayesTrust(int nLevels, List<Context> contexts) {
		this.stats = new Stats(nLevels);
		this.misc = new Misc(stats);
		
		this.dts = new DirectTrustSet(stats);
		this.des = new DirectExperienceSet(stats);
		this.rts = new RecommendedTrustSet(stats);
		this.srs = new SentRecommendationSet(stats);
		
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
			// Init DTS with trust values resulting in no confidence (variance = 0), or 1/n
			des.store(ck, py, this.misc.makeMatrix());
			
			// Init DES with blank experience counts
			des.store(ck, py, this.misc.makeMatrix());
			
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
	 * 
	 * See <b>Trust evolution through direct experience evaluation</b>
	 * @param ck
	 * @param py
	 * @param lb
	 */
	public void storeEncounter(Context ck, Peer py, double lb) throws LevelRangeException {
		// Encountered trust level lb while interacting with a peer with trust level la (previous trust)
		des.storeEncounter(ck, py, misc.discretize(lb), misc.discretize(getOverallTrust(ck, py)));
	}
	
	/**
	 * Record a recommendation
	 * 
	 * See <b>Trust evolution through direct recommendation evaluation</b>
	 * @param ck context
	 * @param pr recommender
	 * @param py subject of recommendation
	 * @param ctsBeta continuous recommendation level on [0, 1.0]
	 * @author Catalin Patulea <cat@vv.carleton.ca>
	 */
	public void storeRecommendation(Context ck, Peer pr, Peer py, double ctsBeta) throws LevelRangeException {
		int beta = misc.discretize(ctsBeta);
		
		// Prior belief of recommended trust in py.
		double [] r = rts.retrieve(ck, py);
		
		// Prior belief of what pr will send about py.
		double [][] rc = srs.retrieve(ck, pr);
		
		double [] newR = misc.makeTuple();
		for (int alpha = 0; alpha < stats.getN(); alpha++) {
			double denom = 0.0;
			for (int gamma = 0; gamma < stats.getN(); gamma++) {
				denom += r[gamma] * pSRgivenRT(rc, beta, gamma);
			}
			newR[alpha] = r[alpha] * pSRgivenRT(rc, beta, alpha) / denom;
		}
		
		rts.store(ck, py, newR);
		
		for (int alpha = 0; alpha < stats.getN(); alpha++) {
			rc[alpha][beta] = rc[alpha][beta] + newR[alpha];
		}
		
		srs.store(ck, pr, rc);
	}
	
	private double pSRgivenRT(double [][] rc, int beta, int alpha) {
		double denom = 0.0;
		for (int gamma = 0; gamma < stats.getN(); gamma++) {
			denom += rc[alpha][gamma]; 
		}
		return rc[alpha][beta] / denom;
	}
	
	/**
	 * Retrieves the recommended trust in peer py, as computed from
	 * recommendations received from other peers. For unit testing only.
	 * 
	 * @param ck context
	 * @param py subject of recommendation
	 * @return pmf of recommended trust
	 */
	public double[] getRecommendedTrust(Context ck, Peer py) {
		return rts.retrieve(ck, py);
	}
	
	
	/**
	 * Returns overall trust in a peer for a context
	 * @param ck Context
	 * @param py Peer
	 * @return level from 0 to n
	 */
	public double getOverallTrust(Context ck, Peer py) {
		double[] d = dts.retrieve(ck, py);
		double[] r = rts.retrieve(ck, py);
		double dy = 0;
		double ry = 0;
		
		for (int j = 0; j < stats.getN(); j++)
		{
			dy += (j+1) * d[j];
			ry += (j+1) * r[j];
		}
		
		return ((SIGMA * dy) + ((1-SIGMA) * ry)) / (stats.getN());
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
		double[] t = this.misc.makeTuple();
		
		for (int j = 0; j < stats.getN(); j++)
		{
			t[j] = (SIGMA * d[j]) + ((1-SIGMA) * r[j]);
		}
		
		return stats.confidencePmfMax() / stats.variancePmf(t);
	}
}
