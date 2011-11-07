package toxtree.plugins.proteinbinding.rules;

import org.openscience.cdk.interfaces.IAtomContainer;
import org.openscience.cdk.renderer.selection.IChemObjectSelection;

import toxTree.tree.rules.RuleVerifyAlertsCounter;
import ambit2.base.interfaces.IProcessor;

public class VerifyAlertsProteinBinding extends RuleVerifyAlertsCounter {

	/**
	 * 
	 */
	private static final long serialVersionUID = -6848759397201273916L;
	public VerifyAlertsProteinBinding() {
		super();
		setID("6");
		setTitle("At least one alert for protin binding?");
	}
	@Override
	public boolean isImplemented() {
		return true;
	}
	@Override
	public IProcessor<IAtomContainer, IChemObjectSelection> getSelector() {
		return null;
	}
}