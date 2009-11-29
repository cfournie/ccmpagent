package trust;

import trust.model.TrustInterface;

/**
 * Abstract trust decision interface module.
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
	 * Condenses a trust pmf into a single value
	 */
	public abstract double getCondensedTrustValue(double[] d);
}
