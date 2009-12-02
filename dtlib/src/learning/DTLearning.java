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
 * @author pdinniss
 *
 */

public class DTLearning {
	J48 tree;
	DTWekaARFF arff;	
	
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
		Instances data;
		double result = 0;
		try {
			data = new Instances(new StringReader(BuildTest(nonCatTest)));
			if (data.classIndex() == -1)
				data.setClassIndex(data.numAttributes() - 1);
			
			Instance testInst = data.firstInstance();
			testInst.setClassMissing();
			result = tree.classifyInstance(testInst);
			retVal = this.arff.attributes.get(this.arff.attributes.size()-1).type.split("[,]")[(int)result];
		} catch (Exception e) {			
			e.printStackTrace();
		}
		
		return retVal;
	}
	
	public void Visualize()
	{		
		try {
			final javax.swing.JFrame jf = 
				new javax.swing.JFrame("Weka Classifier Tree Viusualizer");
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
