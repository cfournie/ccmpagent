package trust;

import trust.model.TrustInterface;

/**
 * Random trust decision interface module.
 * @author cfournie
 */
public class RandomDecision extends TrustDecision {
	
	/**
	 * Constructor
	 * @param trust
	 */
	public RandomDecision(TrustInterface trust) {
		super(trust);
	}

	/**
	 * @see trust.TrustDecision#getCondensedTrustValue(double[])
	 */
	public double getCondensedTrustValue(double[] d) {
		return Math.random() * this.trust.getN();
	}

}
