package trust.model.sets;

import java.util.*;

import trust.model.exceptions.MalformedTupleException;
import trust.model.exceptions.PeerContextUnfoundException;
import trust.model.math.Stats;
import trust.model.primitives.Context;
import trust.model.primitives.Peer;

/**
 * Direct Experience Set implementation.
 * 
 * See <b>reputation data structures</b>.
 * @author cfournie
 */
public class DirectExperienceSet extends TrustSet {
	/** DES */
	protected Map<String,double [][]> set;
	
	/**
	 * Constructor
	 * @param stats Statistics helper, holds n
	 */
	public DirectExperienceSet(Stats stats)
	{
		super(stats);
		set = new HashMap<String, double[][]>();
	}
	
	/**
	 * Set trust n-tuple
	 * @param py Peer
	 * @param ck Context
	 * @param ec Set of probabilities that p_x has l_j
	 * @throws MalformedTupleException
	 * @return Previous n-tuple ec
	 */
	public double[][] store(Context ck, Peer py, double[][] ec)
	{
		this.misc.checkMatrix(ec);
		return set.put(super.keyFrom(ck, py), ec);
	}
	
	/**
	 * Get trust n-tuple
	 * @param py Peer
	 * @param ck Context
	 * @return n-tuple ec
	 */
	public double[][] retrieve(Context ck, Peer py)
	{
		double[][] tuple = set.get(super.keyFrom(ck, py));
		
		if (tuple == null)
			throw new PeerContextUnfoundException(ck, py);
		
		return tuple;
	}
}
