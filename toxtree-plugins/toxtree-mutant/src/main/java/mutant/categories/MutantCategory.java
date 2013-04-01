/*
Copyright (C) 2005-2006  

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

package mutant.categories;

import toxTree.core.IDecisionCategory;
import toxTree.tree.DefaultCategory;

public class MutantCategory extends DefaultCategory {
	/**
	 * 
	 */
	private static final long serialVersionUID = 0L;
	protected int priority;

	public MutantCategory(String name, int id,int priority) {
		super(name,id);
		setPriority(priority);
	}
	
	public MutantCategory(String name, int id) {
		super(name,id);
		setPriority(id);
	}
	@Override
	public int compareTo(IDecisionCategory o) {
		if (o instanceof MutantCategory) {
			 return getPriority()-((MutantCategory) o).getPriority();
		} else return super.compareTo(o);
	}
	public int getPriority() {
		return priority;
	}
	public void setPriority(int priority) {
		this.priority = priority;
	}
	
	@Override
	public CategoryType getCategoryType() {
		return CategoryType.hasInconclusiveCategory;
	}
}


