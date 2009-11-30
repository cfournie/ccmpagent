package tests;

import agent.decision.WekaDT.DTLearningNames;
import learning.*;

public class WekaDTTest {
	
	public enum ENonCatAtt {
		STRATEGY,
		MSGREM,
		CERTAINTY,
		TRUST
	}

	/**
	 * @param args
	 */
	public static void main(String[] args) {		
		DTAttribute[] nonCatAtt = {new DTAttribute("strategy", "{NICE,REFLEX}"),
										  new DTAttribute("msgrem", "numeric"),
										  new DTAttribute("certainty", "numeric"),
				                          new DTAttribute("trust", "numeric")
		};
		
		DTAttribute[] catAtt = {new DTAttribute("adjustAppraisal", "{UNCHANGED,INFLATEx2,INFLATEx10}"),
								new DTAttribute("generateOpinion", "{DO,DONT}"),
								new DTAttribute("appraisalCost", "{MINIMAL,MODERATE,BEST}"),
								new DTAttribute("getCertainty", "{TRUTH,LIE}"),
								new DTAttribute("getReputation", "{TRUTH,LIE}"),
								new DTAttribute("provideCertainty", "{DO,DONT}"),
								new DTAttribute("provideOpinion", "{DO,DONT}"),
								new DTAttribute("provideReputation", "{DO,DONT}"),
								new DTAttribute("requestCertainty", "{DO,DONT}"),
								new DTAttribute("requestOpinion", "{DO,DONT}"),
								new DTAttribute("requestReputation", "{DO,DONT}"),
								new DTAttribute("respondCertainty", "{DO,DONT}"),
								new DTAttribute("respondReputation", "{DO,DONT}"),
								new DTAttribute("provideWeight", "{DO,DONT}")
									   
		};
		
		// Adjust Appraisal
		DTAttribute[] adjApp = {nonCatAtt[ENonCatAtt.CERTAINTY.ordinal()], 
								nonCatAtt[ENonCatAtt.TRUST.ordinal()],
								catAtt[DTLearningNames.DT_ADJUSTAPPRAISAL.ordinal()]};
		
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
		
		// Generate Opinion
		DTAttribute[] genOpi = {nonCatAtt[ENonCatAtt.MSGREM.ordinal()], 
								nonCatAtt[ENonCatAtt.CERTAINTY.ordinal()],
								nonCatAtt[ENonCatAtt.TRUST.ordinal()], 
								catAtt[1]};
		
		String[] genOpiData = { "10,0.7,0.7,DO",
								"10,0.7,0.5,DO",
								"10,0.7,0.3,DONT",
								"10,0.5,0.7,DO",
								"10,0.5,0.5,DO",
								"10,0.5,0.3,DONT",
								"10,0.3,0.7,DONT",
								"3,0.7,0.7,DO",
								"3,0.7,0.5,DONT",
								"3,0.5,0.7,DONT",
								"3,0.3,0.3,DONT"
				
		};
		
		try{
			DTLearning genOpiDt = new DTLearning(new DTWekaARFF(genOpi, genOpiData));
			genOpiDt.Visualize();
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		// Get Appraisal Cost
		DTAttribute[] getApp = {nonCatAtt[ENonCatAtt.TRUST.ordinal()], 
								catAtt[DTLearningNames.DT_GETAPPRAISAL.ordinal()]};
		
		String[] genAppData = { "0.7,BEST",
								"0.8,BEST",
								"0.5,MODERATE",
								"0.6,MODERATE",
								"0.3,MINIMAL",
								"0.25,MINIMAL"
				
		};
		
		try{
			DTLearning genAppDT = new DTLearning(new DTWekaARFF(getApp, genAppData));
			genAppDT.Visualize();
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		// Get Certainty Request Value
		DTAttribute[] getCer = {nonCatAtt[ENonCatAtt.TRUST.ordinal()],
								catAtt[DTLearningNames.DT_GETCERTAINTY.ordinal()]
		};
		
		String[] getCerData = { "0.7,TRUTH",
								"0.3,LIE",
								"0.4,LIE",
								"0.5,TRUTH"
		};
		
		try{
			DTLearning getCerDT = new DTLearning(new DTWekaARFF(getCer, getCerData));
			getCerDT.Visualize();
		} catch (Exception e) {
			e.printStackTrace();
		}

	}

}
