package learning;

import java.util.Vector;

public class DTLearningCollection extends Vector<DTLearning>{
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	public DTLearningCollection() {
		super();
	}
	
	public void addDT(DTWekaARFF rhs)
	{
		this.addElement(new DTLearning(rhs));
	}
}
