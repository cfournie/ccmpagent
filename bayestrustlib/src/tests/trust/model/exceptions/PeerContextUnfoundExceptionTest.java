package tests.trust.model.exceptions;


import org.junit.Test;
import static org.junit.Assert.*;

import trust.model.exceptions.*;
import trust.model.primitives.*;

public class PeerContextUnfoundExceptionTest {
	
	@Test
	public void testMessage() {
		Peer py = new Peer("agent");
		Context ck = new Context("renaisance");
		
		PeerContextUnfoundException exception = new PeerContextUnfoundException(ck, py);
		String message = exception.getMessage();
		
		assertEquals("Unknown context 'renaisance' or peer 'agent' encountered", message);
	}
}
