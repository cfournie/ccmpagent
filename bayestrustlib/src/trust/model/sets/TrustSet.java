/**
 * 
 */
package trust.model.sets;

/**
 * @author cfournie
 *
 */
public abstract class TrustSet {
	protected int nLevel = 0;
	
	public TrustSet(int nLevel) {
		this.nLevel = nLevel;
	}
}
