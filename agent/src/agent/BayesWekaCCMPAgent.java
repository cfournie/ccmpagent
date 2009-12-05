package agent;

import java.io.IOException;

import learning.DTAttribute;
import learning.DTLearningCollection;
import learning.DTWekaARFF;

import org.apache.commons.digester.Digester;
import org.xml.sax.SAXException;

import agent.CCMPAgent;

import agent.decision.WekaDT;
import agent.trust.BayesTrustNetwork;
import agent.trust.TrustNetwork;
import agent.trust.SimpleTrust;

/**
 * CCMPAgent using B-Trust and WEKA-DT Learning.
 */
public class BayesWekaCCMPAgent extends CCMPAgent {
	private DTLearningCollection parsedTrees;

	/**
	 * Constructor
	 */
	public BayesWekaCCMPAgent()
	{
		super();	
	}

	
	/**
	 * Constructor
	 * @param paramFile
	 */
	public BayesWekaCCMPAgent(String paramFile)
	{
		super(paramFile);		
	}
	
	protected WekaDT createDecisionTree()
    {
    	return new WekaDT(this, parsedTrees);
    }
    
	protected TrustNetwork createTrustNetwork()
    {
    	return new BayesTrustNetwork(this);
    }
    
    protected void parseConfigFile( String paramFile )
    {      
    	super.parseConfigFile(paramFile);    	
    	parsedTrees = null;
        try
        {
            mDigester = new Digester();
            mDigester.setClassLoader(this.getClass().getClassLoader());
            mDigester.addObjectCreate("agentConfig/CCMPParams/decisiontrees", DTLearningCollection.class);
			
			mDigester.addObjectCreate("agentConfig/CCMPParams/decisiontrees/dt", DTWekaARFF.class);
			mDigester.addSetProperties("agentConfig/CCMPParams/decisiontrees/dt", "name", "name");
			
			mDigester.addObjectCreate("agentConfig/CCMPParams/decisiontrees/dt/attribute", DTAttribute.class);
			mDigester.addSetProperties("agentConfig/CCMPParams/decisiontrees/dt/attribute", "name", "name");
			mDigester.addSetProperties("agentConfig/CCMPParams/decisiontrees/dt/attribute", "type", "type");
			mDigester.addSetNext("agentConfig/CCMPParams/decisiontrees/dt/attribute", "addAttribute");
			mDigester.addBeanPropertySetter("agentConfig/CCMPParams/decisiontrees/dt/data", "data");
			
			mDigester.addSetNext("agentConfig/CCMPParams/decisiontrees/dt", "addDT");
            
            parsedTrees = (DTLearningCollection)mDigester.parse(paramFile);
        } catch (IOException e1) {
          System.out.println("File not found exception: " + paramFile);
          System.out.println(e1);
        } catch (SAXException e2) {
          System.out.println("Error parsing file: " + paramFile);
          System.out.println(e2);
        }
    }
}
