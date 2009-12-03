package agent.trust;

import testbed.sim.AppraisalAssignment;
import testbed.sim.Era;
import agent.CCMPAgent;
import agent.trust.TrustNetwork;

import java.util.LinkedList;
import java.util.List;
import testbed.sim.Appraisal;
import testbed.sim.Opinion;
import trust.model.BayesTrust;
import trust.model.primitives.Context;
import trust.model.primitives.Peer;

/**
 * Bayesian Trust shim for agent
 */
public class BayesTrustNetwork extends TrustNetwork {

		public static final int TRUST_LEVELS = 4;
		private BayesTrust mTrust;
		
		/**
		 * Contructor
		 * @param agent Requires the agent to interrogate ART for problem parameters
		 */
		public BayesTrustNetwork(CCMPAgent agent)
		{
			super(agent);
			List<Context> contexts = new LinkedList<Context>();
			
			for(Era era : mAgent.getEras()) {
				Context c = new Context(era.getName());
				contexts.add(c);
			}
			
			mTrust = new BayesTrust(TRUST_LEVELS, contexts);
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
			mTrust.addPeer(p);
		}

		/* (non-Javadoc)
		 * @see agent.trust.TrustInterface#agentDidNotAcceptCertainty(java.lang.String, testbed.sim.Era)
		 */
		public void agentDidNotAcceptCertainty(String agent, Era era,
				double certaintyValue)
		{
			// TODO: Evaluate implementation
		}

		/* (non-Javadoc)
		 * @see agent.trust.TrustInterface#agentDidNotAcceptReputationRequest(java.lang.String, testbed.sim.Era)
		 */
		public void agentDidNotAcceptReputationRequest(String agent, Era era)
		{
			// TODO: Evaluate implementation
		}

		/* (non-Javadoc)
		 * @see agent.trust.TrustInterface#agentDidNotProvideCertainty(java.lang.String, testbed.sim.Era)
		 */
		public void agentDidNotProvideCertainty(String agent, Era era)
		{
			// TODO: Evaluate implementation
		}

		/* (non-Javadoc)
		 * @see agent.trust.TrustInterface#agentDidNotProvideOpinion(java.lang.String, testbed.sim.Era)
		 */
		public void agentDidNotProvideOpinion(String agent, Era era)
		{
			// Ignored, no encounter, assumed to be a benign response
		}

		/* (non-Javadoc)
		 * @see agent.trust.TrustInterface#agentDidNotProvideReputation(java.lang.String, testbed.sim.Era)
		 */
		public void agentDidNotProvideReputation(String agent, Era era)
		{
			// Ignored, no encounter, assumed to be a benign response
		}

		/* (non-Javadoc)
		 * @see agent.trust.TrustInterface#didNotAcceptCertaintyRequest(java.lang.String, testbed.sim.Era)
		 */
		public void didNotAcceptCertaintyRequest(String fromAgent, Era era)
		{
			// Ignored, no encounter, assumed to be a benign response
		}

		/* (non-Javadoc)
		 * @see agent.trust.TrustInterface#didNotProvideAcceptReputationRequest(java.lang.String, java.lang.String, testbed.sim.Era)
		 */
		public void didNotProvideAcceptReputationRequest(String fromAgent,
				String aboutAgent, Era era)
		{
			// Ignored, no encounter, assumed to be a benign response 
		}

		/* (non-Javadoc)
		 * @see agent.trust.TrustInterface#didNotProvideOpinionAfterPayment(java.lang.String, testbed.sim.Era)
		 */
		public void didNotProvideOpinionAfterPayment(String fromAgent, Era era)
		{
			// Ignored, no encounter, assumed to be a benign response
		}

		/* (non-Javadoc)
		 * @see agent.trust.TrustInterface#didNotProvideReputationAfterPayment(java.lang.String, java.lang.String, testbed.sim.Era)
		 */
		public void didNotProvideReputationAfterPayment(String fromAgent,
				String aboutAgent, Era era)
		{
			// Ignored, no encounter, assumed to be a benign response 
		}

		/* (non-Javadoc)
		 * @see agent.trust.TrustInterface#generatedOpinion(java.lang.String, testbed.sim.AppraisalAssignment, double)
		 */
		public void generatedOpinion(String toAgent, AppraisalAssignment art,
				double hoursSpent)
		{
			// TODO: Evaluate implementation: Affects certainty
		}

		/* (non-Javadoc)
		 * @see agent.trust.TrustInterface#getInferredTrustValue(java.lang.String, testbed.sim.Era)
		 */
		public double getInferredTrustValue(String agent, Era era)
		{
			Context ck = new Context(era.getName());
			Peer py = new Peer(agent);
			return mTrust.getCondensedOverallTrust(ck, py);
		}

		/* (non-Javadoc)
		 * @see agent.trust.TrustInterface#getReputationWeight(java.lang.String, testbed.sim.Era)
		 */
		public double getReputationWeight(String agent, Era era)
		{
			Context ck = new Context(era.getName());
			Peer py = new Peer(agent);
			return mTrust.getOverallTrustConfidence(ck, py);
		}
		
		/* (non-Javadoc)
		 * @see agent.trust.TrustInterface#getTrustValue(java.lang.String, testbed.sim.Era)
		 */
		public double getTrustValue(String agent, Era era)
		{
			// TODO: Evaluate whether different from inferred or not
			return getInferredTrustValue(agent, era);
		}

		/* (non-Javadoc)
		 * @see agent.trust.TrustInterface#providedAcceptReputationRequest(java.lang.String, java.lang.String, testbed.sim.Era)
		 */
		public void providedAcceptReputationRequest(String toAgent,
				String aboutAgent, Era era)
		{
			// TODO: Evaluate implementation
			
			// Simple Trust doesn't care that we decided to accept a reputation request
			//from another agent.

		}

		/* (non-Javadoc)
		 * @see agent.trust.TrustInterface#providedCertaintyReply(java.lang.String, testbed.sim.Era, double)
		 */
		public void providedCertaintyReply(String toAgent, Era era,
				double certaintyValue)
		{
			// TODO: Evaluate implementation
			
			// Simple Trust doesn't care that we provided a certainty value to another agent
		}

		/* (non-Javadoc)
		 * @see agent.trust.TrustInterface#providedOpinion(java.lang.String, testbed.sim.AppraisalAssignment, int)
		 */
		public void providedOpinion(String toAgent, AppraisalAssignment art,
				int appraisedValue)
		{
			// TODO: Evaluate implementation
			
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
		public void receiveAgentReputationUpdate(String fromAgent, String aboutAgent, Era era, double reputation)
		{
			Context ck = new Context(era.getName());
			Peer py = new Peer(fromAgent);
			Peer pr = new Peer(aboutAgent);
			
			// TODO: Evaluate beta calculation
			double ctsBeta = reputation;
			
			mTrust.storeRecommendation(ck, pr, py, ctsBeta);
		}

		/* (non-Javadoc)
		 * @see agent.trust.TrustInterface#removeAgent(java.lang.String)
		 */
		public void removeAgent(String agent)
		{
			// Ignored, trust does not care about agent removal
		}

		/* (non-Javadoc)
		 * @see agent.trust.TrustInterface#setAgentEraCertainty(java.lang.String, testbed.sim.Era, double)
		 */
		public void setAgentEraCertainty(String agent, Era era, double certainty)
		{
			Context c = new Context(era.getName());
			Peer p = new Peer(agent);
	        
			// TODO: Evaluate implementation
		}

		/* (non-Javadoc)
		 * @see agent.trust.TrustInterface#setOurEraCertainty(testbed.sim.Era)
		 */
		public void setOurEraCertainty(Era era)
		{
			// Ignored, trust does not care about our era certainty.
		}

		/* (non-Javadoc)
		 * @see agent.trust.TrustInterface#updateAgentTrustValue(java.lang.String, testbed.sim.AppraisalAssignment, int, int, double)
		 */
		public void updateAgentTrustFromFinalAppraisal(String agent, Appraisal appraisal, Opinion opinion)
		{

			Context ck = new Context(appraisal.getEra().getName());
			Peer py = new Peer(agent);
			
			// What was the difference encountered
			double difference = Math.abs(appraisal.getTrueValue() - opinion.getAppraisedValue());
			
			// What is that difference in percent
			difference = difference / ((double)appraisal.getTrueValue());
	        
			// What is the satisfaction level of this encounter
			// TODO: Calculate satisfaction
			
			// Record the encounter
			mTrust.storeEncounter(ck, py, (int)difference);
		}
		
		public void frameReset()
		{
			// TODO: Evaluate implementation: requires changes to BayesTrust
		}	
}
