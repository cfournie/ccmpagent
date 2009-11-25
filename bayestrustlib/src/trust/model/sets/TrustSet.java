/**
 * 
 */
package trust.model.sets;

import java.util.Arrays;

import trust.model.math.Misc;
import trust.model.math.Stats;
import trust.model.primitives.Context;
import trust.model.primitives.Peer;

/**
 * @author cfournie
 *
 */
public abstract class TrustSet {
	protected Stats stats;
	protected Misc misc;
	
	protected TrustSet(Stats stats) {
		this.stats = stats;
		this.misc = new Misc(stats);
	}
	
	protected String keyFrom(Context c, Peer p) {
		return c.getName() + "." + p.getName();
	}
	
	protected double[] defaultTrust() {
		double[] trust = new double[this.stats.getN()];
		Arrays.fill(trust, 1.0 / this.stats.getN());
		return trust;
	}
}
