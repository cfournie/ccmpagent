package trust.model.sets;

import java.util.*;

import trust.model.exceptions.MalformedTupleException;
import trust.model.exceptions.PeerContextUnfoundException;
import trust.model.math.Stats;
import trust.model.primitives.*;

/**
 * Direct Trust Set implementation.
 * 
 * See <b>reputation data structures</b>.
 * @author cfournie
 */
public class DirectTrustSet extends TrustSet {
	/** DTS */
	protected HashMap<String,double []> set;
	
	/**
	 * Constructor
	 * @param stats Statistics helper, holds n
	 */
	public DirectTrustSet(Stats stats)
	{
		super(stats);
		this.set = new HashMap<String,double []>();
		
	}
	
	/**
	 * Set trust n-tuple
	 * @param py Peer
	 * @param ck Context
	 * @param d Set of probabilities that p_x has l_j
	 * @throws MalformedTupleException
	 * @return Previous value
	 */
	public double[] store(Context ck, Peer py, double[] d) throws MalformedTupleException
	{
		this.misc.checkTuple(d);
		return set.put(super.keyFrom(ck, py), d);
	}
	
	/**
	 * Get trust n-tuple
	 * @param py Peer
	 * @param ck Context
	 * @return n-tuple d
	 */
	public double[] retrieve(Context ck, Peer py)
	{
		double[] tuple = this.set.get(super.keyFrom(ck, py));
		
		if (tuple == null)
			throw new PeerContextUnfoundException(ck, py);
		
		return tuple;
	}
}
