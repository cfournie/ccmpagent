package agent.decision;

import java.util.HashMap;
import java.util.Hashtable;
import java.util.Map;
import java.util.Vector;

import learning.*;

import agent.CCMPAgent;

import testbed.sim.AppraisalAssignment;
import testbed.sim.Era;

public class WekaDT extends DecisionTree {
	
	public DTLearningCollection dtreeCol;
	private Vector<Vector<String>> treeAtts;
	
	public enum DTLearningNames{
		DT_ADJUSTAPPRAISAL,
		DT_GENERATEOPINION,
		DT_GETAPPRAISAL,
		DT_GETCERTAINTY,
		DT_GETREPUTATION,
		DT_PROVIDECERTAINTY,
		DT_PROVIDEOPINION,
		DT_PROVIDEREPUTATION,
		DT_REQUESTCERTAINTY,
		DT_REQUESTOPINION,
		DT_REQUESTREPUTATION,
		DT_RESPONDCERTAINTY,
		DT_RESPONDREPUTATION,
		DT_PROVIDEWEIGHT,
		DT_NUMDT
	}	
		
	public WekaDT(CCMPAgent agent, DTLearningCollection treeCol)
	{
		super(agent);
		this.dtreeCol = treeCol;
		
		for(DTLearning tree : treeCol)
		{
			Vector<String> atts = new Vector<String>();
			for(DTAttribute att : tree.arff.attributes)
			{
				atts.add(att.name);
			}
			treeAtts.add(atts);
		}
	}
	
	private String BuildTest(DTLearningNames tree, String forAgent)
	{
		StringBuffer test = new StringBuffer();
		
		for(String att : treeAtts.get(tree.ordinal()))
		{
			if(att == "stategy")
			{
				test.append("curStragforAgent"+",");
			}
			else if(att == "msgrem")
			{
				test.append("msgrem"+",");
			}
			else if(att == "certainty")
			{
				test.append("certainty"+",");
			}
			else if(att == "trust")
			{
				test.append("trust"+",");
			}
			else // this should be the nonCat
			{
				test.append("?");
			}
		}
		
		return test.toString();
	}
	
	
	/* (non-Javadoc)
	 * @see agent.learning.LearningInterface#adjustAppraisalValue(java.lang.String, testbed.sim.Era, int)
	 */
	public int adjustAppraisalValue(String toAgent, Era era, int appraisal) {
		String dtTest = BuildTest(DTLearningNames.DT_ADJUSTAPPRAISAL, toAgent);			
		String result = dtreeCol.get(DTLearningNames.DT_ADJUSTAPPRAISAL.ordinal()).DTClassify(dtTest);
		
		if(result=="UNCHANGED")
		{
			return appraisal;
		}
		else if(result=="INFLATEx2")
		{
			return 2*appraisal;
		}
		else if(result=="INFLATEx10")
		{
			return 10*appraisal;
		}
		else
		{
			return appraisal;
		}
	}

	/* (non-Javadoc)
	 * @see agent.learning.LearningInterface#generateOpinion(java.lang.String, testbed.sim.Era)
	 */
	public boolean generateOpinion(String requestingAgent, Era era) {
		String dtTest = BuildTest(DTLearningNames.DT_GENERATEOPINION, requestingAgent);	
		String result = dtreeCol.get(DTLearningNames.DT_GENERATEOPINION.ordinal()).DTClassify(dtTest);

		if(result == "DO")
		{
			return true;
		}
		else if(result == "DONT")
		{
			return false;
		}
		else
		{
			return false;
		}
	}

	/* (non-Javadoc)
	 * @see agent.learning.LearningInterface#getAppraisalCost(java.lang.String, testbed.sim.Era)
	 */
	public double getAppraisalCost(String requestingAgent, Era era)
	{
		String dtTest = BuildTest(DTLearningNames.DT_GETAPPRAISAL, requestingAgent);
		String result = dtreeCol.get(DTLearningNames.DT_GETAPPRAISAL.ordinal()).DTClassify(dtTest);
		
		if(result == "MINIMAL")
		{
			return 0.01;
		}
		else if(result == "MODERATE")
		{
			return mAgent.getOpinionCost()*0.8;
		}
		else if(result == "BEST")
		{
			return mAgent.getOpinionCost();
		}
		else
		{
			return 0.01;
		}
    }

	/* (non-Javadoc)
	 * @see agent.learning.LearningInterface#getCertaintyRequestValue(java.lang.String, testbed.sim.Era)
	 */
	public double getCertaintyRequestValue(String agent, Era era)
	{
		// SimpleDT responds with the agents true expertise value
        String eraName = era.getName();
        double myExpertise = mAgent.getExpertise(eraName);
        
        String dtTest = BuildTest(DTLearningNames.DT_GETCERTAINTY, agent);
		String result = dtreeCol.get(DTLearningNames.DT_GETCERTAINTY.ordinal()).DTClassify(dtTest);

		if(result == "TRUTH")
		{
			return 1-myExpertise;
		}
		else if(result == "LIE")
		{
			return myExpertise;
		}
		else
		{
			return 1-myExpertise;
		}
	}

	/* (non-Javadoc)
	 * @see agent.learning.LearningInterface#getReputationRequestValue(java.lang.String, java.lang.String, testbed.sim.Era)
	 */
	public double getReputationRequestValue(String requestingAgent,
			String aboutAgent, Era era)
	{
		// The simple agent returns are real values
		double repValue = mReputations.get(aboutAgent).doubleValue();
		
		String dtTest = BuildTest(DTLearningNames.DT_GETREPUTATION, requestingAgent);
		String result = dtreeCol.get(DTLearningNames.DT_GETREPUTATION.ordinal()).DTClassify(dtTest);

		if(result == "TRUTH")
		{
			return repValue;
		}
		else if(result == "LIE")
		{
			return 1-repValue;
		}
		else
		{
			return repValue;
		}
	}

	/* (non-Javadoc)
	 * @see agent.learning.LearningInterface#provideCertaintyRequest(java.lang.String, testbed.sim.Era)
	 */
	public boolean provideCertaintyReply(String agent, Era era)
	{
		String dtTest = BuildTest(DTLearningNames.DT_PROVIDECERTAINTY, agent);
		String result = dtreeCol.get(DTLearningNames.DT_PROVIDECERTAINTY.ordinal()).DTClassify(dtTest);
		
		if(result == "DO")
		{
			return true;
		}
		else if(result == "DONT")
		{
			return false;
		}
		else
		{
			return true;
		}
	}

	/* (non-Javadoc)
	 * @see agent.learning.LearningInterface#provideOpinion(java.lang.String, testbed.sim.Era)
	 */
	public boolean provideOpinion(String requestionAgent, Era era)
	{
		String dtTest = BuildTest(DTLearningNames.DT_PROVIDEOPINION, requestionAgent);
		String result = dtreeCol.get(DTLearningNames.DT_PROVIDEOPINION.ordinal()).DTClassify(dtTest);
		
		if(result == "DO")
		{
			return true;
		}
		else if(result == "DONT")
		{
			return false;
		}
		else
		{
			return true;
		}
	}

	/* (non-Javadoc)
	 * @see agent.learning.LearningInterface#provideReputationReply(java.lang.String, java.lang.String, testbed.sim.Era)
	 */
	public boolean provideReputationReply(String requestingAgent,
			String aboutAgent, Era era)
	{
		String dtTest = BuildTest(DTLearningNames.DT_PROVIDEREPUTATION, requestingAgent);
		String result = dtreeCol.get(DTLearningNames.DT_PROVIDEREPUTATION.ordinal()).DTClassify(dtTest);
		
		if(result == "DO")
		{
			return true;
		}
		else if(result == "DONT")
		{
			return false;
		}
		else
		{
			return true;
		}
	}

	/* (non-Javadoc)
	 * @see agent.learning.LearningInterface#requestAgentCertainty(java.lang.String, testbed.sim.Era)
	 */
	public boolean requestAgentCertainty(String toAgent, Era era)
	{
		//If we haven't sent to many requests, and we don't already know the agents
		//certainty, return true.
		/*if( mNumCertaintyRequestsSent < mAgent.getMaxCertaintyRequests() )
		{
		    Map<Era,Double> agCert = mCertainties.get(toAgent); 
		    if (agCert == null || !agCert.containsKey(era))
		    {			
		    	return true;
		    }
		}
		return false;*/
		String dtTest = BuildTest(DTLearningNames.DT_REQUESTCERTAINTY, toAgent);
		String result = dtreeCol.get(DTLearningNames.DT_REQUESTCERTAINTY.ordinal()).DTClassify(dtTest);
		
		if(result == "DO")
		{
			return true;
		}
		else if(result == "DONT")
		{
			return false;
		}
		else
		{
			return true;
		}
	}

	/* (non-Javadoc)
	 * @see agent.learning.LearningInterface#requestAgentOpinion(java.lang.String, testbed.sim.AppraisalAssignment)
	 */
	public boolean requestAgentOpinion(String toAgent, AppraisalAssignment art)
	{
		/*if( mNumOpinionRequestsSent.get(art.getPaintingID()) < mAgent.getMaxOpinionRequests() )
		{
            if (mReputations.get(toAgent) > 0.5)
            {
                return true;
            }
		}
		return false;*/
		String dtTest = BuildTest(DTLearningNames.DT_REQUESTOPINION, toAgent);
		String result = dtreeCol.get(DTLearningNames.DT_REQUESTOPINION.ordinal()).DTClassify(dtTest);
		
		if(result == "DO")
		{
			return true;
		}
		else if(result == "DONT")
		{
			return false;
		}
		else
		{
			return true;
		}
	}

	/* (non-Javadoc)
	 * @see agent.learning.LearningInterface#requestAgentReputationUpdate(java.lang.String, java.lang.String, testbed.sim.Era, int)
	 */
	public boolean requestAgentReputationUpdate(String toAgent,
			String aboutAgent, Era era, int currentTimestep)
	{
		String dtTest = BuildTest(DTLearningNames.DT_REQUESTREPUTATION, toAgent);
		String result = dtreeCol.get(DTLearningNames.DT_REQUESTREPUTATION.ordinal()).DTClassify(dtTest);
		
		if(result == "DO")
		{
			return true;
		}
		else if(result == "DONT")
		{
			return false;
		}
		else
		{
			return true;
		}
	}

	/* (non-Javadoc)
	 * @see agent.learning.LearningInterface#respondToCertaintyRequest(java.lang.String, testbed.sim.Era)
	 */
	public boolean respondToCertaintyRequest(String agent, Era era)
	{
		String dtTest = BuildTest(DTLearningNames.DT_RESPONDCERTAINTY, agent);
		String result = dtreeCol.get(DTLearningNames.DT_RESPONDCERTAINTY.ordinal()).DTClassify(dtTest);
		
		if(result == "DO")
		{
			return true;
		}
		else if(result == "DONT")
		{
			return false;
		}
		else
		{
			return true;
		}
	}

	/* (non-Javadoc)
	 * @see agent.learning.LearningInterface#respondToReputationRequest(java.lang.String, java.lang.String, testbed.sim.Era)
	 */
	public boolean respondToReputationRequest(String requestingAgent,
			String aboutAgent, Era era)
	{
		String dtTest = BuildTest(DTLearningNames.DT_RESPONDREPUTATION, requestingAgent);
		String result = dtreeCol.get(DTLearningNames.DT_RESPONDREPUTATION.ordinal()).DTClassify(dtTest);
		
		if(result == "DO")
		{
			return true;
		}
		else if(result == "DONT")
		{
			return false;
		}
		else
		{
			return true;
		}
	}

	/* (non-Javadoc)
	 * @see agent.learning.LearningInterface#sentCertaintyRequest(java.lang.String, testbed.sim.Era)
	 */
	public void sentCertaintyRequest(String toAgent, Era era)
	{
		mNumCertaintyRequestsSent++;
	}

	/* (non-Javadoc)
	 * @see agent.learning.LearningInterface#sentOpinionRequest(java.lang.String, testbed.sim.AppraisalAssignment)
	 */
	public void sentOpinionRequest(String toAgent, AppraisalAssignment art)
	{
		Integer currentSent = mNumOpinionRequestsSent.get(art.getPaintingID());
		currentSent++;		
		mNumOpinionRequestsSent.put( art.getPaintingID(), currentSent);
	}

	/* (non-Javadoc)
	 * @see agent.learning.LearningInterface#sentReputationRequest(java.lang.String, java.lang.String)
	 */
	public void sentReputationRequest(String toAgent, String aboutAgent)
	{
		// Simple DT doesn't send reputation requests and doesn't care if we do
	}

	/* (non-Javadoc)
	 * @see agent.decision.DecisionTree#setAgentEraCertainty(java.lang.String, testbed.sim.Era, double)
	 */
	public void setAgentEraCertainty(String agent, Era era, double certainty)
	{
        Map<Era,Double> agCert = mCertainties.get(agent);
        if (agCert == null) {
            agCert = new HashMap<Era,Double>();
            mCertainties.put(agent, agCert);
        }
        agCert.put(era, certainty);
	}

	/* (non-Javadoc)
	 * @see agent.decision.DecisionTree#setAgentPerceivedTrust(java.lang.String, testbed.sim.Era, double)
	 */
	public void setAgentPerceivedTrust(String agent, Era era, double trust)
	{
		// Not used in Simple DecisionTree
	}

	/* (non-Javadoc)
	 * @see agent.decision.DecisionTree#setAgentTrust(java.lang.String, testbed.sim.Era, double)
	 */
	public void setAgentTrust(String agent, Era era, double trust)
	{
		mReputations.put(agent, trust);
	}

	/* (non-Javadoc)
	 * @see agent.decision.DecisionTree#setOurEraCertainty(testbed.sim.Era, double)
	 */
	public void setOurEraCertainty(Era era, double certainty)
	{
		// Not used in SimpleDT, it just explicity asks for the agents expertice 
	}
	
	/* (non-Javadoc)
	 * @see agent.decision.DecisionTree#initTrustNetwork()
	 */
	public void init()
	{
		mReputations = new Hashtable<String,Double>();
		mCertainties = new Hashtable<String,Map<Era,Double>>();
		mNumCertaintyRequestsSent = 0;
		mNumOpinionRequestsSent = new Hashtable<String,Integer>();
		
		mNumOpinionRequestsSent.clear();
	}
	
	/* (non-Javadoc)
	 * @see agent.decision.DecisionTree#frameReset()
	 */
	public void frameReset()
	{
		mNumCertaintyRequestsSent= 0;
		mNumOpinionRequestsSent.clear();
		for( AppraisalAssignment art: mAgent.getAppraisalAssignments())
		{
			mNumOpinionRequestsSent.put(art.getPaintingID(), 0);
		}
	}	

	/* (non-Javadoc)
	 * @see agent.decision.DecisionTree#addAgent()
	 */	
	public void addAgent( String newAgent )
	{
		mReputations.put(newAgent, new Double(1.0));
	}
	
	/* (non-Javadoc)
	 * @see agent.decision.DecisionTree#removeAgent()
	 */	
	public void removeAgent( String agent )
	{
		mReputations.remove(agent);
	}	
	
	public boolean provideWeight( String aboutAgent, Era era )
	{
        /*if (mReputations.get(aboutAgent) > 0.8)
        	return true;
        else
        	return false;*/
		String dtTest = BuildTest(DTLearningNames.DT_PROVIDEWEIGHT, aboutAgent);
		String result = dtreeCol.get(DTLearningNames.DT_PROVIDEWEIGHT.ordinal()).DTClassify(dtTest);
		
		if(result == "DO")
		{
			return true;
		}
		else if(result == "DONT")
		{
			return false;
		}
		else
		{
			return true;
		}		
	}
	
	public void agentDidNotAcceptReputationRequest( String agent, Era era ) {}
	public void agentDidNotProvideReputation( String agent, Era era ) {}
	public void agentDidNotProvideCertainty( String agent, Era era) {}
	public void agentDidNotProvideOpinion( String agent, Era era) {}
	public void agentDidNotAcceptCertainty( String agent, Era era, double certaintyValue ) {}		
}
