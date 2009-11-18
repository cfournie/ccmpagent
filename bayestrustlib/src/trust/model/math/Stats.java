package trust.model.math;

public class Stats {
	protected static int n = 0;
	
	public static void setN(int n) {
		Stats.n = n;
	}
	
	public static int getN() {
		return Stats.n;
	}
	
	/**
	 * Mean for a d that is a pmf
	 * @param n
	 * @return
	 */
	public static double meanPmf()
	{
		return 1/Stats.n;
	}
	
	/**
	 * Max variance for a d that is a pmf
	 * @param n
	 * @return
	 */
	public static double confidencePmfMax()
	{
		return (1-Stats.meanPmf());
	}
	
	/**
	 * Variance for a d that is a pmf
	 * @param d
	 * @return
	 */
	public static double variancePmf(double[] d)
	{
		double mean = Stats.meanPmf();
		
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
