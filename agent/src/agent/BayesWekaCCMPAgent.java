package agent;
import java.io.IOException;

import learning.DTAttribute;
import learning.DTLearningCollection;
import learning.DTWekaARFF;

import org.apache.commons.digester.Digester;
import org.xml.sax.SAXException;

import agent.CCMPAgent;

import agent.decision.WekaDT;
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
	private DTLearningCollection parsedTrees;

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
	
	WekaDT createDecisionTree()
    {
    	return new WekaDT(this, parsedTrees);
    }
    
    TrustNetwork createTrustNetwork()
    {
    	return new SimpleTrust(this);
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
