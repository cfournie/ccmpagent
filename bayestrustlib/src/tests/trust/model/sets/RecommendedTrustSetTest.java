package tests.trust.model.sets;

import org.junit.Test;
import static org.junit.Assert.*;

import trust.model.math.Stats;
import trust.model.primitives.*;
import trust.model.exceptions.*;
import trust.model.sets.*;

public class RecommendedTrustSetTest {
	public static final int TRUST_LEVELS = 4;
	
	@Test
	public void testStoreRetrieve() {
		Stats stats = new Stats(TRUST_LEVELS);
		RecommendedTrustSet set = new RecommendedTrustSet(stats);
		double[] trust = new double[]{0.1, 0.2, 0.3, 0.4};
		set.store(new Context("renaissance"), new Peer("picasso"), trust);
		
		assertEquals(trust, set.retrieve(new Context("renaissance"), new Peer("picasso")));
	}
	
	@Test(expected=MalformedTupleException.class)
	public void testStoreValidatesLength() throws Exception {
		Stats stats = new Stats(TRUST_LEVELS);
		RecommendedTrustSet set = new RecommendedTrustSet(stats);
		double[] trust = new double[TRUST_LEVELS + 1];
		set.store(new Context("renaissance"), new Peer("picasso"), trust);
	}
}
