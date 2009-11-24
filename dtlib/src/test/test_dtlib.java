package test;

import learning.DTLearning;

public class test_dtlib {

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		String arff = 
			"@relation bank\n\n" +
			"@attribute age numeric\n" +
			"@attribute sex {MALE,FEMALE}\n" +
			"@attribute region {INNER_CITY,RURAL,TOWN,SUBURBAN}\n\n" +
			"@data\n" +
			"48,FEMALE,INNER_CITY\n" +
			"40,MALE,TOWN\n" +
			"51,FEMALE,INNER_CITY\n" +
			"23,FEMALE,TOWN\n" +
			"57,FEMALE,RURAL\n" +
			"57,FEMALE,TOWN\n" +
			"22,MALE,RURAL\n" +
			"58,MALE,TOWN\n" +
			"37,FEMALE,SUBURBAN\n" +
			"54,MALE,TOWN\n" +
			"66,FEMALE,TOWN\n" +
			"52,FEMALE,INNER_CITY\n" +
			"44,FEMALE,TOWN\n" +
			"66,FEMALE,TOWN\n" +
			"36,MALE,RURAL\n" +
			"38,FEMALE,INNER_CITY";
		String test = 
			"@relation bank-test\n\n" +
			"@attribute age numeric\n" +
			"@attribute sex {MALE,FEMALE}\n" +
			"@attribute region {INNER_CITY,RURAL,TOWN,SUBURBAN}\n\n" +
			"@data\n" +
			"48,FEMALE,?";
		try {
			DTLearning myDT = new DTLearning(arff);
			myDT.DTClassify(test);
		} catch (Exception e) {			
			e.printStackTrace();
		}

	}

}
