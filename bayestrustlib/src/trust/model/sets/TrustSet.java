/**
 * 
 */
package trust.model.sets;

import java.util.Arrays;

import trust.model.math.Stats;
import trust.model.primitives.Context;
import trust.model.primitives.Peer;

/**
 * @author cfournie
 *
 */
public abstract class TrustSet {
	
	protected String keyFrom(Context c, Peer p) {
		return c.getName() + "." + p.getName();
	}
	
	protected double[] defaultTrust() {
		double[] trust = new double[Stats.getN()];
		Arrays.fill(trust, 1.0 / Stats.getN());
		return trust;
	}
}
