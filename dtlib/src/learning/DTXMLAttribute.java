package learning;

public class DTXMLAttribute {
	private String name;
	private String type;
	
	public DTXMLAttribute(){}
	
	public void setName(String rhs)
	{
		name = rhs;
	}
	
	public void setType(String rhs)
	{
		type = rhs;
	}
	
	public DTAttribute toWekaARFF()
	{
		return new DTAttribute(name,type);
	}
}
