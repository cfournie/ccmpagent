/**
 * 
 */
package trust.model.math;

import java.util.Arrays;

import trust.model.exceptions.LevelRangeException;
import trust.model.exceptions.MalformedTupleException;

/**
 * @author cfournie
 *
 */
public class Misc {
	
	/**
	 * Truncates a double, establishing whether a cts level has hit a particular lvl threshold
	 * @param l cts level
	 * @return
	 */
	public static int discretize(double l) {
		return (int)l;
	}
	
	/**
	 * Creates an empty 2d array of size n*n
	 * @return
	 */
	public static double[][] makeMatrix() {
		double d[][] = {};
		
		for(int i = 0; i < Stats.getN(); i++)
		{
			Arrays.fill(d[i], 0, Stats.getN(), 0.0);
		}
		
		return d;
	}
	
	/**
	 * Creates an filled 2d array of size n*n
	 * @return
	 */
	public static double[][] makeMatrix(double l) {
		double d[][] = {};
		
		for(int i = 0; i < Stats.getN(); i++)
		{
			Arrays.fill(d[i], 0, Stats.getN(), l);
		}
		
		return d;
	}
	
	/**
	 * Creates an empty array of size n
	 * @return
	 */
	public static double[] makeTuple() {
		double d[] = {};
		Arrays.fill(d, 0, Stats.getN(), 0.0);
		return d;
	}
	
	/**
	 * Creates a filled array of size n
	 * @return
	 */
	public static double[] makeTuple(double l) {
		double d[] = {};
		Arrays.fill(d, 0, Stats.getN(), l);
		return d;
	}
	
	/**
	 * Checks whether a discrete level is within range
	 * @param l
	 */
	public static void checkLevel(int l) throws LevelRangeException {
		if (l < 0 || l > Stats.getN())
			throw new LevelRangeException(l);
	}
	
	/**
	 * Checks whether a cts level is within range
	 * @param l
	 */
	public static void checkLevel(double l) throws LevelRangeException {
		if (l < 0 || l > Stats.getN())
			throw new LevelRangeException(l);
	}
	
	/**
	 * Checks whether a tuple size is within range
	 * @param l
	 */
	public static void checkTuple(double[] d) throws MalformedTupleException {
		if (d.length != Stats.getN())
			throw new MalformedTupleException(d.length);
	}
	
	/**
	 * Checks whether a matrix size is within range
	 * @param l
	 */
	public static void checkMatrix(double[][] d) throws MalformedTupleException {
		if (d.length != Stats.getN())
			throw new MalformedTupleException(d.length);
		
		for(int i = 0; i < d.length; i++)
			if (d[i].length != Stats.getN())
				throw new MalformedTupleException(d[i].length);
	}
}
