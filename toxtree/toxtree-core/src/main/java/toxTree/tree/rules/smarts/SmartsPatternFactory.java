/*
Copyright (C) 2005-2007  

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

package toxTree.tree.rules.smarts;

import ambit2.smarts.query.ISmartsPattern;
import ambit2.smarts.query.ISmartsPatternFactory;
import ambit2.smarts.query.SMARTSException;
import ambit2.smarts.query.SmartsPatternAmbit;
import ambit2.smarts.query.SmartsPatternCDK;

/**
 * Creates any of SMARTS pattern classes.
 * @author Nina Jeliazkova nina@acad.bg
 *
 */
public class SmartsPatternFactory implements ISmartsPatternFactory {

	public static enum SmartsParser {
			smarts_cdk,
			smarts_nk
	};
	protected SmartsParser parserType;

	public SmartsPatternFactory() {
		super();
		setParserType(SmartsParser.smarts_nk);
	}
	
	public ISmartsPattern createSmartsPattern(String smarts, boolean negate) throws SMARTSException {
		return createSmartsPattern(parserType, smarts, negate);
	}

	public static ISmartsPattern createSmartsPattern(SmartsParser parser, String smarts,boolean negate) throws SMARTSException {
		//System.out.println("createSmartsPattern "+parser);
		switch (parser) {
		//case smarts_joelib: return new SmartsPattern(smarts,negate);
		case smarts_cdk: return new SmartsPatternCDK(smarts,negate);
		case smarts_nk: return new SmartsPatternAmbit(smarts,negate); 
		default:  return new SmartsPattern(smarts,negate);	
		}
	}
	
	public SmartsParser getParserType() {
		return parserType;
	}
	public void setParserType(SmartsParser parserType) {
		this.parserType = parserType;
	}
	
}


