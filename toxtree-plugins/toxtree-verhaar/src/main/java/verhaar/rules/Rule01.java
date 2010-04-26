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


import org.openscience.cdk.interfaces.IAtomContainer;
import org.openscience.jchempaint.renderer.selection.IChemObjectSelection;

import ambit2.base.interfaces.IProcessor;
import toxTree.tree.rules.RuleElements;
import toxTree.tree.rules.smarts.RuleSMARTSSubstructureAmbit;

/**
 * 
 * Consists only of C,H,N,O,S,halogens (exluding I).
 * @author Nina Jeliazkova nina@acad.bg
 * <b>Modified</b> Dec 17, 2006
 */
public class Rule01 extends RuleElements {

	/**
	 * 
	 */
	private static final long serialVersionUID = -6192036039649653201L;

	public Rule01() {
		super();
		super.setHalogens(new String[] {"Cl","Br","F"});
		setComparisonMode(modeOnlySpecifiedElements);
		addElement("C");
		addElement("H");
		addElement("N");
		addElement("O");
		addElement("S");
		addElement("X");

		setTitle("Consists only of C,H,N,O,S,halogens (exluding I)");
		id = "0.1";
		setExplanation("Consists only of C,H,N,O,S,halogens (excluding I)");
		examples[0] = "OP(O)(O)=O.CCOC(=O)C=1CC(N)C(NC(C)=O)C(OC(CC)CC)C=1";
		examples[1] = "CCCCCCCO";
		editable = false;
	}

}
