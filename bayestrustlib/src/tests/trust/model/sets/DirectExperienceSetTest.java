package tests.trust.model.sets;

import org.junit.*;
import static org.junit.Assert.*;

import trust.model.math.*;
import trust.model.primitives.*;
import trust.model.exceptions.*;
import trust.model.sets.*;

public class DirectExperienceSetTest {
	public static final int TRUST_LEVELS = 4;

	private DirectExperienceSet set;
	private Stats stats;
	private Misc misc;
	
	@Before
	public void setUpDirectExperienceSet() {
		stats = new Stats(TRUST_LEVELS);
		misc = new Misc(stats);
		set = new DirectExperienceSet(stats);
	}
	
	@Test
	public void testStoreRetrieve() {
		double[][] storedEc = misc.makeMatrix(0.123);
		set.store(new Context("renaissance"), new Peer("picasso"), storedEc);
		double[][] ec = set.retrieve(new Context("renaissance"), new Peer("picasso"));
		assertTrue(storedEc.equals(ec));
		
		storedEc = misc.makeMatrix(0.789);
		set.store(new Context("roman"), new Peer("dali"), storedEc);
		ec = set.retrieve(new Context("roman"), new Peer("dali"));
		assertTrue(storedEc.equals(ec));
	}
	
	@Test(expected=MalformedTupleException.class)
	public void testStoreValidatesLength() throws Exception {
		Misc misc2 = new Misc(new Stats(TRUST_LEVELS + 1));
		double[][] trust = misc2.makeMatrix(0.567);
		
		set.store(new Context("renaissance"), new Peer("picasso"), trust);
	}

	@Test(expected=PeerContextUnfoundException.class)
	public void testRetrieveFail() throws Exception {
		Context ck = new Context("modern");
		Peer py = new Peer("newAgent");
		set.retrieve(ck, py);
	}
}
