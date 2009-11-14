package learning;
import testbed.sim.Era;;
/**
 * 
 */

/**
 * @author cfournie
 *
 */
public interface LearningInterface {
	
	void  setAgentTrust( String agent, Era era );
	void  setAgentPerceivedTrust( String agent, Era era );
	void  setOurEraCertainty( Era era, double certainty );
	void  setAgentEraCertainty( String agent, Era era, double certainty);
	
	//get the reputation value, about an agent, we want to give to another agent.
	double getReputationRequestValue( String requestingAgent, String aboutAgent, Era era );
	//should we respond to an agents reputation request?
	boolean respondToReputationRequest( String requestingAgent, String aboutAgent, Era era );
	//should we actually provide an agents reputation request?
	boolean provideReputationRequest( String requestingAgent, String aboutAgent, Era era );
	
	//get the certainty value, based on the eraCertainty we want to give to a 
	//specific agent.
	double getCertaintyRequestValue( String agent, Era era );
	//should we respond to an agents certainty request?
	boolean respondToCertaintyRequest( String agent, Era era );
	//should we actually provide our certainty request to the agent?
	boolean provideCertaintyRequest( String agent, Era era );

	//Should we generate an opinion for an agent if we have this era certainty about a painting?
	boolean generateOpinion(String requestingAgent, Era era);
	//Should we actually provide the opinion?
	boolean provideOpinion(String requestionAgent, Era era );
	//How much time should we put into the opinion generation?
	double getAppraisalCost(String requestingAgent, Era era);
	
	//Should we request a reputation update from an agent, about another agent?
	boolean requestAgentReputationUpdate( String toAgent, String aboutAgent, Era era );
	//Should we request a certainty value from an agent?
	boolean requestAgentCertainty( String toAgent, Era era );
	//Should we request an opinion from an agent?
	boolean requestAgentOpinion( String toAgent, Era era );
}
