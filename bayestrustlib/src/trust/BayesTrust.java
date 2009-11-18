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
public class BayesTrust implements BayesTrustInterface {
	protected List<Context> c = new LinkedList<Context>();
	protected List<Peer> p = new LinkedList<Peer>();
	protected DirectTrustSet dts;
	protected DirectExperienceSet des;
	protected RecommendedTrustSet rts;
	protected SentRecommendationSet srs;
	
	/**
	 * Constructor
	 * @param n
	 */
	public BayesTrust(int nLevels, List<Context> contexts) {
		Stats.setN(nLevels);
		
		this.dts = new DirectTrustSet(nLevels);
		this.des = new DirectExperienceSet(nLevels);
		this.rts = new RecommendedTrustSet(nLevels);
		this.srs = new SentRecommendationSet(nLevels);
		
		this.c = new LinkedList<Context>(contexts);
	}
	
	/**
	 * @throws DuplicatePeerException 
	 * @see trust.BayesTrustInterface.addPeer
	 */
	public boolean addPeer(Peer py) throws DuplicatePeerException {
		
		if (p.contains(py))
			throw new DuplicatePeerException(py);
		
		for (Context ck : c)
		{
			// Init DTS
			
			// Init DES with blank experience counts
			des.store(ck, py, Misc.makeMatrix());
			
			// Init RTS
			
			// Init SRS
		}
		
		return this.p.add(py);
	}
	
	
	public void storeEncounter(Context ck, Peer py, int lb) {
		des.storeEncounter(ck, py, lb, getOverallTrust(ck, py));
	}

	/**
	 * @see trust.getOverallTrust
	 */
	public int getOverallTrust(Context ck, Peer py) {
		// TODO Auto-generated method stub
		return Misc.discretize(0);
	}

	/**
	 * @see trust.getRecommendedTrust
	 */
	public int getRecommendedTrust(Context ck, Peer pu) {
		// TODO Auto-generated method stub
		return Misc.discretize(0);
	}
}
