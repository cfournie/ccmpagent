package tests.trust.model.exceptions;


import org.junit.Test;
import static org.junit.Assert.*;

import trust.model.exceptions.*;
import trust.model.math.Stats;

public class LevelRangeExceptionTest {
	public static final int TRUST_LEVELS = 4;
	
	@Test
	public void testMessageDiscrete() {
		Stats stats = new Stats(TRUST_LEVELS);
		
		LevelRangeException exception = new LevelRangeException(4, stats);
		String message = exception.getMessage();
		
		assertEquals("Invalid discrete level 4 is outside of [0, 3]", message);
	}
	
	@Test
	public void testMessageContinuous() {
		LevelRangeException exception = new LevelRangeException(1.2);
		String message = exception.getMessage();
		
		assertEquals("Invalid continuous level 1.2 is outside of [0, 1]", message);
	}
}
