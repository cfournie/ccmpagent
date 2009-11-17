/**
 * 
 */
package agent.trust;

import testbed.sim.AppraisalAssignment;
import testbed.sim.Era;
import learning.DTLearningInterface;

/**
 * @author cfournie
 *
 */
public class TrustModule implements TrustInterface {

	/* (non-Javadoc)
	 * @see agent.trust.TrustInterface#addAgent(java.lang.String)
	 */
	public void addAgent(String newAgent) {
		// TODO Auto-generated method stub

	}

	/* (non-Javadoc)
	 * @see agent.trust.TrustInterface#agentDidNotAcceptCertainty(java.lang.String, testbed.sim.Era, double)
	 */
	public void agentDidNotAcceptCertainty(String agent, Era era,
			double certaintyValue) {
		// TODO Auto-generated method stub

	}

	/* (non-Javadoc)
	 * @see agent.trust.TrustInterface#agentDidNotAcceptReputationRequest(java.lang.String, testbed.sim.Era)
	 */
	public void agentDidNotAcceptReputationRequest(String agent, Era era) {
		// TODO Auto-generated method stub

	}

	/* (non-Javadoc)
	 * @see agent.trust.TrustInterface#agentDidNotProvideCertainty(java.lang.String, testbed.sim.Era)
	 */
	public void agentDidNotProvideCertainty(String agent, Era era) {
		// TODO Auto-generated method stub

	}

	/* (non-Javadoc)
	 * @see agent.trust.TrustInterface#agentDidNotProvideOpinion(java.lang.String, testbed.sim.Era)
	 */
	public void agentDidNotProvideOpinion(String agent, Era era) {
		// TODO Auto-generated method stub

	}

	/* (non-Javadoc)
	 * @see agent.trust.TrustInterface#agentDidNotProvideReputation(java.lang.String, testbed.sim.Era)
	 */
	public void agentDidNotProvideReputation(String agent, Era era) {
		// TODO Auto-generated method stub

	}

	/* (non-Javadoc)
	 * @see agent.trust.TrustInterface#didNotAcceptCertaintyRequest(java.lang.String, testbed.sim.Era)
	 */
	public void didNotAcceptCertaintyRequest(String fromAgent, Era era) {
		// TODO Auto-generated method stub

	}

	/* (non-Javadoc)
	 * @see agent.trust.TrustInterface#didNotProvideAcceptReputationRequest(java.lang.String, java.lang.String, testbed.sim.Era)
	 */
	public void didNotProvideAcceptReputationRequest(String fromAgent,
			String aboutAgent, Era era) {
		// TODO Auto-generated method stub

	}

	/* (non-Javadoc)
	 * @see agent.trust.TrustInterface#didNotProvideOpinionAfterPayment(java.lang.String, testbed.sim.Era)
	 */
	public void didNotProvideOpinionAfterPayment(String fromAgent, Era era) {
		// TODO Auto-generated method stub

	}

	/* (non-Javadoc)
	 * @see agent.trust.TrustInterface#didNotProvideReputationAfterPayment(java.lang.String, java.lang.String, testbed.sim.Era)
	 */
	public void didNotProvideReputationAfterPayment(String fromAgent,
			String aboutAgent, Era era) {
		// TODO Auto-generated method stub

	}

	/* (non-Javadoc)
	 * @see agent.trust.TrustInterface#generatedOpinion(java.lang.String, testbed.sim.AppraisalAssignment, double)
	 */
	public void generatedOpinion(String toAgent, AppraisalAssignment art,
			double hoursSpent) {
		// TODO Auto-generated method stub

	}

	/* (non-Javadoc)
	 * @see agent.trust.TrustInterface#getInferredTrustValue(java.lang.String, testbed.sim.Era)
	 */
	public double getInferredTrustValue(String agent, Era era) {
		// TODO Auto-generated method stub
		return 0;
	}

	/* (non-Javadoc)
	 * @see agent.trust.TrustInterface#getReputationWeight(java.lang.String, testbed.sim.Era)
	 */
	public double getReputationWeight(String agent, Era era) {
		// TODO Auto-generated method stub
		return 0;
	}

	/* (non-Javadoc)
	 * @see agent.trust.TrustInterface#getTrustValue(java.lang.String, testbed.sim.Era)
	 */
	public double getTrustValue(String agent, Era era) {
		// TODO Auto-generated method stub
		return 0;
	}

	/* (non-Javadoc)
	 * @see agent.trust.TrustInterface#providedAcceptReputationRequest(java.lang.String, java.lang.String, testbed.sim.Era)
	 */
	public void providedAcceptReputationRequest(String toAgent,
			String aboutAgent, Era era) {
		// TODO Auto-generated method stub

	}

	/* (non-Javadoc)
	 * @see agent.trust.TrustInterface#providedCertaintyReply(java.lang.String, testbed.sim.Era, double)
	 */
	public void providedCertaintyReply(String toAgent, Era era,
			double certaintyValue) {
		// TODO Auto-generated method stub

	}

	/* (non-Javadoc)
	 * @see agent.trust.TrustInterface#providedOpinion(java.lang.String, testbed.sim.AppraisalAssignment, int)
	 */
	public void providedOpinion(String toAgent, AppraisalAssignment art,
			int appraisedValue) {
		// TODO Auto-generated method stub

	}

	/* (non-Javadoc)
	 * @see agent.trust.TrustInterface#providedReputationReply(java.lang.String, java.lang.String, testbed.sim.Era, double)
	 */
	public void providedReputationReply(String toAgent, String aboutAgent,
			Era era, double reputationValue) {
		// TODO Auto-generated method stub

	}

	/* (non-Javadoc)
	 * @see agent.trust.TrustInterface#receiveAgentReputationUpdate(java.lang.String, java.lang.String, testbed.sim.Era, double)
	 */
	public void receiveAgentReputationUpdate(String fromAgent,
			String aboutAgent, Era era, double reputation) {
		// TODO Auto-generated method stub

	}

	/* (non-Javadoc)
	 * @see agent.trust.TrustInterface#removeAgent(java.lang.String)
	 */
	public void removeAgent(String agent) {
		// TODO Auto-generated method stub

	}

	/* (non-Javadoc)
	 * @see agent.trust.TrustInterface#setAgentEraCertainty(java.lang.String, testbed.sim.Era, double)
	 */
	public void setAgentEraCertainty(String agent, Era era, double certainty) {
		// TODO Auto-generated method stub

	}

	/* (non-Javadoc)
	 * @see agent.trust.TrustInterface#setOurEraCertainty(testbed.sim.Era)
	 */
	public void setOurEraCertainty(Era era) {
		// TODO Auto-generated method stub

	}

	/* (non-Javadoc)
	 * @see agent.trust.TrustInterface#updateAgentTrustValue(java.lang.String, testbed.sim.AppraisalAssignment, int, int, double)
	 */
	public void updateAgentTrustValue(String agent, AppraisalAssignment art,
			int paintingValue, int appraisalValue, double certainty) {
		// TODO Auto-generated method stub

	}

}
