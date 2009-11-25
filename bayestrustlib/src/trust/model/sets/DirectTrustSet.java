package trust.model.sets;

import java.util.*;

import trust.model.exceptions.MalformedTupleException;
import trust.model.math.Stats;
import trust.model.primitives.*;

public class DirectTrustSet extends TrustSet {
	/** DTS */
	protected HashMap<String,double []> set;
	
	/**
	 * Constructor
	 * @param n
	 */
	public DirectTrustSet(Stats stats)
	{
		super(stats);
		this.set = new HashMap<String,double []>();
		
	}
	
	/**
	 * Set trust n-tuple
	 * @param p Peer
	 * @param c Context
	 * @param d Set of probabilities that p_x has l_j
	 * @return Previous value
	 * @throws MalformedTupleException
	 */
	public double[] store(Context ck, Peer py, double[] d) throws MalformedTupleException
	{
		this.misc.checkTuple(d);
		return set.put(super.keyFrom(ck, py), d);
	}
	
	/**
	 * Get trust n-tuple
	 * @param p Peer
	 * @param c Context
	 * @return
	 */
	public double[] retrieve(Context ck, Peer py)
	{
		return set.get(super.keyFrom(ck, py));
	}
}
