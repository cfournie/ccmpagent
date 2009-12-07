/**
 * 
 */
package learning;

import java.awt.BorderLayout;
import java.io.StringReader;

import weka.core.Instance;
import weka.core.Instances;
import weka.classifiers.trees.J48;
import weka.gui.treevisualizer.*;

/**
 * Class that contructs the Weka J48 decision tree and Classifies Tests.
 * 
 * @author Pierre Dinnissen
 */

public class DTLearning {
	/** Weka J48 tree **/
	J48 tree;
	/** Instance of the ARFF file representation **/
	public DTWekaARFF arff;
	
	/**
	 * Constructor
	 * @param Data DTWekaARFF that the tree is to be built from.
	 */
	public DTLearning(DTWekaARFF Data)
	{
		Instances data;
		try {
			data = new Instances(new StringReader(Data.toString()));
			// setting class attribute if the data format does not provide this information
			// E.g., the XRFF format saves the class attribute information as well
			if (data.classIndex() == -1)
				data.setClassIndex(data.numAttributes() - 1);
			String[] options = new String[1];
			options[0] = "-U";            // unpruned tree
			tree = new J48();             // new instance of tree
			tree.setOptions(options);     // set the options
			tree.buildClassifier(data);   // build classifier
			this.arff = Data;
		} catch (Exception e) {
			tree = null;
			e.printStackTrace();
		}		
	}
	
	/**
	 * Helper function that will convert the test into a proper Weka ARFF format
	 * @param nonCatTest String nonCatTest is a comma separated test
	 */
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
	
	/**
	 * Classifies a test.
	 * @return Returns the action to be taken based on the given test
	 * @param nonCatTest String nonCatTest is a comma separated test
	 */
	public String DTClassify(String nonCatTest)
	{
		String retVal = null;	
		Instances data;
		double result = 0;
		try {
			data = new Instances(new StringReader(BuildTest(nonCatTest)));
			if (data.classIndex() == -1)
				data.setClassIndex(data.numAttributes() - 1);
			
			Instance testInst = data.firstInstance();
			testInst.setClassMissing();
			result = tree.classifyInstance(testInst);
			String catType = this.arff.attributes.get(this.arff.attributes.size()-1).type;
			retVal = catType.substring(1,catType.length()-1).split("[,]")[(int)result];
		} catch (Exception e) {			
			e.printStackTrace();
		}
		
		return retVal;
	}
	
	/**
	 * Opens a Java window and shows a visual representation of the built tree
	 */
	public void Visualize()
	{		
		try {
			final javax.swing.JFrame jf = 
				new javax.swing.JFrame("Weka Classifier Tree Visualizer - "+arff.name);
			jf.setSize(500,400);
			jf.getContentPane().setLayout(new BorderLayout());
			TreeVisualizer tv;
			tv = new TreeVisualizer(null, tree.graph(), new PlaceNode2());
			jf.getContentPane().add(tv, BorderLayout.CENTER);
			jf.addWindowListener(new java.awt.event.WindowAdapter() {
				public void windowClosing(java.awt.event.WindowEvent e) {
					jf.dispose();
				}
			});
			jf.setVisible(true);
			tv.fitToScreen();
		} catch (Exception e) {
			e.printStackTrace();
		}
		
	}

}
