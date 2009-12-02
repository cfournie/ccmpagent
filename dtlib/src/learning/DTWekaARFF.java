package learning;

import java.util.Arrays;
import java.util.Vector;

public class DTWekaARFF {
	public Vector<DTAttribute> attributes;
	public String[] data;
	
	public DTWekaARFF() {
		attributes = new Vector<DTAttribute>();
	}
	
	public DTWekaARFF(DTAttribute[] attributes, String[] data)
	{
		this.attributes = new Vector<DTAttribute>(Arrays.asList(attributes));
		this.data = data;
	}
	
	public void addAttribute(DTAttribute rhs)
	{
		attributes.addElement(rhs);
	}
	
	public void setData(String rhs)
	{
		data = rhs.replaceAll("\t","").split("\r\n|\r|\n");
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
