/**
 * 
 */
package learning;

import java.io.StringReader;

import weka.core.Instance;
import weka.core.Instances;
import weka.classifiers.trees.J48;

/**
 * @author pdinniss
 *
 */

public class DTLearning {
	J48 tree;
	int nonCatCount;
	DTWekaARFF arff;
	
	public DTLearning(DTWekaARFF Data)
	{
        StringReader srData = new StringReader(Data.toString());		
		Instances data;
		try {
			data = new Instances(srData);
			// setting class attribute if the data format does not provide this information
			// E.g., the XRFF format saves the class attribute information as well
			if (data.classIndex() == -1)
				data.setClassIndex(data.numAttributes() - 1);
			String[] options = new String[1];
			options[0] = "-U";            // unpruned tree
			tree = new J48();             // new instance of tree
			tree.setOptions(options);     // set the options
			tree.buildClassifier(data);   // build classifier
			nonCatCount = data.numAttributes();
			this.arff = Data;
		} catch (Exception e) {
			tree = null;
			nonCatCount = 0;
			e.printStackTrace();
		}		
	}
	
	private String BuildTest(String nonCatTest)
	{
		StringBuffer acc = new StringBuffer();
		
		acc.append("@relation test\n\n");
		
		for (DTAttribute attribute : this.arff.attributes)
		{
			acc.append("@attribute "+attribute.name+" "+attribute.type+"\n");
		}
		acc.append("\n@data\n"+nonCatTest);
		
		return acc.toString();
	}
	
	public String DTClassify(String nonCatTest)
	{
		String retVal = null;
		StringReader srNonCatTest = new StringReader(BuildTest(nonCatTest));		
		Instances data;
		double result = 0;
		try {
			data = new Instances(srNonCatTest);
			if (data.classIndex() == -1)
				data.setClassIndex(data.numAttributes() - 1);
			
			Instance testInst = data.firstInstance();
			testInst.setClassMissing();
			result = tree.classifyInstance(testInst);
			retVal = this.arff.attributes[this.arff.attributes.length-1].type.split("[,]")[(int)result];
		} catch (Exception e) {			
			e.printStackTrace();
		}
		
		return retVal;
	}

}
