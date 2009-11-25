package agent;
import agent.CCMPAgent;

import agent.decision.SimpleDT;
import agent.decision.DecisionTree;
import agent.trust.SimpleTrust;
import agent.trust.TrustNetwork;

import java.util.Collections;
import java.util.List;

import testbed.sim.Era;



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
    	return new SimpleDT();
    }
    
    TrustNetwork createTrustNetwork(List<Era> eras)
    {
    	return new SimpleTrust(eras);
    }
}
