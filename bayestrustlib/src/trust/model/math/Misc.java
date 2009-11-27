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
	Stats stats;
	
	public Misc(Stats stats) {
		this.stats = stats;
	}
	
	/**
	 * Maps a continuous level on [0, 1] to a discrete level on [0, N - 1].
	 * @param l continuous level on [0, 1]
	 * @return discrete level in [0, N - 1]
	 */
	public int discretize(double level) {
		checkLevel(level);
		
		// Approximately map [0, 1] to [0, 1).
		double exclusive = level * 0.999;
		
		// Map equally-sized intervals [i/N, (i+1)/N) to i.
		return (int)Math.floor(exclusive * stats.getN());
	}
	
	/**
	 * Creates an empty 2d array of size n*n
	 * @return
	 */
	public double[][] makeMatrix() {
		double d[][] = {};
		
		for(int i = 0; i < this.stats.getN(); i++)
		{
			Arrays.fill(d[i], 0, this.stats.getN(), 0.0);
		}
		
		return d;
	}
	
	/**
	 * Creates an filled 2d array of size n*n
	 * @return
	 */
	public double[][] makeMatrix(double l) {
		double d[][] = {};
		
		for(int i = 0; i < this.stats.getN(); i++)
		{
			Arrays.fill(d[i], 0, this.stats.getN(), l);
		}
		
		return d;
	}
	
	/**
	 * Creates an empty array of size n
	 * @return
	 */
	public double[] makeTuple() {
		double d[] = {};
		Arrays.fill(d, 0, this.stats.getN(), 0.0);
		return d;
	}
	
	/**
	 * Creates a filled array of size n
	 * @return
	 */
	public double[] makeTuple(double l) {
		double d[] = {};
		Arrays.fill(d, 0, this.stats.getN(), l);
		return d;
	}
	
	/**
	 * Checks whether a discrete level is within range
	 * @param l
	 */
	public void checkLevel(int l) throws LevelRangeException {
		if (l < 0 || l > this.stats.getN())
			throw new LevelRangeException(l, stats);
	}
	
	/**
	 * Checks whether a cts level is within range
	 * @param l
	 */
	public void checkLevel(double l) throws LevelRangeException {
		if (l < 0 || l > 1.0)
			throw new LevelRangeException(l, stats);
	}
	
	/**
	 * Checks whether a tuple size is within range
	 * @param l
	 */
	public void checkTuple(double[] d) throws MalformedTupleException {
		if (d.length != this.stats.getN())
			throw new MalformedTupleException(d.length, this.stats);
	}
	
	/**
	 * Checks whether a matrix size is within range
	 * @param l
	 */
	public void checkMatrix(double[][] d) throws MalformedTupleException {
		if (d.length != this.stats.getN())
			throw new MalformedTupleException(d.length, this.stats);
		
		for(int i = 0; i < d.length; i++)
			if (d[i].length != this.stats.getN())
				throw new MalformedTupleException(d[i].length, this.stats);
	}
}
