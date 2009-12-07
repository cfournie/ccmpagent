package learning;

import java.util.Vector;

/**
 * Vector extension to store DTLearning
 * 
 * @author Pierre Dinnissen
 */
public class DTLearningCollection extends Vector<DTLearning>{
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	/**
	 * Default Constructor
	 */
	public DTLearningCollection() {
		super();
	}
	
	/**
	 * XML Digester callback to add an element
	 * @param DTWekaARFF rhs is a parsed ARFF representation
	 */
	public void addDT(DTWekaARFF rhs)
	{
		this.addElement(new DTLearning(rhs));
	}
}
