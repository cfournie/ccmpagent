package trust;

import trust.model.TrustInterface;

/**
 * 
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
	 * 
	 */
	public double getCondensedTrustValue(double[] d) {
		return Math.random() * this.trust.getN();
	}

}
