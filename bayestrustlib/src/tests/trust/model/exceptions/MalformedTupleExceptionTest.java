package tests.trust.model.exceptions;


import org.junit.Test;
import static org.junit.Assert.*;

import trust.model.exceptions.*;
import trust.model.math.Stats;

public class MalformedTupleExceptionTest {
	public static final int TRUST_LEVELS = 4;
	
	@Test
	public void testMessage() {
		Stats stats = new Stats(TRUST_LEVELS);
		
		MalformedTupleException exception = new MalformedTupleException(5, stats);
		String message = exception.getMessage();
		
		assertEquals("Received tuple length 5, expected 4", message);
	}
}
