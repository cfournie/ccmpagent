package tests.trust.model.exceptions;


import org.junit.Test;
import static org.junit.Assert.*;

import trust.model.exceptions.*;
import trust.model.primitives.*;

public class DuplicatePeerExceptionTest {
	
	@Test
	public void testMessage() {
		Peer py = new Peer("agentAlpha");
		
		DuplicatePeerException exception = new DuplicatePeerException(py);
		String message = exception.getMessage();
		
		assertEquals("Duplicate Peer 'agentAlpha' encountered", message);
	}
}
