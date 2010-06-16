package mutant.test.rules;

import mutant.rules.SA31c;
import mutant.test.TestMutantRules;
import toxTree.core.IDecisionRule;

public class SA31cTest extends TestMutantRules {

	@Override
	protected IDecisionRule createRuleToTest() throws Exception {
		return new SA31c();
	}
	@Override
	public String getHitsFile() {
		return "NA31c/halobenzodiox.sdf";
	}
	@Override
	public String getResultsFolder() {
		return "NA31c";
	}
	
}