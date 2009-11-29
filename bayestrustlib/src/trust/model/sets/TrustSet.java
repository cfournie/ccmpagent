package trust.model.sets;

import java.util.Arrays;

import trust.model.math.Misc;
import trust.model.math.Stats;
import trust.model.primitives.Context;
import trust.model.primitives.Peer;

/**
 * Generic methods for trust sets
 * @author cfournie
 */
public abstract class TrustSet {
	/** Statistics instance, containing n */
	protected Stats stats;
	/** Misc methods instance */
	protected Misc misc;
	
	/**
	 * Constructor
	 * @param stats Statistics instance
	 */
	protected TrustSet(Stats stats) {
		this.stats = stats;
		this.misc = new Misc(stats);
	}
	
	/**
	 * Returns a suitable key for usage in hashmaps from a Context and a Peer
	 * @param c Context
	 * @param p Peer
	 * @return
	 */
	protected String keyFrom(Context c, Peer p) {
		return c.getName() + "." + p.getName();
	}

	/**
	 * Get default trust value for bootstrapping
	 * @return
	 */
	protected double defaultTrust() {
		return 1.0 / this.stats.getN();
	}
	
	/**
	 * Get default trust value array for bootstrapping
	 * @return
	 */
	protected double[] defaultTrustArray() {
		double[] trust = new double[this.stats.getN()];
		Arrays.fill(trust, this.defaultTrust());
		return trust;
	}
}
