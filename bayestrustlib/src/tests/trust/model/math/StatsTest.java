package tests.trust.model.math;

import org.junit.Test;
import static org.junit.Assert.*;

import trust.model.math.Stats;

public class StatsTest {
	public static final int TRUST_LEVELS = 4;
	private Stats stats = new Stats(TRUST_LEVELS);
	
	@Test
	public void testMean() {
		double[] trust = new double[]{0.1, 0.2, 0.3, 0.4};
		assertTrue(stats.mean(trust) == 0.25);
	}
	
	@Test
	public void testNoVariance() {
		double[] trust = new double[]{0.25, 0.25, 0.25, 0.25};
		assertTrue(stats.variance(trust) == 0.0);
	}
	
	@Test
	public void testAverageVariance() {
		double[] trust = new double[]{0.1, 0.1, 0.8, 0.0};
		System.out.println(stats.variance(trust));
		assertTrue(stats.variance(trust) == 0.1366666666666667);
	}
}
