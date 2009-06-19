/*
Copyright Ideaconsult Ltd. (C) 2005-2008 

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
package toxTree.ui.molecule;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Image;
import java.awt.Rectangle;
import java.awt.event.ComponentEvent;
import java.awt.event.ComponentListener;
import java.util.EventObject;

import javax.swing.JPanel;

import org.openscience.cdk.event.ICDKChangeListener;
import org.openscience.cdk.interfaces.IAtomContainer;

import ambit2.core.io.CompoundImageTools;


/**
 * 2D structure diagram
 * @author Nina Jeliazkova
 *
 */
public class Panel2D extends JPanel implements ICDKChangeListener, ComponentListener
{
	/**
	 * 
	 */
	private static final long serialVersionUID = -6293533800645578594L;
	protected CompoundImageTools tools;
	protected IAtomContainer atomContainer;
	protected IAtomContainer selected;
	protected Image image=null;
	protected boolean generate2d = true;
	
	public Panel2D() {
		super();
		tools = new CompoundImageTools();
		tools.setImageSize(getPreferredSize());
		image=tools.getDefaultImage();
		addComponentListener(this);
	}
	@Override
	public void paint(Graphics g) {
		super.paintComponent(g);    
		if (image == null) {
			image = tools.getImage(atomContainer,selected);
		}
		g.drawImage(image,0,0,this);
	}
	
	public void setAtomContainer(IAtomContainer mol, boolean generate2d) {
		this.generate2d = generate2d;
		image = null;
		atomContainer = mol;
		selected = null;
		repaint();
	}	
	public void setAtomContainer(IAtomContainer mol) {
		setAtomContainer(mol,true);
	}
	public void setSelected(IAtomContainer selected) {
		this.selected = selected;
		image = null;
		repaint();
	}	
	public void stateChanged(EventObject e) {
		Rectangle r = ((Component)e.getSource()).getBounds();
		tools.setImageSize(new Dimension(r.width,r.height));
		image = null;
		repaint();
	}
	public void componentHidden(ComponentEvent e) {
		System.out.println(e);
		
	}
	public void componentMoved(ComponentEvent e) {
		System.out.println(e);
		
	}
	public void componentResized(ComponentEvent e) {
		Rectangle r = ((Component)e.getSource()).getBounds();
		tools.setImageSize(new Dimension(r.width,r.height));
		image = null;
		repaint();
		
	}
	public void componentShown(ComponentEvent e) {
		
	}
}


