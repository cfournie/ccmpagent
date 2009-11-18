package tests.trust.model.math;

import org.junit.Test;
import static org.junit.Assert.*;

import trust.model.math.Stats;
import trust.model.primitives.*;
import trust.model.exceptions.*;
import trust.model.sets.*;

public class StatsTest {
	public static final int TRUST_LEVELS = 4;
	
	@Test
	public void testMean() {
		double[] trust = new double[]{0.1, 0.2, 0.3, 0.4};
		assertTrue(Stats.mean(trust) == 0.25);
	}
	
	@Test
	public void testNoVariance() {
		double[] trust = new double[]{0.25, 0.25, 0.25, 0.25};
		assertTrue(Stats.variance(trust) == 0.0);
	}
	
	@Test
	public void testAverageVariance() {
		double[] trust = new double[]{0.1, 0.1, 0.8, 0.0};
		System.out.println(Stats.variance(trust));
		assertTrue(Stats.variance(trust) == 0.1366666666666667);
	}
}
