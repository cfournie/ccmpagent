package trust.model.math;

public class Stats {
	protected int n = 0;
	
	
	public Stats(int n) {
		this.n = n;
	}
	
	public int getN() {
		return n;
	}
	
	/**
	 * Mean for a d that is a pmf
	 * @param n
	 * @return
	 */
	public double meanPmf()
	{
		return 1/this.n;
	}
	
	/**
	 * Max variance for a d that is a pmf
	 * @param n
	 * @return
	 */
	public double confidencePmfMax()
	{
		return (1-this.meanPmf());
	}
	
	/**
	 * Variance for a d that is a pmf
	 * @param d
	 * @return
	 */
	public double variancePmf(double[] d)
	{
		double mean = this.meanPmf();
		
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
	public double mean(double[] d)
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
	public double variance(double[] d)
	{
		double mean = this.mean(d);
		
		double sum = 0;
		for(int i = 0; i < d.length; i++)
			sum += Math.pow(d[i] - mean, 2);
		
		return sum / (d.length - 1);
	}
}
