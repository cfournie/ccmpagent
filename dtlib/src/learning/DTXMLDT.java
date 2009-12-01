package learning;

import java.util.Vector;

public class DTXMLDT {
	private Vector<DTXMLAttribute> attributes;
	private String[] data;
	
	public DTXMLDT() {
		attributes = new Vector<DTXMLAttribute>();
	}
	
	public void addAttribute(DTXMLAttribute rhs)
	{
		attributes.addElement(rhs);
	}
	
	public void setData(String rhs)
	{
		data = rhs.replaceAll("\t","").split("\r\n|\r|\n");
	}
	
	public DTWekaARFF toWekaARFF()
	{
		Vector<DTAttribute> temp = new Vector<DTAttribute>();
		
		for(DTXMLAttribute attribute : attributes)
		{
			temp.addElement(attribute.toWekaARFF());
		}
		
		return new DTWekaARFF((DTAttribute[]) temp.toArray(new DTAttribute[temp.size()]),data);
	}

}
