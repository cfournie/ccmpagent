package agent.decision;

import java.util.HashMap;
import java.util.Hashtable;
import java.util.Map;
import java.util.Vector;

import learning.*;

import agent.CCMPAgent;

import testbed.sim.AppraisalAssignment;
import testbed.sim.Era;

/**
 * Weka Based Decision Tree class
 * 
 * @author Pierre Dinnissen
 */
public class WekaDT extends DecisionTree {
	
	/** Collection of Weka decision trees **/
	public DTLearningCollection dtreeCol;
	/** Vector that allows quick access to a DTLearning's name of Attributes **/
	private Vector<Vector<String>> treeAtts;
	
	/** Map of which strategy should be employed for each other Agent **/
	private HashMap<String,String> strategy;
	
	/** Map of last action from an agent in terms of providing Reputation **/
	private HashMap<String,Boolean> provideRep;
	/** Map of last action from an agent in terms of providing Certainty **/
	private HashMap<String,Boolean> provideCer;
	/** Map of last action from an agent in terms of providing Opinion **/
	private HashMap<String,Boolean> provideOpi;
	/** Map of how many times an agent how not provided once promised **/
	private HashMap<String,Integer> notProvideCount;
	
	/** Enums of all the decision trees **/
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
		
	/**
	 * Constructor
	 * @param Reference to the CCMP agent
	 * @param DTLearningCollection of all the decision trees
	 */
	public WekaDT(CCMPAgent agent, DTLearningCollection treeCol)
	{
		super(agent);
		this.dtreeCol = treeCol;
		treeAtts = new Vector<Vector<String>>();
		strategy = new HashMap<String,String>();
		
		provideRep = new HashMap<String,Boolean>();
		provideCer = new HashMap<String,Boolean>();
		provideOpi = new HashMap<String,Boolean>();
		
		notProvideCount = new HashMap<String,Integer>();
		
		for(DTLearning tree : treeCol)
		{
			Vector<String> atts = new Vector<String>();
			// Get the attributes of all the trees
			for(DTAttribute att : tree.arff.attributes)
			{
				atts.add(att.name);
			}
			treeAtts.add(atts);
		}
	}
	
	/**
	 * Helper function that will builds a test
	 * @return Comma separated string test
	 * @param Enum for the tree that is to be queried
	 * @param Agent that this query regards to
	 * @param Era in which this query regards to
	 * @param boolean as to whether certainty is for this agent or other agent
	 * @param art piece to be appraised
	 */
	private String BuildTest(DTLearningNames tree, 
							 String agent, Era era, boolean ourCertainty,
							 AppraisalAssignment art)
	{
		StringBuffer test = new StringBuffer();
		
		// Go through the attributes and retrieve the appropriate value and
		// append it to the test
		for(String att : treeAtts.get(tree.ordinal()))
		{
			if(att.equals("strategy"))
			{
				test.append(strategy.get(agent)+",");
			}
			else if(att.equals("lastaction"))
			{
				Boolean lastaction;
				switch (tree)
				{
				case DT_PROVIDECERTAINTY:
					lastaction = provideCer.get(agent);
					break;
				case DT_PROVIDEOPINION:
					lastaction = provideOpi.get(agent);
					break;
				case DT_PROVIDEREPUTATION:
					lastaction = provideRep.get(agent);
					break;
				default:
					lastaction = Boolean.TRUE;
					break;
				}
				
				test.append(lastaction.toString().toUpperCase()+",");				
			}
			else if(att.equals("msgrem"))
			{
				switch (tree)
				{
				case DT_REQUESTCERTAINTY:
					test.append(mNumCertaintyRequestsLeft.toString());
					break;
				case DT_REQUESTOPINION:
					test.append(mNumOpinionRequestsLeft.get(art.getPaintingID()).toString());
					break;
				default:
					test.append("crash");
					break;
				}
				test.append(",");
			}
			else if(att.equals("certainty"))
			{
				try
				{
					if(ourCertainty)
					{
						test.append(Double.toString(mAgent.getEraCertainty(era)));
					}
					else
					{
						test.append(Double.toString(mCertainties.get(agent).get(era)));						
					}
				}
				catch(NullPointerException e) {
					// If no certainty... put zero
					test.append("0.0");
				}
				test.append(",");
			}
			else if(att.equals("trust"))
			{
				test.append(Double.toString(mReputations.get(agent))+",");
			}
			else // this should be the Cat
			{
				test.append("?");
			}
		}
		
		return test.toString();
	}
	
	
	/** Adjust the determined appraisal value
	 * @param toAgent is the agent that this appraisal is for
	 * @param era in which the appraisal is
	 * @param appraisal integer value
	 * @return adjusted appraisal value
	 */
	public int adjustAppraisalValue(String toAgent, Era era, int appraisal) {
		String dtTest = BuildTest(DTLearningNames.DT_ADJUSTAPPRAISAL, toAgent, era, true, null);			
		String result = dtreeCol.get(DTLearningNames.DT_ADJUSTAPPRAISAL.ordinal()).DTClassify(dtTest);
		mAgent.writeToLogFile("Test:"+dtTest+"Result:"+result);
		
		if(result.equals("UNCHANGED"))
		{
			return appraisal;
		}
		else if(result.equals("INFLATEx2"))
		{
			return 2*appraisal;
		}
		else if(result.equals("INFLATEx10"))
		{
			return 10*appraisal;
		}
		else
		{
			return appraisal;
		}
	}

	/** Whether or not to generate an opinion
	 * @param requestingAgent is the agent that the opinion is for
	 * @param era in question
	 * @return boolean decision
	 */
	public boolean generateOpinion(String requestingAgent, Era era) {
		String dtTest = BuildTest(DTLearningNames.DT_GENERATEOPINION, requestingAgent, era, true, null);	
		String result = dtreeCol.get(DTLearningNames.DT_GENERATEOPINION.ordinal()).DTClassify(dtTest);
		mAgent.writeToLogFile("Test:"+dtTest+"Result:"+result);

		if(result.equals("DO"))
		{
			return true;
		}
		else if(result.equals("DONT"))
		{
			return false;
		}
		else
		{
			return false;
		}
	}

	/** How much to spend on the Appraisal
	 * @param requestingAgent
	 * @param era in question
	 * @return double value of the appraisal
	 */
	public double getAppraisalCost(String requestingAgent, Era era)
	{
		String dtTest = BuildTest(DTLearningNames.DT_GETAPPRAISAL, requestingAgent, era, false, null);
		String result = dtreeCol.get(DTLearningNames.DT_GETAPPRAISAL.ordinal()).DTClassify(dtTest);
		mAgent.writeToLogFile("Test:"+dtTest+"Result:"+result);
		
		if(result.equals("MINIMAL"))
		{
			return 0.01;
		}
		else if(result.equals("MODERATE"))
		{
			return mAgent.getOpinionCost()*0.8;
		}
		else if(result.equals("BEST"))
		{
			return mAgent.getOpinionCost();
		}
		else
		{
			return 0.01;
		}
    }

	/** How certain the CCMP agent is in a particular era
	 * @param agent requesting
	 * @param era in question
	 * @return double value of the expertise of the CCMP agent
	 */
	public double getCertaintyRequestValue(String agent, Era era)
	{
        String eraName = era.getName();
        double myExpertise = mAgent.getExpertise(eraName);
        
        String dtTest = BuildTest(DTLearningNames.DT_GETCERTAINTY, agent, era, false, null);
		String result = dtreeCol.get(DTLearningNames.DT_GETCERTAINTY.ordinal()).DTClassify(dtTest);
		mAgent.writeToLogFile("Test:"+dtTest+"Result:"+result);

		if(result.equals("TRUTH"))
		{
			return 1-myExpertise;
		}
		else if(result.equals("LIE"))
		{
			return myExpertise;
		}
		else
		{
			return 1-myExpertise;
		}
	}

	/** Retrieve reputation of another agent for another agent
	 * @param requestingAgent
	 * @param aboutAgent is the agent whose reputation is requested for
	 * @param era in question
	 * @return boolean decision
	 */
	public double getReputationRequestValue(String requestingAgent,
			String aboutAgent, Era era)
	{
		// The simple agent returns are real values
		double repValue = mReputations.get(aboutAgent).doubleValue();
		
		String dtTest = BuildTest(DTLearningNames.DT_GETREPUTATION, requestingAgent, era, false, null);
		String result = dtreeCol.get(DTLearningNames.DT_GETREPUTATION.ordinal()).DTClassify(dtTest);
		mAgent.writeToLogFile("Test:"+dtTest+"Result:"+result);

		if(result.equals("TRUTH"))
		{
			return repValue;
		}
		else if(result.equals("LIE"))
		{
			return 1-repValue;
		}
		else
		{
			return repValue;
		}
	}

	/** Whether or not to provide a Certainty
	 * @param agent requesting
	 * @param era in question
	 * @return boolean decision
	 */
	public boolean provideCertaintyReply(String agent, Era era)
	{
		String dtTest = BuildTest(DTLearningNames.DT_PROVIDECERTAINTY, agent, era, true, null);
		String result = dtreeCol.get(DTLearningNames.DT_PROVIDECERTAINTY.ordinal()).DTClassify(dtTest);
		mAgent.writeToLogFile("Test:"+dtTest+"Result:"+result);
		
		if(result.equals("DO"))
		{
			return true;
		}
		else if(result.equals("DONT"))
		{
			return false;
		}
		else
		{
			return true;
		}
	}

	/** Whether or not to provide a Opinion
	 * @param agent requesting
	 * @param era in question
	 * @return boolean decision
	 */
	public boolean provideOpinion(String requestionAgent, Era era)
	{
		String dtTest = BuildTest(DTLearningNames.DT_PROVIDEOPINION, requestionAgent, era, true, null);
		String result = dtreeCol.get(DTLearningNames.DT_PROVIDEOPINION.ordinal()).DTClassify(dtTest);
		mAgent.writeToLogFile("Test:"+dtTest+"Result:"+result);
		
		if(result.equals("DO"))
		{
			return true;
		}
		else if(result.equals("DONT"))
		{
			return false;
		}
		else
		{
			return true;
		}
	}

	/** Whether or not to provide a Reputation
	 * @param agent requesting
	 * @param era in question
	 * @return boolean decision
	 */
	public boolean provideReputationReply(String requestingAgent,
			String aboutAgent, Era era)
	{
		String dtTest = BuildTest(DTLearningNames.DT_PROVIDEREPUTATION, requestingAgent, era, true, null);
		String result = dtreeCol.get(DTLearningNames.DT_PROVIDEREPUTATION.ordinal()).DTClassify(dtTest);	    
		mAgent.writeToLogFile("Test:"+dtTest+"Result:"+result);
		
		if(result.equals("DO"))
		{
			return true;
		}
		else if(result.equals("DONT"))
		{
			return false;
		}
		else
		{
			return true;
		}
	}

	/** Decide whether to request to an agent about their certainty
	 * @param toAgent agent to ask
	 * @param era in question
	 * @return boolean decision
	 */
	public boolean requestAgentCertainty(String toAgent, Era era)
	{
		String dtTest = BuildTest(DTLearningNames.DT_REQUESTCERTAINTY, toAgent, era, false, null);
		String result = dtreeCol.get(DTLearningNames.DT_REQUESTCERTAINTY.ordinal()).DTClassify(dtTest);
		mAgent.writeToLogFile("Test:"+dtTest+"Result:"+result);
		
		if(result.equals("DO"))
		{
			return true;
		}
		else if(result.equals("DONT"))
		{
			return false;
		}
		else
		{
			return true;
		}
	}

	/** Decide whether to request to an agent for their opinion
	 * @param toAgent agent to ask
	 * @param art in question
	 * @return boolean decision
	 */
	public boolean requestAgentOpinion(String toAgent, AppraisalAssignment art)
	{
		String dtTest = BuildTest(DTLearningNames.DT_REQUESTOPINION, toAgent, null, false, art);
		String result = dtreeCol.get(DTLearningNames.DT_REQUESTOPINION.ordinal()).DTClassify(dtTest);
		mAgent.writeToLogFile("Test:"+dtTest+"Result:"+result);
		
		if(result.equals("DO"))
		{
			return true;
		}
		else if(result.equals("DONT"))
		{
			return false;
		}
		else
		{
			return true;
		}
	}

	/** Decide whether to request to an agent about another agent's reputation
	 * @param toAgent agent to ask
	 * @param aboutAgent
	 * @param era in question
	 * @param current time step of the simulation
	 * @return boolean decision
	 */
	public boolean requestAgentReputationUpdate(String toAgent,
			String aboutAgent, Era era, int currentTimestep)
	{
		String dtTest = BuildTest(DTLearningNames.DT_REQUESTREPUTATION, toAgent, era, false, null);
		String result = dtreeCol.get(DTLearningNames.DT_REQUESTREPUTATION.ordinal()).DTClassify(dtTest);
		mAgent.writeToLogFile("Test:"+dtTest+"Result:"+result);
		
		if(result.equals("DO"))
		{
			return true;
		}
		else if(result.equals("DONT"))
		{
			return false;
		}
		else
		{
			return true;
		}
	}

	/** Decide whether to respond to another agents certainty request
	 * @param agent agent to ask
	 * @param era in question
	 * @return boolean decision
	 */
	public boolean respondToCertaintyRequest(String agent, Era era)
	{
		String dtTest = BuildTest(DTLearningNames.DT_RESPONDCERTAINTY, agent, era, true, null);
		String result = dtreeCol.get(DTLearningNames.DT_RESPONDCERTAINTY.ordinal()).DTClassify(dtTest);
		mAgent.writeToLogFile("Test:"+dtTest+"Result:"+result);
		
		if(result.equals("DO"))
		{
			return true;
		}
		else if(result.equals("DONT"))
		{
			return false;
		}
		else
		{
			return true;
		}
	}

	/** Decide whether to respond to another agents reputation request about another agent
	 * @param agent agent to ask
	 * @param aboutAgent
	 * @param era in question
	 * @return boolean decision
	 */
	public boolean respondToReputationRequest(String requestingAgent,
			String aboutAgent, Era era)
	{
		String dtTest = BuildTest(DTLearningNames.DT_RESPONDREPUTATION, requestingAgent, era, false, null);
		String result = dtreeCol.get(DTLearningNames.DT_RESPONDREPUTATION.ordinal()).DTClassify(dtTest);
		mAgent.writeToLogFile("Test:"+dtTest+"Result:"+result);
		
		if(result.equals("DO"))
		{
			return true;
		}
		else if(result.equals("DONT"))
		{
			return false;
		}
		else
		{
			return true;
		}
	}

	/** Adjust the number of certainty requests left
	 * @param agent agent to ask
	 * @param era in question
	 */
	public void sentCertaintyRequest(String toAgent, Era era)
	{
		mNumCertaintyRequestsLeft--;
	}

	/** Adjust the number of opinion requests left
	 * @param agent agent to ask
	 * @param era in question
	 */
	public void sentOpinionRequest(String toAgent, AppraisalAssignment art)
	{
		Integer currentLeft = mNumOpinionRequestsLeft.get(art.getPaintingID());
		currentLeft--;		
		mNumOpinionRequestsLeft.put( art.getPaintingID(), currentLeft);
	}

	/* (non-Javadoc)
	 * @see agent.learning.LearningInterface#sentReputationRequest(java.lang.String, java.lang.String)
	 */
	public void sentReputationRequest(String toAgent, String aboutAgent)
	{
		// No need to implement
	}

	/** store an agents era certainty
	 * @param agent agent to ask
	 * @param era in question
	 * @param certainty value
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

	/** store the reputation of another agent
	 * @param agent agent to ask
	 * @param era in question
	 * @param trust value
	 */
	public void setAgentTrust(String agent, Era era, double trust)
	{
		mReputations.put(agent, trust);
	}
	
	/** Initialize simulation storage variables
	 */
	public void init()
	{
		mReputations = new Hashtable<String,Double>();
		mCertainties = new Hashtable<String,Map<Era,Double>>();
		mNumCertaintyRequestsLeft = 0;
		mNumOpinionRequestsLeft = new Hashtable<String,Integer>();
		
		mNumOpinionRequestsLeft.clear();
	}
	
	/** Reset simulation storage variables after every frame
	 */
	public void frameReset()
	{
		mNumCertaintyRequestsLeft= mAgent.getMaxCertaintyRequests();;
		mNumOpinionRequestsLeft.clear();
		for( AppraisalAssignment art: mAgent.getAppraisalAssignments())
		{
			mNumOpinionRequestsLeft.put(art.getPaintingID(), mAgent.getMaxOpinionRequests());
		}
	}	

	/** Add space in every storage variable for a competitor agent
	 * @param newAgent
	 */
	public void addAgent( String newAgent )
	{
		mReputations.put(newAgent, new Double(1.0));
		strategy.put(newAgent, "NICE");
		provideRep.put(newAgent, Boolean.TRUE);
		provideCer.put(newAgent, Boolean.TRUE);
		provideOpi.put(newAgent, Boolean.TRUE);
		notProvideCount.put(newAgent, Integer.valueOf(0));
	}
	
	/* (non-Javadoc)
	 * @see agent.decision.DecisionTree#removeAgent()
	 */	
	public void removeAgent( String agent )
	{
		mReputations.remove(agent);
		strategy.remove(agent);
	}	
	
	/** Whether to provide Weight to the sim
	 * @param aboutAgent
	 * @param era in question
	 */
	public boolean provideWeight( String aboutAgent, Era era )
	{
		String dtTest = BuildTest(DTLearningNames.DT_PROVIDEWEIGHT, aboutAgent, era, true, null);
		String result = dtreeCol.get(DTLearningNames.DT_PROVIDEWEIGHT.ordinal()).DTClassify(dtTest);
		mAgent.writeToLogFile("Test:"+dtTest+"Result:"+result);
		
		if(result.equals("DO"))
		{
			return true;
		}
		else if(result.equals("DONT"))
		{
			return false;
		}
		else
		{
			return true;
		}		
	}
		
	public void agentDidNotAcceptReputationRequest( String agent, Era era ) 
	{
		//benign encounter		
	}
	
	/** Store that an agent did not provide reputation
	 * @param agent
	 * @param era in question
	 */
	public void agentDidNotProvideReputation( String agent, Era era ) 
	{
		Integer notProvideCnt = notProvideCount.get(agent);
		notProvideCnt++;
		if(notProvideCnt>1) // Every agent gets one chance to reneg
		{
			strategy.put(agent, "REFLEX");
		}
		notProvideCount.put(agent, notProvideCnt);
		provideRep.put(agent, Boolean.FALSE);
	}
	
	/** Store that an agent did not provide certainty
	 * @param agent
	 * @param era in question
	 */
	public void agentDidNotProvideCertainty( String agent, Era era)
	{
		Integer notProvideCnt = notProvideCount.get(agent);
		notProvideCnt++;
		if(notProvideCnt>1) // Every agent gets one chance to reneg
		{
			strategy.put(agent, "REFLEX");
		}
		notProvideCount.put(agent, notProvideCnt);
		provideCer.put(agent, Boolean.FALSE);
	}
	
	/** Store that an agent did not provide opinion
	 * @param agent
	 * @param era in question
	 */
	public void agentDidNotProvideOpinion( String agent, Era era) 
	{
		Integer notProvideCnt = notProvideCount.get(agent);
		notProvideCnt++;
		if(notProvideCnt>1) // Every agent gets one chance to reneg
		{
			strategy.put(agent, "REFLEX");
		}
		notProvideCount.put(agent, notProvideCnt);
		provideOpi.put(agent, Boolean.FALSE);	
	}
	public void agentDidNotAcceptCertainty( String agent, Era era, double certaintyValue ) 
	{
		//benign encounter
	}
	
	public void agentDidAcceptReputationRequest( String agent, Era era ) 
	{
		//benign encounter	
	}
	
	/** Store that an agent did provide reputation
	 * @param agent
	 * @param era in question
	 */
	public void agentDidProvideReputation( String agent, Era era ) 
	{
		provideRep.put(agent, Boolean.TRUE);
	}
	
	/** Store that an agent did provide certainty
	 * @param agent
	 * @param era in question
	 */
	public void agentDidProvideCertainty( String agent, Era era)
	{
		provideCer.put(agent, Boolean.TRUE);
	}
	
	/** Store that an agent did provide opinion
	 * @param agent
	 * @param era in question
	 */
	public void agentDidProvideOpinion( String agent, Era era) 
	{
		provideOpi.put(agent, Boolean.TRUE);	
	}
	
	public void agentDidAcceptCertainty( String agent, Era era, double certaintyValue ) 
	{
		//benign encounter
	}
}
