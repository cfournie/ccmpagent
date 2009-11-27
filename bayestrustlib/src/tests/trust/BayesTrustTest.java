package tests.trust;

import trust.model.primitives.*;
import trust.*;
import java.util.*;
import org.junit.*;
import static org.junit.Assert.*;
import static junitx.framework.ArrayAssert.*;

public class BayesTrustTest {
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
		bt = new BayesTrust(4, Arrays.asList(CONTEXTS));
	}
	
	@Test
	public void testRecommendedTrustInit() {
		for (Peer p: PEERS) {
			assertEquals(new double[] {0.25, 0.25, 0.25, 0.25},
				bt.getRecommendedTrust(CONTEXTS[0], p),
				0.001);
		}
	}
}
