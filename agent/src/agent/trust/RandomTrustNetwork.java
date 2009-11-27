/**
 * 
 */
package agent.trust;

import testbed.sim.AppraisalAssignment;
import testbed.sim.Era;
import agent.CCMPAgent;
import agent.trust.TrustNetwork;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import testbed.sim.Appraisal;
import testbed.sim.Opinion;
import trust.RandomTrust;
import trust.TrustInterface;
import trust.model.primitives.Context;
import trust.model.primitives.Peer;

import java.util.Hashtable;

/**
 * @author cfournie
 *
 */
public class RandomTrustNetwork extends TrustNetwork {

	public static final int TRUST_LEVELS = 4;
	
	public RandomTrustNetwork(CCMPAgent agent)
	{
		super(agent);
		List<Context> contexts = new LinkedList<Context>();
		
		for(Era era : mAgent.getEras()) {
			Context c = new Context(era.getName());
			contexts.add(c);
		}
		
		this.trust = new RandomTrust(TRUST_LEVELS, contexts);
	}
	
	/* (non-Javadoc)
	 * @see agent.trust.TrustInterface#addAgent(java.lang.String)
	 */
	public void init()
	{
	}	
	
	/* (non-Javadoc)
	 * @see agent.trust.TrustInterface#addAgent(java.lang.String)
	 */
	public void addAgent(String newAgent)
	{
		Peer p = new Peer(newAgent);
		trust.addPeer(p);
	}

	/* (non-Javadoc)
	 * @see agent.trust.TrustInterface#agentDidNotAcceptCertainty(java.lang.String, testbed.sim.Era)
	 */
	public void agentDidNotAcceptCertainty(String agent, Era era,
			double certaintyValue)
	{
		// Don't care about this in RandomTrust
	}

	/* (non-Javadoc)
	 * @see agent.trust.TrustInterface#agentDidNotAcceptReputationRequest(java.lang.String, testbed.sim.Era)
	 */
	public void agentDidNotAcceptReputationRequest(String agent, Era era)
	{
		// Simple Trust doesn't pay attention
	}

	/* (non-Javadoc)
	 * @see agent.trust.TrustInterface#agentDidNotProvideCertainty(java.lang.String, testbed.sim.Era)
	 */
	public void agentDidNotProvideCertainty(String agent, Era era)
	{
		// Simple Trust doesn't pay attention
	}

	/* (non-Javadoc)
	 * @see agent.trust.TrustInterface#agentDidNotProvideOpinion(java.lang.String, testbed.sim.Era)
	 */
	public void agentDidNotProvideOpinion(String agent, Era era)
	{
		// Simple Trust doesn't pay attention
	}

	/* (non-Javadoc)
	 * @see agent.trust.TrustInterface#agentDidNotProvideReputation(java.lang.String, testbed.sim.Era)
	 */
	public void agentDidNotProvideReputation(String agent, Era era)
	{
		// Simple Trust doesn't pay attention to whether an agent provided a reputation after payment
	}

	/* (non-Javadoc)
	 * @see agent.trust.TrustInterface#didNotAcceptCertaintyRequest(java.lang.String, testbed.sim.Era)
	 */
	public void didNotAcceptCertaintyRequest(String fromAgent, Era era)
	{
		// Simple Trust doesn't pay attention to whether we accepted a certainty request 
	}

	/* (non-Javadoc)
	 * @see agent.trust.TrustInterface#didNotProvideAcceptReputationRequest(java.lang.String, java.lang.String, testbed.sim.Era)
	 */
	public void didNotProvideAcceptReputationRequest(String fromAgent,
			String aboutAgent, Era era)
	{
		// Simple Trust doesn't pay attention to whether we provided an accept reputation request 
	}

	/* (non-Javadoc)
	 * @see agent.trust.TrustInterface#didNotProvideOpinionAfterPayment(java.lang.String, testbed.sim.Era)
	 */
	public void didNotProvideOpinionAfterPayment(String fromAgent, Era era)
	{
		// Simple Trust doesn't pay attention to whether we provided an opinion or not 
	}

	/* (non-Javadoc)
	 * @see agent.trust.TrustInterface#didNotProvideReputationAfterPayment(java.lang.String, java.lang.String, testbed.sim.Era)
	 */
	public void didNotProvideReputationAfterPayment(String fromAgent,
			String aboutAgent, Era era)
	{
		// Simple Trust doesn't pay attention to whether we provided a reputation or not 
	}

	/* (non-Javadoc)
	 * @see agent.trust.TrustInterface#generatedOpinion(java.lang.String, testbed.sim.AppraisalAssignment, double)
	 */
	public void generatedOpinion(String toAgent, AppraisalAssignment art,
			double hoursSpent)
	{
		// Simple Trust doesn't care how much time we spent generating an opinion
	}

	/* (non-Javadoc)
	 * @see agent.trust.TrustInterface#getInferredTrustValue(java.lang.String, testbed.sim.Era)
	 */
	public double getInferredTrustValue(String agent, Era era)
	{
		Context c = new Context(era.getName());
		Peer p = new Peer(agent);
		return trust.getOverallTrust(c, p);
	}

	/* (non-Javadoc)
	 * @see agent.trust.TrustInterface#getReputationWeight(java.lang.String, testbed.sim.Era)
	 */
	public double getReputationWeight(String agent, Era era)
	{
		// TODO Auto-generated method stub
		return 0;
	}
	
	/* (non-Javadoc)
	 * @see agent.trust.TrustInterface#getTrustValue(java.lang.String, testbed.sim.Era)
	 */
	public double getTrustValue(String agent, Era era)
	{
		Context c = new Context(era.getName());
		Peer p = new Peer(agent);
		return trust.getOverallTrust(c, p);
	}

	/* (non-Javadoc)
	 * @see agent.trust.TrustInterface#providedAcceptReputationRequest(java.lang.String, java.lang.String, testbed.sim.Era)
	 */
	public void providedAcceptReputationRequest(String toAgent,
			String aboutAgent, Era era)
	{
		// Simple Trust doesn't care that we decided to accept a reputation request
		//from another agent.

	}

	/* (non-Javadoc)
	 * @see agent.trust.TrustInterface#providedCertaintyReply(java.lang.String, testbed.sim.Era, double)
	 */
	public void providedCertaintyReply(String toAgent, Era era,
			double certaintyValue)
	{
		// Simple Trust doesn't care that we provided a certainty value to another agent
	}

	/* (non-Javadoc)
	 * @see agent.trust.TrustInterface#providedOpinion(java.lang.String, testbed.sim.AppraisalAssignment, int)
	 */
	public void providedOpinion(String toAgent, AppraisalAssignment art,
			int appraisedValue)
	{
		// Simple trust doesn't care that we provided an opinion to another agent
	}

	/* (non-Javadoc)
	 * @see agent.trust.TrustInterface#providedReputationReply(java.lang.String, java.lang.String, testbed.sim.Era, double)
	 */
	public void providedReputationReply(String toAgent, String aboutAgent,
			Era era, double reputationValue)
	{
		// Simple Trust doesn't care whether we provided a reputation update to an agent
	}

	/* (non-Javadoc)
	 * @see agent.trust.TrustInterface#receiveAgentReputationUpdate(java.lang.String, java.lang.String, testbed.sim.Era, double)
	 */
	public void receiveAgentReputationUpdate(String fromAgent,
			String aboutAgent, Era era, double reputation)
	{
		// Simple Trust never asks (or receives) reputation updates
	}

	/* (non-Javadoc)
	 * @see agent.trust.TrustInterface#removeAgent(java.lang.String)
	 */
	public void removeAgent(String agent)
	{
		// Trustlib does not care about agent removal
	}

	/* (non-Javadoc)
	 * @see agent.trust.TrustInterface#setAgentEraCertainty(java.lang.String, testbed.sim.Era, double)
	 */
	public void setAgentEraCertainty(String agent, Era era, double certainty)
	{
		Context c = new Context(era.getName());
		Peer p = new Peer(agent);
        
        trust.storeRecommendation(c, p, certainty);
	}

	/* (non-Javadoc)
	 * @see agent.trust.TrustInterface#setOurEraCertainty(testbed.sim.Era)
	 */
	public void setOurEraCertainty(Era era)
	{
		// Simple Trust doesn't store our era certainty.
	}

	/* (non-Javadoc)
	 * @see agent.trust.TrustInterface#updateAgentTrustValue(java.lang.String, testbed.sim.AppraisalAssignment, int, int, double)
	 */
	public void updateAgentTrustFromFinalAppraisal(String agent, Appraisal appraisal, Opinion opinion)
	{

		Context c = new Context(appraisal.getEra().getName());
		Peer p = new Peer(agent);
		
		double difference = Math.abs(appraisal.getTrueValue() - opinion.getAppraisedValue());
		difference = difference / ((double)appraisal.getTrueValue());
		
		double reputation = trust.getOverallTrust(c, p);
		
		if (difference > 0.5) reputation = reputation - 0.03;
		else reputation = reputation + 0.03;
		if (reputation > 1) reputation = 1;
		if (reputation < 0) reputation = 0;
		
        
        trust.storeEncounter(c, p, (int)reputation);
		
		
	}
	
	public void frameReset()
	{
	}	
}
