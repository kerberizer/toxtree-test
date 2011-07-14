/*
Copyright Ideaconsult Ltd. (C) 2005-2011 

Contact: jeliazkova.nina@gmail.com

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
package toxtree.plugins.verhaar2.test.rules.class3;

import toxTree.core.IDecisionRule;
import toxtree.plugins.verhaar2.rules.Rule32;
import toxtree.plugins.verhaar2.test.rules.AbstractRuleTest;

public class Rule32Test extends AbstractRuleTest {
	@Override
	protected IDecisionRule createRule() {
		return new Rule32();
	}
	public void test() throws Exception {
	    Object[][] answer = {
	            	{"c1ccc(cc1)CCl",new Boolean(true)},
	            	{"c1ccc(cc1)CBr",new Boolean(true)},
	            	{"c1ccc(cc1)CI",new Boolean(true)},
	            	{"c1ccc(cc1)CC#N",new Boolean(true)},
	            	{"c1ccc(cc1)CO",new Boolean(true)},
	            	{"c1ccc(cc1)CC(C)=O",new Boolean(true)},
	            	{"c1ccc(cc1)CC=O",new Boolean(true)},
	    };
	    
	    ruleTest(answer); 
	}	
	
	
}