package agent;

import java.util.Collections;

import agent.CCMPAgent;

import agent.decision.SimpleDT;
import agent.decision.DecisionTree;
import agent.trust.RandomTrustNetwork;
import agent.trust.TrustNetwork;

public class RandomCCMPAgent extends CCMPAgent
{
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
    	return new RandomTrustNetwork(this);
    }
    
    public String getLogFileName()
    {
    	return "RandomCCMPAgentLog.txt";
    } 
}
