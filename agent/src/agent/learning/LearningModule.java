/**
 * 
 */
package agent.learning;

import testbed.sim.AppraisalAssignment;
import testbed.sim.Era;
import trust.BayesTrustInterface;

/**
 * @author cfournie
 *
 */
public class LearningModule implements LearningInterface {

	/* (non-Javadoc)
	 * @see agent.learning.LearningInterface#adjustAppraisalValue(java.lang.String, testbed.sim.Era, int)
	 */
	public int adjustAppraisalValue(String toAgent, Era era, int appraisal) {
		// TODO Auto-generated method stub
		return 0;
	}

	/* (non-Javadoc)
	 * @see agent.learning.LearningInterface#generateOpinion(java.lang.String, testbed.sim.Era)
	 */
	public boolean generateOpinion(String requestingAgent, Era era) {
		// TODO Auto-generated method stub
		return false;
	}

	/* (non-Javadoc)
	 * @see agent.learning.LearningInterface#getAppraisalCost(java.lang.String, testbed.sim.Era)
	 */
	public double getAppraisalCost(String requestingAgent, Era era) {
		// TODO Auto-generated method stub
		return 0;
	}

	/* (non-Javadoc)
	 * @see agent.learning.LearningInterface#getCertaintyRequestValue(java.lang.String, testbed.sim.Era)
	 */
	public double getCertaintyRequestValue(String agent, Era era) {
		// TODO Auto-generated method stub
		return 0;
	}

	/* (non-Javadoc)
	 * @see agent.learning.LearningInterface#getReputationRequestValue(java.lang.String, java.lang.String, testbed.sim.Era)
	 */
	public double getReputationRequestValue(String requestingAgent,
			String aboutAgent, Era era) {
		// TODO Auto-generated method stub
		return 0;
	}

	/* (non-Javadoc)
	 * @see agent.learning.LearningInterface#provideCertaintyRequest(java.lang.String, testbed.sim.Era)
	 */
	public boolean provideCertaintyRequest(String agent, Era era) {
		// TODO Auto-generated method stub
		return false;
	}

	/* (non-Javadoc)
	 * @see agent.learning.LearningInterface#provideOpinion(java.lang.String, testbed.sim.Era)
	 */
	public boolean provideOpinion(String requestionAgent, Era era) {
		// TODO Auto-generated method stub
		return false;
	}

	/* (non-Javadoc)
	 * @see agent.learning.LearningInterface#provideReputationRequest(java.lang.String, java.lang.String, testbed.sim.Era)
	 */
	public boolean provideReputationRequest(String requestingAgent,
			String aboutAgent, Era era) {
		// TODO Auto-generated method stub
		return false;
	}

	/* (non-Javadoc)
	 * @see agent.learning.LearningInterface#requestAgentCertainty(java.lang.String, testbed.sim.Era)
	 */
	public boolean requestAgentCertainty(String toAgent, Era era) {
		// TODO Auto-generated method stub
		return false;
	}

	/* (non-Javadoc)
	 * @see agent.learning.LearningInterface#requestAgentOpinion(java.lang.String, testbed.sim.AppraisalAssignment)
	 */
	public boolean requestAgentOpinion(String toAgent, AppraisalAssignment art) {
		// TODO Auto-generated method stub
		return false;
	}

	/* (non-Javadoc)
	 * @see agent.learning.LearningInterface#requestAgentReputationUpdate(java.lang.String, java.lang.String, testbed.sim.Era, int)
	 */
	public boolean requestAgentReputationUpdate(String toAgent,
			String aboutAgent, Era era, int currentTimestep) {
		// TODO Auto-generated method stub
		return false;
	}

	/* (non-Javadoc)
	 * @see agent.learning.LearningInterface#respondToCertaintyRequest(java.lang.String, testbed.sim.Era)
	 */
	public boolean respondToCertaintyRequest(String agent, Era era) {
		// TODO Auto-generated method stub
		return false;
	}

	/* (non-Javadoc)
	 * @see agent.learning.LearningInterface#respondToReputationRequest(java.lang.String, java.lang.String, testbed.sim.Era)
	 */
	public boolean respondToReputationRequest(String requestingAgent,
			String aboutAgent, Era era) {
		// TODO Auto-generated method stub
		return false;
	}

	/* (non-Javadoc)
	 * @see agent.learning.LearningInterface#sentCertaintyRequest(java.lang.String, testbed.sim.Era)
	 */
	public void sentCertaintyRequest(String toAgent, Era era) {
		// TODO Auto-generated method stub

	}

	/* (non-Javadoc)
	 * @see agent.learning.LearningInterface#sentOpinionRequest(java.lang.String, testbed.sim.AppraisalAssignment)
	 */
	public void sentOpinionRequest(String toAgent, AppraisalAssignment art) {
		// TODO Auto-generated method stub

	}

	/* (non-Javadoc)
	 * @see agent.learning.LearningInterface#sentReputationRequest(java.lang.String, java.lang.String)
	 */
	public void sentReputationRequest(String toAgent, String aboutAgent) {
		// TODO Auto-generated method stub

	}

	/* (non-Javadoc)
	 * @see agent.learning.LearningInterface#setAgentEraCertainty(java.lang.String, testbed.sim.Era, double)
	 */
	public void setAgentEraCertainty(String agent, Era era, double certainty) {
		// TODO Auto-generated method stub

	}

	/* (non-Javadoc)
	 * @see agent.learning.LearningInterface#setAgentPerceivedTrust(java.lang.String, testbed.sim.Era, double)
	 */
	public void setAgentPerceivedTrust(String agent, Era era, double trust) {
		// TODO Auto-generated method stub

	}

	/* (non-Javadoc)
	 * @see agent.learning.LearningInterface#setAgentTrust(java.lang.String, testbed.sim.Era, double)
	 */
	public void setAgentTrust(String agent, Era era, double trust) {
		// TODO Auto-generated method stub

	}

	/* (non-Javadoc)
	 * @see agent.learning.LearningInterface#setBankBalance(double)
	 */
	public void setBankBalance(double balance) {
		// TODO Auto-generated method stub

	}

	/* (non-Javadoc)
	 * @see agent.learning.LearningInterface#setOurEraCertainty(testbed.sim.Era, double)
	 */
	public void setOurEraCertainty(Era era, double certainty) {
		// TODO Auto-generated method stub

	}

}
