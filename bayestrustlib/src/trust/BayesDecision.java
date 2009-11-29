package trust;

import trust.model.TrustInterface;

/**
 * Default B-trust decision module.
 * 
 * Simplifies trust framework output into a more intelligible output.
 * 
 * See <b>Trust decision</b>.
 * @author cfournie
 */
public class BayesDecision extends TrustDecision {
	
	/**
	 * Constructor
	 * @param trust
	 */
	public BayesDecision(TrustInterface trust) {
		super(trust);
	}

	/**
	 * Translates a trust pmf into one value
	 * @see trust.TrustDecision#getCondensedTrustValue(double[])
	 * @param d Trust pmf
	 * @return Trust level [0,n-1]
	 */
	public double getCondensedTrustValue(double[] d) {
		double t_xy = 0;
		int n = this.trust.getN();
		
		for(int j = 0; j < n; j++) {
			t_xy = d[j] * (j / n);
		}
		
		return t_xy;
	}
}
