package tests.trust.model.sets;

import org.junit.Test;
import static org.junit.Assert.*;

import trust.model.primitives.*;
import trust.model.exceptions.*;
import trust.model.sets.*;

public class RecommendedTrustSetTest {
	public static final int TRUST_LEVELS = 4;
	
	@Test
	public void testStoreRetrieve() {
		RecommendedTrustSet set = new RecommendedTrustSet(TRUST_LEVELS);
		double[] trust = new double[]{0.1, 0.2, 0.3, 0.4};
		set.store(new Context("renaissance"), new Peer("picasso"), trust);
		
		assertEquals(trust, set.retrieve(new Context("renaissance"), new Peer("picasso")));
	}
	
	@Test(expected=MalformedTupleException.class)
	public void testStoreValidatesLength() throws Exception {
		RecommendedTrustSet set = new RecommendedTrustSet(TRUST_LEVELS);
		double[] trust = new double[TRUST_LEVELS + 1];
		set.store(new Context("renaissance"), new Peer("picasso"), trust);
	}
}
