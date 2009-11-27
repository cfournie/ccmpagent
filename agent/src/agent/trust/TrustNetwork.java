/**
 * 
 */
package agent.trust;

import agent.CCMPAgent;
import testbed.sim.Appraisal;
import testbed.sim.Era;
import testbed.sim.AppraisalAssignment;
import testbed.sim.Opinion;
import trust.TrustInterface;


/**
 * @author cfournie
 *
 */
public abstract class TrustNetwork {
	
	protected CCMPAgent mAgent;
	protected TrustInterface trust;
	
	public abstract void addAgent( String newAgent );
	public abstract void removeAgent( String agent );
	
	public TrustNetwork(CCMPAgent agent)
	{
		// Initialization
		mAgent = agent;
	}
	
	public abstract void frameReset();
	public abstract void init();
		
	public abstract void setOurEraCertainty(Era era);
	public abstract void setAgentEraCertainty( String agent, Era era, double certainty);	

	//handle a reputation response from an Agent about another Agent.
	public abstract void receiveAgentReputationUpdate( String fromAgent, String aboutAgent, Era era, double reputation );
	//return the weight (trust) for the sim to use in appraisal calculation
	public abstract double getReputationWeight( String agent, Era era );
	
	//Get our local trust value, for dtlibrary
	public abstract double getTrustValue( String agent, Era era);
	
	//how much do we think the other agent trusts us?
	public abstract double getInferredTrustValue( String agent, Era era );
	
	public abstract void updateAgentTrustFromFinalAppraisal(String agent, Appraisal appraisal, Opinion opinion);

	public abstract void agentDidNotAcceptReputationRequest( String agent, Era era );
	public abstract void agentDidNotProvideReputation( String agent, Era era );
	public abstract void agentDidNotProvideCertainty( String agent, Era era);
	public abstract void agentDidNotProvideOpinion( String agent, Era era);
	public abstract void agentDidNotAcceptCertainty( String agent, Era era, double certaintyValue );
	
	public abstract void providedAcceptReputationRequest( String toAgent, String aboutAgent, Era era );
	public abstract void didNotProvideAcceptReputationRequest( String fromAgent, String aboutAgent, Era era);
	public abstract void providedReputationReply( String toAgent, String aboutAgent, Era era, double reputationValue );
	public abstract void didNotProvideReputationAfterPayment( String fromAgent, String aboutAgent, Era era );
	public abstract void providedCertaintyReply( String toAgent, Era era, double certaintyValue );
	public abstract void didNotAcceptCertaintyRequest( String fromAgent, Era era );
	public abstract void providedOpinion( String toAgent, AppraisalAssignment art, int appraisedValue );
	public abstract void generatedOpinion( String toAgent, AppraisalAssignment art, double hoursSpent );
	public abstract void didNotProvideOpinionAfterPayment( String fromAgent, Era era );
}
