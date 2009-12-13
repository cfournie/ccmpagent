package tests.agent.trust;

import java.util.LinkedList;
import java.util.List;

import org.junit.*;

import testbed.sim.*;

import agent.*;
import agent.trust.BayesTrustNetwork;

/**
 * Crash tests for BayesTrustNetwork.
 * @author cfournie
 */
public class BayesTrustNetworkTest {

	private BayesTrustNetwork trustNetwork;
	
	public static final double certainty = 0.5;
	public static final double hoursSpent = 200;
	public static final double reputation = 1.0;
	public static final int appraised = 100;
	
	public static final String agent = "agent";
	public static final String fromAgent = "fromAgent";
	public static final String aboutAgent = "aboutAgent";
	public static final String toAgent = "toAgent";
	public static final String unrelatedAgent = "unrelatedAgent";
	
	public static final String painting = "Magnificat";
	public static final Era era = new Era("renaissance");
	AppraisalAssignment appraisalAssignment = new AppraisalAssignment(agent, era, agent);
	
	
	@Before
	public void setup() {
		CCMPAgent ccmpAgent = new BayesWekaCCMPAgent();
		ccmpAgent.setName("agentAlpha");
		
		List<Era> eras = new LinkedList<Era>();
		eras.add(era);
		ccmpAgent.receiveEras(eras);
		
		this.trustNetwork = new BayesTrustNetwork(ccmpAgent);

		this.trustNetwork.addAgent(agent);
		this.trustNetwork.addAgent(fromAgent);
		this.trustNetwork.addAgent(aboutAgent);
		this.trustNetwork.addAgent(toAgent);
	}

	@Test
	public void testCrashaddAgent() {
		this.trustNetwork.addAgent(unrelatedAgent);
	}
	
	@Test
	public void testCrashAgentDidNotAcceptCertainty() {
		this.trustNetwork.agentDidNotAcceptCertainty(agent, era, certainty);
	}

	@Test
	public void testCrashAgentDidNotAcceptReputationRequest() {
		this.trustNetwork.agentDidNotAcceptReputationRequest(agent, era);
	}
	
	@Test
	public void testCrashAgentDidNotProvideCertainty() {
		this.trustNetwork.agentDidNotProvideCertainty(agent, era);
	}
	
	@Test
	public void testCrashAgentDidNotProvideOpinion() {
		this.trustNetwork.agentDidNotProvideOpinion(agent, era);
	}
	
	@Test
	public void testCrashAgentDidNotProvideReputation() {
		this.trustNetwork.agentDidNotProvideReputation(agent, era);
	}
	
	@Test
	public void testCrashGetReputationWeight() {
		this.trustNetwork.getReputationWeight(agent, era);
	}
	
	@Test
	public void testCrashGetTrustValue() {
		this.trustNetwork.getTrustValue(agent, era);
	}
	
	@Test
	public void testCrashReceiveAgentReputationUpdate() {
		this.trustNetwork.receiveAgentReputationUpdate(fromAgent, aboutAgent, era, reputation);
	}
	
	@Test
	public void testCrashSetAgentEraCertainty() {
		this.trustNetwork.setAgentEraCertainty(agent, era, certainty);
	}
	
	@Test
	public void testCrashUpdateAgentTrustFromFinalAppraisal() {
		Appraisal a = new Appraisal(fromAgent, painting, era, appraised);
		Opinion o = new Opinion(fromAgent, painting, appraised + 100, agent, "transactionID");
		this.trustNetwork.updateAgentTrustFromFinalAppraisal(agent, a, o);
	}
	
	@Test
	public void testCrashFrameReset() {			
		this.trustNetwork.frameReset();
	}
	
	@Test
	public void testCallSequence() {
		this.trustNetwork.getTrustValue(aboutAgent, era);
		this.trustNetwork.getTrustValue(toAgent, era);
		this.trustNetwork.receiveAgentReputationUpdate(fromAgent, aboutAgent, era, reputation);
		this.trustNetwork.receiveAgentReputationUpdate(aboutAgent, fromAgent, era, reputation);		
		this.trustNetwork.getTrustValue(aboutAgent, era);
		this.trustNetwork.getTrustValue(toAgent, era);				
		this.trustNetwork.frameReset();
	}
}
