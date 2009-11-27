package learning;

public class DTWekaARFF {
	public DTAttribute[] attributes;
	public String[] data;
	
	public DTWekaARFF(DTAttribute[] attributes, String[] data)
	{
		this.attributes = attributes;
		this.data = data;
	}
	
	public String toString()
	{
		StringBuffer acc = new StringBuffer();
		
		acc.append("@relation training\n\n");
		
		for (DTAttribute attribute : this.attributes)
		{
			acc.append("@attribute "+attribute.name+" "+attribute.type+"\n");
		}
		acc.append("\n@data");
		
		for (String datum : this.data)
		{
			acc.append("\n"+datum);
		}
		
		return acc.toString();
	}
}
