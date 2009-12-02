package tests.trust.model.primitives;


import org.junit.Test;
import static org.junit.Assert.*;

import trust.model.primitives.*;

public class ContextTest {
	
	@Test
	public void testEquality() {
		Context ck1 = new Context("renaissance");
		Context cl1 = new Context("renaissance");
		Context ck2 = new Context("picasso");

		// Self
		assertTrue(ck1.equals(ck1));
		// Equivalent
		assertTrue(ck1.equals(cl1));
		// Different
		assertFalse(ck1.equals(ck2));
	}
	
	@Test
	public void testGetName() {
		String name = "renaissance";
		Context ck = new Context("renaissance");

		// Self
		assertEquals(name, ck.getName());
	}
}
