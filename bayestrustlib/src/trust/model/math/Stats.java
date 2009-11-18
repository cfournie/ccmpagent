package trust.model.math;

public class Stats {
	
	/**
	 * Mean for a d that is a pmf
	 * @param n
	 * @return
	 */
	public static double mean(int n)
	{
		return 1/n;
	}
	
	/**
	 * Max variance for a d that is a pmf
	 * @param n
	 * @return
	 */
	public static double confidenceMax(int n)
	{
		return (1-Stats.mean(n));
	}
	
	/**
	 * Variance for a d that is a pmf
	 * @param d
	 * @return
	 */
	public static double variance(double[] d, int n)
	{
		double mean = Stats.mean(n);
		
		double sum = 0;
		for(int i = 0; i < d.length; i++)
			sum += Math.pow(d[i] - mean, 2);
		
		return sum / (d.length - 1);
	}
	
	/**
	 * Mean for a d that is not a pmf
	 * @param d
	 * @return
	 */
	public static double mean(double[] d)
	{
		double sum = 0;
		for(int i = 0; i < d.length; i++)
			sum += d[i];
		return sum / d.length;
	}
	
	/**
	 * Variance for a d that is not a pmf
	 * @param d
	 * @return
	 */
	public static double variance(double[] d)
	{
		double mean = Stats.mean(d);
		
		double sum = 0;
		for(int i = 0; i < d.length; i++)
			sum += Math.pow(d[i] - mean, 2);
		
		return sum / (d.length - 1);
	}
}
