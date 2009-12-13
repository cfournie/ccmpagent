package tests;

import org.junit.runner.RunWith;
import org.junit.runners.Suite;

import tests.agent.trust.*;

@RunWith(Suite.class)
@Suite.SuiteClasses({
	BayesTrustNetworkTest.class
	})
public class AllTests {
}

