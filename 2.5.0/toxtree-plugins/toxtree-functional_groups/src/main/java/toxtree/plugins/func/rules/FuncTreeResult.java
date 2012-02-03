
package toxtree.plugins.func.rules;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.Hashtable;

import org.openscience.cdk.interfaces.IAtomContainer;

import toxTree.core.IDecisionRule;
import toxTree.exceptions.DMethodNotAssigned;
import toxTree.exceptions.DecisionResultException;
import toxTree.tree.DecisionNode;
import toxTree.tree.ProgressStatus;
import toxTree.tree.RuleResult;
import toxTree.tree.TreeResult;

public class FuncTreeResult extends TreeResult {
	
	private static final long serialVersionUID = -1505391697761215610L;

	public void assignResult(IAtomContainer mol) throws DecisionResultException {
		if (mol == null) return;
    	if (getCategory()!=null) 
            mol.setProperty(
            		getResultPropertyNames()[0],
                    getCategory().toString());
            

        String paths = getClass().getName()+"#explanation";
        if (getDecisionMethod().getRules().size() > 1) {
	        mol.setProperty(
	        		paths,
	                explain(false).toString());
	        Hashtable<String,String> b = getExplanation(mol);
	        Enumeration<String> k = b.keys();
	        while (k.hasMoreElements()) {
	        	String key = k.nextElement();
	        	mol.setProperty(key,b.get(key));
	        }
        } else
        	mol.removeProperty(paths);
        firePropertyChangeEvent(ProgressStatus._pRuleResult, null, status);        

	}
	@Override
	public String[] getResultPropertyNames() {
		return new String[] {"ISSFUNC"};
	}
	protected ArrayList<IAtomContainer> getAllAssignedMolecules() {
        ArrayList<IAtomContainer> residues = new ArrayList<IAtomContainer>();
        return residues;
        
    }

	public Hashtable<String,String> getExplanation(IAtomContainer mol) throws DecisionResultException {
		Hashtable<String,String> b = new  Hashtable<String,String>();
        ArrayList<IAtomContainer> residues = getAllAssignedMolecules();
        try {
		    if (status.isEstimated()) {
		    	for (int i=0;i < ruleResults.size();i++) {
		    		RuleResult r = ((RuleResult)ruleResults.get(i));
		    		if (r.isSilent()) continue;

		    		if (r.getCategory() == null) { //not a leaf node
		    			if (r.isResult())
		    				b.put(r.getRule().getID(),Answers.toString(Answers.YES));
		    			else 
		    				b.put(r.getRule().getID(),Answers.toString(Answers.NO));
		    		}				
                                    }
                               
    		    					
    		    			}
                            
            residues.clear();
		
		} catch (NullPointerException x) {
			throw new DMethodNotAssigned(ProgressStatus._eMethodNotAssigned);
		}
		return b;
	}
	
	public void addRuleResult(IDecisionRule rule, boolean value, IAtomContainer molecule)
	throws DecisionResultException {
			super.addRuleResult(rule, value,molecule);
			if (rule instanceof RuleAlertsForFunc)
				setSilent(true);
			else setSilent((rule instanceof DecisionNode) &&	
					(
				(((DecisionNode)rule).getRule() instanceof RuleAlertsForFunc) 
				

				)
				);
				
					
	}
	/*
	@Override
   public List<CategoryFilter> getFilters() {
    	ArrayList<CategoryFilter> l = new ArrayList<CategoryFilter>();
    	IDecisionCategories c = getDecisionMethod().getCategories();
		for (int i=0; i < c.size();i++) 
		try { 
    		l.add(new CategoryFilter(c.get(i).toString(),Answers.toString(Answers.YES)));
    		l.add(new CategoryFilter(c.get(i).toString(),Answers.toString(Answers.NO)));
    	} catch (Exception x) {
    		logger.error(x);
    	}
    	return l;
    }
    */
}



class Answers {
	public static int NO=0;
	public static int YES=1;
	protected Answers() {
		
	}
	public static String toString(int answer) {
		switch (answer) {
		case 0: return "NO";
		case 1: return "YES";
		default: return "";
		}
	}
}