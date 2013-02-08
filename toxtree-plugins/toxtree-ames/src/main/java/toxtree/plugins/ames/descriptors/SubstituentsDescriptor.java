/*
Copyright Ideaconsult Ltd. (C) 2005-2012 

Contact: jeliazkova.nina@gmail.com

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

package toxtree.plugins.ames.descriptors;

import java.util.Enumeration;
import java.util.Hashtable;
import java.util.logging.Logger;

import org.openscience.cdk.exception.CDKException;
import org.openscience.cdk.interfaces.IAtomContainer;
import org.openscience.cdk.interfaces.IAtomContainerSet;
import org.openscience.cdk.isomorphism.matchers.QueryAtomContainer;
import org.openscience.cdk.qsar.DescriptorSpecification;
import org.openscience.cdk.qsar.DescriptorValue;
import org.openscience.cdk.qsar.IMolecularDescriptor;
import org.openscience.cdk.qsar.result.DoubleArrayResult;
import org.openscience.cdk.qsar.result.IDescriptorResult;

public abstract class SubstituentsDescriptor implements IMolecularDescriptor {
	protected static transient Logger logger = Logger.getLogger(SubstituentsDescriptor.class.getName()); 
	protected String[] paramNames = {"ring","descriptor_names"};
	protected String[] descriptorNames = null;
	protected SubstituentExtractor extractor;
	
	
	public SubstituentsDescriptor() {
		this(null);
	}
	public SubstituentsDescriptor(QueryAtomContainer query) {
		super();
		extractor = new SubstituentExtractor(query);
	}

	public DescriptorValue calculate(IAtomContainer arg0) {
        try {
	        Hashtable<String,IAtomContainerSet> substituents = extractor.extractSubstituents(arg0);
	        String mark = select(substituents);
	        Enumeration<String> marks = substituents.keys();
	        while (marks.hasMoreElements()) {
	            String m = marks.nextElement();
	            if (m.equals(mark)) continue;
	                for (int i=0; i < arg0.getAtomCount();i++)
	                    arg0.getAtom(i).removeProperty(m);
	        }
	
			return calculate(substituents.get(mark),mark);
        } catch (Exception x) {
    		return new DescriptorValue(getSpecification(), getParameterNames(),
    				getParameters(), null, null ,x);
        }
	}
    public abstract String select(Hashtable<String,IAtomContainerSet> substituents) throws CDKException;
	
	public abstract DescriptorValue calculate(IAtomContainerSet substituents,String mark) throws CDKException;
	
	public IDescriptorResult getDescriptorResultType() {
		return new DoubleArrayResult();
	}

	public DescriptorSpecification getSpecification() {
        return new DescriptorSpecification(
                "http://toxTree.sf.net",
                this.getClass().getName(),
                "$Id: SubstituentsDescriptor.java 6171 2007-08-06 14:09:00 +0000 (Mon, 06 August 2007) nina $",
                "Toxtree plugin");
	}
	public Object[] getParameters() {
		return new Object[] {extractor.getRingQuery(),descriptorNames};
	}

	public void setParameters(Object[] params) throws CDKException {
		if (params.length > 2) throw new CDKException(getClass().getName() + " expects 2 parameters only.");
		if (params.length >= 1)
			if (params[0] instanceof QueryAtomContainer )
				extractor.setRingQuery((QueryAtomContainer) params[0]);
			else 
				throw new CDKException(params[0] + " not an instance of IQueryAtomContainer");
		if ((params.length == 2) && (params[1] instanceof String[])) {
			setDescriptorNames((String[])params[1]);
		}	
	}
	public Object getParameterType(String arg0) {
		if (paramNames[0].equals(arg0))
			return new QueryAtomContainer();
		else if (paramNames[1].equals(arg0)) return new String[] {};
		else return null;
	}
	public String[] getParameterNames() {
		return paramNames;
	}

	public void setParameterNames(String[] paramNames) {
		this.paramNames = paramNames;
	}
	public String[] getDescriptorNames() {
		return descriptorNames;
	}
	public void setDescriptorNames(String[] descriptorNames) {
		this.descriptorNames = descriptorNames;
	}
	public QueryAtomContainer getRingQuery() {
		return extractor.getRingQuery();
	}
	public void setRingQuery(QueryAtomContainer ringQuery) {
		extractor.setRingQuery(ringQuery);
	}
	@Override
	public String toString() {
		StringBuffer b = new StringBuffer();
		for (int i=0; i < descriptorNames.length;i++) {
			if (i>0) b.append(",");
			b.append(descriptorNames[i]);
		}	
		return b.toString();
	}
 
}
