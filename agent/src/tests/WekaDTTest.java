package tests;

import learning.*;

public class WekaDTTest {

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		DTAttribute[] nonCatAttributes = {new DTAttribute("strategy", "{NICE,REFLEX}"),
										  new DTAttribute("msgremaining", "numeric"),
										  new DTAttribute("certainty", "numeric"),
				                          new DTAttribute("trust", "numeric")
		};
		
		DTAttribute[] catAttributes = {new DTAttribute("adjustAppraisal", "{UNCHANGED,INFLATEx2,INFLATEx10}")
									   
		};
		
		// Adjust Appraisal
		DTAttribute[] adjApp = {nonCatAttributes[2], nonCatAttributes[3], catAttributes[0]};
		
		String[] adjAppData = { "0.3,0.7,UNCHANGED",
								"0.3,0.5,INFLATEx2",
								"0.3,0.3,INFLATEx10",
								"0.5,0.3,INFLATEx10",
								"0.5,0.5,INFLATEx2",
								"0.5,0.7,UNCHANGED",
								"0.7,0.3,INFLATEx10",
								"0.7,0.5,INFLATEx2",
								"0.7,0.7,UNCHANGED"
				
		};
		
		try{
			DTLearning adjAppDT = new DTLearning(new DTWekaARFF(adjApp, adjAppData));
			adjAppDT.Visualize();
		} catch (Exception e) {
			e.printStackTrace();
		}

	}

}
