package trust.model.sets;

import java.util.HashMap;

import trust.model.exceptions.MalformedTupleException;
import trust.model.primitives.Context;
import trust.model.primitives.Peer;

public class DirectExperienceSet extends TrustSet {
	/** DES */
	HashMap<String,double [][]> set;
	
	/**
	 * Constructor
	 * @param nLevels
	 */
	public DirectExperienceSet(int nLevels)
	{
		super(nLevels);
	}
	
	/**
	 * Set trust n-tuple
	 * @param p Peer
	 * @param c Context
	 * @param d Set of probabilities that p_x has l_j
	 * @return Previous value
	 * @throws MalformedTupleException
	 */
	public double[][] store(Context ck, Peer py, double[][] ec) throws MalformedTupleException
	{
		if (ec.length != this.nLevels)
			throw new MalformedTupleException(ec.length, this.nLevels);
		
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
