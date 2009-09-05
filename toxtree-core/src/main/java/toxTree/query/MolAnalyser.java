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

/**
 * <b>Filename</b> MolAnalyser.java 
 * @author Nina Jeliazkova nina@acad.bg
 * <b>Created</b> 2005-8-2
 * <b>Project</b> toxTree
 */
package toxTree.query;

import java.util.Hashtable;
import java.util.Iterator;

import org.openscience.cdk.CDKConstants;
import org.openscience.cdk.aromaticity.CDKHueckelAromaticityDetector;
import org.openscience.cdk.atomtype.CDKAtomTypeMatcher;
import org.openscience.cdk.config.Symbols;
import org.openscience.cdk.exception.CDKException;
import org.openscience.cdk.graph.ConnectivityChecker;
import org.openscience.cdk.interfaces.IAtom;
import org.openscience.cdk.interfaces.IAtomContainer;
import org.openscience.cdk.interfaces.IAtomType;
import org.openscience.cdk.interfaces.IBond;
import org.openscience.cdk.interfaces.IMolecule;
import org.openscience.cdk.interfaces.IMoleculeSet;
import org.openscience.cdk.interfaces.IRing;
import org.openscience.cdk.interfaces.IRingSet;
import org.openscience.cdk.ringsearch.AllRingsFinder;
import org.openscience.cdk.ringsearch.SSSRFinder;
import org.openscience.cdk.tools.CDKHydrogenAdder;
import org.openscience.cdk.tools.manipulator.AtomContainerManipulator;
import org.openscience.cdk.tools.manipulator.AtomTypeManipulator;

import toxTree.exceptions.MolAnalyseException;
import toxTree.logging.TTLogger;
import ambit2.base.config.Preferences;


/**
 * 
 * @author Nina Jeliazkova <br>
 * <b>Modified</b> 2005-8-2
 */
public class MolAnalyser {
	protected static TTLogger logger  = new TTLogger(MolAnalyser.class);
    //protected Molecule mol = null;
    //protected RingSet ringSet = null;
    protected static AllRingsFinder arf = new AllRingsFinder();
    protected static Hashtable<String,Integer> elements = null;
	public final static String HETEROCYCLIC = "Heterocyclic";
	public final static String HETEROCYCLIC3 = "3-membered heterocyclic";
	public final static String HETEROAROMATIC = "Heteroaromatic";
	
    /**
     * 
     */
    protected MolAnalyser() {
        super();
    }
    
    public static Hashtable<String,Integer> getElements() {
    	if (elements != null) return elements;
    	elements = new Hashtable<String, Integer>();
    	for (int i=0; i < Symbols.byAtomicNumber.length; i++)
    		elements.put(Symbols.byAtomicNumber[i], i);
    	elements.put("R", 0);
    	elements.put("*", 0);
    	return elements;
    	
    }
    public static void analyse(IAtomContainer mol) throws MolAnalyseException {
    	
    	if (mol==null) throw new MolAnalyseException("Null molecule!",mol);
    	if (mol.getAtomCount()==0) throw new MolAnalyseException("No atoms!",mol);
    	getElements();

        
    	try {
    		//New - 16 Jan 2008 - configure atom types
    		logger.debug("Configuring atom types ...");
	        CDKAtomTypeMatcher matcher = CDKAtomTypeMatcher.getInstance(mol.getBuilder());
	        Iterator<IAtom> atoms = mol.atoms();
	        while (atoms.hasNext()) {
	           IAtom atom = atoms.next();
	           IAtomType type = matcher.findMatchingAtomType(mol, atom);
	           try {
	        	   AtomTypeManipulator.configure(atom, type);
                   logger.debug("Found " + atom.getSymbol() + " of type " + type.getAtomTypeName());                   
	           } catch (Exception x) {
	        	   logger.error(x.getMessage() + " " + atom.getSymbol(),x);
                   
                   if ("true".equals(Preferences.getProperty(Preferences.STOP_AT_UNKNOWNATOMTYPES))) {
                       throw new MolAnalyseException(atom.getSymbol(),x);
                   }
                   
	           }
	        }    	        
	        //adding hydrogens
	        CDKHydrogenAdder h = CDKHydrogenAdder.getInstance(mol.getBuilder());
	        
        	if (mol instanceof IMolecule) {
                try {
    	            h.addImplicitHydrogens(mol);
    	            logger.debug("Adding implicit hydrogens; atom count "+mol.getAtomCount());
    	            AtomContainerManipulator.convertImplicitToExplicitHydrogens(mol);
    	            logger.debug("Convert explicit hydrogens; atom count "+mol.getAtomCount());
                } catch (Exception x) {
                    logger.error(x);
                    if ("true".equals(Preferences.getProperty(Preferences.STOP_AT_UNKNOWNATOMTYPES))) {
                        throw new MolAnalyseException(x);
                    }
                }
        	} else {
        		IMoleculeSet moleculeSet = ConnectivityChecker.partitionIntoMolecules(mol);
        	      
        	      for (int k = 0; k < moleculeSet.getMoleculeCount(); k++) {
        	    	  IMolecule molPart = moleculeSet.getMolecule(k);
      		          h.addImplicitHydrogens(molPart);
      		          logger.debug("Adding implicit hydrogens; atom count "+molPart.getAtomCount());
    		          AtomContainerManipulator.convertImplicitToExplicitHydrogens(molPart);
    		          logger.debug("Convert explicit hydrogens; atom count "+molPart.getAtomCount());    		          
        	      }
        	}
        	atoms = mol.atoms();
	        while (atoms.hasNext()) {
		           IAtom atom = atoms.next();
		           if (atom.getAtomicNumber() == 0) {
		        	   Integer no = elements.get(atom.getSymbol());
		        	   if (no != null)
		        		   atom.setAtomicNumber(no.intValue());
		           }	   
		        }            	
	        //logger.debug("MolAnalyser\t",mol.getID());
    		Object o = mol.getProperty(MolFlags.MOLFLAGS);
    		MolFlags mf = null;
    		if ((o!=null) && (o instanceof MolFlags))       	mf = (MolFlags)o;
    		else mf = null;
	        if (mf == null) mf = new MolFlags(); else mf.clear();
	        mol.setProperty(MolFlags.MOLFLAGS,mf);
	
	        IRingSet ringSet = null;
	        try {
	        	ringSet = arf.findAllRings(mol);
	        } catch (CDKException x) {
	        	//timeout on AllRingsFinder, will try SSSR
	        	logger.warn(x.getMessage());
	        	SSSRFinder ssrf = new SSSRFinder(mol);
	        	ringSet = ssrf.findEssentialRings();
	        }
	        int size = ((IRingSet) ringSet).getAtomContainerCount();
	        mf.setOpenChain(size == 0);
	        if (size == 0) {
	        	mf.setAliphatic(true);
		        for (int i=0; i < mol.getBondCount(); i++)
		            if ((mol.getBond(i)).getOrder() == CDKConstants.BONDORDER_TRIPLE) {
		                mf.setAliphatic(false);
		                mf.setAcetylenic(true);
		                break;
		            }
	        } else {
	        	mf.setRingset(ringSet);
	        	//HueckelAromaticityDetector.detectAromaticity(mol,ringSet,true);
	        	
	        	
		        if (CDKHueckelAromaticityDetector.detectAromaticity(mol)) 
		        	logger.debug("Aromatic\t","YES");
		        else logger.debug("Aromatic\t","NO");
		        
		        IRing ring = null;
		        IAtom a;
		        int aromaticRings = 0;
		        boolean heterocyclicRing, heteroCyclic = false, heteroCyclic3 = false;
		        //boolean heteroaromaticRing, 
		        boolean heteroAromatic = false;
		        for (int i =0; i < ringSet.getAtomContainerCount(); i++) {
		        	ring = (IRing)ringSet.getAtomContainer(i);
		        	heterocyclicRing = false;
		        	//boolean isaromatic = ring.getFlag(CDKConstants.ISAROMATIC);
		        	//if (isaromatic) logger.debug("Aromatic ring found");
		        	for (int j=0; j < ring.getBondCount(); j++) {
		        		IBond b = ring.getBond(j);
		                b.setFlag(CDKConstants.ISINRING,true);
		                //if (isaromatic)
		                	//b.setFlag(CDKConstants.ISAROMATIC,true);	        		
		        	}
		        	int aromatic_atoms = 0;
	          	    for (int j=0; j < ring.getAtomCount(); j++) {
		                 a = ring.getAtom(j);
		                 a.setFlag(CDKConstants.ISINRING,true);
		                 if (a.getFlag(CDKConstants.ISAROMATIC)) aromatic_atoms++;
		                 /*This flag should already be set
		                 if (isaromatic)
		                 	a.setFlag(CDKConstants.ISAROMATIC,true);
		                 	*/
		                 if (!a.getSymbol().equals("C")) {
		                     heterocyclicRing = true;
		                     heteroCyclic = true;	                     
		                 }
		            }
	          	    boolean isaromaticRing = (aromatic_atoms == ring.getAtomCount());
	          	    if (heterocyclicRing) {
	          	        ring.setProperty(HETEROCYCLIC,new Boolean(true));
	          	        if (ring.getRingSize() == 3) {
	          	        	heteroCyclic3 = true;
	              	        ring.setProperty(HETEROCYCLIC3,new Boolean(true));
	          	        }
	          	    } 
	          	    ring.setFlag(CDKConstants.ISAROMATIC, isaromaticRing);
		        	if (isaromaticRing) { 
		        		
		        	    aromaticRings++;
		        	    if (heterocyclicRing)  {
	              	        ring.setProperty(HETEROAROMATIC,new Boolean(true));
	              	        heteroAromatic = true;
		        	    }
		        	}
		        }
	   	        mf.setHeterocyclic(heteroCyclic);
	   	        mf.setHeterocyclic3(heteroCyclic3);
	   	        mf.setHeteroaromatic(heteroAromatic);
	   	        mf.setAromatic(aromaticRings > 0);
		        mf.setAromaticRings(aromaticRings);
		        mol.setFlag(CDKConstants.ISAROMATIC,aromaticRings>0);	        
	        }
	        /*
	        HydrogenAdder h = new HydrogenAdder();
	        try {
	        	if (mol instanceof Molecule)
	        		h.addExplicitHydrogensToSatisfyValency((Molecule)mol);
	        	else {
	        		IMoleculeSet moleculeSet = ConnectivityChecker.partitionIntoMolecules(mol);
	        	      
	        	      for (int k = 0; k < moleculeSet.getMoleculeCount(); k++) {
	        	    	  IMolecule molPart = moleculeSet.getMolecule(k);
	        	          for (int i = 0; i < molPart.getAtomCount(); i++) {
		        	          h.addExplicitHydrogensToSatisfyValency(molPart, molPart.getAtom(i), mol);
		        	      }
	        	      }
	        	}
	        	
	        } catch (IOException x) {
	            throw new MolAnalyseException(x);
	        } catch (ClassNotFoundException x) {
	            throw new MolAnalyseException(x);
	        } catch (CDKException x) {
	        	throw new MolAnalyseException(x);
	        }
	        */
	        
	        
	        try {
	        	FunctionalGroups.associateIonic(mol);
	        } catch (CDKException x) {
	        	
	        	logger.warn(x);
	        }        
	        clearVisitedFlags(mol);
    	} catch (Exception x) {
    		//just in case ....
            System.out.println(mol);
            x.printStackTrace();
    		throw new MolAnalyseException(x);
    		
    	}
    }
    public static void clearVisitedFlags(IAtomContainer mol) {
    	for (int i=0; i < mol.getAtomCount(); i++) 
    		mol.getAtom(i).setFlag(CDKConstants.VISITED,false); 
    	for (int i=0; i < mol.getBondCount(); i++) 
    		mol.getBond(i).setFlag(CDKConstants.VISITED,false);    		
    }

}
