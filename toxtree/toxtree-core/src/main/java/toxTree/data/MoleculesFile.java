/*
Copyright Ideaconsult (C) 2005-2007 
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

package toxTree.data;

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.beans.PropertyChangeSupport;
import java.io.File;
import java.util.ArrayList;
import java.util.BitSet;
import java.util.Collection;
import java.util.Hashtable;
import java.util.Iterator;
import java.util.Map;

import org.openscience.cdk.ChemObject;
import org.openscience.cdk.aromaticity.CDKHueckelAromaticityDetector;
import org.openscience.cdk.fingerprint.Fingerprinter;
import org.openscience.cdk.interfaces.IAtomContainer;
import org.openscience.cdk.interfaces.IAtomContainerSet;
import org.openscience.cdk.interfaces.IChemObjectBuilder;
import org.openscience.cdk.interfaces.IChemObjectChangeEvent;
import org.openscience.cdk.interfaces.IChemObjectListener;
import org.openscience.cdk.interfaces.IMolecule;
import org.openscience.cdk.io.listener.IReaderListener;
import org.openscience.cdk.smiles.SmilesGenerator;
import org.openscience.cdk.tools.manipulator.AtomContainerManipulator;

import toxTree.io.Tools;
import toxTree.logging.TTLogger;
import toxTree.query.FunctionalGroups;
import ambit2.core.io.RandomAccessReader;
import ambit2.core.io.RandomAccessSDFReader;

public class MoleculesFile extends ChemObject implements IAtomContainerSet, IChemObjectListener {
	/**
	 * 
	 */
	public static String propertyFingerprint = "FINGERPRINT1024";
	protected SmilesGenerator smigen = new SmilesGenerator();
	protected Fingerprinter fp = new Fingerprinter(1024);
    protected static String NA = "";
	private static final long serialVersionUID = 4566523260890189354L;
	protected RandomAccessReader reader;
	protected final Hashtable[] updatedProperties;
	private IAtomContainer recordLoaded = null;
	private int recordLoadedIndex = -1;
	protected int selectedIndex = -1;
	protected boolean bufferProperties = true;
    protected PropertyChangeSupport propertyChangeSupport ;
    protected int foundIndex = -1;
    protected TTLogger logger = new TTLogger(MoleculesFile.class);
    /*
    protected Hashtable<String,String> propertyTranslator;
    protected PropertyTranslator availableProperties;
    */
    
    protected SortedPropertyList sortedProperties;
    public MoleculesFile(String resource,IChemObjectBuilder builder,IReaderListener listener) throws Exception {
    	this(Tools.getFileFromResource(resource),builder,listener);
    }
	public MoleculesFile(File file,IChemObjectBuilder builder) throws Exception {
    	this(file,builder,null);
    }
	public MoleculesFile(File file,IChemObjectBuilder builder,IReaderListener listener) throws Exception {
		super();
//		if (file.length() < 2000000) throw new Exception("File too small :)");
        propertyChangeSupport =new PropertyChangeSupport(this);
		this.reader = createReader(file, builder,listener);
		reader.readRecord(0);
		updatedProperties = new Hashtable[reader.size()];
		//propertyTranslator = new Hashtable<String,String>();
		reader.removeChemObjectIOListener(listener);
        sortedProperties = new SortedPropertyList("");
		/*
        availableProperties = new PropertyTranslator();
        */
	}
    public RandomAccessReader createReader(File file,IChemObjectBuilder builder,IReaderListener listener) throws Exception {
        if (file.getName().toLowerCase().endsWith(".sdf"))
            return new RandomAccessSDFReader(file, builder,listener);
        else throw new Exception("Unsupported file format! "+file.getName());
    }
    public void close() {
        //sortedProperties.clear();
        if (recordLoaded != null) recordLoaded.removeListener(this);
        if (reader != null)
            try {
                propertyChangeSupport.firePropertyChange("op_clear", null, reader);
                
            reader.close();
            } catch (Exception x) {
                x.printStackTrace();
            }
        reader = null;
    }
    
	@Override
	protected void finalize() throws Throwable {
	    close();
		super.finalize();
	}

	public Object first() {
		return setSelectedIndex(0);
	}

	public Object last() {
		return setSelectedIndex(getAtomContainerCount()-1);
	}

	public Object setSelectedIndex(int index) {
        selectedIndex = index;
		return getAtomContainer(selectedIndex);
	}

	public int getSelectedIndex() {
		return selectedIndex;
	}

	public void setAtomContainers(IAtomContainer[] molecules) {
		throw new UnsupportedOperationException("setAtomContainers");

	}
	public IAtomContainer getAtomContainer(int index) {
		try {
			if (recordLoadedIndex != index) {
				if (recordLoaded != null) {
					recordLoaded = null;
				}
				
				recordLoaded = (IAtomContainer) reader.readRecord(index);
				//recordLoaded.addListener(this);
				recordLoadedIndex = index;
			}
            //if (bufferProperties && (updatedProperties[index]==null)) updatedProperties[index] =  new Hashtable();
			if (updatedProperties[index] != null) {
                Map p = recordLoaded.getProperties();
                Object[] keys = p.keySet().toArray();
                //this effectively does buffering of properties
                //Hashtable newp = new Hashtable();
                for (int i=0; i < keys.length;i++) {
                    Object key = keys[i];
                    if ((key ==null) || ("".equals(key))) return null;

                    Object oldValue = p.get(key);
                    p.remove(key);
                    p.put(key, oldValue);
                }
                p.putAll(updatedProperties[index]);
                //recordLoaded.setProperties(p);
				//recordLoaded.getProperties().putAll(updatedProperties[index]);
            }    

			return recordLoaded;
			
		} catch (Exception x) {
			x.printStackTrace();
			return null;
		}
	}
	

	public void removeAllAtomContainers() {
        sortedProperties.clear();
	}


	
	public int getAtomContainerCount() {
		if (reader ==null) return 0;
		else
		return reader.size();
	}

	public boolean hasNext() {
		if (reader == null) return false;
		return reader.hasNext();
	}

	public Object next() {
		return setSelectedIndex(getSelectedIndex()+1);
	}

	public boolean hasPrevious() {
		if (reader == null) return false;
		return reader.hasPrevious();
	}

	public Object previous() {
		return setSelectedIndex(getSelectedIndex()-1);
	}

	public int nextIndex() {
		return reader.nextIndex();
	}

	public int previousIndex() {
		return reader.previousIndex();
	}
	
	public Map getProperties(final int index) {
		if (updatedProperties[index] == null)
			return getAtomContainer(index).getProperties();
		else {
			Hashtable h = new Hashtable();
			h.putAll(updatedProperties[index]);
			h.putAll(getAtomContainer(index).getProperties());
			return h;
		}
	}
	public boolean isBuffered(final int index, final Object key) {
		if (updatedProperties[index] != null) {
			return updatedProperties[index].get(key) != null;
		} else return false;	
	}

	public Object getProperty(final int index, final Object key) {
        if ((key ==null) || ("".equals(key.toString()))) return null;
        
		Object o = null;
		if (updatedProperties[index] != null)
			o = updatedProperties[index].get(key);
		if (o == null) {
            //System.out.println("Reading record to get Property "+key + " " + index);
			IAtomContainer c = getAtomContainer(index);
			if (c ==null) return null;
			else {
				Object value = c.getProperty(key);
                if (bufferProperties) {
                    if (value == null) value = NA;
                    else
	                    try {
	                    	Double dvalue = new Double(value.toString());
	                    	value = dvalue;
	                    } catch (Exception x) {
	                    	
	                    }
                    
                    setProperty(index, key, value);
                }
				return value;
			}
		}
		else return o;
	}

	public final void setProperty(final int index, final Object key, final Object value) {
        if (value == null) return; 
		if (updatedProperties[index] == null) updatedProperties[index] = new Hashtable();
        updatedProperties[index].put(key,value);
        
	}
	public void stateChanged(IChemObjectChangeEvent event) {
		//System.out.println(((IChemObject) event.getSource()).getProperties());
		
	}
    @Override
    public String toString() {
        return reader.toString();
    }
	public boolean isBufferProperties() {
		return bufferProperties;
	}
	public void setBufferProperties(boolean bufferProperties) {
		this.bufferProperties = bufferProperties;
	}
	

	@Override
	public boolean equals(Object obj) {
	    if (obj instanceof MoleculesFile)
	    return obj.toString().equals(toString());
        else return false;
	}
    public synchronized RandomAccessReader getReader() {
        return reader;
    }
    public synchronized void addPropertyChangeListener(
            PropertyChangeListener listener) {
        propertyChangeSupport.addPropertyChangeListener(listener);
    }
    public synchronized void removePropertyChangeListener(
            PropertyChangeListener listener) {
        propertyChangeSupport.removePropertyChangeListener(listener);
    }
    public synchronized void addPropertyChangeListener(String propertyName,
            PropertyChangeListener listener) {
        propertyChangeSupport.addPropertyChangeListener(propertyName,listener);
    }
    public synchronized void removePropertyChangeListener(String propertyName,
            PropertyChangeListener listener) {
        propertyChangeSupport.removePropertyChangeListener(propertyName,listener);
    }
    public void firePropertyChange(String propertyName, 
            Object oldValue, Object newValue) {
        propertyChangeSupport.firePropertyChange(propertyName, oldValue, newValue);
    }
    public void firePropertyChange(PropertyChangeEvent event) {
        propertyChangeSupport.firePropertyChange(event);
    }

    protected void prepareProperty(Object property) throws Exception {
        if ((sortedProperties.size()==0) || (!sortedProperties.getPropertyName().equals(property))) {
            sortedProperties.setPropertyName(property);
            
            if ("USMILES".equals(property))  {
                //generate unique smiles
                
                for (int i=0; i < getAtomContainerCount();i++) try {
                    setProperty(i, "USMILES",smigen.createSMILES((IMolecule)getAtomContainer(i)));
                 } catch (Exception x) {
                     x.printStackTrace();
                 }
                 
            } else
            if (propertyFingerprint.equals(property))  {
                //generate fingerprints
                for (int i=0; i < getAtomContainerCount();i++) 
                try {
                	//IAtomContainer clone = (IAtomContainer)getAtomContainer(i).clone();
                	IAtomContainer clone = (IAtomContainer)getAtomContainer(i);
                	//MolAnalyser.analyse(clone);
                    AtomContainerManipulator.percieveAtomTypesAndConfigureAtoms(clone);
                    CDKHueckelAromaticityDetector.detectAromaticity(clone);
                	BitSet bs = fp.getFingerprint(clone);
                    setProperty(i, propertyFingerprint,bs);
                    /*
                    System.out.print(i);
                    System.out.print('\t');
                    System.out.print(clone.getAtomCount());
                    System.out.print('\t');
                    System.out.print(smigen.createSMILES((IMolecule)clone));
                    System.out.print('\t');
                    System.out.println(bs);
                    */
                 } catch (Exception x) {
                	 setProperty(i, propertyFingerprint,null);
                     x.printStackTrace();
                 }
            }            
            sortedProperties.addList(this);
        }    
    }
    public int indexOf(Object property,Object value) throws Exception {
    	prepareProperty(property);
        return sortedProperties.getOriginalIndexOf(value);
    }
    public void sortBy(Object property, boolean ascending) throws Exception {
        if ((sortedProperties.size()==0) || (!sortedProperties.getPropertyName().equals(property))) {
            sortedProperties.clear();
            sortedProperties.setPropertyName(property);
            sortedProperties.addList(this);
        }    
        sortedProperties.sort(ascending);
        //System.out.println(sortedProperties);
    }
    public Collection<Comparable> getValuesPerField(Object field,int limit) {
        ArrayList<Comparable> values = new ArrayList<Comparable>();
        for (int i=0; i < getAtomContainerCount();i++) {
            Object o = getProperty(i, field);
            if (o==null) continue;
            Comparable c=null;
            if (o instanceof Comparable) c = (Comparable)o;
            else c = o.toString();
            if (!values.contains(c)) values.add(c);
            if (values.size()==limit) break;
        }
        return values;
    }
	public void add(IAtomContainerSet arg0) {
		Iterator i = arg0.atomContainers();
		while (i.hasNext()) 
			addAtomContainer((IAtomContainer)i.next());
		sortedProperties.clear();
	}
	public void addAtomContainer(IAtomContainer a) {
        sortedProperties.clear();
	}
	
	public void addAtomContainer(IAtomContainer arg0, double arg1) {
		addAtomContainer(arg0);
		
	}
	public Iterator atomContainers() {
		// TODO Auto-generated method stub
		return null;
	}
	public double getMultiplier(int arg0) {
		return 0;
	}
	public double getMultiplier(IAtomContainer arg0) {
		return 0;
	}
	public double[] getMultipliers() {
		// TODO Auto-generated method stub
		return null;
	}
	public void removeAtomContainer(IAtomContainer arg0) {
		// TODO Auto-generated method stub
		
	}
	public void removeAtomContainer(int arg0) {
		// TODO Auto-generated method stub
		
	}
	public void replaceAtomContainer(int arg0, IAtomContainer arg1) {
		// TODO Auto-generated method stub
		
	}
	public boolean setMultiplier(IAtomContainer arg0, double arg1) {
		return false;
	}
	public void setMultiplier(int arg0, double arg1) {
		
	}
	public boolean setMultipliers(double[] arg0) {
		return false;
	}
	/*
    public int find(IMolecule mol ) throws Exception {
        if (mol == null) throw new Exception("Null molecule");
        SmilesGenerator g = new SmilesGenerator();
        String smiles = g.createSMILES(mol);
        System.out.print("<lookup smiles=\"");
        System.out.print(smiles);
        System.out.println("\">");
        int index = indexOf("SMILES",smiles);
        System.out.println(index);
        System.out.println("</lookup>");
        return index;
        
    }
    */
	/**
	 * TODO introduce isomorphism check to handle nonunique fingerprints
	 */
    public int find(IAtomContainer mol ) throws Exception {
        if (mol == null) throw new Exception("Null molecule");
//        MolAnalyser.analyse(mol);
        BitSet bs = getFingerprint(mol);
        
        logger.debug("<lookup fingerprint=\"",bs,"\">");
        //System.err.println(smigen.createSMILES((IMolecule)mol));
        prepareProperty(propertyFingerprint);
        int first_index = sortedProperties.indexOf(bs);
        int index = -1;
        if (first_index >=0) {
        	int last_index = sortedProperties.lastIndexOf(bs);
        	//System.out.print("Verifying "+first_index + "-" + last_index);
        	for (int i=first_index; i <= last_index; i++) {
        		int original_index = sortedProperties.getOriginalIndexOf(i);
        		
        		//System.out.print('\n');
        		//System.out.print(sortedProperties.get(i));
        		//System.out.print('\n');
        		logger.debug(original_index);
        		
        		IAtomContainer c = getAtomContainer(original_index);
                AtomContainerManipulator.percieveAtomTypesAndConfigureAtoms(c);
                CDKHueckelAromaticityDetector.detectAromaticity(c);
                if (FunctionalGroups.isSubstance(c,mol)) {
                	index = original_index;
                	foundIndex = index;
                    logger.debug(getProperty(index,"Group"));                	
                    //System.out.println(index);
                	break;
                } else {
                    /*
                	for (int j=0; j < mol.getAtomCount(); j++)
                		System.out.println(mol.getAtom(j).getSymbol() + " "+ mol.getAtom(j).getFlag(CDKConstants.ISAROMATIC));
                	System.out.println(" ");
                	for (int j=0; j < c.getAtomCount(); j++)
                		System.out.println(c.getAtom(j).getSymbol() + " "+ c.getAtom(j).getFlag(CDKConstants.ISAROMATIC));                	
                	System.err.println(smigen.createSMILES((IMolecule)c));
                    */
                }
                //System.out.println(smigen.createSMILES((IMolecule)mol));        
        	}
        	if (index == -1) {
        		logger.debug("Not found " + smigen.createSMILES((IMolecule)mol));
        	}
        }
        logger.debug("</lookup>");

        return index;
    }
    public BitSet getFingerprint(IAtomContainer mol) throws Exception {
    	return fp.getFingerprint(mol);
    }
}

