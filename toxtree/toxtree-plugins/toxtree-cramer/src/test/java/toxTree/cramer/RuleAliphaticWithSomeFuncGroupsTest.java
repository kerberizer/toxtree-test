/*
Copyright Ideaconsult Ltd. (C) 2005-2007 

Contact: nina@acad.bg

This program is free software; you can redistribute it and/or
modify it under the terms of the GNU General Public License
as published by the Free Software Foundation; either version 2
of the License, or (at your option) any later version.
All we ask is that proper credit is given for our work, which includes
- but is not limited to - adding the above copyright notice to the beginning
of your source code files, and to any copyright notice that you may distribute
with programs based on this work.

This program is distributed in the hope that it will be useful,
but WITHOUT ANY WARRANTY; without even the implied warranty of
MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
GNU General Public License for more details.

You should have received a copy of the GNU General Public License
along with this program; if not, write to the Free Software
Foundation, Inc., 51 Franklin Street, Fifth Floor, Boston, MA  02110-1301, USA.

*/
package toxTree.cramer;

import org.openscience.cdk.interfaces.IMolecule;
import org.openscience.cdk.isomorphism.UniversalIsomorphismTester;

import toxTree.core.IDecisionRule;
import toxTree.query.FunctionalGroups;
import toxTree.query.MolAnalyser;
import toxTree.tree.cramer.RuleAliphaticWithSomeFuncGroups;

/**
 * Test for {@link RuleAliphaticWithSomeFuncGroups} rule.
 * @author Nina Jeliazkova
 * <b>Modified</b> 2005-8-19
 */
public class RuleAliphaticWithSomeFuncGroupsTest extends AbstractRuleTest {

	public void test() throws Exception  {
	    Object[][] answer = {
	            {"CC(=O)CCC(C)=O",new Boolean(false)}, //20N
	            {"CSCCCNCS",new Boolean(false)}, //20N
	            {"O1C(C)=CC(=O)C(C(=O)C)C(=O)1",new Boolean(false)}, //20N
	            {"CC(=O)C(O)=O",new Boolean(true)},  //20Y
	            {"C=C(C(=O)O)C(=O)O",new Boolean(true)},  //20Y
	            {"CCCC(=O)SC",new Boolean(true)},  //20Y
	            {"CCOCCOCCOCCCCCCCCCCCC",new Boolean(true)},  //SLTS unsulphated"
	            };
        
	   ruleTest(answer);
	    
	}
	/**
	 * This is an example from Munro paper, it fails for some reason.
	 * @throws Exception
	 */
	public void testMunroExample() throws Exception  {
	    Object[][] answer = {
				{"CN(C)C(=S)SCSC(=S)N(C)C",new Boolean(true)}  //fails (Munro )
	            };
        
	   ruleTest(answer);
	    
	}	
	@Override
	protected IDecisionRule createRule() {
		return new RuleAliphaticWithSomeFuncGroups();
	}
	
	public void testBugOverlapingGroups() throws Exception {
	    //two smiles of the same compound
	    Object[][] answer = {
	            {"COCCOC(=O)C=C",new Boolean(true)}, 
	            {"C=CC(=O)OCCOC",new Boolean(true)} 
	            
	            };
	    //1N,2N,3N,5N,6N,7N,16N,17N,19Y,20Y,21N,18N        
	    IMolecule mol1 = (IMolecule) FunctionalGroups.createAtomContainer((String)answer[0][0]);
	    IMolecule mol2 = (IMolecule) FunctionalGroups.createAtomContainer((String)answer[1][0]);
     	    assertTrue(UniversalIsomorphismTester.isIsomorph(mol1,mol2));
	            	
          	MolAnalyser.analyse(mol1);
          	MolAnalyser.analyse(mol2);
           assertTrue(rule2test.verifyRule(mol1));

           assertTrue(rule2test.verifyRule(mol2));

	    
	}
	
		
	

}
