/**
 * @author Catalin Patulea <cat@vv.carleton.ca>
 */
package trust.model.sets;
import trust.model.math.Stats;
import trust.model.primitives.*;
import java.util.HashMap;
import java.util.Map;

public class RecommendedTrustSet extends TrustSet {
	protected Map<String, double[]> store;
	
	/**
	 * Construct an RTS store.
	 * @param nLevels number of trust levels
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
			return defaultTrust();
		} else {
			return trust;
		}
	}
}
