/**
 * 
 */
package learning;

import weka.core.Instance;
import weka.core.Instances;
import weka.classifiers.trees.J48;
import weka.core.converters.ConverterUtils.DataSource;

/**
 * @author pdinniss
 *
 */
public class DTLearning implements DTLearningInterface {
	J48 tree;
	int nonCatCount;
	
	public DTLearning(String Data) throws Exception
	{
		DataSource source = new DataSource(Data);
		Instances data = source.getDataSet();
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
	}
	
	public String DTClassify(String nonCatTest) throws Exception
	{
		DataSource source = new DataSource(nonCatTest);
		Instance test = new Instance(nonCatCount);
		test.setDataset(source.getDataSet());
		
		tree.classifyInstance(test);
		
		return "aweomse";
	}

}
