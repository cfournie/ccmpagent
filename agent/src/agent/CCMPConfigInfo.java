package agent;

public class CCMPConfigInfo
{
	private boolean mLoggingEnabled;
	private String  mDecisionTrees;
	
    public CCMPConfigInfo()
    {
    	mLoggingEnabled = true;
    }

    public boolean getLogging()
    {
        return mLoggingEnabled;
    }

    public void setLogging(String _passParam)
    {
    	mLoggingEnabled = Boolean.valueOf(_passParam).booleanValue();
    }
    
    public String getDecisionTrees()
    {
        return mDecisionTrees;
    }

    public void setDecisionTrees(String _passParam)
    {
    	StringBuffer temp = new StringBuffer();
    	temp.append("<?xml version=\"1.0\"?>\n\n<DecisionTrees>\n");
    	temp.append(_passParam);
    	temp.append("\n</DecisionTrees");
    	mDecisionTrees = temp.toString();
    }    
}
