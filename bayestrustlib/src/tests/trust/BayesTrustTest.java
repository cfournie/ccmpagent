package tests.trust;

import trust.model.BayesTrust;
import trust.model.math.*;
import trust.model.exceptions.*;
import trust.model.primitives.*;
import java.util.*;
import org.junit.*;
import static org.junit.Assert.*;
import static junitx.framework.ArrayAssert.*;

public class BayesTrustTest {
	public static final int TRUST_LEVELS = 4;
	
	public static final Context ROMAN = new Context("roman");
	public static final Context MIDDLE_AGES = new Context("middleAges");
	public static final Context RENAISSANCE = new Context("renaissance");
	public static final Context BAROQUE = new Context("baroque");
	public static final Context SURREALISM = new Context("surrealism");
	public static final Context[] CONTEXTS = {
		ROMAN, MIDDLE_AGES, RENAISSANCE, BAROQUE, SURREALISM
	};
	
	public static final Peer ALICE = new Peer("alice");
	public static final Peer BOB = new Peer("bob");
	public static final Peer CHARLIE = new Peer("charlie");
	public static final Peer DAVID = new Peer("david");
	public static final Peer EARL = new Peer("earl");
	public static final Peer[] PEERS = {ALICE, BOB, CHARLIE, DAVID, EARL};
	
	private BayesTrust bt;
	private Stats stats;
	
	@Before
	public void setUpBayesTrust() {
		bt = new BayesTrust(TRUST_LEVELS, Arrays.asList(CONTEXTS));
		stats = new Stats(TRUST_LEVELS);
		
		for (Peer p: PEERS) {
			bt.addPeer(p);
		}
	}
	
	@Test
	public void testRecommendedTrustInit() {
		for (Peer p: PEERS) {
			assertEquals(new double[] {0.25, 0.25, 0.25, 0.25},
				bt.getRecommendedTrust(ROMAN, p),
				0.001);
		}
	}

	@Test(expected=DuplicatePeerException.class)
	public void testDuplicatePeerAddition() {
		Peer px = new Peer("joe");
		Peer py = new Peer("joe");
		this.bt.addPeer(px);
		this.bt.addPeer(py);
	}
	
	@Test
	public void testConsistentRecommendation() {
		bt.storeRecommendation(ROMAN, ALICE, BOB, 0.7);
		bt.storeRecommendation(ROMAN, ALICE, BOB, 0.7);
		bt.storeRecommendation(ROMAN, ALICE, BOB, 0.7);
		double [] r = bt.getRecommendedTrust(ROMAN, BOB);
		stats.printPmf(r);
		assertEquals(2, stats.mean(r), 1);
	}
	
	@Test
	public void testVaryingRecommendation() {
		bt.storeRecommendation(ROMAN, ALICE, BOB, 0.1);
		bt.storeRecommendation(ROMAN, ALICE, BOB, 0.9);
		double [] r = bt.getRecommendedTrust(ROMAN, BOB);
		stats.printPmf(r);
		assertEquals(2, stats.mean(r), 1);
	}
	
	@Test
	public void testTwoRecommenders() {
		bt.storeRecommendation(ROMAN, ALICE, BOB, 0.0);
		bt.storeRecommendation(ROMAN, CHARLIE, BOB, 0.4);
		double [] r = bt.getRecommendedTrust(ROMAN, BOB);
		stats.printPmf(r);
		assertEquals(1, stats.mean(r), 1);
	}
	
	public static void main(String[] args) {
		new BayesTrustTest().main();
	}
	
	private void main() {
		setUpBayesTrust();
		
		bt.storeRecommendation(ROMAN, BOB, ALICE, 0.5);
		stats.printPmf(bt.getRecommendedTrust(ROMAN, ALICE));
		System.out.println(bt);
		
		bt.storeRecommendation(ROMAN, CHARLIE, ALICE, 0.5);
		stats.printPmf(bt.getRecommendedTrust(ROMAN, ALICE));
		
		bt.storeRecommendation(ROMAN, DAVID, ALICE, 0.5);
		stats.printPmf(bt.getRecommendedTrust(ROMAN, ALICE));

		bt.storeRecommendation(ROMAN, EARL, ALICE, 0.0);
		stats.printPmf(bt.getRecommendedTrust(ROMAN, ALICE));
		
		bt.storeRecommendation(ROMAN, EARL, ALICE, 1.0);
		stats.printPmf(bt.getRecommendedTrust(ROMAN, ALICE));

		/*double[][] m = /misc.makeIdentityMatrix()/misc.makeMatrix(0.1);
		for (int i = 0; i < stats.getN(); i++) {
			for (int j = 0; j < stats.getN(); j++) {
				m[i][j] += 0.1;
			}
		}
		System.out.println(bt.pSRgivenRT(m, 0, 0));*/
	}
}
