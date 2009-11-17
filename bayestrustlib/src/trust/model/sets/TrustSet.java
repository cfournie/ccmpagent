/**
 * 
 */
package trust.model.sets;

import java.util.Arrays;

import trust.model.primitives.Context;
import trust.model.primitives.Peer;

/**
 * @author cfournie
 *
 */
public abstract class TrustSet {
	protected int nLevels = 0;
	
	/**
	 * 
	 * @param nLevel
	 */
	public TrustSet(int nLevels) {
		this.nLevels = nLevels;
	}
	
	protected String keyFrom(Context c, Peer p) {
		return c.getName() + "." + p.getName();
	}
	
	protected double[] defaultTrust() {
		double[] trust = new double[nLevels];
		Arrays.fill(trust, 1.0 / nLevels);
		return trust;
	}
}
