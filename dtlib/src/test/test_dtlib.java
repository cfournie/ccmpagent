package test;

import learning.*;

public class test_dtlib {

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		DTAttribute[] myAttributes = {new DTAttribute("age","numeric"),
								      new DTAttribute("sex","{MALE,FEMALE}"),
								      new DTAttribute("region", "{RURAL,INNER_CITY,TOWN,SUBURBAN}")};
		String[] myData = { "48,FEMALE,INNER_CITY",
							"40,MALE,TOWN",
							"51,FEMALE,INNER_CITY",
							"23,FEMALE,TOWN",
							"57,FEMALE,RURAL",
							"57,FEMALE,TOWN",
							"22,MALE,RURAL",
							"58,MALE,TOWN",
							"37,FEMALE,SUBURBAN",
							"54,MALE,TOWN",
							"66,FEMALE,TOWN",
							"52,FEMALE,INNER_CITY",
							"44,FEMALE,TOWN",
							"66,FEMALE,TOWN",
							"36,MALE,RURAL",
							"38,FEMALE,INNER_CITY" };
							
		
		DTWekaARFF myARFF = new DTWekaARFF(myAttributes,myData);
		
		String myTest = "48,FEMALE,?";
			
		try {
			DTLearning myDT = new DTLearning(myARFF);
			System.out.println(myDT.DTClassify(myTest));
		} catch (Exception e) {			
			e.printStackTrace();
		}

	}

}
