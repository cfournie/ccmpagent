package trust.model.math;

public class Stats {
	
	public static double mean(double[] d)
	{
		double sum = 0;
		for(int i = 0; i < d.length; i++)
			sum += d[i];
		return sum / d.length;
	}
	
	public static double variance(double[] d)
	{
		double mean = Stats.mean(d);
		
		double sum = 0;
		for(int i = 0; i < d.length; i++)
			sum += Math.pow(d[i] - mean, 2);
		
		return sum / (d.length - 1);
	}
}
