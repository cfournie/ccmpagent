package agent;

public class CCMPConfigInfo
{
	private boolean mLoggingEnabled;
	
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
}
