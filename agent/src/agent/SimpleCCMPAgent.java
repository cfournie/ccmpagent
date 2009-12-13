package agent;
import agent.CCMPAgent;

import agent.decision.SimpleDT;
import agent.decision.DecisionTree;
import agent.trust.SimpleTrust;
import agent.trust.TrustNetwork;

import java.util.Collections;

/**
 * Simple CCMP Agent.
 */
public class SimpleCCMPAgent extends CCMPAgent {

	/**
	 * @param paramFile
	 */
	public SimpleCCMPAgent()
	{
		super();
	}

	
	/**
	 * @param paramFile
	 */
	public SimpleCCMPAgent(String paramFile)
	{
		super(paramFile);
	}
	
	/**
	 * 
	 */ 
	@Override
	public void prepareCertaintyRequests()
	{
		Collections.shuffle(agentNames);
		super.prepareCertaintyRequests();
	}
	
	protected DecisionTree createDecisionTree()
    {
    	return new SimpleDT(this);
    }
    
	protected TrustNetwork createTrustNetwork()
    {
    	return new SimpleTrust(this);
    }    
}
