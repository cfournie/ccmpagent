package trust.model.sets;
import trust.model.math.Misc;
import trust.model.primitives.*;
import java.util.HashMap;
import java.util.Map;

public class RecommendedTrustSet extends TrustSet {
	protected Map<String, double[]> store;
	
	/**
	 * Construct an RTS store.
	 * @param nLevels number of trust levels
	 */
	public RecommendedTrustSet()
	{
		store = new HashMap<String, double[]>();
	}
	
	public void store(Context ck, Peer py, double[] r) throws IllegalArgumentException
	{
		Misc.checkTuple(r);
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
