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

package toxTree.test.prefuse;


import java.awt.BorderLayout;

import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;

import prefuse.util.display.ExportDisplayAction;
import toxTree.tree.cramer.CramerRules;
import toxTree.ui.tree.TreePrefuse;

public class PrefuseTest  {
  

    public static void main(String[] args) {
        try {
        TreePrefuse hello = new TreePrefuse(new CramerRules(),null);
        JFrame frame = new JFrame("Test");
        
        //frame.getContentPane().add(new JScrollPane(hello));
        JPanel p = new JPanel(new BorderLayout());
        p.add(hello,BorderLayout.CENTER);

        /*
        SearchQueryBinding searchQ = new SearchQueryBinding(hello.m_vis.getVisualGroup(GRAPH), LABEL);
        AndPredicate filter = new AndPredicate(searchQ.getPredicate());
        JSearchPanel searcher = searchQ.createSearchPanel(true);
        searcher.setBorder(BorderFactory.createEmptyBorder(5, 5, 5, 0));
        searcher.setLabelText(LABEL+": ");

        
        // container for elements at the bottom of the screen
        Box bottomContainer = new Box(BoxLayout.X_AXIS);
        bottomContainer.add(Box.createHorizontalStrut(5));
        bottomContainer.add(searcher);
        bottomContainer.add(Box.createHorizontalGlue());
        bottomContainer.add(Box.createHorizontalStrut(5));
        bottomContainer.add(Box.createHorizontalStrut(16));
        p.add(bottomContainer,BorderLayout.SOUTH);
*/        
        // container for elements at the top of the screen
        Box topContainer = new Box(BoxLayout.X_AXIS);
        topContainer.add(Box.createHorizontalStrut(5));
        topContainer.add(new JLabel(hello.getDecisionMethod().getTitle()));
        topContainer.add(Box.createHorizontalGlue());
        /*
        topContainer.add(Box.createHorizontalStrut(5));
        topContainer.add(g_total);
        topContainer.add(Box.createHorizontalStrut(5));
        */        
        p.add(topContainer,BorderLayout.NORTH);
        p.add(new JButton(new ExportDisplayAction(hello)),BorderLayout.EAST);
        
        frame.getContentPane().add(p);
        frame.pack();
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(800,400);

        frame.setVisible(true);
        } catch (Exception x) {
            x.printStackTrace();
        }

    }
    
}

