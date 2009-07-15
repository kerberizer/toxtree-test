package mutant.descriptors;


import joelib.desc.types.MolarRefractivity;
import joelib.molecule.JOEMol;

import org.openscience.cdk.exception.CDKException;
import org.openscience.cdk.interfaces.IAtomContainer;
import org.openscience.cdk.interfaces.IMolecule;
import org.openscience.cdk.qsar.DescriptorSpecification;
import org.openscience.cdk.qsar.DescriptorValue;
import org.openscience.cdk.qsar.IMolecularDescriptor;
import org.openscience.cdk.qsar.result.DoubleResult;
import org.openscience.cdk.qsar.result.IDescriptorResult;

import toxTree.tree.rules.smarts.Convertor;

/**
 * This is now a wrapper of Joelib MolarRefractivity, but needs to be rewritten without joelib.
 * [wc99] S. A. Wildman and G. M. Crippen, Prediction of Physicochemical Parameters by Atomic Contributions, J. Chem. Inf. Comput. Sci., 39, 868-873, 1999
 * @author Nina Jeliazkova
 *
 */
public class DescriptorMolarRefractivity implements IMolecularDescriptor {
	protected static String[] names = {"MR"};
	protected MolarRefractivity mr = new MolarRefractivity();
	
	public DescriptorValue calculate(IAtomContainer arg0) throws CDKException {
		try {
			JOEMol converted = Convertor.convert((IMolecule)arg0);
	        return new DescriptorValue(getSpecification(), 
	        		getParameterNames(), getParameters(),
	                new DoubleResult(mr.getDoubleValue(converted)), names);
			
		} catch (Exception x) {
			throw new CDKException(x.getMessage());
		}
	}

	public IDescriptorResult getDescriptorResultType() {
		return new DoubleResult(0);
	}

	public String[] getParameterNames() {
		return null;
	}

	public Object getParameterType(String arg0) {
		return null;
	}

	public Object[] getParameters() {
		return null;
	}

	 public DescriptorSpecification getSpecification() {
	        return new DescriptorSpecification(
	            "MolarRefractivity",
	            this.getClass().getName(),
	            "$Id: DescriptorMolarRefractivity.java 2007-08-21 5:56 nina$",
	            "ToxTree Mutant plugin");
	    }

	public void setParameters(Object[] arg0) throws CDKException {
		throw new CDKException("No parameters expected!");
	}

}