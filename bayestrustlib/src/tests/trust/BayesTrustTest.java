package tests.trust;

import trust.model.BayesTrust;
import trust.model.exceptions.DuplicatePeerException;
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
	
	@Before
	public void setUpBayesTrust() {
		bt = new BayesTrust(TRUST_LEVELS, Arrays.asList(CONTEXTS));
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
}
