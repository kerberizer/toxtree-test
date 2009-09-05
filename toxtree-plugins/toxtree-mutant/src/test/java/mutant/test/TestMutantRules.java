/*
Copyright Ideaconsult Ltd. (C) 2005-2007  

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

package mutant.test;

import java.beans.XMLEncoder;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.Iterator;

import junit.framework.TestCase;

import org.openscience.cdk.CDKConstants;
import org.openscience.cdk.DefaultChemObjectBuilder;
import org.openscience.cdk.aromaticity.CDKHueckelAromaticityDetector;
import org.openscience.cdk.atomtype.CDKAtomTypeMatcher;
import org.openscience.cdk.exception.CDKException;
import org.openscience.cdk.interfaces.IAtom;
import org.openscience.cdk.interfaces.IAtomContainer;
import org.openscience.cdk.interfaces.IAtomType;
import org.openscience.cdk.interfaces.IMolecule;
import org.openscience.cdk.io.MDLWriter;
import org.openscience.cdk.io.iterator.IIteratingChemObjectReader;
import org.openscience.cdk.io.iterator.IteratingMDLReader;
import org.openscience.cdk.smiles.SmilesParser;
import org.openscience.cdk.smiles.smarts.SMARTSQueryTool;
import org.openscience.cdk.tools.manipulator.AtomTypeManipulator;

import toxTree.core.IDecisionMethod;
import toxTree.core.IDecisionRule;
import toxTree.exceptions.DecisionMethodException;
import toxTree.logging.TTLogger;
import toxTree.query.FunctionalGroups;
import toxTree.query.MolAnalyser;
import toxTree.tree.rules.smarts.AbstractRuleSmartSubstructure;
import toxTree.tree.rules.smarts.ISmartsPattern;
import toxTree.ui.tree.actions.NewRuleAction;

public abstract class TestMutantRules extends TestCase {
	protected static TTLogger logger = new TTLogger(TestMutantRules.class);
	protected IDecisionRule ruleToTest = null;
	
	public abstract String getHitsFile();
	public abstract String getResultsFolder();
	@Override
	protected void setUp() throws Exception {
		super.setUp();
		ruleToTest = createRuleToTest();
		TTLogger.configureLog4j(true);
	}
	@Override
	protected void tearDown() throws Exception {
		ruleToTest = null;
		super.tearDown();
	}
	protected abstract IDecisionRule createRuleToTest() throws Exception;
	
	
	protected String getTestFileName() {
	    return "iss2_rid.sdf";
    }
	protected void applyRule(IDecisionRule rule,  String resultsfile, String resultsFolder) throws Exception {
		applyRule(rule, getTestFileName(), resultsfile,resultsFolder);
	}
	protected void applyRule(IDecisionRule rule,  String sourcefile, String resultsfile, String resultsFolder) throws Exception {
		InputStream in_source = new FileInputStream("plugins/mutant/data/"+sourcefile);
		InputStream in_results = new FileInputStream("plugins/mutant/data/" + resultsfile);
		applyRule(rule, in_source,in_results,getSubstanceID() ,"plugins/mutant/data/"+resultsFolder);
		in_source.close();
		in_results.close();
	}	
    protected String getSubstanceID() {
        return "Substance ID";
    }
    protected String getRuleID(IDecisionRule rule) {
        return rule.getID();
    }
    protected IIteratingChemObjectReader getReader(InputStream stream, DefaultChemObjectBuilder b) {
    	return  new IteratingMDLReader(stream,b);
    }
	protected void applyRule(IDecisionRule rule, InputStream source, InputStream results, String compoundID, String resultsFolder) throws Exception {
		
		assertNotNull(source);
		assertNotNull(results);
		
		DefaultChemObjectBuilder b = DefaultChemObjectBuilder.getInstance();
		
		ArrayList<String> resultsID = new ArrayList<String>();
		ArrayList<String> missedResults = new ArrayList<String>();
		//IteratingMDLReader resultsReader = new IteratingMDLReader(results,b);
		IIteratingChemObjectReader resultsReader = getReader(results, b);
		
        String filename_missed_hits = resultsFolder+"/"+getRuleID(rule) + "_missed_hits.sdf"; 
        File f2 = new File(filename_missed_hits);
        if (f2.exists()) f2.delete();
		OutputStream outMissed = new FileOutputStream(f2);
		MDLWriter writerMissed = new MDLWriter(outMissed);
		
		while (resultsReader.hasNext()) {
			Object o = resultsReader.next();
			if (o instanceof IAtomContainer) {
				long now = System.currentTimeMillis();
				IAtomContainer a = (IAtomContainer) o;
				Object id = a.getProperty(compoundID);
				if (id != null) {
					resultsID.add(id.toString());
				
				}
				
				
				
				try {
					MolAnalyser.analyse(a);
					if (!rule.verifyRule(a)) {
						missedResults.add(id.toString());
						writerMissed.write(a);
					}

				} catch (Exception x) {
					x.printStackTrace();
//					fail(rule.toString() + " " + x.getMessage());
				}
				//System.out.println("Elapsed " + Long.toString(System.currentTimeMillis()-now) + " ms." );
			}
		}
		outMissed.flush();
		outMissed.close();
		writerMissed.close();
		
		resultsReader.close();
		assertTrue(resultsID.size()>0);
		
		if (missedResults.size() > 0) {
			System.out.print("Rule\t"+rule);
			System.out.println("\tMissed results\t"+missedResults);
		} else f2.delete();
		
		
		ArrayList<String> wronghits = new ArrayList<String>();
		ArrayList<String> missedhits = new ArrayList<String>();
		
		//IteratingMDLReader sourceReader = new IteratingMDLReader(source,b);
		
		IIteratingChemObjectReader sourceReader = getReader(source, b);
		
		File f = new File(resultsFolder+"/"+getRuleID(rule)+"_wrong_hits.sdf");
        if (f.exists()) f.delete();
		OutputStream outWrong = new FileOutputStream(f);
		MDLWriter writerWrong = new MDLWriter(outWrong);
		
		while (sourceReader.hasNext()) {
			Object o = sourceReader.next();
			if (o instanceof IMolecule) {
				IMolecule a = (IMolecule) o;
				Object id = a.getProperty(compoundID);
				if (id == null) id = "???";
				//System.out.println(id);
				try {
					MolAnalyser.analyse(a);
					if (rule.verifyRule(a)) {
						if (resultsID.indexOf(id.toString()) == -1) { 
							wronghits.add(id.toString());
							writerWrong.writeMolecule(a);
						}	
					} else {
						if (resultsID.indexOf(id.toString()) > -1) {
							missedhits.add(id.toString());
							//writerMissed.writeMolecule(a);
						}
					}
				} catch (Exception x) {
					//System.err.println(rule.toString() + " " + x.getMessage());
				}
			}
		
		}

		/*
		writerMissed.close();
		writerWrong.close();
		*/
		sourceReader.close();

		//outMissed.flush();
		//outMissed.close();
		outWrong.flush();
		outWrong.close();
		
		if (missedhits.size() > 0) {
			System.out.println("Rule " + rule.getID() + " missed hits\t"+missedhits.size());
			System.out.println(missedhits);
		}	
		if (wronghits.size() > 0) {
			System.out.println("Rule " + rule.getID() + " wrong hits\t"+wronghits.size());
			System.out.println(wronghits);
		} else f.delete();
		saveRuleAsTree(rule,resultsFolder+"/"+getRuleID(rule)+".tml");
		assertEquals(0,missedhits.size());
		assertEquals(0,wronghits.size());
		
				
	}
	public void test() {
		try {
			applyRule(ruleToTest, getHitsFile(),getResultsFolder());
		} catch (Exception x) {
			x.printStackTrace();
			fail(x.getMessage());
		}
	}
	
	protected void saveRuleAsTree(IDecisionRule rule,String filename) throws Exception {
		IDecisionMethod tree = NewRuleAction.treeFromRule(rule);
		FileOutputStream os = new FileOutputStream(filename);
		//Thread.currentThread().setContextClassLoader(Introspection.getLoader());
		XMLEncoder encoder = new XMLEncoder(os);
		encoder.writeObject(tree);
		encoder.close();	
	}
	
	public boolean applySmarts(String smarts,String smiles) throws Exception {
		IAtomContainer ac = toxTree.query.FunctionalGroups.createAtomContainer(smiles,true);
		return applySmarts(smarts, ac);
	}
	public boolean applySmarts(String smarts,IAtomContainer ac) throws Exception {
		toxTree.tree.rules.smarts.RuleSMARTSubstructure r = new toxTree.tree.rules.smarts.RuleSMARTSubstructure();
		r.addSubstructure(smarts);
		toxTree.query.MolAnalyser.analyse(ac);
		return r.verifyRule(ac);		
	}
	public boolean verifyRule(IDecisionRule rule,String smiles) throws Exception {

		IAtomContainer c = toxTree.query.FunctionalGroups.createAtomContainer(smiles);
		toxTree.query.MolAnalyser.analyse(c);
		return rule.verifyRule(c);
	}	
    public int verifySMARTS(AbstractRuleSmartSubstructure rule, String smarts,String smiles) throws Exception {
        IAtomContainer c = toxTree.query.FunctionalGroups.createAtomContainer(smiles);
        ISmartsPattern p = rule.createSmartsPattern(smarts,false);
        toxTree.query.MolAnalyser.analyse(c);
        return p.match(c);
    }   
	protected void verifyExample(boolean answer) throws DecisionMethodException {
		IMolecule m = ruleToTest.getExampleMolecule(answer);
		try {
			/*
			HydrogenAdder ha = new HydrogenAdder();
			ha.addExplicitHydrogensToSatisfyValency(m);
			*/
			MolAnalyser.analyse(m);
			for (int i=0; i < m.getAtomCount();i++)
				System.out.println(m.getAtom(i).getSymbol() + '\t'+ m.getAtom(i).getHydrogenCount());
		} catch (Exception x) {
			throw new DecisionMethodException(x);
		}
		assertEquals(answer,ruleToTest.verifyRule(m));
	}
	
	public void testExampleNo() throws DecisionMethodException {
		verifyExample(false);
	}
	public void testExampleYes()  throws DecisionMethodException {
		verifyExample(true);
	}
    public int printAromaticity() throws Exception {
    	IteratingMDLReader resultsReader = new IteratingMDLReader(
    			new FileInputStream("plugins/v 1.11/mutant/data/"+getHitsFile()),DefaultChemObjectBuilder.getInstance());
    	int aromaticCompounds = 0;
    	while (resultsReader.hasNext()) {
    		Object o = resultsReader.next();
    		IAtomContainer a = (IAtomContainer)o;
    		findAndConfigureAtomTypesForAllAtoms(a);
    		boolean ok = CDKHueckelAromaticityDetector.detectAromaticity(a);
    		if (ok) aromaticCompounds++;
    		int aromatic = 0;
    		for (int i=0; i < a.getAtomCount();i++) {
    			if (a.getAtom(i).getFlag(CDKConstants.ISAROMATIC))
    				aromatic ++;
    			//stem.out.print(a.getAtom(i).getHybridization());
    			//System.out.print('\t');
    		}
    		/*
    		System.out.print(a.getProperty("CAS_iss2"));
    		System.out.print('\t');
    		System.out.print("is aromatic\t");
    		System.out.print(ok);
    		System.out.print('\t');
    		System.out.print(aromatic);
    		System.out.println(" aromatic atoms");
    		*/
    		
    	}
    	return aromaticCompounds;
    }	
	private void findAndConfigureAtomTypesForAllAtoms(IAtomContainer container) throws CDKException {
    	CDKAtomTypeMatcher matcher = CDKAtomTypeMatcher.getInstance(container.getBuilder());
    	Iterator<IAtom> atoms = container.atoms();
    	while (atoms.hasNext()) {
    		IAtom atom = atoms.next();
    		IAtomType type = matcher.findMatchingAtomType(container, atom);
        	assertNotNull(type);
        	AtomTypeManipulator.configure(atom, type);
    	}
	}	
    public boolean verify(String smiles) throws Exception {
    		IAtomContainer no = FunctionalGroups.createAtomContainer(smiles);
            MolAnalyser.analyse(no);
            return ruleToTest.verifyRule(no);
    }
    protected int[] match(String smarts, String smiles) throws Exception {
        SMARTSQueryTool sqt = new SMARTSQueryTool(smarts, true);
        SmilesParser sp = new SmilesParser(DefaultChemObjectBuilder.getInstance());
        IAtomContainer atomContainer = sp.parseSmiles(smiles);
        MolAnalyser.analyse(atomContainer);
        boolean status = sqt.matches(atomContainer);
        if (status) {
            return new int[] {
              sqt.countMatches(),
              sqt.getUniqueMatchingAtoms().size()
            };
        } else {
            return new int[]{0,0};
        }
    }
    public void testRuleRoundTrip() throws Exception  {     
            //writing
            File f = File.createTempFile("rule"+ruleToTest.getNum(),"test");
            ObjectOutputStream os = new ObjectOutputStream(new FileOutputStream(f));
            os.writeObject(ruleToTest);
            os.close();
            
            //reading
            ObjectInputStream is = new ObjectInputStream(new FileInputStream(f));
            Object rule2 =is.readObject();
            is.close();
            f.delete();
            System.out.println(ruleToTest.toString());
            System.out.println("old");
            System.out.println(((AbstractRuleSmartSubstructure)ruleToTest).getImplementationDetails());
            System.out.println("new");
            System.out.println(((AbstractRuleSmartSubstructure)rule2).getImplementationDetails());
            assertEquals(ruleToTest,rule2);
    }   
        
}

