package tests.trust.model.primitives;


import org.junit.Test;
import static org.junit.Assert.*;

import trust.model.primitives.*;

public class PeerTest {
	
	@Test
	public void testEquality() {
		Peer py1 = new Peer("agentAlpha");
		Peer pz1 = new Peer("agentAlpha");
		Peer py2 = new Peer("agentBeta");

		// Self
		assertTrue(py1.equals(py1));
		// Equivalent
		assertTrue(py1.equals(pz1));
		// Different
		assertFalse(py1.equals(py2));
	}
	
	@Test
	public void testGetName() {
		String name = "agentAlpha";
		Peer py = new Peer("agentAlpha");

		// Self
		assertEquals(name, py.getName());
	}
}
