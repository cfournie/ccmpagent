package agent.decision;
import agent.CCMPAgent;
import testbed.sim.Era;
import testbed.sim.AppraisalAssignment;

/**
 * 
 */

/**
 * @author cfournie
 *
 */
public abstract class DecisionTree {
	
	protected CCMPAgent mAgent;
	
	public void setAgent( CCMPAgent agent )
	{
		mAgent = agent; 
	}	

	public abstract void addAgent( String newAgent );
	public abstract void removeAgent( String agent );
	
	public abstract void init();
	public abstract void frameReset();	
	
	public abstract void  setAgentTrust( String agent, Era era, double trust );
	public abstract void  setAgentPerceivedTrust( String agent, Era era, double trust );
	public abstract void  setOurEraCertainty( Era era, double certainty );
	public abstract void  setAgentEraCertainty( String agent, Era era, double certainty);
	public abstract void  setBankBalance( double balance );
	
	//get the reputation value, about an agent, we want to give to another agent.
	public abstract double getReputationRequestValue( String requestingAgent, String aboutAgent, Era era );
	//should we respond to an agents reputation request?
	public abstract boolean respondToReputationRequest( String requestingAgent, String aboutAgent, Era era );
	//should we actually provide an agents reputation request?
	public abstract boolean provideReputationRequest( String requestingAgent, String aboutAgent, Era era );
	
	//get the certainty value, based on the eraCertainty we want to give to a 
	//specific agent.
	public abstract double getCertaintyRequestValue( String agent, Era era );
	//should we respond to an agents certainty request?
	public abstract boolean respondToCertaintyRequest( String agent, Era era );
	//should we actually provide our certainty request to the agent?
	public abstract boolean provideCertaintyRequest( String agent, Era era );

	//Should we generate an opinion for an agent if we have this era certainty about a painting?
	public abstract boolean generateOpinion(String requestingAgent, Era era);
	//Should we actually provide the opinion?
	public abstract boolean provideOpinion(String requestionAgent, Era era );
	//How much time should we put into the opinion generation?
	public abstract double getAppraisalCost(String requestingAgent, Era era);
	
	//Should we request a reputation update from an agent, about another agent?
	public abstract boolean requestAgentReputationUpdate( String toAgent, String aboutAgent, Era era, int currentTimestep );
	//Should we request a certainty value from an agent?
	public abstract boolean requestAgentCertainty( String toAgent, Era era );
	//Should we request an opinion from an agent?
	public abstract boolean requestAgentOpinion( String toAgent, AppraisalAssignment art );
	
	//Do we want to adjust an appraisal value (given by the sim) to a different value
	//before we send it out?
	public abstract int adjustAppraisalValue( String toAgent, Era era, int appraisal);
	
	public abstract void sentCertaintyRequest( String toAgent, Era era );
	public abstract void sentOpinionRequest( String toAgent, AppraisalAssignment art );
	public abstract void sentReputationRequest( String toAgent, String aboutAgent );

}
