package michaelacceptors;
import java.beans.PropertyChangeListener;
import java.beans.PropertyChangeSupport;
import java.util.Observable;

import javax.swing.JComponent;
import javax.swing.JOptionPane;



import org.openscience.cdk.interfaces.IAtomContainer;

import toxTree.core.IDecisionInteractive;
import toxTree.core.IDecisionResult;
import toxTree.exceptions.DecisionMethodException;
import toxTree.exceptions.DecisionResultException;
import toxTree.tree.CategoriesList;
import toxTree.tree.DecisionNodesFactory;
import toxTree.tree.UserDefinedTree;

public class MichaelAcceptorRules extends UserDefinedTree implements IDecisionInteractive{
	private static final long serialVersionUID = 0;
    protected boolean residuesIDVisible;
    public final static transient String[] c_rules = { 
    	 "michaelacceptors.rules.Rule1", // Rule  1         
         "michaelacceptors.rules.Rule2", //2
         "michaelacceptors.rules.Rule3", //3
         "michaelacceptors.rules.Rule4A", //4A
         "michaelacceptors.rules.Rule4B", //4B
         "michaelacceptors.rules.Rule5", //5
         "michaelacceptors.rules.Rule6", //6
         "michaelacceptors.rules.Rule7", //7
         "michaelacceptors.rules.Rule8", //8
         "michaelacceptors.rules.Rule9A", //9A
         "michaelacceptors.rules.Rule9B", //9B
         "michaelacceptors.rules.Rule10A", //10A
         "michaelacceptors.rules.Rule10B", //10B
         "michaelacceptors.rules.Rule11", //11
         "michaelacceptors.rules.Rule12A", //12A
         "michaelacceptors.rules.Rule12B", //12B
         "michaelacceptors.rules.Rule13A", //13A
         "michaelacceptors.rules.Rule13B", //13B
         "michaelacceptors.rules.Rule14", //14       
        };
    private final static transient int c_transitions[][] ={
        //{if no go to, if yes go to, assign if no, assign if yes}
        {2,2,0,1}, //Rule1 1  
        {3,3,0,1}, //Rule2 2
        {4,4,0,1}, //Rule3 3
        {5,5,0,1}, //Rule4A  4
        {6,6,0,1}, //Rule4B 5
        {7,7,0,1}, //Rule5 6
        {8,8,0,1}, //Rule6 7
        {9,9,0,1}, //Rule7 8
        {10,10,0,1}, //Rule8 9
        {11,11,0,1}, //Rule9A 10
        {12,12,0,1}, //Rule9B 11
        {13,13,0,1}, //Rule10A 12
        {14,14,0,1}, //Rule10B 13 
        {15,15,0,1}, //Rule11 14
        {16,16,0,1}, //Rule12A 15
        {17,17,0,1}, //Rule12B 16 
        {18,18,0,1}, //Rule13A 17
        {19,19,0,1}, //Rule13B 18
        {0,0,2,1}, //Rule14 19        
    };	
    private final static transient String c_categories[] ={
		"michaelacceptors.categories.CategoryMichaelAcceptor", //1
		"michaelacceptors.categories.CategoryUnknown" //2
		
	};
    public MichaelAcceptorRules() throws DecisionMethodException {
		
		
        
    	super(new CategoriesList(c_categories,true),c_rules,c_transitions,new DecisionNodesFactory(true));
		
		setChanged();
		notifyObservers();
		/*
		if (changes != null ) {
			changes.firePropertyChange("Rules", rules,null);
			changes.firePropertyChange("Transitions", transitions,null);
		}
		*/
		setTitle("Michael Acceptors");
		setExplanation(
				"Verification of the Structural Alerts for Michael Acceptors. <p>T. Wayne Schultz, Jason W. Yarbrough, Robert S. Hunter, Aynur O. Aptula (2007) Verification of the Structural Alerts for Michael Acceptors.Chem. Res. Toxicol. 20, 1359�1363<br>\n");
        setPriority(20);
        setFalseIfRuleNotImplemented(false);
        
	}
    public void addPropertyChangeListener(PropertyChangeListener l) {
        if (changes == null) 		changes = new PropertyChangeSupport(this);        
        changes.addPropertyChangeListener(l);
		for (int i=0; i < rules.size(); i++) 
			if (rules.getRule(i) != null)
				rules.getRule(i).addPropertyChangeListener(l);
    }
    /* (non-Javadoc)
     * @see toxTree.core.IDecisionMethod#removePropertyChangeListener(java.beans.PropertyChangeListener)
     */
    public void removePropertyChangeListener(PropertyChangeListener l) {
        if (changes == null) {
	        changes.removePropertyChangeListener(l);
	        for (int i=0; i < rules.size(); i++) 
	        	if (rules.getRule(i) != null)
	        		rules.getRule(i).removePropertyChangeListener(l);
        }	
    }	
    /* (non-Javadoc)
     * @see java.lang.Object#toString()
     */
    public String toString() {
        return getName();
    }
	


	/* (non-Javadoc)
     * @see toxTree.core.IDecisionMethod#getName()
     */
    public String getName() {
        return name;
    }
    /* (non-Javadoc)
     * @see toxTree.core.IDecisionMethod#setName(java.lang.String)
     */
    public void setName(String value) {
       name = value;

    }
	public StringBuffer explainRules(IDecisionResult result,boolean verbose) throws DecisionMethodException{
		try {
			StringBuffer b = result.explain(verbose);
			return b;
		} catch (DecisionResultException x) {
			throw new DecisionMethodException(x);
		}
	}
	/* (non-Javadoc)
	 * @see toxTree.tree.AbstractTree#createDecisionResult()
	 */
	/*public IDecisionResult createDecisionResult() {
		IDecisionResult result =  new CramerTreeResult();
		result.setDecisionMethod(this);
		return result;

	}*/


	public boolean isResiduesIDVisible() {
		return residuesIDVisible;
	}


	public void setResiduesIDVisible(boolean residuesIDVisible) {
		this.residuesIDVisible = residuesIDVisible;
		for (int i=0;i< rules.size(); i++) {
			rules.getRule(i).hideResiduesID(!residuesIDVisible);
		}
	}
	public void setEditable(boolean value) {
		editable = value;
		for (int i=0;i<rules.size();i++)
			rules.getRule(i).setEditable(value);
	}
    @Override
    public void setParameters(IAtomContainer mol) {
        if (interactive) {
            JComponent c = optionsPanel(mol);
            if (c != null)
                JOptionPane.showMessageDialog(null,c,"Enter properties",JOptionPane.PLAIN_MESSAGE);
        }    
        
    }


    public boolean getInteractive() {
        return interactive;
    }




    public void setInteractive(boolean value) {
        interactive=value;
        
    }

}
