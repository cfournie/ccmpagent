package agent;

/**
 * CCMP Agent configuration.
 */
public class CCMPConfigInfo
{
	private boolean mLoggingEnabled;
	public double trustDidNotProvideCertainty;
	public double trustDidNotProvideOpinion;
	public double trustDidNotProvideReputation;
	public boolean setTrustDidNotProvideCertainty;
	public boolean setTrustDidNotProvideOpinion;
	public boolean setTrustDidNotProvideReputation;
	
    public CCMPConfigInfo()
    {
    	mLoggingEnabled = true;
    	setTrustDidNotProvideCertainty = false;
    	setTrustDidNotProvideOpinion = false;
    	setTrustDidNotProvideReputation = false;
    }

    public boolean getLogging()
    {
        return mLoggingEnabled;
    }

    public void setLogging(String _passParam)
    {
    	mLoggingEnabled = Boolean.valueOf(_passParam).booleanValue();
    }
        
    public void setNotCertainty(String _passParam)
    {
    	trustDidNotProvideCertainty = Double.valueOf(_passParam).doubleValue();
    	setTrustDidNotProvideCertainty = true;
    }
    
    public void setNotOpinion(String _passParam)
    {
    	trustDidNotProvideOpinion = Double.valueOf(_passParam).doubleValue();
    	setTrustDidNotProvideOpinion = true;
    }
    
    public void setNotReputation(String _passParam)
    {
    	trustDidNotProvideReputation = Double.valueOf(_passParam).doubleValue();
    	setTrustDidNotProvideReputation = true;
    }
}
