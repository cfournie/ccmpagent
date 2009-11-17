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
	protected int nLevel = 0;
	
	/**
	 * 
	 * @param nLevel
	 */
	public TrustSet(int nLevel) {
		this.nLevel = nLevel;
	}
	
	public String keyFrom(Context c, Peer p) {
		return c.getName() + "." + p.getName();
	}
}
