/**
 * 
 */
package trust.model.sets;

import trust.model.primitives.Context;
import trust.model.primitives.Peer;

/**
 * @author cfournie
 *
 */
public abstract class TrustSet {
	protected int n = 0;
	
	/**
	 * 
	 * @param nLevel
	 */
	public TrustSet(int n) {
		this.n = n;
	}
	
	public String keyFrom(Context c, Peer p) {
		return c.getName() + "." + p.getName();
	}
}
