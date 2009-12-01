package agent;

public class CCMPConfigInfo
{
	private boolean mLoggingEnabled;
	private String  mWekaARFFile;
	
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
    
    public String getWekaARFFile()
    {
        return mWekaARFFile;
    }

    public void setWekaARFFile(String _passParam)
    {
    	mWekaARFFile = _passParam;
    }    
}
