package agent;
import agent.CCMPAgent;

import agent.decision.SimpleDT;
import agent.decision.DecisionTree;
import agent.trust.SimpleTrust;
import agent.trust.TrustNetwork;

import java.util.Collections;


/**
 * 
 */

/**
 * @author cfournie
 *
 */
public class SimpleCCMPAgent extends CCMPAgent {
    		
	/**
	 * 
	 */ 
	@Override
	public void prepareCertaintyRequests()
	{
		Collections.shuffle(agentNames);
		super.prepareCertaintyRequests();
	}
	
    DecisionTree createDecisionTree()
    {
    	return new SimpleDT(this);
    }
    
    TrustNetwork createTrustNetwork()
    {
    	return new SimpleTrust(this);
    }    
}
