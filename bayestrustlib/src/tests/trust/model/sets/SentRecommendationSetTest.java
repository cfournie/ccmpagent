package tests.trust.model.sets;

import org.junit.Test;
import static org.junit.Assert.*;

import trust.model.math.Stats;
import trust.model.primitives.*;
import trust.model.exceptions.*;
import trust.model.sets.*;

public class SentRecommendationSetTest {
	public static final int TRUST_LEVELS = 4;
	
	@Test
	public void testStoreRetrieve() {
		Stats stats = new Stats(TRUST_LEVELS);
		SentRecommendationSet set = new SentRecommendationSet(stats);
		double[][] trust = new double[][]{{0.1, 0.2, 0.3, 0.4},{0.1, 0.2, 0.3, 0.4},{0.1, 0.2, 0.3, 0.4},{0.1, 0.2, 0.3, 0.4}};
		set.store(new Context("renaissance"), new Peer("picasso"), trust);
		
		double[][] trust2 = set.retrieve(new Context("renaissance"), new Peer("picasso"));
		
		assertEquals(trust[0], trust2[0]);
		assertEquals(trust[1], trust2[1]);
		assertEquals(trust[2], trust2[2]);
		assertEquals(trust[3], trust2[3]);
	}
	
	@Test(expected=MalformedTupleException.class)
	public void testStoreValidatesLength() throws Exception {
		Stats stats = new Stats(TRUST_LEVELS);
		SentRecommendationSet set = new SentRecommendationSet(stats);
		double[][] trust = new double[TRUST_LEVELS + 1][TRUST_LEVELS];
		set.store(new Context("renaissance"), new Peer("picasso"), trust);
	}
	
	@Test(expected=PeerContextUnfoundException.class)
	public void testRetrieveFail() throws Exception {
		Context ck = new Context("modern");
		Peer py = new Peer("newAgent");
		Stats stats = new Stats(TRUST_LEVELS);
		SentRecommendationSet set = new SentRecommendationSet(stats);
		set.retrieve(ck, py);
	}
}
