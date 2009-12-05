package trust.model.math;

import trust.model.exceptions.*;
import java.text.*;

/**
 * Statistics helper class.
 * @author cfournie
 */
public class Stats {
	/** Number of levels */
	protected int n = 0;
	
	/**
	 * Constructor
	 * @param n Number of levels
	 */
	public Stats(int n) {
		this.n = n;
	}
	
	/**
	 * Gets the number of levels
	 * @return levels
	 */
	public int getN() {
		return n;
	}
	
	/**
	 * Mean for a d that is not a pmf
	 * @param d sample
	 * @return mean
	 */
	public double mean(double[] d)
	{
		Misc misc = new Misc(this);
		misc.checkPmf(d);
		
		double sum = 0.0;
		for(int j = 0; j < d.length; j++)
			sum += j * d[j];
		
		return sum; // * getN() / (getN() - 1);
	}
	
	/**
	 * Variance for a d that is not a pmf
	 * @param d sample
	 * @return variance
	 */
	public double variance(double[] d)
	{
		Misc misc = new Misc(this);
		misc.checkPmf(d);
		
		double mean = this.mean(d);
		
		double sum = 0;
		for(int j = 0; j < d.length; j++)
			sum += j * j * d[j];
		
		return sum - mean * mean;
	}
	
	/**
	 * Shows a pmf, its mean and variance on stdout.
	 * @param d pmf
	 */
	public void printPmf(double[] d) 
	{
		if (d.length != getN()) {
			throw new MalformedTupleException(d.length, this);
		}

		// mean = E(X)
		// var = E(X^2) - mean^2
		double sum = 0.0, mean = 0.0, var = 0.0;
		for (int j = 0; j < getN(); j++) {
			sum += d[j];
			mean += j * d[j];
			var += j * j * d[j];
		}
		
		var -= mean * mean;
		
		final DecimalFormat fmt = new DecimalFormat("0.000");
		System.out.print("[");
		for (int j = 0; j < getN(); j++) {
			System.out.print(fmt.format(d[j]));
			if (j != getN() - 1) {
				System.out.print(" ");
			}
		}
		
		mean = mean * getN() / (getN() - 1);
		System.out.print("] sum=" + fmt.format(sum));
		System.out.print(" mean=" + fmt.format(mean(d)));
		System.out.println(" var=" + fmt.format(variance(d)));
	}
}
