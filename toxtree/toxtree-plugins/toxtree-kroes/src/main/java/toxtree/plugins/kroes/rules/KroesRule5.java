package toxtree.plugins.kroes.rules;

import org.openscience.cdk.interfaces.IAtomContainer;

import toxTree.exceptions.DecisionMethodException;
import toxTree.tree.AbstractRule;

/**
 * Does estimated intake exceed TTC of 1.5 mcg/day ?
 * @author nina
 *
 */
public class KroesRule5 extends RuleVerifyIntake {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = -553860642437002004L;
	public KroesRule5() {
		setID("Q5");
		setTitle("Does estimated intake exceed TTC of 1.5 \u00B5g/day ?");
		setExplanation(getTitle());
		propertyStaticValue = 1.5;
	}



}
