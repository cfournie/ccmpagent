package agent;
import agent.CCMPAgent;

import agent.decision.WekaDT;
import agent.decision.DecisionTree;
import agent.trust.SimpleTrust;
import agent.trust.TrustNetwork;


/**
 * 
 */

/**
 * @author cfournie
 *
 */
public class BayesWekaCCMPAgent extends CCMPAgent {

	/**
	 * @param paramFile
	 */
	public BayesWekaCCMPAgent()
	{
		super();
	}

	
	/**
	 * @param paramFile
	 */
	public BayesWekaCCMPAgent(String paramFile)
	{
		super(paramFile);
	}
	
    DecisionTree createDecisionTree()
    {
    	return new WekaDT(this, mConfigInfo.getDecisionTrees());
    }
    
    TrustNetwork createTrustNetwork()
    {
    	return new SimpleTrust(this);
    }    
}
