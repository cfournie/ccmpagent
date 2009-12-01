package learning;

import java.util.Vector;

public class DTXML {
	private Vector<DTXMLDT> dts;
	
	public DTXML() {
		dts = new Vector<DTXMLDT>();
	}
	
	public void addDT(DTXMLDT rhs)
	{
		dts.addElement(rhs);
	}
	
	public DTWekaARFF[] toWekaARFF()
	{
		Vector<DTWekaARFF> temp = new Vector<DTWekaARFF>();
		
		for(DTXMLDT dt : dts)
		{		
			temp.addElement(dt.toWekaARFF());
		}
				
		return (DTWekaARFF[]) temp.toArray(new DTWekaARFF[temp.size()]);		
	}
}
