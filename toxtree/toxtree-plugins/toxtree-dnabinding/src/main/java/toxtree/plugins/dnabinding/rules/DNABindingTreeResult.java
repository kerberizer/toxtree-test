/*
Copyright (C) 2005-2006  

Contact: nina@acad.bg

This program is free software; you can redistribute it and/or
modify it under the terms of the GNU Lesser General Public License
as published by the Free Software Foundation; either version 2.1
of the License, or (at your option) any later version.
All we ask is that proper credit is given for our work, which includes
- but is not limited to - adding the above copyright notice to the beginning
of your source code files, and to any copyright notice that you may distribute
with programs based on this work.

This program is distributed in the hope that it will be useful,
but WITHOUT ANY WARRANTY; without even the implied warranty of
MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
GNU Lesser General Public License for more details.

You should have received a copy of the GNU Lesser General Public License
along with this program; if not, write to the Free Software
Foundation, Inc., 59 Temple Place - Suite 330, Boston, MA  02111-1307, USA
*/

package toxtree.plugins.dnabinding.rules;

import java.util.ArrayList;
import java.util.Hashtable;
import java.util.List;
import java.util.logging.Level;

import org.openscience.cdk.interfaces.IAtomContainer;

import toxTree.core.IDecisionCategories;
import toxTree.core.IDecisionRule;
import toxTree.data.CategoryFilter;
import toxTree.exceptions.DMethodNotAssigned;
import toxTree.exceptions.DecisionResultException;
import toxTree.tree.DecisionNode;
import toxTree.tree.ProgressStatus;
import toxTree.tree.RuleResult;
import toxTree.tree.TreeResult;
import toxTree.tree.rules.IAlertCounter;

public class DNABindingTreeResult extends TreeResult {
    protected static String SUFFIX = "SUFFIX";

	/**
	 * 
	 */
	private static final long serialVersionUID = -1505391697761215610L;

	
	public void assignResult(IAtomContainer mol) throws DecisionResultException {
		if (mol == null) return;
		IDecisionCategories c = decisionMethod.getCategories();
		for (int i=0; i < c.size();i++) {
			String result = Answers.toString(Answers.NO);
			if (getAssignedCategories().indexOf(c.get(i))>-1)
				result = Answers.toString(Answers.YES);
			mol.setProperty(
	        		c.get(i).toString(),
	                result);
		}
		/*
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
        	*/
        firePropertyChangeEvent(ProgressStatus._pRuleResult, null, status);        

	}
	public void addRuleResult(IDecisionRule rule, boolean value, IAtomContainer molecule)
	throws DecisionResultException {
			super.addRuleResult(rule, value,molecule);
			if (rule instanceof RuleDNABindingAlerts)
				setSilent(true);

			else setSilent((rule instanceof DecisionNode) &&	
					(
				(((DecisionNode)rule).getRule() instanceof RuleDNABindingAlerts) 


				)
				);
				
					
	}

	@Override
	public String[] getResultPropertyNames() {
		IDecisionCategories c = decisionMethod.getCategories();
		String[] names = new String[c.size()];
		for (int i=0; i < c.size();i++) 
			names[i] = c.get(i).toString();
		return names;
	}
	
	public Hashtable<String,String> getExplanation(IAtomContainer mol) throws DecisionResultException {
		Hashtable<String,String> b = new  Hashtable<String,String>();

        IAtomContainer originalMol = getOriginalMolecule();
		try {
		    if (status.isEstimated()) {
		    	for (int i=0;i < ruleResults.size();i++) {
		    		RuleResult r = ((RuleResult)ruleResults.get(i));
		    		if (r.isSilent()) continue;

		    		if ((r.getCategory() == null) 
		    			|| (r.getRule() instanceof IAlertCounter)
		    			|| (
		    			(r.getRule() instanceof DecisionNode) && (((DecisionNode)r.getRule()).getRule() instanceof IAlertCounter)
		    			)
		    			) { //not a leaf node or an alert
		    			if (r.isResult())
		    				b.put(r.getRule().getID(),Answers.toString(Answers.YES));
		    			else 
		    				b.put(r.getRule().getID(),Answers.toString(Answers.NO));
		    		}
		    		//Use this to get descriptor values 

		    	}
		    	
		    } else if (status.isEstimating()) {
		        b.put(ProgressStatus._eResultIsEstimating,"YES");
		        
		    }   else if (status.isError()) {
		        b.put(ProgressStatus._eError,status.getErrMessage());
		    }

		//} catch (DecisionMethodException x) {
			//throw new DecisionResultException(x);
		} catch (NullPointerException x) {
			throw new DMethodNotAssigned(ProgressStatus._eMethodNotAssigned);
		}
		return b;
	}
	
    public List<CategoryFilter> getFilters() {

    	ArrayList<CategoryFilter> l = new ArrayList<CategoryFilter>();
    	IDecisionCategories c = getDecisionMethod().getCategories();
		for (int i=0; i < c.size();i++) 
		try { 
    		l.add(new CategoryFilter(c.get(i).toString(),Answers.toString(Answers.YES)));
    		l.add(new CategoryFilter(c.get(i).toString(),Answers.toString(Answers.NO)));
    	} catch (Exception x) {
    		logger.log(Level.SEVERE,x.getMessage(),x);
    	}
    	return l;
    }
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