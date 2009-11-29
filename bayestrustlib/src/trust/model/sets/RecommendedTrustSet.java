package trust.model.sets;

import trust.model.math.Stats;
import trust.model.primitives.*;
import java.util.HashMap;
import java.util.Map;

/**
 * Recommended Trust Set implementation
 * 
 * See <b>reputation data structures</b>
 * Catalin Patulea <cat@vv.carleton.ca>
 */
public class RecommendedTrustSet extends TrustSet {
	protected Map<String, double[]> store;
	
	/**
	 * Construct an RTS store.
	 * @param stats Statistics helper, holds n
	 */
	public RecommendedTrustSet(Stats stats)
	{
		super(stats);
		store = new HashMap<String, double[]>();
	}
	
	public void store(Context ck, Peer py, double[] r) throws IllegalArgumentException
	{
		misc.checkTuple(r);
		store.put(keyFrom(ck, py), r);
	}
	
	public double[] retrieve(Context ck, Peer py)
	{
		double[] trust = store.get(keyFrom(ck, py));
		if (trust == null) {
			return defaultTrustArray();
		} else {
			return trust;
		}
	}
}
