/**
 * 
 */
package trust;
import testbed.sim.Era;;

/**
 * @author cfournie
 *
 */
public interface TrustInterface {

	void addAgent( String newAgent );
	void removeAgent( String agent );
	
	void setOurEraCertainty(Era era);

	//handle a reputation response from an Agent about another Agent.
	void receiveAgentReputationUpdate( String fromAgent, String aboutAgent, Era era, double reputation );
	//return the weight (trust) for the sim to use in appraisal calculation
	double getReputationWeight( String agent, Era era );
	
	//Get our local trust value, for dtlibrary
	double getTrustValue( String agent);
	//how much do we think the other agent trusts us?
	double getPerceiveTrustValue( String agent );
	
	void updateAgentTrustValue( String agent, Era era, double paintingValue, double appraisalValue, double certainty);

	void agentDidNotAcceptReputationRequest( String agent, Era era );
	void agentDidNotProvideReputation( String agent, Era era );
	void agentDidNotProvideCertainty( String agent, Era era);
	void agentDidNotProvideOpinion( String agent, Era era);
}
