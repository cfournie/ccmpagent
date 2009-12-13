package agent.trust;

import agent.CCMPAgent;
import testbed.sim.Appraisal;
import testbed.sim.Era;
import testbed.sim.Opinion;

/**
 * Abstract trust shim.
 */
public abstract class TrustNetwork {
	
	protected CCMPAgent mAgent;
	
	public abstract void addAgent( String newAgent );
	
	/**
	 * Constructor
	 * @param agent Requires the agent to interrogate ART for problem parameters
	 */
	public TrustNetwork(CCMPAgent agent)
	{
		// Initialization
		mAgent = agent;
	}
	
	public abstract void frameReset();
	
	public abstract void setAgentEraCertainty( String agent, Era era, double certainty);	

	//handle a reputation response from an Agent about another Agent.
	public abstract void receiveAgentReputationUpdate( String fromAgent, String aboutAgent, Era era, double reputation );
	//return the weight (trust) for the sim to use in appraisal calculation
	public abstract double getReputationWeight( String agent, Era era );
	
	//Get our local trust value, for dtlibrary
	public abstract double getTrustValue( String agent, Era era);
	
	public abstract void updateAgentTrustFromFinalAppraisal(String agent, Appraisal appraisal, Opinion opinion);

	public abstract void agentDidNotAcceptReputationRequest( String agent, Era era );
	public abstract void agentDidNotProvideReputation( String agent, Era era );
	public abstract void agentDidNotProvideCertainty( String agent, Era era);
	public abstract void agentDidNotProvideOpinion( String agent, Era era);
	public abstract void agentDidNotAcceptCertainty( String agent, Era era, double certaintyValue );
}
