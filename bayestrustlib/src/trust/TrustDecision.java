package trust;

import trust.model.TrustInterface;

/**
 * 
 * @author cfournie
 */
public abstract class TrustDecision {
	/** B-trust implementation framework */
	protected TrustInterface trust;
	
	/**
	 * Constructor 
	 * @param trust B-trust framework
	 */
	public TrustDecision(TrustInterface trust) {
		this.trust = trust;
	}
	
	/**
	 * Interpret a trust pmf as a single value
	 * @param d
	 * @return cts trust value
	 */
	public abstract double getCondensedTrustValue(double[] d);
}
