/*
Copyright Nina Jeliazkova (C) 2005-2006  
Contact: nina@acad.bg

This program is free software; you can redistribute it and/or
modify it under the terms of the GNU General Public License
as published by the Free Software Foundation; either version 2
of the License, or (at your option) any later version.

This program is distributed in the hope that it will be useful,
but WITHOUT ANY WARRANTY; without even the implied warranty of
MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
GNU General Public License for more details.

You should have received a copy of the GNU General Public License
along with this program; if not, write to the Free Software
Foundation, Inc., 51 Franklin Street, Fifth Floor, Boston, MA  02110-1301, USA.

*/

package verhaar.rules;


import org.openscience.cdk.exception.CDKException;
import org.openscience.cdk.qsar.XLogPDescriptor;

import toxTree.tree.rules.RuleDescriptorRange;

public class RuleLogPRange extends RuleDescriptorRange {
	protected Object[] params = {new Boolean(true)};
	/**
	 * 
	 */
	private static final long serialVersionUID = 4370691438824318131L;

	public RuleLogPRange() {
		super();
		id = "0.2";
		setDescriptor(new XLogPDescriptor());
		setMaxValue(6.0);
		setMinValue(0.0);
		try {
			getDescriptor().setParameters(params);
		} catch (CDKException x) {
			logger.error(x);
		}
		setTitle("Have a logKow between 0 and 6");
		explanation = new StringBuffer();
		explanation.append(
				"Compounds with a Log Kow lower than 0 will not be considered, due to the unrealistically high effect concentrations that "+
				"will be predicted by using narcosis type QSARs. For compounds acting through a nonpolar narcosis type mode of action, it is"+
				"considered unlikely that they would exhibit acute toxic action towards biota in aqueous environments because of this. Compounds"+
				"that have a Log Kow higher than 6 do not normally exhibit acute toxicity. The most plausible explanation for this observation is that,"+
				"since Log Kow is considered to be a parameter describing the kinetics of uptake of chemicals from water, chemicals that have Log "+
				"Kow's in this range are genereally taken up too slowly to show acute toxic effects. Furthermore some of these high Log Kow "+
				"compounds are simply too bulky to be taken up through membranes. An example of this is tetradecanol, which can be considered to"+ 
				"be essentially nontoxic in experiments that measure acute toxicity. Of course this rule does not mean that compounds with Log K~"+
				"values that lie outside this range are to be considered nontoxic, but only that we do not recommend modeling their toxicity using"+
				"narcosis--type QSAR equations."
				);
		examples[0] = "c(c(c(c(c1)ccc2)c2)cc(c3c(c(c4)ccc5)c5)c4)(c1)c3";  //Kowwin logKow 6.67
		examples[1] = "O=C(N(N(C1(=O))c(cccc2)c2)c(cccc3)c3)C1CCCC";  //Kowwin logKow 3.523
		editable = false;
	}
	
}
