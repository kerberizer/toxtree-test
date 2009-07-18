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

package mutant.descriptors;

import org.openscience.cdk.exception.CDKException;
import org.openscience.cdk.interfaces.IAtomContainer;
import org.openscience.cdk.qsar.DescriptorSpecification;

import toxTree.tree.rules.smarts.ISmartsPattern;
import toxTree.tree.rules.smarts.SmartsPatternCDK;

public class DescriptorHasNO2Group extends DescriptorStructurePresence<IAtomContainer> {
	protected static String NO2 = "I(NO2)";
	public DescriptorHasNO2Group() {
		super();
		try {
		setParameters(new Object[] {
                "[$([N+]([O-])=O),$([N](=O)=O)]",
				NO2}
				);
		} catch (CDKException x) {
		    
			setResultName(NO2);
			logger.error(x);
		}
	}
	
	 public DescriptorSpecification getSpecification() {
	        return new DescriptorSpecification(
	            "True if contains NO2 group, false otherwise",
	            this.getClass().getName(),
	            "$Id: DescriptorHasNO2Group.java  2007-08-09 13:18 nina$",
	            "ToxTree Mutant plugin");
	    }

    @Override
    protected ISmartsPattern<IAtomContainer> createSmartsPattern() {
        return new SmartsPatternCDK();
    }
}


