package tests.trust;

import org.junit.runner.RunWith;
import org.junit.runners.Suite;

import tests.trust.model.exceptions.*;
import tests.trust.model.math.*;
import tests.trust.model.primitives.*;
import tests.trust.model.sets.*;

@RunWith(Suite.class)
@Suite.SuiteClasses({
	BayesTrustTest.class,
	DuplicatePeerExceptionTest.class,
	LevelRangeExceptionTest.class,
	LevelRangeExceptionTest.class,
	PeerContextUnfoundExceptionTest.class,
	MiscTest.class,
	StatsTest.class,
	ContextTest.class,
	PeerTest.class,
	DirectExperienceSetTest.class,
	DirectTrustSetTest.class,
	RecommendedTrustSetTest.class,
	SentRecommendationSetTest.class
	})
public class AllTests {
}

