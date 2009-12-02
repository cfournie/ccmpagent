package tests.trust.model.math;

import static org.junit.Assert.*;

import org.junit.Before;
import org.junit.Test;

import trust.model.math.*;
import trust.model.exceptions.*;

public class MiscTest {
	public static final int TRUST_LEVELS = 4;

	private Misc misc;
	
	@Before
	public void setUp() throws Exception {
		misc = new Misc(new Stats(4));
	}

	@Test
	public void testDiscretize() {
		assertEquals(0, misc.discretize(0.0));
		assertEquals(1, misc.discretize(0.3));
		assertEquals(3, misc.discretize(0.9));
		assertEquals(3, misc.discretize(0.999));
		assertEquals(3, misc.discretize(1.0));
	}
	
	@Test
	public void testContinuate() {
		double value = 0.0;
		
		value = misc.continuate(0.0);
		assertEquals(0, value, 0.0);
		value = misc.continuate(1.0);
		assertEquals(0.333, value, 0.001);
		value = misc.continuate(2.0);
		assertEquals(0.666, value, 0.001);
		value = misc.continuate(3.0);
		assertEquals(1.0, value, 0.001);
	}
	
	@Test(expected=LevelRangeException.class)
	public void testDiscretizeNegative() {
		misc.discretize(-0.3);
	}

	@Test(expected=LevelRangeException.class)
	public void testDiscretizeGreaterThanOne() {
		misc.discretize(1.2);
	}
	
	@Test
	public void testMakeTupleDoesntCrash() {
		misc.makeTuple();
	}
	
	@Test
	public void testMakeTupleLDoesntCrash() {
		misc.makeTuple(3.14);
	}
	
	@Test
	public void testExpectedTupleLength() {
		assertEquals(TRUST_LEVELS, this.misc.getExpectedTupleLength());
	}
	
	@Test
	public void testMaxDiscreteLevel() {
		assertEquals(TRUST_LEVELS - 1, this.misc.getMaxDiscreteLevel());
	}
	
	@Test(expected=LevelRangeException.class)
	public void testCheckLevelIntOver() {
		this.misc.checkLevel(TRUST_LEVELS);
	}
	
	@Test(expected=LevelRangeException.class)
	public void testCheckLevelIntNegative() {
		this.misc.checkLevel(TRUST_LEVELS * -1);
	}
	
	@Test(expected=LevelRangeException.class)
	public void testCheckLevelDoubleOver() {
		this.misc.checkLevel(1.2);
	}
	
	@Test(expected=LevelRangeException.class)
	public void testCheckLevelDoubleNegative() {
		this.misc.checkLevel(-1.2);
	}
	
	@Test(expected=LevelRangeException.class)
	public void testCheckRangedLevelOver() {
		this.misc.checkLevel((double)TRUST_LEVELS);
	}

	@Test(expected=LevelRangeException.class)
	public void testCheckRangedLevelNegative() {
		this.misc.checkLevel(-1);
	}
	
	@Test(expected=MalformedTupleException.class)
	public void testCheckTuple() {
		double[] d = new double[TRUST_LEVELS + 1];
		this.misc.checkTuple(d);
	}
	
	@Test(expected=MalformedTupleException.class)
	public void testcheckMatrixHeight() {
		double[][] matrix = new double[TRUST_LEVELS + 1][TRUST_LEVELS];
		this.misc.checkMatrix(matrix);
	}
	
	@Test(expected=MalformedTupleException.class)
	public void testcheckMatrixWidth() {
		double[][] matrix = new double[TRUST_LEVELS][TRUST_LEVELS + 1];
		this.misc.checkMatrix(matrix);
	}
}
