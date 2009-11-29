package trust.model.sets;

import java.util.HashMap;

import trust.model.exceptions.MalformedTupleException;
import trust.model.math.Stats;
import trust.model.primitives.Context;
import trust.model.primitives.Peer;

/**
 * Direct Experience Set implementation
 * 
 * See <b>reputation data structures</b>
 * @author cfournie
 */
public class DirectExperienceSet extends TrustSet {
	/** DES */
	protected HashMap<String,double [][]> set;
	
	/**
	 * Constructor
	 * @param nLevels
	 */
	public DirectExperienceSet(Stats stats)
	{
		super(stats);
	}
	
	/**
	 * Updates data structure to represent an encounter
	 * 
	 * See <b>Trust evolution through direct experience evaluation</b>
	 * @param ck Context
	 * @param py Peer
	 * @param lb Trust level determined after encounter
	 * @param la Trust level acted upon
	 */
	public void storeEncounter(Context ck, Peer py, int lb, int la) {
		this.misc.checkLevel(la);
		this.misc.checkLevel(lb);
		
		double[][] ec = this.retrieve(ck, py);
		ec[lb][la]++;
		
		this.store(ck, py, ec);
	}
	
	/**
	 * Set trust n-tuple
	 * @param p Peer
	 * @param c Context
	 * @param d Set of probabilities that p_x has l_j
	 * @return Previous value
	 * @throws MalformedTupleException
	 */
	public double[][] store(Context ck, Peer py, double[][] ec)
	{
		this.misc.checkMatrix(ec);
		return set.put(super.keyFrom(ck, py), ec);
	}
	
	/**
	 * Get trust n-tuple
	 * @param p Peer
	 * @param c Context
	 * @return
	 */
	public double[][] retrieve(Context ck, Peer py)
	{
		return set.get(super.keyFrom(ck, py));
	}
}
