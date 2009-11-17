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
	public double[][] setTrustTuple(Peer p, Context c, double[][] ec) throws MalformedTupleException
	{
		if (ec.length != this.nLevels)
			throw new MalformedTupleException(ec.length, this.nLevels);
		
		return set.put(super.keyFrom(c, p), ec);
	}
	
	/**
	 * Get trust n-tuple
	 * @param p Peer
	 * @param c Context
	 * @return
	 */
	public double[][] getTrustTuple(Peer p, Context c)
	{
		return set.get(super.keyFrom(c, p));
	}
}
