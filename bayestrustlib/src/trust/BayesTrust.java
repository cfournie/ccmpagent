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
	protected int n = 0;
	
	/**
	 * 
	 * @param n
	 */
	public BayesTrust(int nLevels, List<Context> contexts) {
		this.dts = new DirectTrustSet(nLevels);
		this.des = new DirectExperienceSet(nLevels);
		this.rts = new RecommendedTrustSet(nLevels);
		this.srs = new SentRecommendationSet(nLevels);
		
		this.n = nLevels;
		this.c = new LinkedList<Context>(contexts);
	}
}
