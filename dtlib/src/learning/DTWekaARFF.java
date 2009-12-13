package learning;

import java.util.Arrays;
import java.util.Vector;

/**
 * Weka ARFF representation
 * 
 * @author Pierre Dinnissen
 */
public class DTWekaARFF {
	/** Vector of Attributes for this decision tree, the last is the categorical attribute **/
	public Vector<DTAttribute> attributes;
	/** String array of comma separated training data **/
	public String[] data;
	/** Name of this decision tree **/
	public String name;
	
	/**
	 * Default Constructor
	 */
	public DTWekaARFF() {
		attributes = new Vector<DTAttribute>();
	}
	
	/**
	 * Constructor
	 * @param attributes Array of attributes, will be converted into the Vector
	 * @param data String array of comma separated training date
	 */
	public DTWekaARFF(DTAttribute[] attributes, String[] data)
	{
		this.attributes = new Vector<DTAttribute>(Arrays.asList(attributes));
		this.data = data;
	}
	
	/**
	 * XML Digester callback to add an Attribute
	 * @param rhs DTAttribute rhs to be added
	 */
	public void addAttribute(DTAttribute rhs)
	{
		attributes.addElement(rhs);
	}
	
	/**
	 * XML Digester callback to store the training data
	 * @param rhs String rhs reprensenting all of the training data
	 */
	public void setData(String rhs)
	{
		data = rhs.replaceAll("\t","").split("\r\n|\r|\n");
	}
	
	/**
	 * XML Digester callback to set the name
	 * @param rhs String rhs for the name
	 */
	public void setName(String rhs)
	{
		name = rhs;
	}
	
	/**
	 * Helper function to converte DTWekaARFF classes into Weka ARFF acceptable text.
	 * @return converted String in proper ARFF text format
	 */
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
