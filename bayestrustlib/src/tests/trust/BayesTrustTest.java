package tests.trust;

import trust.model.BayesTrust;
import trust.model.math.*;
import trust.model.exceptions.*;
import trust.model.primitives.*;
import java.util.*;
import org.junit.*;
import static junitx.framework.ArrayAssert.*;

public class BayesTrustTest {
	public static final int TRUST_LEVELS = 4;
	
	public static final Context[] CONTEXTS = {
		new Context("roman"),
		new Context("middleAges"),
		new Context("renaissance"),
		new Context("baroque"),
		new Context("surrealism")
	};
	
	public static final Peer[] PEERS = {
		new Peer("alice"),
		new Peer("bob"),
		new Peer("charlie")
	};
	
	private BayesTrust bt;
	private Stats stats;
	private Misc misc;
	
	@Before
	public void setUpBayesTrust() {
		bt = new BayesTrust(TRUST_LEVELS, Arrays.asList(CONTEXTS));
		stats = new Stats(TRUST_LEVELS);
		misc = new Misc(stats);
		
		for (Peer p: PEERS) {
			bt.addPeer(p);
		}
	}
	
	@Test
	public void testRecommendedTrustInit() {
		for (Peer p: PEERS) {
			assertEquals(new double[] {0.25, 0.25, 0.25, 0.25},
				bt.getRecommendedTrust(CONTEXTS[0], p),
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
	
	public static void main(String[] args) {
		new BayesTrustTest().main();
	}
	
	private void main() {
		final Context ctx = CONTEXTS[0];
		final Peer alice = PEERS[0];
		final Peer bob = PEERS[1];
		setUpBayesTrust();
		
		bt.storeRecommendation(ctx, alice, bob, 0.7);
		stats.printPmf(bt.getRecommendedTrust(ctx, bob));
		
		bt.storeRecommendation(ctx, alice, bob, 0.7);
		stats.printPmf(bt.getRecommendedTrust(ctx, bob));
		bt.storeRecommendation(ctx, alice, bob, 0.7);
		stats.printPmf(bt.getRecommendedTrust(ctx, bob));
		bt.storeRecommendation(ctx, alice, bob, 0.3);
		stats.printPmf(bt.getRecommendedTrust(ctx, bob));

		/*double[][] m = /misc.makeIdentityMatrix()/misc.makeMatrix(0.1);
		for (int i = 0; i < stats.getN(); i++) {
			for (int j = 0; j < stats.getN(); j++) {
				m[i][j] += 0.1;
			}
		}
		System.out.println(bt.pSRgivenRT(m, 0, 0));*/
	}
}
