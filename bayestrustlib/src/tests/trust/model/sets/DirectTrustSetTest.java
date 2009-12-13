package tests.trust.model.sets;

import org.junit.Test;
import static org.junit.Assert.*;

import trust.model.math.Stats;
import trust.model.primitives.*;
import trust.model.exceptions.*;
import trust.model.sets.*;

public class DirectTrustSetTest {
	public static final int TRUST_LEVELS = 4;
	
	@Test
	public void testStoreRetrieve() {
		Stats stats = new Stats(TRUST_LEVELS);
		DirectTrustSet set = new DirectTrustSet(stats);
	
		double[] trust = new double[]{0.1, 0.2, 0.3, 0.4};
		set.store(new Context("renaissance"), new Peer("picasso"), trust);
		assertEquals(trust, set.retrieve(new Context("renaissance"), new Peer("picasso")));
		
		double[] trust2 = new double[]{0.2, 0.2, 0.2, 0.4};
		set.store(new Context("renaissance"), new Peer("picasso"), trust2);
		assertEquals(trust2, set.retrieve(new Context("renaissance"), new Peer("picasso")));
	}
	
	@Test(expected=MalformedTupleException.class)
	public void testStoreValidatesLength() throws Exception {
		Stats stats = new Stats(TRUST_LEVELS);
		RecommendedTrustSet set = new RecommendedTrustSet(stats);
		
		double[] trust = new double[TRUST_LEVELS + 1];
		
		set.store(new Context("renaissance"), new Peer("picasso"), trust);
	}
	
	@Test(expected=PeerContextUnfoundException.class)
	public void testRetrieveFail() throws Exception {
		Context ck = new Context("modern");
		Peer py = new Peer("newAgent");
		Stats stats = new Stats(TRUST_LEVELS);
		DirectTrustSet set = new DirectTrustSet(stats);
		set.retrieve(ck, py);
	}
}
