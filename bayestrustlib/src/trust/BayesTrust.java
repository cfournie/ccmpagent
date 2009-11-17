/**
 * 
 */
package trust;

import java.util.LinkedList;
import java.util.List;
import trust.model.sets.*;
import trust.model.primitives.*;

/**
 * @author cfournie
 *
 */
public class BayesTrust implements BayesTrustInterface {
	protected List<Context> c = new LinkedList<Context>();
	protected List<Peer> p = new LinkedList<Peer>();
	protected DirectTrustSet dts;
	protected DirectExperienceSet des;
	protected RecommendedTrustSet rts;
	protected SentRecommendationSet srs;
	protected int nLevels = 0;
	
	/**
	 * Constructor
	 * @param n
	 */
	public BayesTrust(int nLevels, List<Context> contexts) {
		this.dts = new DirectTrustSet(nLevels);
		this.des = new DirectExperienceSet(nLevels);
		this.rts = new RecommendedTrustSet(nLevels);
		this.srs = new SentRecommendationSet(nLevels);
		
		this.nLevels = nLevels;
		this.c = new LinkedList<Context>(contexts);
	}
	
	/**
	 * @see trust.BayesTrustInterface.addPeer
	 */
	public boolean addPeer(Peer p) {
		return this.p.add(p);
	}

	/**
	 * @see trust.getOverallTrust
	 */
	public double getOverallTrust(Peer p, Context c) {
		// TODO Auto-generated method stub
		return 0;
	}

	/**
	 * @see trust.getRecommendedTrust
	 */
	public double getRecommendedTrust(Peer p, Context c) {
		// TODO Auto-generated method stub
		return 0;
	}
}
