package michaelacceptors.rules;

import toxTree.tree.rules.smarts.RuleSMARTSubstructureCDK;
import toxTree.tree.rules.smarts.SMARTSException;
public class Rule1 extends RuleSMARTSubstructureCDK {
	private static final long serialVersionUID = 0;
	public Rule1() {
		super();		
		try {
			super.initSingleSMARTS(super.smartsPatterns,"1", "[c,C,O,N,S,P]C#CC=O");
			super.initSingleSMARTS(super.smartsPatterns,"2", "[CH]#CC=O");
			super.initSingleSMARTS(super.smartsPatterns,"3", "C#C[CH]=O");
			id = "1";
			title = "Acetylenic- and an internal carbonyl";
			
			examples[0] = "";
			examples[1] = "";	
			editable = false;		
		} catch (SMARTSException x) {
			logger.error(x);
		}

	}

}
