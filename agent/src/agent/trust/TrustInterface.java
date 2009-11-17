/**
 * 
 */
package agent.trust;
import testbed.sim.Era;
import testbed.sim.AppraisalAssignment;


/**
 * @author cfournie
 *
 */
public interface TrustInterface {

	void addAgent( String newAgent );
	void removeAgent( String agent );
	
	void setOurEraCertainty(Era era);
	void setAgentEraCertainty( String agent, Era era, double certainty);	

	//handle a reputation response from an Agent about another Agent.
	void receiveAgentReputationUpdate( String fromAgent, String aboutAgent, Era era, double reputation );
	//return the weight (trust) for the sim to use in appraisal calculation
	double getReputationWeight( String agent, Era era );
	
	//Get our local trust value, for dtlibrary
	double getTrustValue( String agent, Era era);
	//how much do we think the other agent trusts us?
	double getInferredTrustValue( String agent, Era era );
	
	void updateAgentTrustValue( String agent, AppraisalAssignment art, int paintingValue, int appraisalValue, double certainty);

	void agentDidNotAcceptReputationRequest( String agent, Era era );
	void agentDidNotProvideReputation( String agent, Era era );
	void agentDidNotProvideCertainty( String agent, Era era);
	void agentDidNotProvideOpinion( String agent, Era era);
	void agentDidNotAcceptCertainty( String agent, Era era, double certaintyValue );
	
	void providedAcceptReputationRequest( String toAgent, String aboutAgent, Era era );
	void didNotProvideAcceptReputationRequest( String fromAgent, String aboutAgent, Era era);
	void providedReputationReply( String toAgent, String aboutAgent, Era era, double reputationValue );
	void didNotProvideReputationAfterPayment( String fromAgent, String aboutAgent, Era era );
	void providedCertaintyReply( String toAgent, Era era, double certaintyValue );
	void didNotAcceptCertaintyRequest( String fromAgent, Era era );
	void providedOpinion( String toAgent, AppraisalAssignment art, int appraisedValue );
	void generatedOpinion( String toAgent, AppraisalAssignment art, double hoursSpent );
	void didNotProvideOpinionAfterPayment( String fromAgent, Era era );
}
