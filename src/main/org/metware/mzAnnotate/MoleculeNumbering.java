package org.metware.mzAnnotate;

import java.util.ArrayList;
import java.util.List;

import org.openscience.cdk.interfaces.IAtom;
import org.openscience.cdk.interfaces.IAtomContainer;
import org.openscience.cdk.interfaces.IBond;

public class MoleculeNumbering {
	
	Integer atomCount;
	Integer bondCount;
	
	public MoleculeNumbering()
	{
		atomCount = 0;
		bondCount = 0;
	}
	
	/**
	 * Gets the numbered mol.
	 * 
	 * @param mol the mol
	 * @param start the start
	 * 
	 * @return the numbered mol
	 */
	public IAtomContainer getNumberedMol(IAtomContainer mol, int atomStart, int bondStart)
	{		
		bondCount = bondStart;
		atomCount = atomStart;
		
		List<IAtom> atomsDone = new ArrayList<IAtom>();
		List<IBond> bondsDone = new ArrayList<IBond>();
		
		for (IBond bond : mol.bonds()) {
			if(bondsDone.contains(bond))
				continue;
			else
				bondsDone.add(bond);
			bondCount++;
			bond.setID("b" + bondCount.toString());
    		for (IAtom atom : bond.atoms()) {
    			if(atomsDone.contains(atom))
    				continue;
    			else
    				atomsDone.add(atom);
    			atomCount++;
    			atom.setID("a" + atomCount.toString());
			}
		}
		return mol;
	}
	
	public Integer getAtomCount() {
		return atomCount;
	}

	public void setAtomCount(Integer atomCount) {
		this.atomCount = atomCount;
	}

	public Integer getBondCount() {
		return bondCount;
	}

	public void setBondCount(Integer bondCount) {
		this.bondCount = bondCount;
	}
	
	

}
